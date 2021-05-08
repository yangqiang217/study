//
//  Person.m
//  CommondLineTest
//
//  Created by YangQiang on 2019/3/2.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Dog.h"
@implementation Dog {
    //写在实现里面是真私有，xcode都不会提示，和写在声明里的唯一区别就是是不是xcode会不会提示
    //任何访问修饰符都无效，反正都是真私有
    @public
    NSString *sex;
}

- (instancetype)init {
    if (self = [super init]) {//super会初始化isa指针类似的属性。可能失败，
        self.name = @"yq";
    }
    return self;
}

- (instancetype) init :(NSString *)name age:(int)age {
    if (self == [super init]) {
        self.age = age;
        self.name = name;
    }
    return self;
}

- (void) sayHi {
    NSLog(@"wang wang wang, i am %@", _name);
}

- (NSString *)description {
    return [NSString stringWithFormat:@"dog, age: %d", _age];
}

//不要声明，只写实现，就是私有方法
- (void) haveSex {
    NSLog(@"have sex");
}

@end
