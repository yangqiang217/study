//
//  TabItem.h
//  ImageViewer
//
//  Created by YangQiang on 2019/4/30.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface TabItem : NSObject

@property(nonatomic, strong) NSString *tabName;
@property(nonatomic, assign) int tabIndex;

+(TabItem *) convert :(NSDictionary *) dic;

@end

NS_ASSUME_NONNULL_END
