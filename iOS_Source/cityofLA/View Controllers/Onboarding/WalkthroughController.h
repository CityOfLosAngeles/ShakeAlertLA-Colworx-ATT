//
//  WalkthroughController.h
//  cityofLA
//
//  Created by Sam Sidd on 01/11/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface WalkthroughController : UIViewController

@property (weak,nonatomic) IBOutlet UIImageView *topImageView;
@property (weak,nonatomic) IBOutlet UILabel *titleLabel;
@property (weak,nonatomic) IBOutlet UILabel *descLabel;

@property NSUInteger index;
@property NSDictionary *data;
@end

NS_ASSUME_NONNULL_END
