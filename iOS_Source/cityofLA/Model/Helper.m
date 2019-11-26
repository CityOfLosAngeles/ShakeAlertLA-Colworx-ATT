//
//  Helper.m
//  cityofLA
//
//  Created by Sam Sidd on 03/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//




#import "Helper.h"
#import "HTMLViewController.h"
#import "OnboardingController.h"
#import "AppDelegate.h"
#import "UIFont+FontAdditions.h"
#import "BuildKitController.h"
#import "Reachability.h"

@import UIKit;


@implementation Helper

+(BOOL) isConnectedToNetwork {
    Reachability *reachability =  [Reachability reachabilityForInternetConnection];
    NetworkStatus netStatus = [reachability currentReachabilityStatus];
    if (netStatus == ReachableViaWiFi || netStatus == ReachableViaWWAN) {
        return YES;
    } else {
        return NO;
    }
}

+(UIViewController *)getViewControllerWithIdentifier:(NSString *)identifier{
    UIStoryboard *sb = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
    if ([identifier isEqualToString:@"onboarding"] || [identifier isEqualToString:@"terms"]){
        sb = [UIStoryboard storyboardWithName:@"Onboarding" bundle:nil];
    }
    if ([identifier isEqualToString:@"shelterList"] || [identifier isEqualToString:@"shelterMap"] || [identifier isEqualToString:@"quakeList"] || [identifier isEqualToString:@"quakeMap"] || [identifier isEqualToString:@"twoTabs"]){
        
        sb = [UIStoryboard storyboardWithName:@"TwoTabs" bundle:nil];
    }
    UIViewController *vc = [sb instantiateViewControllerWithIdentifier:identifier];
    return vc;
}
+(void)startOnboarding{
    OnboardingController *vc = (OnboardingController *)[Helper getViewControllerWithIdentifier:@"onboarding"];
    UINavigationController *nav = [[UINavigationController alloc]initWithRootViewController:vc];
    nav.navigationController.navigationBar.hidden = YES;
    vc.index = 0;
    [Helper animateRootViewController:nav];
}
+(void)animateRootViewController:(UIViewController *)vc {
    dispatch_async(dispatch_get_main_queue(), ^{
        [UIView transitionWithView:[AppDelegate sharedInstance].window duration:1 options:UIViewAnimationOptionTransitionCrossDissolve animations:^{
            [AppDelegate sharedInstance].window.rootViewController = vc;
        } completion:nil];
    });
}
+(void)launchHomeController{
    UITabBarController *tabBar = [[UIStoryboard storyboardWithName:@"Main" bundle:nil]instantiateInitialViewController];
    [Helper animateRootViewController:tabBar];
}
+ (UIViewController*) topMostController{
    UIViewController *topController = [UIApplication sharedApplication].keyWindow.rootViewController;
    
    if ([topController isKindOfClass:[UITabBarController class]]) {
        UITabBarController* tab = (UITabBarController*)topController;
        topController = tab.viewControllers[tab.selectedIndex];
    }
    
    if ([topController isKindOfClass:[UINavigationController class]]) {
        UINavigationController* navigationController = (UINavigationController*)topController;
        topController = navigationController.visibleViewController;
    }
    
    while (topController.presentedViewController) {
        topController = topController.presentedViewController;
    
        if ([topController isKindOfClass:[UINavigationController class]]) {
            UINavigationController* navigationController = (UINavigationController*)topController;
            topController = navigationController.visibleViewController;
        }
        if ([topController isKindOfClass:[UITabBarController class]]) {
            UITabBarController* tab = (UITabBarController*)topController;
            topController = tab.viewControllers[tab.selectedIndex];
        }
        
    }
    
    return topController;
}

+(void)pushHtmlControllerWithFile:(NSString*)htmlFilename
                             onVc:(UIViewController *)viewController
                        withColor:(UIColor*)color
                   dontHideNavBar:(BOOL)dontHideNavBar
                            title:(NSString*)title{
    HTMLViewController *htmlVc = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"html"];
    //SAM
