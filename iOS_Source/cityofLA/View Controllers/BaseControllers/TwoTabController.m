//
//  TwoTabController.m
//  cityofLA
//
//  Created by Sam Sidd on 05/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import "TwoTabController.h"
#import "UIView+ViewAdditions.h"
#import "Helper.h"
#import "ShelterListController.h"
#import "ShelterMapController.h"
#import "ShelterListItem.h"

#import "QuakeListController.h"
#import "QuakeMapController.h"
#import "AppDelegate.h"

@interface TwoTabController ()

@end

@implementation TwoTabController
TwoTabController *twoTabInstance;
+(TwoTabController *)sharedInstance{
    return twoTabInstance;
}
-(void)viewDidAppear:(BOOL)animated{
    [Helper multineLineTitle:self.navigationController];
    if (self.tabType == shelterTabs) {
        self.title = [Helper localized:@"shelterNextVc"];
    }else{
        self.title = [Helper localized:@"viewTitle"];
    }
}
-(void)viewWillDisappear:(BOOL)animated{
    [self hideMenu];
}
- (void)viewDidLoad {
    [super viewDidLoad];
    twoTabInstance = self;
    [self setup];
    
}
-(void)setup{
    self.mapBtn.backgroundColor = UIColor.whiteColor;
    self.navigationItem.largeTitleDisplayMode = UINavigationItemLargeTitleDisplayModeAlways;
    [self.btnsView curveView];
    self.btnsView.backgroundColor = UIColor.clearColor;
    self.btnsView.layer.borderWidth = 1;
    
    self.timeBtn.isAccessibilityElement = YES;
    self.magnitudeBtn.isAccessibilityElement = YES;
    
    CGRect frame = self.sortBtn.frame;
    frame.size.width = 50;
    self.sortBtn.frame = frame;
    [Helper setupNavBarOnPush:self withColor:self.navColor];
    
    [self.timeBtn setTitle:[Helper localized:@"byTime"] forState:UIControlStateNormal];
    [self.magnitudeBtn setTitle:[Helper localized:@"byMagnitude"] forState:UIControlStateNormal];
    
    if (self.tabType == shelterTabs) {
        self.sortBtn.hidden = YES;
        self.btnsView.layer.borderColor = recoveryGreenColorCG;
        [[self additionalView] setBackgroundColor:recoveryGreenColor];
        [[self label] setHidden:YES];
        [self.listBtn setImage:[UIImage imageNamed:@"recover_list"] forState:UIControlStateNormal];
        [self.listBtn setImage:[UIImage imageNamed:@"select_list"] forState:UIControlStateSelected];
        
        [self.mapBtn setImage:[UIImage imageNamed:@"recover_map"] forState:UIControlStateNormal];
        [self.mapBtn setImage:[UIImage imageNamed:@"select_map"] forState:UIControlStateSelected];
        
        ShelterListController *list = (ShelterListController *)[Helper getViewControllerWithIdentifier:@"shelterList"];
        ShelterMapController *map = (ShelterMapController *)[Helper getViewControllerWithIdentifier:@"shelterMap"];
        self.viewControllers = @[list,map].mutableCopy;
        
        self.listBtn.accessibilityLabel = @"Shelter locations";
        self.mapBtn.accessibilityLabel = @"Map of Shelters";
        [self loadShelter];
    }else{
        [Helper drawDropShadowOnView:self.sortMenu];
        
        
        [[self label] setHidden:NO];
        self.sortMenu.layer.cornerRadius = 8;
        self.btnsView.layer.borderColor = purpleLAColorCG;
        [self.listBtn setImage:[UIImage imageNamed:@"list"] forState:UIControlStateNormal];
        [self.listBtn setImage:[UIImage imageNamed:@"select_list"] forState:UIControlStateSelected];
        
        [self.mapBtn setImage:[UIImage imageNamed:@"map"] forState:UIControlStateNormal];
        [self.mapBtn setImage:[UIImage imageNamed:@"select_map"] forState:UIControlStateSelected];
        
        QuakeListController *listVc = (QuakeListController*)[Helper getViewControllerWithIdentifier:@"quakeList"];
        QuakeMapController *mapVc = (QuakeMapController*)[Helper getViewControllerWithIdentifier:@"quakeMap"];
        self.viewControllers = @[listVc,mapVc].mutableCopy;
        
        self.notifications = [[DBHelper sharedInstance]getNotifications];
        NSSortDescriptor *sorter = [[NSSortDescriptor alloc]initWithKey:@"startTime" ascending:NO];
        self.notifications = [self.notifications sortedArrayUsingDescriptors:@[sorter]].mutableCopy;
        for (UIViewController *vc in self.viewControllers) {
            [vc performSelector:@selector(reload) withObject:nil afterDelay:0];
        }
//        WASIQ
        _boundedAnnotations = [[NSMutableArray alloc] init];
        _radius = 800;
        [self loadRecentEarthQuakes: 800];
        [[self additionalView] setBackgroundColor:purpleLAColor];
        [[self label] setText:[Helper localized:@"recentEarthQuake"]];
//        END WASIQ
    }
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.2 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        [self listPressed:self.listBtn];
    });
}
-(void)loadShelter{
    [Helper showHudWithMessage:@""];
    NSMutableArray *parameters = @[].mutableCopy;
    [parameters addObject:@{@"name":@"f",@"value":@"json"}];
    [parameters addObject:@{@"name":@"where",@"value":@"FCODE LIKE 'Shelter - Activated'"}];
    [parameters addObject:@{@"name":@"outSr",@"value":@"4326"}];

    [Helper findShelterLocationsWithParams:parameters :^(BOOL success, id json) {
        if (success) {
            __weak TwoTabController *weakSelf = self;
            
            weakSelf.pins = @[].mutableCopy;
            for (NSDictionary *d in [json valueForKey:@"features"]) {
                ShelterListItem *item = [[ShelterListItem alloc]initWithDict:d.mutableCopy];
                [weakSelf.pins addObject:item];
            }
            if (weakSelf.pins.count > 0) {
                [weakSelf getAddresses:-1];
            }else{
                dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                    for (UIViewController *vc in weakSelf.viewControllers) {
                        [vc performSelector:@selector(reload) withObject:nil afterDelay:0];
                    }
                    [Helper hideHud];
                });
            }
            NSLog(@"loaded");
        }else{
            NSLog(@"No information found");
            [Helper showHudWithMessage:@"No shelters found"];
        }
    }];
}
-(void)getAddresses:(int)count{
    __weak TwoTabController *weakSelf = self;
    count++;
    if (count >= weakSelf.pins.count) {
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            for (UIViewController *vc in weakSelf.viewControllers) {
                [vc performSelector:@selector(reload) withObject:nil afterDelay:0];
            }
            [Helper hideHud];
        });
        return;
    }
    ShelterListItem *item = weakSelf.pins[count];
    [item getAddress:^(NSString *address) {
        NSLog(@"address found");
        [weakSelf.pins replaceObjectAtIndex:count withObject:item];
        [weakSelf getAddresses:count];
    }];
}
-(IBAction)listPressed:(UIButton *)btn{
    btn.selected = YES;
    btn.backgroundColor = self.tabType == shelterTabs ? recoveryGreenColor : purpleLAColor;
    self.mapBtn.selected = NO;
    self.mapBtn.backgroundColor = UIColor.whiteColor;
    
    if (self.tabType == shelterTabs){
        ShelterListController *list = [self.viewControllers firstObject];
        ShelterMapController *map   = [self.viewControllers lastObject];
        [map.view removeFromSuperview];
        [map willMoveToParentViewController:nil];
        
        [self.view setNeedsLayout];
        [self.view layoutIfNeeded];
        
        [self addChildViewController:list];
        list.view.frame = self.containerView.bounds;
        [self.containerView addSubview:list.view];
        [list didMoveToParentViewController:self];
    }else{
        QuakeListController *list = [self.viewControllers firstObject];
        QuakeMapController *map   = [self.viewControllers lastObject];
        [map.view removeFromSuperview];
        [map willMoveToParentViewController:nil];
        
        [self.view setNeedsLayout];
        [self.view layoutIfNeeded];
        
        [self addChildViewController:list];
        list.view.frame = self.containerView.bounds;
        [self.containerView addSubview:list.view];
        [list didMoveToParentViewController:self];
    }
    [self.view bringSubviewToFront:self.btnsView];
}
-(IBAction)mapPressed:(UIButton *)btn{
    btn.selected = YES;
    btn.backgroundColor = self.tabType == shelterTabs ? recoveryGreenColor : purpleLAColor;
    self.listBtn.backgroundColor = UIColor.whiteColor;
    self.listBtn.selected = NO;
    
    if (self.tabType == shelterTabs) {
        ShelterListController *list = [self.viewControllers firstObject];
        ShelterMapController *map   = [self.viewControllers lastObject];
        [list.view removeFromSuperview];
        [list willMoveToParentViewController:nil];
        
        
        [self addChildViewController:map];
        map.view.frame = self.containerView.bounds;
        [self.containerView addSubview:map.view];
        [map didMoveToParentViewController:self];
        
        [self.containerView bringSubviewToFront:map.view];
    }else{
        [self.sortMenu removeFromSuperview];
        QuakeListController *list = [self.viewControllers firstObject];
        QuakeMapController *map   = [self.viewControllers lastObject];
        [list.view removeFromSuperview];
        [list willMoveToParentViewController:nil];
        
        [self addChildViewController:map];
        map.view.frame = self.containerView.bounds;
        [self.containerView addSubview:map.view];
        [map didMoveToParentViewController:self];
        
        [self.containerView bringSubviewToFront:map.view];
    }
    [self.view bringSubviewToFront:self.btnsView];
}
-(IBAction)sortBtnPressed:(UIButton *)btn{
    float width = self.sortMenu.frame.size.width;
    float x = UIScreen.mainScreen.bounds.size.width - width - 10;
    float height = self.sortMenu.frame.size.height;
    self.sortMenu.alpha = 1;
    self.sortMenu.frame = CGRectMake(x, 30, width, height);
    [Helper drawDropShadowOnView:self.sortMenu];
    [[AppDelegate sharedInstance].window addSubview:self.sortMenu];
}
-(IBAction)byTimePressed:(UIButton *)btn{
    [self hideMenu];
    NSSortDescriptor *sorter = [[NSSortDescriptor alloc]initWithKey:@"startTime" ascending:NO];
    //WASIQ
    if (self.boundedAnnotation) {
        self.boundedAnnotations = [self.boundedAnnotations sortedArrayUsingDescriptors:@[sorter]].mutableCopy;
    } else {
        self.notifications = [self.notifications sortedArrayUsingDescriptors:@[sorter]].mutableCopy;
    }
    //END WASIQ
    [self.viewControllers.firstObject performSelector:@selector(reload) withObject:nil afterDelay:0];
    [self.viewControllers.firstObject performSelector:@selector(scrollToTop) withObject:nil afterDelay:0.2];
}
-(IBAction)byMagnitudePressed:(UIButton *)btn{
    [self hideMenu];
    NSSortDescriptor *sorter = [[NSSortDescriptor alloc]initWithKey:@"MagnitudeValue" ascending:NO];
    //WASIQ
    if (self.boundedAnnotation) {
        self.boundedAnnotations = [self.boundedAnnotations sortedArrayUsingDescriptors:@[sorter]].mutableCopy;
    } else {
        self.notifications = [self.notifications sortedArrayUsingDescriptors:@[sorter]].mutableCopy;
    }
    //END WASIQ
    [self.viewControllers.firstObject performSelector:@selector(reload) withObject:nil afterDelay:0];
    [self.viewControllers.firstObject performSelector:@selector(scrollToTop) withObject:nil afterDelay:0.2];
}
-(void)hideMenu{
    [self.sortMenu removeFromSuperview];
}
-(void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event{
    [self hideMenu];
}

// 03/April/2019 -> WASIQ
// Method to get Recent Earthquakes
-(NSArray *) getRecentEarthquakes :(double)radius  :(void(^)( bool success ,  id url))completionBlock {
    NSMutableArray *dates = [[self getPreviousMonthDate] mutableCopy];
    
    NSString *lang = [[NSUserDefaults standardUserDefaults]valueForKey:@"lang"];
    if ([lang isEqualToString:@"en"]){
        [SVProgressHUD showWithStatus:@"Loading"];
    }else{
        [SVProgressHUD showWithStatus:@"cargando"];
    }
    NSMutableArray *array = [[NSMutableArray alloc] init];
    
//    NSString *url = [NSString stringWithFormat:@"https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&latitude=34.052235&longitude=-118.243683&maxradiuskm=%.2f&limit=300&minmagnitude=2", radius] ;
    
    NSString *url = [NSString stringWithFormat:@"https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&latitude=34.052235&longitude=-118.243683&maxradiuskm=%.2f&limit=300&minmagnitude=3&orderby=time&starttime=%@", radius, [dates firstObject]];
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:url]
                                                           cachePolicy:NSURLRequestUseProtocolCachePolicy
                                                       timeoutInterval:10.0];
    [request setHTTPMethod:@"GET"];
    NSURLSession *session = [NSURLSession sharedSession];
    NSURLSessionDataTask *dataTask = [session dataTaskWithRequest:request
                                                completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
                                                    if (error) {
                                                        [self.viewControllers.firstObject performSelector:@selector(reload) withObject:nil afterDelay:0];
                                                        
                                                        [[UIApplication sharedApplication] endIgnoringInteractionEvents];
                                                        completionBlock(NO, NULL);
                                                    } else {
                                                        NSDictionary *jsonData = [NSJSONSerialization JSONObjectWithData:data options:kNilOptions error:&error];
                                                        NSLog(@"%@", jsonData);
                                                        if (jsonData[@"error"]) {
                                                           [SVProgressHUD dismiss];
                                                            completionBlock(NO, NULL);
                                                        }
                                                        NSArray* json = [self parseEarthQuackDetail:jsonData];
                                                        if (json) {
                                                            NSLog(@"Data Fetched -> Recent Earthquakes. %lu", (unsigned long)[json count]);
                                                            [SVProgressHUD dismiss];
                                                            completionBlock(YES, json);
                                                        } else {
                                                            [SVProgressHUD dismiss];
                                                            completionBlock(NO, NULL);
                                                        }
                                                    }
                                                }];
    [dataTask resume];
    
    return array;
}

