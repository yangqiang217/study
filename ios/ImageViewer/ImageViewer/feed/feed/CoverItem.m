//
//  CoverItem.m
//  ImageViewer
//
//  Created by YangQiang on 2019/3/10.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import "CoverItem.h"

@implementation CoverItem

+(CoverItem *) convert:(NSDictionary *) dic tabIndex:(int)tabIndex {
    CoverItem *res = [CoverItem new];
    for (NSString *key in dic) {
        if ([key isEqualToString:@"title"]) {
            res.title = dic[key];
        } else if ([key isEqualToString:@"model_name"]) {
            res.modelName = dic[key];
        } else if ([key isEqualToString:@"pub_date"]) {
            res.pubDate = dic[key];
        } else if ([key isEqualToString:@"img_path"]) {
            res.coverUrl = [NSString stringWithFormat:@"%@/%d/%@", Const.getBaseImgUrl, tabIndex, dic[key]];
        } else if ([key isEqualToString:@"group_path"]) {
            res.group_path = dic[key];
        } else if ([key isEqualToString:@"img_count"]) {
            res.detailCount = [dic[key] intValue];
        } else if ([key isEqualToString:@"img_width"]) {
            res.coverWidth = [dic[key] floatValue];
        } else if ([key isEqualToString:@"img_height"]) {
            res.coverHeight = [dic[key] intValue];
        }
    }
    return res;
}

@end
