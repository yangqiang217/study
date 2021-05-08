//
//  HMFrameAnimController.m
//  黑马
//
//  Created by YangQiang on 2020/3/26.
//  Copyright © 2020 YangQiang. All rights reserved.
//

#import "HMFrameAnimController.h"

@interface HMFrameAnimController ()
@property (weak, nonatomic) IBOutlet UIImageView *img;

- (IBAction)start;
@end

@implementation HMFrameAnimController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}
- (IBAction)start {
//    UIImage *img1 = [UIImage imageNamed:@"red"];//!!!!这种方式加载的图片用完不会释放，导致动画结束了图片还在内存里
//    UIImage *img2 = [UIImage imageNamed:@"black"];
    
    NSString *path1 = [[NSBundle mainBundle] pathForResource:@"red.png" ofType:nil];
    UIImage *img1 = [UIImage imageWithContentsOfFile:path1];
    NSString *path2 = [[NSBundle mainBundle] pathForResource:@"black.png" ofType:nil];
    UIImage *img2 = [UIImage imageWithContentsOfFile:path2];
    
    //NSArray是不可变的，NSMutableArray是可变的
    NSMutableArray *arrayM = [NSMutableArray array];
    [arrayM addObject:img1];
    [arrayM addObject:img2];
    [self.img setAnimationImages:arrayM];//.animationImages = arrayM;
    
    self.img.animationDuration = arrayM.count * 0.5;
    self.img.animationRepeatCount = 1;//不设置就无限重复
    [self.img startAnimating];
    
    //清理内存
    [self.img performSelector:@selector(setAnimationImages:) withObject:nil afterDelay:arrayM.count * 0.5];
}
@end
