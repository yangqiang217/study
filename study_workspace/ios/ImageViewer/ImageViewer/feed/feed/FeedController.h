//
//  FeedController.h
//  ImageViewer
//
//  Created by YangQiang on 2019/3/15.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CHTCollectionViewWaterfallLayout.h"
#import "FeedViewCell.h"
#import "DetailListController.h"
#import "Common.h"
#import "CoverItem.h"
#import "SettingViewController.h"
#import "UIImageView+WebCache.h"
#import "PINRemoteImage/PINImageView+PINRemoteImage.h"

NS_ASSUME_NONNULL_BEGIN

@interface FeedController : UIViewController
@property(nonatomic, assign) int tabIndex;
@end

NS_ASSUME_NONNULL_END