//    htmlVc.hidesBottomBarWhenPushed = YES;
    htmlVc.hidesBottomBarWhenPushed = NO;
    htmlVc.htmlFilename = htmlFilename;
    htmlVc.navColor = color;
    htmlVc.title = title;
    if ([title isEqualToString:[Helper localized:@"getFromLANextVc"]]) {
        [Helper setupAccessibility:viewController.navigationController voiceOverText:@"What am I going to get from ShakeAlertLA?"];
    }
    htmlVc.dontHideNavBar = dontHideNavBar;
    htmlVc.navigationItem.largeTitleDisplayMode = UINavigationItemLargeTitleDisplayModeAlways;
    viewController.navigationController.interactivePopGestureRecognizer.delegate = nil;
    [viewController.navigationController pushViewController:htmlVc animated:YES];
}
+(void)pushHtmlControllerWithFile:(NSString*)htmlFilename
                             onVc:(UIViewController *)viewController
                        withColor:(UIColor*)color
                   dontHideNavBar:(BOOL)dontHideNavBar
                dontHideBottomBar:(BOOL)dontHideBottomBar
                            title:(NSString*)title{
    HTMLViewController *htmlVc = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"html"];
    htmlVc.hidesBottomBarWhenPushed = dontHideBottomBar;
    htmlVc.htmlFilename = htmlFilename;
    htmlVc.navColor = color;
    htmlVc.title = title;
    if ([title isEqualToString:[Helper localized:@"getFromLANextVc"]]) {
        [Helper setupAccessibility:viewController.navigationController voiceOverText:@"What am I going to get from ShakeAlertLA?"];
    }
    htmlVc.dontHideNavBar = dontHideNavBar;
    htmlVc.navigationItem.largeTitleDisplayMode = UINavigationItemLargeTitleDisplayModeAlways;
    viewController.navigationController.interactivePopGestureRecognizer.delegate = nil;
    [viewController.navigationController pushViewController:htmlVc animated:YES];
}
+(void)pushTwoTabsControllerOnVc:(UIViewController *)viewController
                       withColor:(UIColor*)color
                     withTabType:(TabType)type
                           title:(NSString*)title{
    TwoTabController *twoTab = (TwoTabController *)[Helper getViewControllerWithIdentifier:@"twoTabs"];
    twoTab.tabType = type;
    twoTab.navColor = color;
    twoTab.title = title;
    viewController.navigationController.interactivePopGestureRecognizer.delegate = nil;
    [viewController.navigationController pushViewController:twoTab animated:YES];
}
+(void)applyAttributesOnVc:(UIViewController *)vc withColor:(UIColor *)color{
//    NSMutableParagraphStyle *p = [[NSMutableParagraphStyle alloc] init];
//    [p setAlignment:NSTextAlignmentLeft];
    vc.navigationController.navigationBar.tintColor = UIColor.whiteColor;
    vc.navigationController.navigationItem.largeTitleDisplayMode = UINavigationItemLargeTitleDisplayModeAlways;
//    ,NSParagraphStyleAttributeName:p
    vc.navigationController.navigationBar.largeTitleTextAttributes = @{NSForegroundColorAttributeName : UIColor.whiteColor,NSFontAttributeName:[UIFont SFHeavyWithSize:32]};
    vc.navigationController.navigationBar.titleTextAttributes = @{NSForegroundColorAttributeName : UIColor.whiteColor,NSFontAttributeName:[UIFont SFHeavyWithSize:16]};
    [vc.navigationController.navigationBar setBarTintColor:color];
    [Helper multineLineTitle:vc.navigationController];
}
+(void)pushListViewControllerWithType:(ListType)listType
                                title:(NSString *)title
                             navColor:(UIColor *)color
                                 onVc:(UIViewController *)vc{
    ListViewController *listVc = [[UIStoryboard storyboardWithName:@"Main" bundle:nil]instantiateViewControllerWithIdentifier:@"list"];
    listVc.hidesBottomBarWhenPushed = NO;
    listVc.listType = listType;
    listVc.title    = title;
    listVc.navColor = color;
    vc.navigationController.interactivePopGestureRecognizer.delegate = nil;
    [vc.navigationController pushViewController:listVc animated:YES];
}
+(void)pushListViewControllerWithType:(ListType)listType
                                title:(NSString *)title
                             navColor:(UIColor *)color
                                 onVc:(UIViewController *)vc
                          nextVcTitle:(NSString*)nextVcTitle{
    ListViewController *listVc = [[UIStoryboard storyboardWithName:@"Main" bundle:nil]instantiateViewControllerWithIdentifier:@"list"];
    listVc.hidesBottomBarWhenPushed = NO;
    listVc.listType = listType;
    listVc.navColor = color;
    listVc.title = nextVcTitle;
    vc.navigationController.interactivePopGestureRecognizer.delegate = nil;
    [vc.navigationController pushViewController:listVc animated:YES];
}
+(void)pushListViewControllerWithType:(ListType)listType
                                title:(NSString *)title
                             navColor:(UIColor *)color
                                 onVc:(UIViewController *)vc
                            listItems:(NSMutableArray *)listItems{
    ListViewController *listVc = [[UIStoryboard storyboardWithName:@"Main" bundle:nil]instantiateViewControllerWithIdentifier:@"list"];
    listVc.hidesBottomBarWhenPushed = NO;
    listVc.listType = listType;
    listVc.title    = title;
    listVc.navColor = color;
    listVc.list     = listItems;
    vc.navigationController.interactivePopGestureRecognizer.delegate = nil;
    [vc.navigationController pushViewController:listVc animated:YES];
}
+(void)pushBuildKitViewControllerWithType:(ListType)listType
                                title:(NSString *)title
                             navColor:(UIColor *)color
                                 onVc:(UIViewController *)vc{
    BuildKitController *buildKitVc = (BuildKitController *)[Helper getViewControllerWithIdentifier:@"buildKit"];
    buildKitVc.hidesBottomBarWhenPushed = NO;;
    buildKitVc.listType = listType;
    buildKitVc.title    = title;
    buildKitVc.navColor = color;
    vc.navigationController.interactivePopGestureRecognizer.delegate = nil;
    [vc.navigationController pushViewController:buildKitVc animated:YES];
}
+(void)setupNavBarOnPush:(UIViewController *)vc withColor:(UIColor*)color{
//    NSMutableParagraphStyle *p = [[NSMutableParagraphStyle alloc] init];
//    [p setAlignment:NSTextAlignmentLeft];
    vc.navigationController.navigationBar.tintColor = UIColor.whiteColor;
    vc.navigationController.navigationItem.largeTitleDisplayMode = UINavigationItemLargeTitleDisplayModeAlways;
    vc.navigationController.navigationBar.largeTitleTextAttributes = @{NSForegroundColorAttributeName : UIColor.whiteColor,NSFontAttributeName:[UIFont SFHeavyWithSize:32]};
    
//    NSParagraphStyleAttributeName:p
    
    vc.navigationController.navigationBar.titleTextAttributes = @{NSForegroundColorAttributeName : UIColor.whiteColor,NSFontAttributeName:[UIFont SFHeavyWithSize:16]};
    
    vc.navigationController.navigationBarHidden = NO;
    [vc.navigationController.navigationBar setBarTintColor:color];
    [vc.navigationController.navigationBar setHidden:NO];
    vc.navigationItem.leftBarButtonItem = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"back_arrow"] style:UIBarButtonItemStylePlain target:vc.navigationController action:@selector(popViewControllerAnimated:)];
    NSString *lang = [[NSUserDefaults standardUserDefaults]valueForKey:@"lang"];
    if ([lang isEqualToString:@"en"]){
        vc.navigationItem.leftBarButtonItem.accessibilityLabel = @"Go back";
    }else{
        vc.navigationItem.leftBarButtonItem.accessibilityLabel = @"Volver";
    }
    
    [Helper multineLineTitle:vc.navigationController];
}
+(void)customBackBtnOnVc:(UIViewController *)vc {
    vc.navigationItem.leftBarButtonItem = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"back_arrow"]
                                                                           style:UIBarButtonItemStylePlain
                                                                          target:vc.navigationController
                                                                          action:@selector(popViewControllerAnimated:)];
    
    NSString *lang = [[NSUserDefaults standardUserDefaults]valueForKey:@"lang"];
    if ([lang isEqualToString:@"en"]){
        vc.navigationItem.leftBarButtonItem.accessibilityLabel = @"Go back";
    }else{
        vc.navigationItem.leftBarButtonItem.accessibilityLabel = @"Volver";
    }
}
+(void)recoveryBackBtn:(UIViewController *)vc {
    vc.navigationItem.leftBarButtonItem = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"back_arrow"]
                                                                           style:UIBarButtonItemStylePlain
                                                                          target:vc
                                                                          action:@selector(goToHome:)];
    
    NSString *lang = [[NSUserDefaults standardUserDefaults]valueForKey:@"lang"];
    if ([lang isEqualToString:@"en"]){
        vc.navigationItem.leftBarButtonItem.accessibilityLabel = @"Go back";
    }else{
        vc.navigationItem.leftBarButtonItem.accessibilityLabel = @"Volver";
    }
}
+(void)resetNavBarOnPop:(UIViewController *)vc{
    vc.navigationController.navigationBarHidden = YES;
    [vc.navigationController.navigationBar setHidden:YES];
}
+(NSString *)localized:(NSString *)key{
    NSString *lang = [[NSUserDefaults standardUserDefaults]valueForKey:@"lang"];
    if (lang == nil){
        lang = @"en";
        [[NSUserDefaults standardUserDefaults]setObject:lang forKey:@"lang"];
    }
    NSString *path  = [[NSBundle mainBundle] pathForResource:lang ofType:@"lproj"];
    NSBundle *bundle = [[NSBundle alloc]initWithPath:path];
    return NSLocalizedStringFromTableInBundle(key, nil, bundle, "");
}
+(void)pushNotesViewControllerWithPlanItem:(PlanListItem*)item
                                     title:(NSString *)title
                                  navColor:(UIColor *)color
                                      onVc:(UIViewController *)vc{
    NotesController *notesVc = [[UIStoryboard storyboardWithName:@"Main" bundle:nil]instantiateViewControllerWithIdentifier:@"notes"];
    notesVc.planListItem = item;
    notesVc.title    = title;
    notesVc.navColor = color;
    vc.navigationController.interactivePopGestureRecognizer.delegate = nil;
    [vc.navigationController pushViewController:notesVc animated:YES];
}
+(void)addBgToVC:(UIViewController *)vc{
    UIImageView *imgview = [[UIImageView alloc]initWithImage:[UIImage imageNamed:@"bg"]];
    imgview.frame = vc.view.bounds;
    imgview.contentMode = UIViewContentModeScaleAspectFill;
    imgview.alpha = 0;
    
    if ([vc isKindOfClass:[UITableViewController class]]) {
        UITableViewController *tableVC = (UITableViewController *)vc;
        tableVC.tableView.backgroundView = imgview;
        imgview.alpha = 1;
    }else{
        [vc.view addSubview:imgview];
        [vc.view sendSubviewToBack:imgview];
        imgview.alpha = 1;
    }
}
+(void)moveToLAOnMap:(GMSMapView *)mapView{
    GMSCameraUpdate *moveToLA = [GMSCameraUpdate setCamera:[GMSCameraPosition cameraWithTarget:LACoordinates zoom:12]];
    [mapView animateWithCameraUpdate:moveToLA];
}
#pragma MARK UIVIEWS HELPER METHODS :-
+(void)drawDropShadowOnCell:(UICollectionViewCell *)cell :(float)shadowSize{
    cell.contentView.layer.cornerRadius = 10;
    cell.layer.cornerRadius = 10;
    cell.contentView.layer.borderWidth = 1.0f;
    cell.contentView.layer.borderColor = [UIColor clearColor].CGColor;
    cell.contentView.layer.masksToBounds = YES;
    cell.contentView.clipsToBounds = YES;
    
    cell.layer.shadowColor = [UIColor blackColor].CGColor;
    cell.layer.shadowOffset = CGSizeMake(2, 2);
    cell.layer.shadowRadius = shadowSize;
    cell.layer.shadowOpacity = 0.1;
    cell.layer.masksToBounds = NO;
    cell.layer.shadowPath = [UIBezierPath bezierPathWithRoundedRect:cell.bounds cornerRadius:cell.contentView.layer.cornerRadius].CGPath;
}
+(void)drawDropShadowOnView:(UIView *)widget{
    
    widget.layer.cornerRadius = 1.0f;
    widget.layer.borderWidth = 0.0f;
    widget.layer.borderColor = [UIColor darkGrayColor].CGColor;
    
    if ([widget isKindOfClass:[UITableView class]]) {
        widget.layer.masksToBounds = YES;
    }
    else{
        widget.layer.masksToBounds = NO;
    }
    widget.layer.shadowOffset = CGSizeMake(2, 2);
    widget.layer.shadowRadius = 5;
    widget.layer.shadowOpacity = 0.1;
    
}

