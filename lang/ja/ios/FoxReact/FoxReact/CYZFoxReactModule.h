//
//  CYZFoxReactModule.h
//  CYZFoxReactModule
//
//  Created by 呉 維 on 2017/10/18.
//  Copyright © 2017 cyberz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>
#import <React/RCTConvert.h>

#import <CYZFox/CYZFox.h>

@interface CYZFoxReactModule : NSObject <RCTBridgeModule>

@end

@interface RCTConvert (CYZFox)

+ (CYZFoxConfig *)CYZFoxConfig:(id)json;
+ (CYZFoxTrackOption *)CYZFoxTrackOption:(id)json;
+ (CYZFoxEvent *)CYZFoxEvent:(id)json;

@end
