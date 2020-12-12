//
//  FeedViewController.m
//  ImageViewer
//
//  Created by YangQiang on 2019/3/6.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import "FeedController.h"

#define CELL_IDENTIFIER @"FeedViewCell"

@interface FeedController () <UICollectionViewDataSource, CHTCollectionViewDelegateWaterfallLayout, UITextFieldDelegate>

@property (weak, nonatomic) IBOutlet UICollectionView *feedCollectView;
@property (weak, nonatomic) IBOutlet UITextField *searchBox;

@property (nonatomic, strong) NSMutableArray<CoverItem *> *fullCoverArray;
@property (nonatomic, strong) NSMutableArray<CoverItem *> *listCoverArray;

@end

@implementation FeedController
- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.fullCoverArray = [NSMutableArray new];
    self.listCoverArray = [NSMutableArray new];
    [self requestCoverList];
    
    CHTCollectionViewWaterfallLayout *layout = (CHTCollectionViewWaterfallLayout *)_feedCollectView.collectionViewLayout;
    layout.sectionInset = UIEdgeInsetsMake(0/*CGRectGetHeight([ScreenUtil getNotchRect])*/, 0, 0, 0);
    layout.minimumColumnSpacing = 0.5;
    layout.minimumInteritemSpacing = 0.5;
    layout.columnCount = COLUMN_COUNT;
    
    _feedCollectView.contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentNever;//避免顶部空白
    _feedCollectView.autoresizingMask = UIViewAutoresizingFlexibleHeight | UIViewAutoresizingFlexibleWidth;
    _feedCollectView.dataSource = self;
    _feedCollectView.delegate = self;
//    _feedCollectView.backgroundColor = [UIColor ];
}

- (void)requestCoverList {
    NSString *url = [NSString stringWithFormat:@"%@/%d/%d", Const.getCoverListUrl, self.tabIndex, SCREEN_W / COLUMN_COUNT];
    NSString *utf8Url = [url stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    manager.responseSerializer.acceptableContentTypes = [NSSet setWithObjects:@"application/json", @"text/json", @"text/javascript",@"text/html", nil];
    [manager GET:utf8Url parameters:nil progress:nil success:^(NSURLSessionTask *task, id responseObject) {
       
        NSArray *list = [responseObject objectForKey:@"coverlist"];
        for (NSDictionary *coverItemDic in list) {
            CoverItem *item = [CoverItem convert:coverItemDic tabIndex:self.tabIndex];
            [self.fullCoverArray addObject:item];
        }
        
        self.listCoverArray = [NSMutableArray arrayWithArray:self.fullCoverArray];
        [self.feedCollectView reloadData];
        NSLog(@"requestCoverList success");
    } failure:^(NSURLSessionTask *operation, NSError *error) {
        NSLog(@"requestCoverList Error: %@", error);
    }];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
}

#pragma mark - UICollectionViewDataSource

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return [self.listCoverArray count];
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    FeedViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:CELL_IDENTIFIER
                                                                   forIndexPath:indexPath];
    NSString *imgUrl = [self.listCoverArray objectAtIndex:indexPath.item].coverUrl;
    NSString *utf8ImgUrl = [imgUrl stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
//    [cell.imgCover sd_setImageWithURL:[NSURL URLWithString: utf8ImgUrl] placeholderImage:nil];
    [cell.imgCover pin_setImageFromURL:[NSURL URLWithString: utf8ImgUrl]];
    
    NSLog(@"url: %@, utfurl: %@", imgUrl, utf8ImgUrl);
    cell.labelTitle.text = [NSString stringWithFormat:@"%@|%@", [self.listCoverArray objectAtIndex:indexPath.item].modelName, [self.listCoverArray objectAtIndex:indexPath.item].title];
    cell.labelDate.text = [self.listCoverArray objectAtIndex:indexPath.item].pubDate;
    cell.labelCount.text = [NSString stringWithFormat:@"%dp", [self.listCoverArray objectAtIndex:indexPath.item].detailCount];
    
    if ([[cell.viewGradient.layer sublayers] count] == 0) {
        CAGradientLayer *gradient = [CAGradientLayer layer];
        gradient = [CAGradientLayer layer];
        gradient.frame = cell.viewGradient.bounds;
        gradient.colors = @[(id)[ColorUtil colorWithHexString:@"00000000"].CGColor, (id)[ColorUtil colorWithHexString:@"77181820"].CGColor];
        [cell.viewGradient.layer insertSublayer:gradient atIndex:0];
    }
    return cell;
}

#pragma mark - CHTCollectionViewDelegateWaterfallLayout
- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    float serverWidth = [self.listCoverArray objectAtIndex:indexPath.item].coverWidth;
    float serverHeight = [self.listCoverArray objectAtIndex:indexPath.item].coverHeight;
    
    int width = (SCREEN_WIDTH - 3) / 3;
    int height = width * serverHeight / serverWidth;
//    NSLog(@"name: %@, wh: %d, %d, swh: %f, %f", [self.listCoverArray objectAtIndex:indexPath.item].title, width, height, serverWidth, serverHeight);
    return CGSizeMake(width, height);
}

//这里的sender就是cell对象
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([segue.identifier isEqualToString:@"toDetail"]) {
        //get index
        NSIndexPath *indexPath = [self.feedCollectView indexPathForCell:sender];
        
        DetailListController *receive = segue.destinationViewController;
        FeedViewCell *cell = sender;
        receive.picTitle = cell.labelTitle.text;
        receive.groupName = [self.listCoverArray objectAtIndex:indexPath.item].group_path;
        receive.tabIndex = self.tabIndex;
    }
}

- (IBAction)onTextChange:(id)sender {
    NSString *content = self.searchBox.text;
    
    if (content.length == 0) {
        self.listCoverArray = [NSMutableArray arrayWithArray:self.fullCoverArray];
    } else {
        [self.listCoverArray removeAllObjects];
        for (CoverItem *item in self.fullCoverArray) {
            if (item.modelName == nil) {
                continue;
            }
            if ([item.modelName rangeOfString:content].location != NSNotFound) {
                [self.listCoverArray addObject:item];
            }
        }
    }
    
    [self.feedCollectView reloadData];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    NSLog(@"textField get focus, press return");
    [textField resignFirstResponder];
    return YES;
}

- (IBAction)onToTopClick:(id)sender {
    [self.feedCollectView setContentOffset:CGPointZero animated:YES];
}

- (IBAction)onRefreshClick:(id)sender {
    [self requestCoverList];
}

@end
