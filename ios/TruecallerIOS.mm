#import "TruecallerIOS.h"
#import "TrueSDK.h"
#import <React/RCTBridge.h>
#import <React/RCTRootView.h>

@implementation TruecallerIOS

RCT_EXPORT_MODULE();

- (NSArray<NSString *> *)supportedEvents {
  return @[@"TruecallerIOSSuccess", @"TruecallerIOSFailure"];
}

RCT_EXPORT_METHOD(initialize:(NSString *)appKey appLink:(NSString *)appLink) {
   if ([[TCTrueSDK sharedManager] isSupported]) {
        [[TCTrueSDK sharedManager] setupWithAppKey:appKey appLink:appLink];
     [TCTrueSDK sharedManager].delegate = self;
    } else {
        // Handle the case where TrueSDK is not supported
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
    NSLog(@"didReceiveTrueProfile %@", profile);
 
     NSLog(@"First Name: %@", profile.firstName);
     NSLog(@"Last Name: %@", profile.lastName);
     NSLog(@"Phone Number: %@", profile.phoneNumber);
     NSLog(@"Country Code: %@", profile.countryCode);
     NSLog(@"Email: %@", profile.email);
     NSLog(@"Gender: %lu", (unsigned long)profile.gender);
     NSLog(@"Is Verified: %d", profile.isVerified);
  
  NSDictionary *profileData = @{
     @"firstName": profile.firstName ?: [NSNull null],
     @"lastName": profile.lastName ?: [NSNull null],
     @"email": profile.email ?: [NSNull null],
     @"phoneNumber": profile.phoneNumber ?: [NSNull null]
   };
   [self sendEventWithName:@"TruecallerIOSSuccess" body:profileData];
}

- (void)didFailToReceiveTrueProfileWithError:(nonnull TCError *)error {
    NSLog(@"didFailToReceiveTrueProfileWithError %@", error);
    NSLog(@"Failed to receive True Profile with error: %@", error.localizedDescription);

     NSDictionary *errorData = @{
     @"errorCode": error ?: [NSNull null],
    //  @"errorMessage": profile.lastName ?: [NSNull null],
    //  @"email": profile.email ?: [NSNull null],
    //  @"phoneNumber": profile.phoneNumber ?: [NSNull null]
   };

    //  params.putInt("errorCode", tcOAuthError.getErrorCode());
    //   params.putString("errorMessage", tcOAuthError.getErrorMessage());

    //   sendEvent(reactContext, "TruecallerAndroidFailure", params);

      [self sendEventWithName:@"TruecallerIOSFailure" body:errorData];
}

@end
