//
//  ScreenUtil.m
//  ImageViewer
//
//  Created by YangQiang on 2019/3/7.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import "ScreenUtil.h"

@implementation ScreenUtil

+ (CGRect)getNotchRect {
    return [[UIApplication sharedApplication] statusBarFrame];
}

@end
