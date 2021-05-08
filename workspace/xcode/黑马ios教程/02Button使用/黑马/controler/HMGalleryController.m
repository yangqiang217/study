//
//  HMGalleryController.m
//  plist简单使用
//
//  Created by YangQiang on 2020/3/26.
//  Copyright © 2020 YangQiang. All rights reserved.

//  标号和名字、图片要对应，所以建一个plist：newfile-resourse-property list
//

#import "HMGalleryController.h"

@interface HMGalleryController ()
@property (weak, nonatomic) IBOutlet UIImageView *imgView;
@property (weak, nonatomic) IBOutlet UILabel *lbNum;
@property (weak, nonatomic) IBOutlet UILabel *lbName;
@property (weak, nonatomic) IBOutlet UIButton *btnPre;
@property (weak, nonatomic) IBOutlet UIButton *btnNext;

- (IBAction)pre;
- (IBAction)next;

@property (nonatomic, strong) NSArray *pic;
@property (nonatomic, assign) int index;
@end

@implementation HMGalleryController

//重写pic的get方法
- (NSArray *)pic {
    if (_pic == nil) {
        //NSBundle mainBundle表示获取app安装在手机上的根目录。:@"gallery.plist" ofType:nil]等同于:@"gallery" ofType:@".plist"];
        NSString *path = [[NSBundle mainBundle] pathForResource:@"gallery.plist" ofType:nil];
        _pic = [NSArray arrayWithContentsOfFile:path];//拿到里面就包含dictionary了
    }
    return _pic;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.index = -1;
    [self next];
    
    
    //dictionary和array创建
    NSDictionary *dic = @{
        @"name": @"zhangsan",
        @"age": @18//字典不能放简单数据，需要转成oc对象
    };
    NSArray *student = @[dic, dic];
    NSLog(@"%@", student);
}

- (IBAction)next {
    self.index++;
    [self setData];
}

- (IBAction)pre {
    self.index--;
    [self setData];
}

- (void) setData {
    NSDictionary *dic = self.pic[self.index];
    //%ld表示long int
    self.lbNum.text = [NSString stringWithFormat:@"%d/%ld", self.index + 1, self.pic.count];
    self.imgView.image = [UIImage imageNamed:dic[@"icon"]];
    self.lbName.text = dic[@"title"];
    
    self.btnPre.enabled = (self.index != 0);
    self.btnNext.enabled = (self.index != (self.pic.count - 1));
}
@end
