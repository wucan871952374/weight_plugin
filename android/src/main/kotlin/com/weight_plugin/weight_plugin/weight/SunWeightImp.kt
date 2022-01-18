package com.weight_plugin.weight_plugin.weight

import android.content.Context
import com.sunmi.scalelibrary.ScaleManager
import com.sunmi.scalelibrary.ScaleResult
import com.weight_plugin.weight_plugin.util.ktxRunOnUi

class SunWeightImp: IWeight{
    private lateinit var scaleManager: ScaleManager

    private lateinit var mContext: Context

    private lateinit var callback: WeightCallback

    companion object {
        val instance: SunWeightImp by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SunWeightImp()
        }
    }

    override fun initWeight(context: Context, weightCallback: WeightCallback) {
        callback = weightCallback
        mContext = context
        scaleManager = ScaleManager.getInstance(context)
        connectService()
    }

    private fun connectService() {
        scaleManager.connectService(object: ScaleManager.ScaleServiceConnection {
            override fun onServiceConnected() {
                getWeight()
            }

            override fun onServiceDisconnect() {
                connectService()  //重连
            }
        })
    }

    private fun getWeight() {
        scaleManager.getData(object: ScaleResult(){
            ///p0 净重  单位克    p1 皮重 单位克   p2秤稳定状态   true 稳定   false  浮动
            override fun getResult(p0: Int, p1: Int, p2: Boolean) {
                ktxRunOnUi {
                    callback.callBack(WeightMode(p0, p1, p2))
                }
            }

            override fun getStatus(p0: Boolean, p1: Boolean, p2: Boolean, p3: Boolean) {
            }
        })
    }

    override fun zero() {
        scaleManager.zero()
    }

    ///秤上有重量 是去皮   没有时是清皮
    override fun tare() {
        scaleManager.tare()
    }

    override fun openBox() {
    }

}