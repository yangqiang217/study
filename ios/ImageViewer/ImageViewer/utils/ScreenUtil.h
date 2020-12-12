//
//  ScreenUtil.h
//  ImageViewer
//
//  Created by YangQiang on 2019/3/7.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

#define SCREEN_WIDTH ([UIScreen mainScreen].bounds.size.width)
#define SCREEN_HEIGHT ([UIScreen mainScreen].bounds.size.height)

NS_ASSUME_NONNULL_BEGIN

static float tabBarHeight;

@interface ScreenUtil : NSObject



+ (CGRect) getNotchRect;
@end

NS_ASSUME_NONNULL_END
