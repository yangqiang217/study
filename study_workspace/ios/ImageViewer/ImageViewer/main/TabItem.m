//
//  TabItem.m
//  ImageViewer
//
//  Created by YangQiang on 2019/4/30.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import "TabItem.h"

@implementation TabItem

+(TabItem *) convert:(NSDictionary *) dic {
    TabItem *res = [TabItem new];
    for (NSString *key in dic) {
        if ([key isEqualToString:@"tab_name"]) {
            res.tabName = dic[key];
        } else if ([key isEqualToString:@"tab_index"]) {
            res.tabIndex = (int)dic[key];
        }
    }
    return res;
}
@end
