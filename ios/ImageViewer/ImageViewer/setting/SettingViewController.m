//
//  SecondViewController.m
//  ImageViewer
//
//  Created by YangQiang on 2019/3/5.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import "SettingViewController.h"

#define KEY_IP @"ip"

@interface SettingViewController ()
@property (weak, nonatomic) IBOutlet UITextField *ipTextField;
@end

@implementation SettingViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.

    self.ipTextField.text = [Const getBaseUrl];
}


- (IBAction)onChangeIpConfirm:(id)sender {
    NSString *newIp = self.ipTextField.text;
    [Const saveBaseUrl:newIp];
}

@end
