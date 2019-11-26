//
//  TabBarController.m
//  cityofLA
//
//  Created by Sam Sidd on 04/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import "TabBarController.h"
#import "Helper.h"

@interface TabBarController ()

@end

@implementation TabBarController
TabBarController *instance;
+(TabBarController *)sharedInstance{
    return instance;
}
- (void)viewDidLoad {
    [super viewDidLoad];
    self.tabBar.tintColor = darkBlueColor;
    [self addRecoveryVc];
    instance = self;
    [self setTabItemsTitle];
}
-(void)addRecoveryVc{
    NSMutableArray *controllers = self.viewControllers.mutableCopy;
    ListViewController *list = [self.storyboard instantiateViewControllerWithIdentifier:@"list"];
    list.navigationItem.largeTitleDisplayMode = UINavigationItemLargeTitleDisplayModeAlways;
    list.tabBarItem.image = [UIImage imageNamed:@"recovery"];
    list.tabBarItem.selectedImage = [UIImage imageNamed:@"select_recovery"];
    UINavigationController *navList = [[UINavigationController alloc]initWithRootViewController:list];
    navList.navigationBar.prefersLargeTitles = YES;
    [controllers insertObject:navList atIndex:2];
    [self setViewControllers:controllers];
}
-(void)tabBar:(UITabBar *)tabBar didSelectItem:(UITabBarItem *)item{
    NSInteger index = [tabBar.items indexOfObject:item];
    if (index == 0 || index == 3) {
        tabBar.tintColor = darkBlueColor;
    }else if (index == 1){
        tabBar.tintColor = quakeRedColor;
    }else{
        tabBar.tintColor = recoveryGreenColor;
    }
}
-(void)setTabItemsTitle{
    for (int i = 0 ; i < self.tabBar.items.count; i++){
        UITabBarItem *item = self.tabBar.items[i];
        NSString *key = [NSString stringWithFormat:@"%d",i];
        [item setTitle:[Helper localized:key]];
    }
}

@end