-(NSArray *) parseEarthQuackDetail: (NSDictionary *)data {
    [self.notifications removeAllObjects];
    NSMutableArray *dataArray = [data objectForKey:@"features"];
    for (NSMutableDictionary *currentEarthQuake in dataArray) {
        
        NSMutableDictionary *propertiesCurrentEarthQuake = [currentEarthQuake objectForKey:@"properties"];
        NSMutableDictionary *geometryCurrentEarthQuake = [currentEarthQuake objectForKey:@"geometry"];
        NSMutableArray *coordinatesCurrentEarthQuake = [geometryCurrentEarthQuake objectForKey:@"coordinates"];
        
        NSString *title = [propertiesCurrentEarthQuake objectForKey:@"title"];
        NSString *magnitude = [NSString stringWithFormat:@"%@", [propertiesCurrentEarthQuake objectForKey:@"mag"]];
        NSString *place = [propertiesCurrentEarthQuake objectForKey:@"place"];
        NSString *latitude = [NSString stringWithFormat:@"%@", [coordinatesCurrentEarthQuake objectAtIndex:1]];
        NSString *longitude = [NSString stringWithFormat:@"%@", [coordinatesCurrentEarthQuake objectAtIndex:0]];
        NSString *updatedTime = [NSString stringWithFormat:@"%@", [propertiesCurrentEarthQuake objectForKey:@"time"]];
        
        Notification *n = [[Notification alloc] initWithTitle:title lat:latitude lon:longitude magnitude:magnitude updatedTime:updatedTime place:place];
        
        [self.notifications addObject:n];
    }
    return self.notifications;
}

