//
//  DetailListController.m
//  ImageViewer
//
//  Created by YangQiang on 2019/3/7.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import "DetailListController.h"

#define CELL_IDENTIFIER @"DetailViewCell"

@interface DetailListController () <UITableViewDataSource, UITableViewDelegate>
@property (strong, nonatomic) IBOutlet UITableView *imgTableView;

@property (nonatomic, strong) NSMutableArray<DetailItem *> *imgArray;

@end

@implementation DetailListController


- (void)viewDidLoad {
    [super viewDidLoad];
    [self requestList];
    
    self.imgArray = [NSMutableArray new];
}

- (void)requestList {
    NSString *url = [NSString stringWithFormat:@"%@/%d/%d/%@", Const.getDetailListUrl, self.tabIndex, SCREEN_W, self.groupName];
    NSString *utf8Url = [url stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    manager.responseSerializer.acceptableContentTypes = [NSSet setWithObjects:@"application/json", @"text/json", @"text/javascript",@"text/html", nil];
    [manager GET:utf8Url parameters:nil progress:nil success:^(NSURLSessionTask *task, id responseObject) {
        
        NSArray *list = [responseObject objectForKey:@"detaillist"];
        for (NSDictionary *detailItemDic in list) {
            DetailItem *item = [DetailItem convert:detailItemDic tabIndex:self.tabIndex];
            [self.imgArray addObject:item];
        }
        
        [self.imgTableView reloadData];
        NSLog(@"requestCoverList success");
    } failure:^(NSURLSessionTask *operation, NSError *error) {
        NSLog(@"requestCoverList Error: %@", error);
    }];
}

#pragma mark - Table view data source

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [self.imgArray count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    DetailViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CELL_IDENTIFIER forIndexPath:indexPath];
    
    NSString *imgUrl = [self.imgArray objectAtIndex:indexPath.item].imageThumbnailUrl;
    NSString *utf8ImgUrl = [imgUrl stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
//    [cell.img sd_setImageWithURL:[NSURL URLWithString: utf8ImgUrl] placeholderImage:nil];
    [cell.img pin_setImageFromURL:[NSURL URLWithString: utf8ImgUrl]];
    
    return cell;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
    return self.picTitle;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    float serverWidth = [self.imgArray objectAtIndex:indexPath.item].imgWidth;
    float serverHeight = [self.imgArray objectAtIndex:indexPath.item].imgHeight;

    return SCREEN_WIDTH * serverHeight / serverWidth;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    WMPhotoBrowser *browser = [WMPhotoBrowser new];
    
    NSMutableArray *pathArray = [NSMutableArray arrayWithCapacity:[self.imgArray count]];
    for (DetailItem *detailItem in self.imgArray) {
        [pathArray addObject:detailItem.imageOriUrl];
    }
    browser.dataSource = pathArray;
    browser.deleteNeeded = YES;
    browser.currentPhotoIndex= indexPath.item;
    [self presentViewController:browser animated:YES completion:^{
        
    }];
}

@end
