//
//  Const.m
//  ImageViewer
//
//  Created by YangQiang on 2019/3/10.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import "Const.h"

@implementation Const

+(NSString *) getBaseUrl {
    NSUserDefaults *ipDefaults = [NSUserDefaults standardUserDefaults];
    NSString *savedIp = [ipDefaults objectForKey:KEY_IP];
    if (savedIp.length == 0) {
        savedIp = @"http://192.168.31.15:9999/";
        [self saveBaseUrl:savedIp];
    }
    return savedIp;
//    return @"http://10.2.154.138:9999/";
}

+(void) saveBaseUrl :(NSString *)ip {
    if (ip.length == 0) {
        return;
    }
    NSUserDefaults *ipDefaults = [NSUserDefaults standardUserDefaults];
    [ipDefaults setObject:ip forKey:KEY_IP];
}

+(NSString *) getConfigUrl {
    return [NSString stringWithFormat:@"%@config", [self getBaseUrl]];
}

+(NSString *) getTabListUrl {
    return [NSString stringWithFormat:@"%@tablist", [self getBaseUrl]];
}

+(NSString *) getCoverListUrl {
    return [NSString stringWithFormat:@"%@coverlist", [self getBaseUrl]];
}

+(NSString *) getBaseImgUrl {
    return [NSString stringWithFormat:@"%@image", [Const getBaseUrl]];
}

+(NSString *) getDetailListUrl {
    return [NSString stringWithFormat:@"%@detaillist", [self getBaseUrl]];
}
@end