+(void)showHud{
    dispatch_async(dispatch_get_main_queue(), ^{
        [SVProgressHUD show];
    });
}
+(void)showHudWithMessage: (NSString *) messageStr{
    
    dispatch_async(dispatch_get_main_queue(), ^{
        [SVProgressHUD showWithStatus:messageStr];
    });
}
+(void)showHudWithError:(NSString *)Message{
    dispatch_async(dispatch_get_main_queue(), ^{
        [SVProgressHUD showErrorWithStatus:Message];
    });
}
+(void)showHudWithSuccess:(NSString *)Message{
    dispatch_async(dispatch_get_main_queue(), ^{
        [SVProgressHUD showSuccessWithStatus:Message];
    });
}

+(void)hideHud{
    dispatch_async(dispatch_get_main_queue(), ^{
        [SVProgressHUD dismiss];
    });
}

+(void)multineLineTitle:(UINavigationController *)nav{
    for (UIView *navItem in nav.navigationBar.subviews) {
        for (UIView *v in navItem.subviews) {
            if ([v isKindOfClass:[UILabel class]]) {
                UILabel *titleLabel = (UILabel*)v;
//                [titleLabel setNumberOfLines:0];
                [titleLabel setLineBreakMode:NSLineBreakByTruncatingTail];
//                [titleLabel sizeToFit];
            }
        }
    }
    [nav.navigationBar layoutSubviews];
    [nav.navigationBar layoutIfNeeded];
}
+(void)setupAccessibility:(UINavigationController *)nav voiceOverText:(NSString *)voiceOverText{
    for (UIView *navItem in nav.navigationBar.subviews) {
        for (UIView *v in navItem.subviews) {
            if ([v isKindOfClass:[UILabel class]]) {
                UILabel *titleLabel = (UILabel*)v;
                titleLabel.accessibilityLabel = voiceOverText;
            }
        }
    }
}

