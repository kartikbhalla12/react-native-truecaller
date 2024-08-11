#import "TruecallerIOS.h"
#import "TrueSDK.h"
#import <React/RCTBridge.h>
#import <React/RCTRootView.h>

@implementation TruecallerIOS

RCT_EXPORT_MODULE();

- (NSArray<NSString *> *)supportedEvents {
    return @[@"TruecallerIOSSuccess", @"TruecallerIOSFailure"];
}

RCT_EXPORT_BLOCKING_SYNCHRONOUS_METHOD(isSupported) {
    @try {
        return @([[TCTrueSDK sharedManager] isSupported]);
    }
    @catch (NSException *exception) {
        [self sendTruecallerFailureEvent:0 message:exception.reason];
        return @NO;
    }
}

RCT_EXPORT_METHOD(initialize:(NSString *)appKey appLink:(NSString *)appLink) {
    if ([[TCTrueSDK sharedManager] isSupported]) {
        [[TCTrueSDK sharedManager] setupWithAppKey:appKey appLink:appLink];
        [TCTrueSDK sharedManager].delegate = self;
    } else {
        [self sendTruecallerFailureEvent:0 message:@"The Truecaller app is not installed or supported"];
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
    [self sendTruecallerFailureEvent:[error getErrorCode] message:error.localizedDescription];
}

- (void)sendTruecallerFailureEvent:(NSInteger)errorCode message:(NSString *)errorMessage {
    NSDictionary *errorData = @{
        @"errorCode": @(errorCode),
        @"errorMessage": errorMessage ?: (NSString *)[NSNull null]
    };
    
    [self sendEventWithName:@"TruecallerIOSFailure" body:errorData];
}

@end
