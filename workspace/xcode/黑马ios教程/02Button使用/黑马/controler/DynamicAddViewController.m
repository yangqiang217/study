//
//  DynamicAddViewController.m
//  动态添加按钮
//  Created by YangQiang on 2019/12/12.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import "DynamicAddViewController.h"

@interface DynamicAddViewController ()

@end

@implementation DynamicAddViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    UIButton *button = [[UIButton alloc] init];
//    UIButton *button = [UIButton buttonWithType:UIButtonTypeSystem];
    
    //加载图片文件
//    UIImage *image = [UIImage imageNamed:@"img_name"];
    
    [button setTitle:@"点我吧" forState:(UIControlStateNormal)];
    [button setTitle:@"高亮了" forState:(UIControlStateHighlighted)];
    
    [button setTitleColor:[UIColor whiteColor] forState:(UIControlStateNormal)];
    [button setTitleColor:[UIColor blueColor] forState:(UIControlStateHighlighted)];
    
    [button setBackgroundImage:[UIImage imageNamed:@"black"] forState:(UIControlStateNormal)];
    [button setBackgroundImage:[UIImage imageNamed:@"red"] forState:(UIControlStateHighlighted)];
    
    [button setTag:1001];
    
    button.frame = CGRectMake(50, 50, 100, 100);
    
    //当前对象的buttonClick方法
    [button addTarget:self action:@selector(buttonClick) forControlEvents:UIControlEventTouchUpInside];
    
    [self.view addSubview:button];
    //移除：
//    [button removeFromSuperview];
    //移除所有
//    while (self.view.subviews.firstObject) {
//        [self.view.subviews.firstObject removeFromSuperview];
//    }
}

//没有拖线就不用IBAction用void
- (void)buttonClick {
    //获取当前控制器管理的所有子view
    NSLog([NSString stringWithFormat:@"%d", [self.view.subviews count]]);
    for (UIView *view in self.view.subviews) {
        view.backgroundColor = [UIColor yellowColor];
    }
    
    //获取父view
    [self.view.subviews objectAtIndex:0].superview.backgroundColor = [UIColor yellowColor];
    
    //利用tag获取到view
    UIButton *btn = [self.view viewWithTag:1001];
    [btn setTitle:[NSString stringWithFormat:@"tag:%d", 1001] forState:UIControlStateNormal];
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