#pragma MARK NETWORK REQUESTS :
+(void)findShelterLocationsWithParams:(NSMutableArray *)parameters :(void(^)(BOOL success,id json))completion {
    
    NSString *boundary = @"----WebKitFormBoundary7MA4YWxkTrZu0gW";
    
    NSError *error;
    NSMutableString *body = [NSMutableString string];
    for (NSDictionary *param in parameters) {
        [body appendFormat:@"--%@\r\n", boundary];
        if (param[@"fileName"]) {
            [body appendFormat:@"Content-Disposition:form-data; name=\"%@\"; filename=\"%@\"\r\n", param[@"name"], param[@"fileName"]];
            [body appendFormat:@"Content-Type: %@\r\n\r\n", param[@"contentType"]];
            [body appendFormat:@"%@", [NSString stringWithContentsOfFile:param[@"fileName"] encoding:NSUTF8StringEncoding error:&error]];
            if (error) {
                NSLog(@"%@", error);
            }
        } else {
            [body appendFormat:@"Content-Disposition:form-data; name=\"%@\"\r\n\r\n", param[@"name"]];
            [body appendFormat:@"%@\r\n", param[@"value"]];
        }
    }
    [body appendFormat:@"--%@--\r\n", boundary];
    NSData *postData = [body dataUsingEncoding:NSUTF8StringEncoding];
    NSString *shelterURL = @"";
    shelterURL = @"https://services7.arcgis.com/aFfS9FqkIRSo0Ceu/arcgis/rest/services/EQ_Early_Warning_Mass_Care_Locations_Public_View/FeatureServer/0/query";
    //DEV
//    shelterURL = @"https://services7.arcgis.com/aFfS9FqkIRSo0Ceu/ArcGIS/rest/services/EQ_Early_Warning_Mass_Care_Locations_(Demo)/FeatureServer/0/query";
    
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:shelterURL]
                                                           cachePolicy:NSURLRequestUseProtocolCachePolicy
                                                       timeoutInterval:30.0];
    [request setHTTPMethod:@"POST"];
    NSDictionary *headers = @{ @"content-type": @"multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW"};
    [request setAllHTTPHeaderFields:headers];
    [request setHTTPBody:postData];
    
    NSURLSession *session = [NSURLSession sharedSession];
    NSURLSessionDataTask *dataTask = [session dataTaskWithRequest:request
                                                completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
                                                    dispatch_async(dispatch_get_main_queue(), ^{
                                                        if (error) {
                                                            NSLog(@"%@", error.localizedDescription);
                                                            completion(NO,nil);
                                                        } else {
                                                            NSHTTPURLResponse *httpResponse = (NSHTTPURLResponse *) response;
                                                            
                                                            if (httpResponse.statusCode == 200) {
                                                                id json = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:nil];
                                                                completion(YES,json);
                                                            }else{
                                                                completion(NO,nil);
                                                            }
                                                        }
                                                    });
                                                }];
    [dataTask resume];
}

