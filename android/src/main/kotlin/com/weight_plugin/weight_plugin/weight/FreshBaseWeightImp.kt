package com.weight_plugin.weight_plugin.weight

import android.content.Context
import android.util.Log
import com.mt.retail.weighapi.IMtWeighView
import com.mt.retail.weighapi.MtWeighApi
import com.weight_plugin.weight_plugin.util.ktxRunOnUi
import org.json.JSONObject
import java.math.BigDecimal

class FreshBaseWeightImp: IWeight {

    val weighApi: MtWeighApi by lazy {
        MtWeighApi.getInstance()
    }

    val weightMode = WeightMode(-1)

    companion object {
        val instance: FreshBaseWeightImp by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            FreshBaseWeightImp()
        }
    }

    lateinit var mContext: Context
    lateinit var mWeightCallback: WeightCallback

    override fun initWeight(context: Context, weightCallback: WeightCallback) {
        mContext = context
        mWeightCallback = weightCallback
        connectService()
        sendWeight()
    }

    private fun connectService() {
        weighApi.connectToService(mContext, object: IMtWeighView{
            override fun onMtWeighServiceConnected() {

            }

            override fun onMtWeighServiceDisconnected() {
                connectService()
            }

            override fun onWeightChanged(msg: String?) {
                if (msg == null) return
                Log.v("FreshBaseWeightImp", msg)
                val jsonObject = JSONObject(msg)

                val result = if(jsonObject.has("result")) {
                    jsonObject.getString("result")
                }else {
                    "-1000"
                }

                val net = if(jsonObject.has("net")) {
                    jsonObject.getString("net")
                }else {
                    "-1"
                }

                val tare = if(jsonObject.has("tare")) {
                    jsonObject.getString("tare")
                }else {
                    "-1"
                }

                parseWeight(result, net, tare)
            }

            override fun onBaseInfoChanged(msg: String?) {
            }

            override fun onSetTareFinished(msg: String?) {
            }

            override fun onZeroFinished(result: Int) {
            }

        })

        weighApi.weight
    }

    private fun sendWeight() {
        Thread {
            while (true) {
                ktxRunOnUi {
                    mWeightCallback.callBack(weightMode)
                }
                Thread.sleep(50)
            }
        }.start()
    }

    //解析重量
    private fun parseWeight(result: String, net: String, tare: String) {
        when(result) {
            "0" -> {
                weightMode.netWeight = BigDecimal(net).multiply(BigDecimal("1000")).toInt()
                weightMode.tareWeight = BigDecimal(tare).multiply(BigDecimal("1000")).toInt()
                weightMode.status = true
                weightMode.errorCode = 0
            }

            "-1000" -> {
                weightMode.status = false
                weightMode.netWeight = BigDecimal(net).multiply(BigDecimal("1000")).toInt()
                weightMode.tareWeight = BigDecimal(tare).multiply(BigDecimal("1000")).toInt()
                weightMode.errorCode = 0
            }

            else -> {
                weightMode.status = false
                weightMode.netWeight = -1
                weightMode.tareWeight = -1
                weightMode.errorCode = Integer.valueOf(result)
            }

        }

    }

    override fun zero() {
        if (weighApi.isNotConnectToService && ::mContext.isInitialized) {
            Log.v("清零","11111111")
            connectService()
        }else {
            weighApi.setZero()
            Log.v("清零","22222222")
        }
    }

    override fun tare() {
        if (weighApi.isNotConnectToService && ::mContext.isInitialized) {
            connectService()
        }else {
            weighApi.setTare()
        }
    }

    override fun openBox() {
        if (weighApi.isNotConnectToService && ::mContext.isInitialized) {
            connectService()
        }else {
            weighApi.openCashDrawer()
        }
    }
}