-(void) loadRecentEarthQuakes: (double)radius {
    [[UIApplication sharedApplication] beginIgnoringInteractionEvents];
    
    BOOL checkingNetwork = [Helper isConnectedToNetwork];
    if (!checkingNetwork) {
        [Helper showHudWithError:@""];
        [_notifications removeAllObjects];
        
        [self.viewControllers.firstObject performSelector:@selector(reload) withObject:nil afterDelay:0];
        [self.viewControllers.lastObject performSelector:@selector(reload) withObject:nil afterDelay:0];
        [[UIApplication sharedApplication] endIgnoringInteractionEvents];
    }
    
    [self getRecentEarthquakes: radius: ^(bool success, NSArray* response) {
        if (success) {
            self.boundedAnnotation = NO;
            NSMutableArray *tempdata =[[NSMutableArray alloc]initWithArray:response];
            if (success && tempdata.count) {
                dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                    [self.viewControllers.firstObject performSelector:@selector(reload) withObject:nil afterDelay:0];
                    [self.viewControllers.lastObject performSelector:@selector(reload) withObject:nil afterDelay:0];
                    [[UIApplication sharedApplication] endIgnoringInteractionEvents];
                });
            }
        }
    }];
}

-(NSArray *) getPreviousMonthDate {
    NSArray *dates = [[NSArray alloc] init];
    
    NSDate *currentDate = [NSDate date];
    NSCalendar *calendar = [NSCalendar currentCalendar];
    NSDateComponents *components = [calendar components:NSCalendarUnitDay | NSCalendarUnitMonth | NSCalendarUnitYear fromDate:currentDate];
    NSDateFormatter * dateFormatter = [[NSDateFormatter alloc]init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd"];
    
    NSString *stringCurrentDate = [dateFormatter stringFromDate:currentDate];
    
    [components setMonth:components.month - 1];
    NSDate *previosDate = [calendar dateFromComponents:components];
    
    
    NSString *stringPrevioseDate = [dateFormatter stringFromDate:previosDate];
    NSLog(@"Previous Date: %@", previosDate.description);
    
    dates = @[stringPrevioseDate, stringCurrentDate];
    
    return dates;
}
//END WASIQ
@end