+(NSDictionary *)testNotification{
    return @{ @"aps" :    @{
                      @"alert" :         @{
                              @"body" : @"Earthquake! Earthquake! Expect Weak shaking. Drop, cover, and hold on. Protect yourself now!",
                              @"title" : @"Earthquake",
                              },
                      },
              @"data" :     @{
                      @"jsonBody" :         @{
                              @"Category" : @"test",
                              @"DepthUnit" : @"km",
                              @"DepthValue": @"8.0000",
                              @"EventOriginTimeStampUnit" : @"UTC",
                              @"EventOriginTimeStampValue" : @"2018-11-15T06:35:21.000Z",
                              @"LatitudeUnit" : @"deg",
                              @"LatitudeValue" :@"33.9454",
                              @"Likelihood" : @"0.9091",
                              @"LongitudeUnit" : @"deg",
                              @"LongitudeValue" : @"-117.9237",
                              @"MMI": @"2.0000",
                              @"Polygon": @[
                                          @"34.6679,-117.9237",
                                          @"34.4547,-117.3042",
                                          @"33.9423,-117.0528",
                                          @"33.4330,-117.3115",
                                          @"33.2229,-117.9237",
                                          @"33.4330,-118.5359",
                                          @"33.9423,-118.7946",
                                          @"34.4547,-118.5432",
                                          @"34.6679,-117.9237"
                                          ],
                              @"MagnitudeValue" : @"5.1000",
                              @"TimeStamp" : @"test",
                              @"Topic" : @79,
                              @"startTime" : @"1542263726229"
                              },
                      }
              };
}
+(NSMutableArray *)getLocationsArray:(NSArray *)polygon{
    NSMutableArray *coordinates = [[NSMutableArray alloc]init];
    for (NSArray *polygonArr in polygon) {
        for (NSString *latLong in polygonArr) {
            NSArray *comps = [latLong componentsSeparatedByString:@","];
            CLLocationCoordinate2D coordinate =  CLLocationCoordinate2DMake([[comps firstObject] doubleValue], [[comps lastObject] doubleValue]);
            CLLocation *loc = [[CLLocation alloc]initWithLatitude:coordinate.latitude longitude:coordinate.longitude];
            [coordinates addObject:loc];
        }
    }
    return coordinates;
}
+ (UIColor *)colorFromHexString:(NSString *)hexString {
    unsigned rgbValue = 0;
    NSScanner *scanner = [NSScanner scannerWithString:hexString];
    [scanner setScanLocation:1]; // bypass '#' character
    [scanner scanHexInt:&rgbValue];
    return [UIColor colorWithRed:((rgbValue & 0xFF0000) >> 16)/255.0 green:((rgbValue & 0xFF00) >> 8)/255.0 blue:(rgbValue & 0xFF)/255.0 alpha:0.5];
}

