//
//  OnboardingController.h
//  cityofLA
//
//  Created by Sam Sidd on 03/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface OnboardingController : UIViewController

@property NSInteger index;
@property  (weak, nonatomic) IBOutlet UIButton *nextBtn;
-(void)swipe;
@property  (weak, nonatomic) IBOutlet UIView *container;
@property  (weak, nonatomic) IBOutlet UIPageControl *pageControl;
@property UIPageViewController *pageController;
@property NSMutableArray *walkThroughData;

@end

