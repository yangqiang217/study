//
//  ViewController.m
//  Test
//
//  Created by YangQiang on 2019/3/5.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import "ViewController.h"

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    [self.imageView sd_setImageWithURL:[NSURL URLWithString: @"http://192.168.10.110:9999/image/0/%E5%86%AC%E5%86%AC_%E7%9C%8B%E5%A5%B9%E4%BC%9A%E4%B8%8A%E7%98%BE_2019-10-18/1.jpg"] placeholderImage:nil];
}


@end
