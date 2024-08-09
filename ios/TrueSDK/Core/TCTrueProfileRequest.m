//
//  TCTrueProfileRequest.m
//  TrueSDK
//
//  Created by Guven Iscan on 23/12/2016.
//  Copyright © 2016 True Software Scandinavia AB. All rights reserved.
//

#import "TCTrueProfileRequest.h"
#import "NSURL+TrueSDK.h"

static NSString *kAppLinkKey = @"appLink";
static NSString *kAppKeyKey = @"appKey";
static NSString *kAppNameKey = @"appName";
static NSString *kAppIdKey = @"appId";
static NSString *kRequestNonceKey = @"requestNonce";
static NSString *kApiVersion = @"apiVersion";
static NSString *kSdkVersion = @"sdkVersion";
static NSString *kTitleType = @"titleType";
static NSString *kLocale = @"locale";
static NSString *kUrlScheme = @"urlScheme";

@implementation TCTrueProfileRequest 

-(void) encodeWithCoder:(NSCoder *)aCoder
{
    [aCoder encodeObject:self.appId forKey:kAppIdKey];
    [aCoder encodeObject:self.appKey forKey:kAppKeyKey];
    [aCoder encodeObject:self.appLink forKey:kAppLinkKey];
    [aCoder encodeObject:self.appName forKey:kAppNameKey];
    [aCoder encodeObject:self.requestNonce forKey:kRequestNonceKey];
    [aCoder encodeObject:self.apiVersion forKey:kApiVersion];
    [aCoder encodeObject:self.sdkVersion forKey:kSdkVersion];
    [aCoder encodeObject:@(self.titleType) forKey:kTitleType];
    [aCoder encodeObject:self.locale forKey:kLocale];
    [aCoder encodeObject:self.urlScheme forKey:kUrlScheme];
}

-(instancetype) initWithCoder:(NSCoder *)aDecoder
{
    self = [super init];
    if (self != nil) {
        _appId = [aDecoder decodeObjectForKey:kAppIdKey];
        _appKey = [aDecoder decodeObjectForKey:kAppKeyKey];
        _appLink = [aDecoder decodeObjectForKey:kAppLinkKey];
        _appName = [aDecoder decodeObjectForKey:kAppNameKey];
        _requestNonce = [aDecoder decodeObjectForKey:kRequestNonceKey];
        _apiVersion = [aDecoder decodeObjectForKey:kApiVersion];
        _sdkVersion = [aDecoder decodeObjectForKey:kSdkVersion];
        _titleType = [[aDecoder decodeObjectForKey:kTitleType] intValue];
        _locale = [aDecoder decodeObjectForKey:kLocale];
        _urlScheme = [aDecoder decodeObjectForKey:kUrlScheme];
    }
    return self;
}

@end
