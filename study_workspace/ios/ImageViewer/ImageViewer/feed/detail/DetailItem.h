//
//  DetailItem.h
//  ImageViewer
//
//  Created by YangQiang on 2019/3/14.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Const.h"

NS_ASSUME_NONNULL_BEGIN

@interface DetailItem : NSObject
@property(nonatomic, strong) NSString *imageThumbnailUrl;
@property(nonatomic, strong) NSString *imageOriUrl;
@property(nonatomic, strong) NSString *imgName;
@property(nonatomic, assign) float imgWidth;
@property(nonatomic, assign) float imgHeight;

+(DetailItem *) convert:(NSDictionary *) dic tabIndex: (int) tabIndex;
@end

NS_ASSUME_NONNULL_END