+(void)hitPushOpenRequest:(NSString *)SegmentID{
    NSDictionary *body = @{@"SegmentID":SegmentID,@"DeviceID":[AppDelegate sharedInstance].deviceToken};
    
    NSData *postData = [NSJSONSerialization dataWithJSONObject:body options:NSJSONWritingPrettyPrinted error:nil];
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:@"<server_address>/pushOpenRate"]
                                                           cachePolicy:NSURLRequestUseProtocolCachePolicy
                                                       timeoutInterval:30.0];
    [request setHTTPMethod:@"POST"];
    [request setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:postData];
    
    NSURLSession *session = [NSURLSession sharedSession];
    NSURLSessionDataTask *dataTask = [session dataTaskWithRequest:request
                                                completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
                                                    dispatch_async(dispatch_get_main_queue(), ^{
                                                        if (error) {
                                                            NSLog(@"%@", error.localizedDescription);
                                                        } else {
                                                            NSHTTPURLResponse *httpResponse = (NSHTTPURLResponse *) response;
                                                            id json = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:nil];
                                                            
                                                            if (httpResponse.statusCode == 200) {
                                                                NSLog(@"push open api success");
                                                                NSLog(@"response from server : %@",json);
                                                            }else{
                                                                NSLog(@"push open api failed");
                                                                NSLog(@"response from server : %@",json);
                                                            }
                                                        }
                                                    });
                                                }];
    [dataTask resume];
}
@end
