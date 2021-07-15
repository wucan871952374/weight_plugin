package com.weight_plugin.weight_plugin.weight

import android.content.Context
import com.weight_plugin.weight_plugin.DeviceType

class WeightProvider{

    private lateinit var mContext: Context
    private var mDeviceType: String = DeviceType.SUNMI

    companion object {
        val INSTANCE: WeightProvider by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            WeightProvider()
        }
    }

    fun init(context: Context) {
        mContext = context
    }


    private fun getWeightInstance(): IWeight {
        return when(mDeviceType) {
            DeviceType.SUNMI-> {
                SunWeightImp.instance
            }

            else -> {
                UNKnowWeightImp.instance
            }
        }
    }

    fun initWeightInstance(deviceType: String, weightCallback: WeightCallback) {
        mDeviceType = deviceType
        getWeightInstance().initWeight(mContext, weightCallback)
    }

    fun zero() {
        getWeightInstance().zero()
    }

    fun tare() {
        getWeightInstance().tare()
    }

}