//
//  HMTransformController.m
//  黑马
//
//  Created by YangQiang on 2020/3/26.
//  Copyright © 2020 YangQiang. All rights reserved.
//

#import "HMTransformController.h"

@interface HMTransformController ()
@property (weak, nonatomic) IBOutlet UIButton *btnIcon;

- (IBAction)translation;
- (IBAction)scale;
- (IBAction)rotation;
- (IBAction)anim;
- (IBAction)reset;
@end

@implementation HMTransformController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (IBAction)scale {
    //从原始大小缩放，点多次会不动
//    self.btnIcon.transform = CGAffineTransformMakeScale(2, 2);//2是放大倍率
    //从上次大小缩放，多次点击继续缩放
    self.btnIcon.transform = CGAffineTransformScale(self.btnIcon.transform, 2, 2);
}

- (IBAction)translation {
    //从原始位置移动到-50，点多次会不动
//    self.btnIcon.transform = CGAffineTransformMakeTranslation(0, -50);//向上平移
    //从上次位置移动-50，多次点击继续移动
    self.btnIcon.transform = CGAffineTransformTranslate(self.btnIcon.transform, 0, -50);
}


- (IBAction)rotation {
    //从原始方向旋转，点多次会不动
//    self.btnIcon.transform = CGAffineTransformMakeRotation(M_PI_2);//单位弧度（90度是π/2）
    //从上次方向旋转，多次点击继续旋转
    self.btnIcon.transform = CGAffineTransformRotate(self.btnIcon.transform, M_PI_2);
}

- (IBAction)anim {
    //把上面操作放在动画里(???这里为什么动画前会移动一下???)
    [UIView animateWithDuration:3 animations:^{
        self.btnIcon.transform = CGAffineTransformScale(self.btnIcon.transform, 2, 2);
        self.btnIcon.transform = CGAffineTransformTranslate(self.btnIcon.transform, 0, -50);
        self.btnIcon.transform = CGAffineTransformRotate(self.btnIcon.transform, M_PI_2);
    }];
}

- (IBAction)reset {
    self.btnIcon.transform = CGAffineTransformIdentity;
}
@end
