import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:weight_plugin/weight_plugin.dart';

void main() {
  const MethodChannel channel = MethodChannel('weight_plugin');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await WeightPlugin.platformVersion, '42');
  });
}
