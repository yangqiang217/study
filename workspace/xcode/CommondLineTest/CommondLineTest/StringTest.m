//
//  StringTest.m
//  CommondLineTest
//
//  Created by YangQiang on 2019/3/2.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import "StringTest.h"

@implementation StringTest
+ (void) stringTest {
    char *strWithC = "rose";
    NSString *ocStr = [NSString stringWithUTF8String:strWithC];//c的串转成oc
    
    int age = 10;
    NSString *name = @"yq";
    NSString *my = [NSString stringWithFormat:@"hi, i am %@, i am %d", name, age];//拼接串
    
    NSUInteger len = [my length];//length
    
    unichar ch = [my characterAtIndex:2];//charAtIndex
    
    NSString *s1 = @"jack";
    NSString *s2 = [NSString stringWithFormat:@"jack"];
    if (s1 == s2) {
        //false, same as java
    }
    if ([s1 isEqualToString:s2]) {
        //true
    }
    
    NSComparisonResult res = [name compare:my];
    NSLog(@"len: %C", ch);
}
@end
