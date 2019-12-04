//
//  OnboardingController.m
//  cityofLA
//
//  Created by Sam Sidd on 03/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

//CTI: Controller class to show tutorial wizard on App Launch

#import "AppDelegate.h"
#import "OnboardingController.h"
#import "Helper.h"
#import <QuartzCore/QuartzCore.h>
#import <CoreText/CTStringAttributes.h>
#import <CoreText/CoreText.h>
#import "WalkthroughController.h"
#import "UIColorHelper.h"
@interface OnboardingController () <UIPageViewControllerDataSource,UIPageViewControllerDelegate>

@end

@implementation OnboardingController
- (void)viewWillAppear:(BOOL)animated{
    [self.navigationController.navigationBar setHidden:YES];
}
- (void)viewDidLoad {
    [super viewDidLoad];
    self.nextBtn.accessibilityLabel = @"Next";
    //Setup WalkThrough Data
    self.walkThroughData = @[].mutableCopy;
    [self.walkThroughData addObject:@{@"Title":[Helper localized:@"0title"], @"Desc":[Helper localized:@"0desc"], @"Image":@"0"}];
    [self.walkThroughData addObject:@{@"Title":[Helper localized:@"1title"], @"Desc":[Helper localized:@"1desc"], @"Image":@"1"}];
    [self.walkThroughData addObject:@{@"Title":[Helper localized:@"2title"], @"Desc":[Helper localized:@"2desc"], @"Image":@"2"}];
    [self.walkThroughData addObject:@{@"Title":[Helper localized:@"3title"], @"Desc":[Helper localized:@"3desc"], @"Image":@"3"}];
    
    //PageController setup
    self.pageController = [[UIPageViewController alloc]initWithTransitionStyle:UIPageViewControllerTransitionStyleScroll navigationOrientation:UIPageViewControllerNavigationOrientationHorizontal options:nil];
    [self.pageController setViewControllers:@[[self viewControllerAtIndex:0]] direction:UIPageViewControllerNavigationDirectionForward animated:YES completion:nil];
    self.pageController.dataSource = self;
    self.pageController.delegate = self;
    self.pageController.view.frame = self.container.bounds;
    [self.container addSubview:self.pageController.view];
    [self addChildViewController:self.pageController];
    [self.pageController didMoveToParentViewController:self];
}
-(IBAction)next:(id)sender{
    [self swipe];
}
-(void)swipe{
    if (self.index == 3){
        UINavigationController *nav = (UINavigationController *)[Helper getViewControllerWithIdentifier:@"terms"];
        [self presentViewController:nav animated:YES completion:nil];
    }else{
        self.index++;
        WalkthroughController *vc = [self viewControllerAtIndex:self.index];
        __weak OnboardingController *weakSelf = self;
        [self.pageController setViewControllers:@[vc] direction:UIPageViewControllerNavigationDirectionForward animated:YES completion:^(BOOL finished) {
            weakSelf.pageControl.currentPage = [(WalkthroughController *)weakSelf.pageController.viewControllers.firstObject index];
            weakSelf.index = [(WalkthroughController *)weakSelf.pageController.viewControllers.firstObject index];
        }];
    }
}

#pragma MARK UIPageViewController DataSource :
-(UIViewController *)pageViewController:(UIPageViewController *)pageViewController viewControllerBeforeViewController:(UIViewController *)viewController{
    NSUInteger index = [(WalkthroughController *)viewController index];
    if (index == 0) {
        return nil;
    }
    index--;
    return [self viewControllerAtIndex:index];
}
-(UIViewController *)pageViewController:(UIPageViewController *)pageViewController viewControllerAfterViewController:(UIViewController *)viewController{
    NSUInteger index = [(WalkthroughController *)viewController index];
    if (index >= self.walkThroughData.count - 1) {
        return nil;
    }
     index++;
    return [self viewControllerAtIndex:index];
}
-(WalkthroughController *)viewControllerAtIndex:(NSUInteger )index{
    WalkthroughController *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"page"];
    vc.index = index;
    vc.data = self.walkThroughData[index];
    return vc;
}
-(void)pageViewController:(UIPageViewController *)pageViewController didFinishAnimating:(BOOL)finished previousViewControllers:(NSArray<UIViewController *> *)previousViewControllers transitionCompleted:(BOOL)completed{
    self.pageControl.currentPage = [(WalkthroughController *)pageViewController.viewControllers.firstObject index];
    self.index = [(WalkthroughController *)pageViewController.viewControllers.firstObject index];
}

@end
