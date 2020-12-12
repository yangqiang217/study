//
//  TestController.m
//  ImageViewer
//
//  Created by YangQiang on 2019/10/28.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import "TestController.h"

@interface TestController ()

@end

@implementation TestController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
//    [self.imageView sd_setImageWithURL:[NSURL URLWithString: @"http://192.168.10.110:9999/image/0/%E5%86%AC%E5%86%AC_%E7%9C%8B%E5%A5%B9%E4%BC%9A%E4%B8%8A%E7%98%BE_2019-10-18/1.jpg"] placeholderImage:nil];
    [self.imageView pin_setImageFromURL:[NSURL URLWithString:@"http://192.168.10.110:9999/image/0/%E5%86%AC%E5%86%AC_%E7%9C%8B%E5%A5%B9%E4%BC%9A%E4%B8%8A%E7%98%BE_2019-10-18/1.jpg"]];
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
