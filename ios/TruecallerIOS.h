#import <React/RCTBridgeModule.h>
#import "TrueSDK.h"
#import <React/RCTEventEmitter.h>

@interface TruecallerIOS : RCTEventEmitter <RCTBridgeModule, TCTrueSDKDelegate>

+ (BOOL)handleUserActivity:(NSUserActivity *)userActivity
            restorationHandler:(void (^)(NSArray *restorableObjects))restorationHandler;

@end

 
