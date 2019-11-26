//
//  LandingViewController.m
//  cityofLA
//
//  Created by Sam Sidd on 04/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import "Helper.h"
#import "LandingViewController.h"

@interface LandingViewController ()

@end

@implementation LandingViewController

-(void)viewDidAppear:(BOOL)animated{
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        BOOL onboarded = [[NSUserDefaults standardUserDefaults]boolForKey:@"onboarded"];
        if (onboarded){
            UITabBarController *tabBar = [[UIStoryboard storyboardWithName:@"Main" bundle:nil]instantiateViewControllerWithIdentifier:@"tabBar"];
            [self presentViewController:tabBar animated:YES completion:nil];
        }else{
            [Helper startOnboarding];
        }
    });
}
-(UIStatusBarStyle)preferredStatusBarStyle{
    return UIStatusBarStyleLightContent;
}
@end
