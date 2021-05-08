//
//  ViewController.m
//  01加法计算
//
//  Created by YangQiang on 2019/12/12.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import "ViewController.h"

@interface ViewController ()
- (IBAction)compute;
@property (weak, nonatomic) IBOutlet UITextField *tvNum1;
@property (weak, nonatomic) IBOutlet UITextField *tvNum2;
@property (weak, nonatomic) IBOutlet UILabel *lbRes;
@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}
//相当于void，但void拖的时候不会做关联
- (IBAction)compute {
    NSString *num1 = self.tvNum1.text;
    NSString *num2 = self.tvNum2.text;
    
    int n1 = [num1 intValue];
    int n2 = [num2 intValue];
    
    int res = n1 + n2;
    self.lbRes.text = [NSString stringWithFormat: @"%d", res];
    
    //收回键盘option 1，第一响应者resign
//    [self.tvNum2 resignFirstResponder];
    
    //收回键盘option 2，第一响应者resign
    //self.view 当前控制器管理的view，让控制器管理的view停止编辑，子view叫出的键盘都回去了
    [self.view endEditing:YES];
}

@end
