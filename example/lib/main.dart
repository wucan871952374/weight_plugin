import 'dart:convert';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:weight_plugin/export.dart';
import 'package:weight_plugin/weight_plugin.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  String _weightMessage = "---";

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await WeightPlugin.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }
    // WeightPlugin.weightStream.listen((event) {
    //
    // });
    WeightPlugin.initWeight(DeviceType.SUNMI);

    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          // child: Text('Running on: $_platformVersion\n'),
          child: Column(
            children: [
              Text('Running on11: $_platformVersion\n'),
              StreamBuilder(
                  stream: WeightPlugin.weightStream,
                  initialData: MethodCall(''),
                  builder: (BuildContext context, AsyncSnapshot<MethodCall> snapt) {
                    if (snapt.data.arguments != null) {
                      _weightMessage = json.encode(snapt.data.arguments);
                    }
                    return Text('weight : $_weightMessage\n');
                  }),
              Container(
                margin: EdgeInsets.only(top: 100),
                child: Row(
                  children: [
                    Expanded(
                      child: button('清零', () {
                        WeightPlugin.zero();
                      }),
                    ),
                    Expanded(
                      child: button('去皮', () {
                        WeightPlugin.tare();
                      }),
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget button(String text, VoidCallback onPress) {
    return Container(
      margin: EdgeInsets.only(left: 20, right: 20),
      color: Colors.blueAccent,
      child: TextButton(
        onPressed: onPress,
        child: Text(
          text,
          style: TextStyle(color: Colors.white, fontSize: 20),
        ),
      ),
    );
  }
}
