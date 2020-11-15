//
//  CoverItem.h
//  ImageViewer
//
//  Created by YangQiang on 2019/3/10.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "JSONModel.h"
#import "Const.h"

NS_ASSUME_NONNULL_BEGIN

@interface CoverItem : NSObject

@property(nonatomic, strong) NSString *title;
@property(nonatomic, strong) NSString *pubDate;
@property(nonatomic, strong) NSString *modelName;
@property(nonatomic, strong) NSString *coverUrl;
@property(nonatomic, strong) NSString *group_path;
@property(nonatomic, assign) int detailCount;
@property(nonatomic, assign) float coverWidth;
@property(nonatomic, assign) float coverHeight;

+(CoverItem *) convert:(NSDictionary *) dic tabIndex:(int)tabIndex;
@end
NS_ASSUME_NONNULL_END
