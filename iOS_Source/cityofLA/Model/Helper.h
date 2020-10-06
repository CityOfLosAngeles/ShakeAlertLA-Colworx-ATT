//
//  Helper.h
//  cityofLA
//
//  Created by Sam Sidd on 03/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//
#define ScreenWidth         [[UIScreen mainScreen] bounds].size.width
#define ScreenHeight        [[UIScreen mainScreen] bounds].size.height

#define quakeRedColor       [UIColor colorWithRed:232.0/255.0 green:23.0/255.0 blue:44.0/255.0 alpha:1]
#define recoveryGreenColor  [UIColor colorWithRed:87.0/255.0 green:129.0/255.0 blue:54.0/255.0 alpha:1]
#define recoveryGreenColorCG  [UIColor colorWithRed:87.0/255.0 green:129.0/255.0 blue:54.0/255.0 alpha:1].CGColor
#define grayTabColor        [UIColor colorWithRed:57.0/255.0 green:128.0/255.0 blue:142.0/255.0 alpha:1]
#define darkBlueColor        [UIColor colorWithRed:19.0/255.0 green:109.0/255.0 blue:166.0/255.0 alpha:1]
#define purpleLAColor        [UIColor colorWithRed:136.0/255.0 green:57.0/255.0 blue:143.0/255.0 alpha:1]
#define purpleLAColorCG        [UIColor colorWithRed:136.0/255.0 green:57.0/255.0 blue:143.0/255.0 alpha:1].CGColor
#define makePlanColor [UIColor colorWithRed:58.0/255.0 green:124.0/255.0 blue:138.0/255.0 alpha:1]
//Add Google Maps API key here
#define google_maps_api_key @""
#define LACoordinates CLLocationCoordinate2DMake(34.052235, -118.243683) 

#import <Foundation/Foundation.h>
#import "ListViewModel.h"
#import "ListViewController.h"
#import "ListItemCell.h"
#import "TwoTabController.h"
#import "NotesController.h"
#import <SVProgressHUD/SVProgressHUD.h>


@import UIKit;
@import GoogleMaps;
@interface Helper : NSObject

//Method declaration and class attributes.
+(UIViewController *)getViewControllerWithIdentifier:(NSString *)identifier;
+(void)startOnboarding;
+(void)animateRootViewController:(UIViewController *)vc;
+ (UIViewController*) topMostController;
+(void)launchHomeController;
+(void)pushHtmlControllerWithFile:(NSString*)htmlFilename
                             onVc:(UIViewController *)viewController
                        withColor:(UIColor*)color
                   dontHideNavBar:(BOOL)dontHideNavBar
                            title:(NSString*)title;
+(void)pushListViewControllerWithType:(ListType)listType
                                title:(NSString *)title
                             navColor:(UIColor *)color
                                 onVc:(UIViewController *)vc;
+(void)pushTwoTabsControllerOnVc:(UIViewController *)viewController
                       withColor:(UIColor*)color
                     withTabType:(TabType)type
                           title:(NSString*)title;
+(void)applyAttributesOnVc:(UIViewController *)vc withColor:(UIColor *)color;
+(void)drawDropShadowOnCell:(UICollectionViewCell *)cell :(float)shadowSize;
+(void)drawDropShadowOnView:(UIView *)widget;


+(void)setupNavBarOnPush:(UIViewController *)vc withColor:(UIColor*)color;
+(void)resetNavBarOnPop:(UIViewController *)vc;
+(NSString *)localized:(NSString *)key;
+(void)pushNotesViewControllerWithPlanItem:(PlanListItem*)item
                                     title:(NSString *)title
                                  navColor:(UIColor *)color
                                      onVc:(UIViewController *)vc;
+(void)pushBuildKitViewControllerWithType:(ListType)listType
                                    title:(NSString *)title
                                 navColor:(UIColor *)color
                                     onVc:(UIViewController *)vc;
+(void)pushListViewControllerWithType:(ListType)listType
                                title:(NSString *)title
                             navColor:(UIColor *)color
                                 onVc:(UIViewController *)vc
                            listItems:(NSMutableArray *)listItems;
+(void)addBgToVC:(UIViewController *)vc;
+(void)pushListViewControllerWithType:(ListType)listType
                                title:(NSString *)title
                             navColor:(UIColor *)color
                                 onVc:(UIViewController *)vc
                          nextVcTitle:(NSString*)nextVcTitle;

+(void)pushHtmlControllerWithFile:(NSString*)htmlFilename
                             onVc:(UIViewController *)viewController
                        withColor:(UIColor*)color
                   dontHideNavBar:(BOOL)dontHideNavBar
                dontHideBottomBar:(BOOL)dontHideBottomBar
                            title:(NSString*)title;

+(void)moveToLAOnMap:(GMSMapView *)mapView;
+(void)customBackBtnOnVc:(UIViewController *)vc ;
+(void)recoveryBackBtn:(UIViewController *)vc ;

+(void)showHud;
+(void)showHudWithMessage: (NSString *) messageStr;
+(void)showHudWithError:(NSString *)Message;
+(void)showHudWithSuccess:(NSString *)Message;
+(void)hideHud;
+(void)multineLineTitle:(UINavigationController *)nav;
#pragma MARK NETWORK REQUESTS :
+(void)findShelterLocationsWithParams:(NSMutableArray *)parameters :(void(^)(BOOL success,id json))completion ;
+(void)getLocationFromCoordinates:(NSString *)coordinates :(void(^)(BOOL success,NSString *address))completion ;


+(NSDictionary *)testNotification;
+(NSMutableArray *)getLocationsArray:(NSArray *)polygon;
+ (UIColor *)colorFromHexString:(NSString *)hexString ;
+(void)hitPushOpenRequest:(NSString *)SegmentID;
+(BOOL) isConnectedToNetwork;
@end
