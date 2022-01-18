package com.weight_plugin.weight_plugin

import android.util.Log
import androidx.annotation.NonNull
import com.weight_plugin.weight_plugin.weight.WeightCallback
import com.weight_plugin.weight_plugin.weight.WeightMode
import com.weight_plugin.weight_plugin.weight.WeightProvider

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** WeightPlugin */
class WeightPlugin: FlutterPlugin, ActivityAware, MethodCallHandler{
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "weight_plugin")
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    when(call.method) {
      ChannelConfig.WEIGHT_INIT -> {
        val type = call.argument<String>("device_type").toString()
        Log.v("weightPlugin", type)
        WeightProvider.INSTANCE.initWeightInstance(type, object: WeightCallback{
              override fun callBack(weightMode: WeightMode) {
                sendWeight(weightMode)
              }
        })

//        when(call.argument<String>("device_type").toString()) {
//          DeviceType.SUNMI -> {
//            WeightProvider.INSTANCE.initWeightInstance(DeviceType.SUNMI, object: WeightCallback{
//              override fun callBack(weightMode: WeightMode) {
//                sendWeight(weightMode)
//              }
//            })
//          }
//
//          DeviceType.FRESHBASE -> {
//            WeightProvider.INSTANCE.initWeightInstance(DeviceType.FRESHBASE, object: WeightCallback{
//              override fun callBack(weightMode: WeightMode) {
//                sendWeight(weightMode)
//              }
//            })
//          }
//
//          else -> {
//            WeightProvider.INSTANCE.initWeightInstance(DeviceType.UNKNOW, object: WeightCallback {
//              override fun callBack(weightMode: WeightMode) {
//                sendWeight(weightMode)
//              }
//            })
//          }
//        }
      }

      ChannelConfig.WEIGHT_SET_ZERO -> {
        WeightProvider.INSTANCE.zero()
      }

      ChannelConfig.WEIGHT_TARE -> {
        WeightProvider.INSTANCE.tare()
      }

      "getPlatformVersion" -> {
        result.success("222Android ${android.os.Build.VERSION.RELEASE}")
      }
    }
  }

  private fun sendWeight(weightMode: WeightMode) {
    val params = mapOf(
            "netWeight" to weightMode.netWeight,
            "tareWeight" to weightMode.tareWeight,
            "status" to weightMode.status,
            "errorCode" to weightMode.errorCode
    )
    channel.invokeMethod(ChannelConfig.WEIGHT_SEND, params)
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    WeightProvider.INSTANCE.init(binding.activity)
  }

  override fun onDetachedFromActivityForConfigChanges() {
    //the Activity your plugin was attached to was
    // destroyed to change configuration.
    // This call will be followed by onReattachedToActivityForConfigChanges().
    //暂无处理
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    //your plugin is now attached to a new Activity
    // after a configuration change.
    //暂无处理
  }

  override fun onDetachedFromActivity() {
    //your plugin is no longer associated with an Activity.
    //暂无处理
  }
}
