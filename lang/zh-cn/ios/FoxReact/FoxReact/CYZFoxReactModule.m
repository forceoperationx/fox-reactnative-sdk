//
//  CYZFoxReactModule.m
//  CYZFoxReactModule
//
//  Created by 呉 維 on 2017/10/18.
//  Copyright © 2017 cyberz. All rights reserved.
//

#import "CYZFoxReactModule.h"

NSString* EVENT_trackingCompletionHandler = @"CYZFox_trackingCompletionHandler";

@implementation CYZFoxReactModule {
    void (^trackingCompletionHandler)(void);
    void (^deferredDeeplinkHandler)(NSDictionary * _Nullable deeplinkInfo);
}

RCT_EXPORT_MODULE(CYZFoxReact);

RCT_EXPORT_METHOD(activate:(CYZFoxConfig*) config)
{
    RCTLogInfo(@"[FOX-RCT] config activate: %@", config);
    [config activate];
}

RCT_EXPORT_METHOD(trackInstall)
{
    RCTLogInfo(@"[FOX-RCT] trackInstall");
    [CYZFox trackInstall];
}

RCT_EXPORT_METHOD(setDeferredDeeplinkHandler:(RCTResponseSenderBlock) handler)
{
    RCTLogInfo(@"[FOX-RCT] setDeferredDeeplinkHandler");
    self->deferredDeeplinkHandler = ^(NSDictionary * _Nullable deeplinkInfo) {
        if (deeplinkInfo) {
            handler(@[deeplinkInfo]);
        } else {
            handler(@[]);
        }
    };
}

RCT_EXPORT_METHOD(setTrackingCompletionHandler:(RCTResponseSenderBlock) handler)
{
    RCTLogInfo(@"[FOX-RCT] setTrackingCompletionHandler");
    self->trackingCompletionHandler = ^(void){
        handler(@[]);
    };
}

RCT_EXPORT_METHOD(trackInstall:(CYZFoxTrackOption*) option)
{
    RCTLogInfo(@"[FOX-RCT] trackInstall: %@", option);
    if (self->trackingCompletionHandler) {
        option.trackingCompletionHandler = self->trackingCompletionHandler;
    }
    if (self->deferredDeeplinkHandler) {
        option.deferredDeeplinkHandler = self->deferredDeeplinkHandler;
    }

    [CYZFox trackInstallWithOption:option];
}

RCT_EXPORT_METHOD(trackSession)
{
    RCTLogInfo(@"[FOX-RCT] trackSession");
    [CYZFox trackSession];
}

RCT_EXPORT_METHOD(trackEvent:(CYZFoxEvent*) event)
{
    RCTLogInfo(@"[FOX-RCT] trackEvent: %@", event);
    [CYZFox trackEvent:event];
}

RCT_EXPORT_METHOD(setUserInfo:(NSDictionary*) userInfo)
{
    RCTLogInfo(@"[FOX-RCT] setUserInfo: %@", userInfo);
    [CYZFox setUserInfo:userInfo];
}

RCT_EXPORT_METHOD(getUserInfo:(RCTResponseSenderBlock)callback)
{
    NSDictionary* userInfo = [CYZFox getUserInfo];
    RCTLogInfo(@"[FOX-RCT] getUserInfo = %@", userInfo);
    callback(@[[NSNull null], userInfo]);
}

RCT_EXPORT_METHOD(isConversionCompleted:(RCTResponseSenderBlock)callback)
{
    BOOL isConversionCompleted = [CYZFox isConversionCompleted];
    RCTLogInfo(@"[FOX-RCT] isConversionCompleted = %@", isConversionCompleted ? @"YES" : @"NO");
    callback(@[[NSNull null], [NSNumber numberWithBool:isConversionCompleted]]);
}

RCT_EXPORT_METHOD(trackEventByBrowser:(NSString*) redirectURL)
{
    RCTLogInfo(@"[FOX-RCT] trackEventByBrowser: %@", redirectURL);
    [CYZFox trackEventByBrowser:redirectURL];
}

@end


@implementation RCTConvert (CYZFox)

+ (CYZFoxConfig *)CYZFoxConfig:(id)json {
    RCTLogInfo(@"[FOX-RCT] Convert CYZFoxConfig");
    NSUInteger appId = [[RCTConvert NSNumber:json[@"appId"]] unsignedIntegerValue];
    NSString* salt = [RCTConvert NSString:json[@"appSalt"]];
    NSString* appKey = [RCTConvert NSString:json[@"appKey"]];

    CYZFoxConfig* config = [CYZFoxConfig configWithAppId:appId salt:salt appKey:appKey];
    if ([RCTConvert BOOL:json[@"debugModeEnabled"]]) {
        [config enableDebugMode];
    }
    if ([RCTConvert BOOL:json[@"customizedUserAgentEnabled"]]) {
        [config enableCustomizedUserAgent];
    }

    return config;
}

+ (CYZFoxTrackOption *)CYZFoxTrackOption:(id)json {
    RCTLogInfo(@"[FOX-RCT] Convert CYZFoxTrackOption");
    CYZFoxTrackOption* option = [CYZFoxTrackOption new];

    option.buid = [RCTConvert NSString:json[@"buid"]];
    option.redirectURL = [RCTConvert NSString:json[@"redirectURL"]];
    option.optout = [RCTConvert BOOL:json[@"optout"]];
    option.durationSinceClick = [RCTConvert double:json[@"durationSinceClick"]];
    return option;
}

+ (CYZFoxEvent *)CYZFoxEvent:(id)json {
    RCTLogInfo(@"[FOX-RCT] Convert CYZFoxEvent");
    CYZFoxEvent* event = [[CYZFoxEvent alloc] initWithEventName:[RCTConvert NSString:json[@"eventName"]] ltvId:[RCTConvert NSUInteger:json[@"ltvId"]]];

    event.buid = [RCTConvert NSString:json[@"buid"]];
    event.value = [RCTConvert NSUInteger:json[@"value"]];
    event.orderId = [RCTConvert NSString:json[@"orderId"]];
    event.sku = [RCTConvert NSString:json[@"sku"]];
    event.itemName = [RCTConvert NSString:json[@"itemName"]];
    event.price = [RCTConvert double:json[@"price"]];
    event.quantity = [RCTConvert NSUInteger:json[@"quantity"]];
    event.currency = [RCTConvert NSString:json[@"currency"]];
    event.eventInfo = [RCTConvert NSDictionary:json[@"eventInfo"]];

    [[RCTConvert NSDictionary:json[@"extraInfo"]] enumerateKeysAndObjectsUsingBlock:^(id  _Nonnull key, id  _Nonnull obj, BOOL * _Nonnull stop) {
        [event addExtraValue:obj forKey:key];
    }];

    return event;
}

@end
