#import "WeightPlugin.h"
#if __has_include(<weight_plugin/weight_plugin-Swift.h>)
#import <weight_plugin/weight_plugin-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "weight_plugin-Swift.h"
#endif

@implementation WeightPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftWeightPlugin registerWithRegistrar:registrar];
}
@end
