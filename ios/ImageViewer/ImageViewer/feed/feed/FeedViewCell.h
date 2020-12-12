//
//  FeedViewCell.h
//  ImageViewer
//
//  Created by YangQiang on 2019/3/6.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Common.h"

NS_ASSUME_NONNULL_BEGIN

@interface FeedViewCell : UICollectionViewCell
@property (weak, nonatomic) IBOutlet UIImageView *imgCover;
@property (weak, nonatomic) IBOutlet UILabel *labelTitle;
@property (weak, nonatomic) IBOutlet UILabel *labelDate;
@property (weak, nonatomic) IBOutlet UILabel *labelCount;
@property (weak, nonatomic) IBOutlet UIView *viewGradient;

@end

NS_ASSUME_NONNULL_END
