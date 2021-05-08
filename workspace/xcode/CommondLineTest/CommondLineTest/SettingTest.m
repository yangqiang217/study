//
//  SettingTest.m
//  CommondLineTest
//
//  Created by YangQiang on 2019/3/2.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import "SettingTest.h"


@implementation SettingTest
+ (void) star {
    Dog *d = [Dog new];
//    d -> _age = 3;
    d.age = 13;
    NSLog(@"settingtest get age: %d", d.age);
}
@end
