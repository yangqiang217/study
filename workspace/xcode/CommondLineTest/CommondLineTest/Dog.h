//
//  Person.h
//  CommondLineTest
//
//  Created by YangQiang on 2019/3/2.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import <Foundation/Foundation.h>

#ifndef Person_h
#define Person_h

@interface Dog : NSObject
    //写在声明里面是假私有，xcode会提示
//    @public//如果是private，下面俩都是private的了，即作用于所有下面，直到碰到public/protect

@property NSString *name;//生成的是私有的，子类访问也得get set
@property int age;//自动生成_age属性,但如果要自己手动写get和set，那么property就不起作用了，_XX的变量要手动声明
@property int height;

- (instancetype) init :(NSString *)name age:(int)age;//名字无所谓

- (void) sayHi;

@end

#endif /* Person_h */
