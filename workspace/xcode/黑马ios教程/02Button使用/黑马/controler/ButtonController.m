//
//  ViewController.m
//  02Button使用
//
//  Created by YangQiang on 2019/12/12.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import "ButtonController.h"

@interface ButtonController ()
@property (weak, nonatomic) IBOutlet UIButton *btnMe;

- (IBAction)clickMe:(id)sender;
- (IBAction)move:(UIButton *)sender;
- (IBAction)clickPlus:(id)sender;
- (IBAction)clickMinus:(id)sender;
- (IBAction)clickAnim:(UIButton *)sender;
- (IBAction)clickBlockAnim:(UIButton *)sender;
@end

@implementation ButtonController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}


- (IBAction)clickMe:(id)sender {
    NSLog(@"me");
}

//移动，多个btn绑定一个方法
- (IBAction)move:(UIButton *)sender {
//    self.btnMe.frame.origin.x = 10;语法：结构体属性不能直接赋值
    CGRect frame = self.btnMe.frame;
    switch (sender.tag) {
        case 1:
            frame.origin.x -= 10;
            break;
        case 2:
            frame.origin.y -= 10;
            break;
        case 3:
            frame.origin.x += 10;
            break;
        case 4:
            frame.origin.y += 10;
            break;
        default:
            break;
    }
    self.btnMe.frame = frame;
}

- (IBAction)clickPlus:(id)sender {
    CGRect frame = self.btnMe.frame;
    frame.size = CGSizeMake(frame.size.width + 10, frame.size.height + 10);
    self.btnMe.frame = frame;
}

- (IBAction)clickMinus:(id)sender {
    CGRect frame = self.btnMe.frame;
    frame.size.width -= 10;
    frame.size.height -= 10;
    self.btnMe.frame = frame;
}

//头尾式动画
- (IBAction)clickAnim:(UIButton *)sender {
    CGRect frame = self.btnMe.frame;
    frame.origin.x += 100;
    frame.size.width += 100;
    frame.size.height += 100;
    
    //头尾式动画
    [UIView beginAnimations:nil context:nil];
    [UIView setAnimationDuration:0.5];//s
    //set代码放在这中间
    self.btnMe.frame = frame;
    [UIView commitAnimations];
}

//block式动画
- (IBAction)clickBlockAnim:(UIButton *)sender {
    CGRect frame = self.btnMe.frame;
    frame.origin.x += 10;
    frame.size.width += 100;
    frame.size.height += 100;
    [UIView animateWithDuration:0.5 animations:^{
        self.btnMe.frame = frame;
    }];
}


- (void) views{
    //遍历所有子view
    for (UIView * view in self.view.subviews) {
        //...
    }
    
    //拿到父view
    self.btnMe.superview.backgroundColor = [UIColor redColor];
    
    //根据tag获取view，然后强转
    UIView *view = [self.view viewWithTag:1000];
    
    //移除
    [self.btnMe removeFromSuperview];
}
@end
