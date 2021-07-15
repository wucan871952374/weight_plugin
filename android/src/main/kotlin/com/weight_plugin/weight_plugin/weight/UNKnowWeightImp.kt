package com.weight_plugin.weight_plugin.weight

import android.content.Context
import com.weight_plugin.weight_plugin.util.ktxRunOnUi
import java.util.*


class UNKnowWeightImp: IWeight{

    companion object {
        val instance: UNKnowWeightImp by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            UNKnowWeightImp()
        }
    }

    override fun initWeight(context: Context, weightCallback: WeightCallback) {
        Thread{
            while(true) {
                Thread.sleep(500)
                ktxRunOnUi {
                    val net = Random().nextInt(1000)
                    val tare = Random().nextInt(1000)
                    weightCallback.callBack(WeightMode(net, tare, net % 2 == 0))
                }
            }
        }.start()
    }

    override fun zero() {
    }

    override fun tare() {
    }

}