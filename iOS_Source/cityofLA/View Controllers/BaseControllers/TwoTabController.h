//
//  TwoTabController.h
//  cityofLA
//
//  Created by Sam Sidd on 05/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef NS_ENUM(NSUInteger,TabType) {
    shelterTabs,
    quakeTabs,
};

@interface TwoTabController : UIViewController
@property TabType tabType;
@property UIColor *navColor;
+(TwoTabController *)sharedInstance;

@property (strong,nonatomic) IBOutlet UIView *btnsView;
@property (strong,nonatomic) IBOutlet UIButton *listBtn;
@property (strong,nonatomic) IBOutlet UIButton *mapBtn;
@property (strong,nonatomic) IBOutlet UIView *containerView;
@property NSMutableArray *viewControllers;
@property NSMutableArray *pins;
@property NSMutableArray *notifications;

@property (strong,nonatomic) IBOutlet UIButton *sortBtn;
@property (strong,nonatomic) IBOutlet UIView *sortMenu;
@property (strong,nonatomic) IBOutlet UIButton *timeBtn;
@property (strong,nonatomic) IBOutlet UIButton *magnitudeBtn;
-(void)hideMenu;
-(void)loadShelter;
-(IBAction)mapPressed:(UIButton *)btn;

//WASIQ
-(void)loadRecentEarthQuakes: (double)radius;
@property float radius;
@property NSMutableArray *boundedAnnotations;
@property BOOL popedViewController;
@property BOOL boundedAnnotation;
@property (weak, nonatomic) IBOutlet UIView *additionalView;
@property (weak, nonatomic) IBOutlet UILabel *label;
//END WASIQ
@end
