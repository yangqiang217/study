//
//  ExceptionTest.m
//  CommondLineTest
//
//  Created by YangQiang on 2019/3/2.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import "ExceptionTest.h"
#import "Dog.h"

@implementation ExceptionTest

+ (void) testException {
    NSArray *array = [NSArray new];
    @try {
        NSLog(@"obj: %@", [array objectAtIndex:0]);
        
//        Dog *d = [Dog new];
//        d -> _name = @"name";//-------------这种异常无法捕获
    } @catch (NSException *exception) {
        NSLog(@"%@", exception);
    } @finally {
        NSLog(@"in finally");
    }
    NSLog(@"after finally");
}

@end
