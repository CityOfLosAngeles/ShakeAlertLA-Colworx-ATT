//
//  TabBarController.h
//  cityofLA
//
//  Created by Sam Sidd on 04/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface TabBarController : UITabBarController
+(TabBarController *)sharedInstance;
-(void)setTabItemsTitle;
-(void)tabBar:(UITabBar *)tabBar didSelectItem:(UITabBarItem *)item;
@end
