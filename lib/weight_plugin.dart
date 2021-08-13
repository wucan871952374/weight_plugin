import 'dart:async';

import 'package:flutter/services.dart';
import 'package:weight_plugin/channel_config.dart';
import 'package:weight_plugin/device_enum.dart';

class WeightPlugin {
  static const MethodChannel _channel = const MethodChannel('weight_plugin');

  // ignore: close_sinks
  static StreamController<MethodCall> _weightStreamController = StreamController<MethodCall>.broadcast();

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future initWeight(DeviceType deviceType) async {
    String type;
    switch (deviceType) {
      case DeviceType.SUNMI:
        type = "sunmi";
        break;
      case DeviceType.UNKNOW:
        type = "unknow";
        break;
    }
    _channel.setMethodCallHandler(_onChannelMethodHandler);
    _channel.invokeMethod(ChannelConfig.WEIGHT_INIT, {"device_type": type});
  }

  static Future<dynamic> _onChannelMethodHandler(MethodCall call) async {
    _weightStreamController.sink.add(call);
    return "success";
  }

  static Future zero() async {
    _channel.invokeMethod(ChannelConfig.WEIGHT_SET_ZERO);
  }

  static Future tare() async {
    _channel.invokeMethod(ChannelConfig.WEIGHT_TARE);
  }

  static Stream<MethodCall>? get weightStream {
    return _weightStreamController.stream;
  }
}
