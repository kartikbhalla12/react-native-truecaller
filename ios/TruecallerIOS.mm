#import "TruecallerIOS.h"
#import "TrueSDK.h"
#import <React/RCTBridge.h>
#import <React/RCTRootView.h>

@implementation TruecallerIOS

RCT_EXPORT_MODULE();

- (NSArray<NSString *> *)supportedEvents {
    return @[@"TruecallerIOSSuccess", @"TruecallerIOSFailure"];
}

RCT_EXPORT_METHOD(isSupported) {
  return [[TCTrueSDK sharedManager] isSupported];
}

RCT_EXPORT_METHOD(initialize:(NSString *)appKey appLink:(NSString *)appLink) {
    if ([[TCTrueSDK sharedManager] isSupported]) {
        [[TCTrueSDK sharedManager] setupWithAppKey:appKey appLink:appLink];
        [TCTrueSDK sharedManager].delegate = self;
    } else {
        // TODO Handle the case where TrueSDK is not supported
    }
}

RCT_EXPORT_METHOD(requestProfile) {
    dispatch_async(dispatch_get_main_queue(), ^{
        [[TCTrueSDK sharedManager] requestTrueProfile];
    });
}


+ (BOOL)handleUserActivity:(NSUserActivity *)userActivity
        restorationHandler:(void (^)(NSArray *restorableObjects))restorationHandler {
    return [[TCTrueSDK sharedManager] application:[UIApplication sharedApplication] continueUserActivity:userActivity restorationHandler:restorationHandler];
}


- (void)didReceiveTrueProfile:(nonnull TCTrueProfile *)profile {
    
    NSDictionary *profileData = @{
        @"firstName": profile.firstName ?: [NSNull null],
        @"lastName": profile.lastName ?: [NSNull null],
        @"email": profile.email ?: [NSNull null],
        @"phoneNumber": profile.phoneNumber ?: [NSNull null],
        @"countryCode": profile.countryCode ?: [NSNull null],
        @"gender": (profile.gender == 0) ? [NSNull null] : @(profile.gender),
    };
    
    [self sendEventWithName:@"TruecallerIOSSuccess" body:profileData];
}

- (void)didFailToReceiveTrueProfileWithError:(nonnull TCError *)error {
    
    NSInteger errorCode = [error getErrorCode];
    
    NSDictionary *errorData = @{
        @"errorCode": (errorCode == 0) ? [NSNull null] : @(errorCode),
        @"errorMessage": error.localizedDescription ?: [NSNull null],
    };
    
    [self sendEventWithName:@"TruecallerIOSFailure" body:errorData];
}

@end
