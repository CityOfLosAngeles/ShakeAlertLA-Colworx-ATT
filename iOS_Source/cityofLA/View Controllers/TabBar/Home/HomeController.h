//
//  HomeController.h
//  cityofLA
//
//  Created by Sam Sidd on 04/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HomeController : UIViewController
@property (strong, nonatomic) IBOutlet UIScrollView *scrollView;
@property (strong, nonatomic) IBOutlet UIImageView *wavesImgView;
@property (strong, nonatomic) IBOutlet UILabel *topLabel;
@property (strong, nonatomic) IBOutlet UIView *cardView;
@property (strong, nonatomic) IBOutlet UILabel *bePrepared;
@property (strong, nonatomic) IBOutlet UILabel *getKitReady;
@property (strong, nonatomic) IBOutlet UICollectionView *collectionView;
@property (strong, nonatomic) IBOutlet UIButton *aboutShakeAlert;
@property (strong, nonatomic) IBOutlet UIButton *termsBtn;
@property (strong, nonatomic) IBOutlet NSLayoutConstraint *collectionViewHeight;
@property NSMutableArray *items;
@end
