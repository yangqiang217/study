#import <Foundation/Foundation.h>
#import "Dog.h"
#import "Gender.h"
#import "ExceptionTest.h"
#import "StringTest.h"
#import "SettingTest.h"
#import "Husky.h"

/* 类的声明,声明和实现必须都有
 isa指向person类在代码段中的地址，所有同类的对象同方法指向同一段
 
 NULL:
 为指针变量的值，NULL表示谁都不指，其实就是个宏：0
 nil:
 同为指针变量值，也其实就是个宏：0
 所以NULL和nil是一样的，
 c指针用NULL: int *p1 = NULL;
 oc的类指针用nil: Person *p = nil;
 
 super只能访问父类的方法，不能访问变量
 */
@interface Person : NSObject {
    @public
    NSString *_name;//属性名以下划线开头
    int _age /* = 1 不允许在声明的时候赋值*/;
    float _height;
    Gender _gender;
    Dog *dog;
    //这里面不能放方法
}
//return void
- (void) run;
//one param method, param: foodName
- (void) eat: (NSString *)foodName;
//more params, param include: "[alias]:(type)name"
- (int) sum /*param1*/:(int)num1 /*param2*/and/*可以比param1多个别名*/:(int)num2;
//not implemented method
- (void) noImplement;
//class as param
- (Dog *) classFunction :(Dog *)dog gender:(Gender)gender;
//static
- (void) staticTest;
@end

/* 类的实现
 
 */
@implementation Person

- (void) run {
    NSLog(@"in run");
}
#pragma mark - eatmethod
- (void) eat: (NSString *)foodName {
    NSLog(@"in eat, foodName: %@", foodName);
}
#pragma mark summethod
- (int) sum: (int)num1 and:(int)num2 {
    int res = num1 + num2;
    NSLog(@"in sum");
    return res;
}
- (Dog *) classFunction :(Dog *)dog gender:(Gender)gender; {
    NSLog(@"in classfunction, gender: %li", gender);
    [dog sayHi];
    dog.name = @"name after classFunction";
    return dog;
}
- (void) staticTest {
    //static 不能修饰属性和方法，可以修饰局部变量
    static int a =  10;
    a++;
    NSLog(@"in staticTest, a: %d", a);
}
@end

//这样写没有声明也可以，但不建议
@implementation AA : NSObject {
    NSString * name;
}
- (void) run {
    NSLog(@"aa run");
}
@end

int main(int argc, const char * argv[]) {
    @autoreleasepool {
        // insert code here...
        NSString *str = @"jack";
        NSLog(@"print str: %@", str);
        
        BOOL b1 = YES;//or NO
        NSLog(@"BOOL: %d", b1);
        
        Boolean b2 = true;//or false
        NSLog(@"Boolean: %d", b2);
        
        NSLog(@"float: %f", 1.2f);
        
        Person *p1 = [Person new];
        Person *p11 = p1;
        
        p1 -> _gender = Male;
        p1 -> _name = @"yq";//priority
        (*p1)._name = @"yq2";
        //call field
        NSLog(@"p1's name: %@", p1 -> _name);
        //method call
        [p1 run];//no param, call func
        [p1 eat: @"noodle"];//1 param
        int sum = [p1 sum:1 and:2];//more param
        NSLog(@"get sum: %d", sum);
        
        Person *p2 = nil;
        [p2 run];//不会被调用
//        p2 -> _name = @"shit";//会报错
        
//        Person *p3 = [Person new];
//        [p3 noImplement];//会报错，如果要调用，必须要实现
        
        Dog *d1 = [Dog new];
        //class as param
        Person *p4 = [Person new];
        Dog *d2 = [p4 classFunction:d1 gender:Female];
        NSLog(d2.name);
        
        p4 -> dog = d1;
        p4 -> dog.name = @"yq's dog";
        [p4 -> dog sayHi];
        
        [ExceptionTest testException];
        [StringTest stringTest];
        [SettingTest star];
        
        //static
        Person *p5 = [Person new];
        [p5 staticTest];
        [p5 staticTest];
        
        
        //extend
        Husky *husky = [Husky new];
        [husky setAge:13];
        [husky sayHi];
        //description
//        NSLog(@"%@", husky);
        
        Dog *d3 = [Dog new];
        //.语法
        d3.age = 333;//= [d1 setAge: 1];
        NSLog(@"d3 age: %d", d3.age);//= [d1 age];//所以这里一定要注意，get方法必须是变量名去掉_，而不是getXXX，否则.age都打不出来
        
        Dog *d4 = [Dog new];
        d4.age = 444;
        d4.height = 134;
        
        /* id和NSObject，区别：调用方法id不需要强转。
         id只能调方法，且不能用.语法
         */
        NSObject *ob1 = [Dog new];
        id id1 = [Dog new];
        [(Dog *)ob1 sayHi];
        [id1 sayHi];
        
        //判断d4是否有sayHi这个方法
        BOOL bb1 = [d4 respondsToSelector:@selector(sayHi)];
        BOOL bb12 = [Dog instancesRespondToSelector:@selector(sayHi)];//dog有没有类方法
        //是否为指定类的对象或子类对象 instanceof
        BOOL bb2 = [husky isKindOfClass:[Dog class]];//true
        BOOL bb22 = [d4 isKindOfClass:[Husky class]];//false
        //是否为指定类的对象，不包括子对象
        BOOL bb3 = [husky isMemberOfClass:[Dog class]];
        //是否是子类
        BOOL bb4 = [Husky isSubclassOfClass:[Dog class]];
        
        //init
        Dog *d5 = [[Dog alloc] init];//=[Person new]
        Dog *d6 = [[Dog alloc] init:@"yq" age:2];
        
        //weak point
        __weak Dog *d7;
        
        int num = 10;
        NSNumber *n0 = @(num);
        NSNumber *n1 = @10;//等价于：
        NSNumber *n2 = [NSNumber numberWithInt:10];

        NSLog(@"end");
        
        
        NSString *sss = @"sd_f";
        NSArray *aa = [sss componentsSeparatedByString:@"_"];
        NSLog(@"%d", [aa count]);
    }
    return 0;
}
