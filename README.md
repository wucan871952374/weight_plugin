# weight_plugin

A new Flutter project.

## Getting Started

This project is a starting point for a Flutter
[plug-in package](https://flutter.dev/developing-packages/),
a specialized package that includes platform-specific implementation code for
Android and/or iOS.

For help getting started with Flutter, view our
[online documentation](https://flutter.dev/docs), which offers tutorials,
samples, guidance on mobile development, and a full API reference.

## 安卓取重插件，目前支持商米的硬件取重



###
 1.使用 在pubspec.yaml中引入
    ```
    weight_plugin:
         git:
           url: https://github.com/wucan871952374/weight_plugin.git
           ref: master
    ```

 2.首先进行插件初始化，传入设备类型，目前仅支持商米

  ` WeightPlugin.initWeight(DeviceType.SUNMI) `

 3.监听重量数据流

  ```
   WeightPlugin.weightStream.listen((event) {
   });
  ```


