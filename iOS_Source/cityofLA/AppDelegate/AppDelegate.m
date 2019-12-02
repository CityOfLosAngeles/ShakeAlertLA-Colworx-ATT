//
//  AppDelegate.m
//  cityofLA
//
//  Created by Sam Sidd on 8/2/18.
//  Copyright © 2018 CityOfLa. All rights reserved.
//


#define btnSize 65
#define btnRightMargin 16
#define btnBottomMargin 20
#define btnX ScreenWidth - (btnSize + btnRightMargin)
#define btnY ScreenHeight - (btnSize + btnBottomMargin)

#import "AppDelegate.h"
#import "Helper.h"
#import "NSString+Timestamp_Handling.h"
#import <UserNotifications/UserNotifications.h>
#import "TabBarController.h"
#import "MapController.h"
#import "Notification.h"

@import AWSMobileClient;
@import AWSCore;
@import AWSPinpoint;

@import IQKeyboardManager;

@interface AppDelegate () <UNUserNotificationCenterDelegate,CLLocationManagerDelegate>{
    AWSPinpoint *pinpoint;
}

@end

@implementation AppDelegate
{
    NSDictionary *options;
}
+(AppDelegate *)sharedInstance{
    return (AppDelegate*)[[UIApplication sharedApplication] delegate];
}
- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    options = launchOptions;
    BOOL appLaunch = [[NSUserDefaults standardUserDefaults] boolForKey:@"appLauchedAlready"];
    if(!appLaunch){
        [[NSUserDefaults standardUserDefaults] setBool:YES forKey:@"appLauchedAlready"];
        [[NSUserDefaults standardUserDefaults] synchronize];
        NSString *language = [[[NSBundle mainBundle] preferredLocalizations] objectAtIndex:0];
        if([language containsString:@"es"]){
            //CTI: Set's the default localization on App install.
            [[NSUserDefaults standardUserDefaults]setObject:@"es" forKey:@"lang"];
            [[NSUserDefaults standardUserDefaults] synchronize];
        }
    }
    //REQUEST PUSH NOTIFICATION PERMISSION
    
    


    [[UINavigationBar appearance]setTranslucent:NO];
//    [[UINavigationBar appearance] setBackgroundImage:[[UIImage alloc] init]
//                                      forBarPosition:UIBarPositionAny
//                                          barMetrics:UIBarMetricsDefault];
    
    [[UINavigationBar appearance] setShadowImage:[[UIImage alloc] init]];
    //CTI: Enables GMSService for Google Maps
    [GMSServices provideAPIKey:google_maps_api_key];
    [GMSPlacesClient provideAPIKey:google_maps_api_key];
    
    
    [self IfOnboardedMoveToTabs];
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(2 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        [IQKeyboardManager sharedManager].enable = YES;
        //AWS Configuration
//        [AWSDDLog addLogger:AWSDDTTYLogger.sharedInstance];
        
        
        
//        [[AWSDDLog sharedInstance] setLogLevel:AWSDDLogLevelInfo];
        
        //CTI: Setup Pinpoint configuartion.
        self->pinpoint = [AWSPinpoint pinpointWithConfiguration:[AWSPinpointConfiguration defaultPinpointConfigurationWithLaunchOptions:launchOptions]];
        
        
        
        
        
        [[DBHelper sharedInstance]initialize];
        [self registerForRemoteNotifications];
        [self setupLocationManager];
        
        
    });
  

    return true;
}

//CTI: This methods decides if Onboarding screens wizard is finished, move to Home screen.
-(void)IfOnboardedMoveToTabs{
    [SVProgressHUD setDefaultStyle:SVProgressHUDStyleDark];
    BOOL onboarded = [[NSUserDefaults standardUserDefaults]boolForKey:@"onboarded"];
    if (onboarded){
        UITabBarController *tabBar = [[UIStoryboard storyboardWithName:@"Main" bundle:nil]instantiateViewControllerWithIdentifier:@"tabBar"];
        self.window.rootViewController = tabBar;
    }
}

//CTI: Registers device for remote notifications
-(void) registerForRemoteNotifications{
    UNUserNotificationCenter *center = [UNUserNotificationCenter currentNotificationCenter];
    center.delegate = self;
    [center requestAuthorizationWithOptions:(UNAuthorizationOptionSound | UNAuthorizationOptionAlert | UNAuthorizationOptionCriticalAlert | UNAuthorizationOptionBadge) completionHandler:^(BOOL granted, NSError * _Nullable error){
        if(!error){
            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                    [[UIApplication sharedApplication] registerForRemoteNotifications];
            });
        }
    }];
}


//CTI: Enables the location manager to monitor location changes.
-(void)setupLocationManager{
    self.manager = [[CLLocationManager alloc]init];
    [self.manager setDelegate:self];
    
    if ([options objectForKey:UIApplicationLaunchOptionsLocationKey]) {
        NSLog(@"app launched from killed state for location event");
        [self.manager startMonitoringSignificantLocationChanges];
    }else{
        [self.manager stopMonitoringSignificantLocationChanges];
    }
    
    [self.manager setDistanceFilter:kCLHeadingFilterNone];
    [self.manager setDesiredAccuracy:kCLLocationAccuracyBest];
    [self.manager setPausesLocationUpdatesAutomatically:NO];
    [self.manager requestAlwaysAuthorization];
    [self.manager startUpdatingLocation];
}

//CTI: Registers device token at Pinpoint
-(void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken{

    const char *data = [deviceToken bytes];
    NSMutableString *token = [NSMutableString string];
    for (NSUInteger i = 0; i < [deviceToken length]; i++) {
        [token appendFormat:@"%02.2hhX", data[i]];
    }
    self.deviceToken = [token copy];
    NSLog(@"COLA current device token is %@",self.deviceToken);
    [[pinpoint notificationManager] interceptDidRegisterForRemoteNotificationsWithDeviceToken:deviceToken];
    [[NSUserDefaults standardUserDefaults]setObject:self.deviceToken forKey:@"deviceToken"];
    if (self.manager.location != nil) {
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.7 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                [self registerDevice];
        });
        
    }
}
-(void)applicationWillEnterForeground:(UIApplication *)application{
    if (self.manager.location != nil) {
        [self registerDevice];
    }
}
-(void)applicationDidEnterBackground:(UIApplication *)application{
    //this keeps the location update running in background
    if ([[UIDevice currentDevice] respondsToSelector:@selector(isMultitaskingSupported)]) { //Check if our iOS version supports multitasking I.E iOS 4
        
        if ([[UIDevice currentDevice] isMultitaskingSupported]) { //Check if device supports mulitasking
            UIApplication *application = [UIApplication sharedApplication]; //Get the shared application instance
            
            __block UIBackgroundTaskIdentifier background_task; //Create a task object
            
            background_task = [application beginBackgroundTaskWithExpirationHandler: ^{
                [application endBackgroundTask:background_task]; //Tell the system that we are done with the tasks
                background_task = UIBackgroundTaskInvalid; //Set the task to be invalid
                //System will be shutting down the app at any point in time now
            }];
        }
    }
}
-(void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error{
    NSLog(@"COLA Failed %@",error.description);
}

//RECEIVED
- (void)userNotificationCenter:(UNUserNotificationCenter *)center didReceiveNotificationResponse:(UNNotificationResponse *)response withCompletionHandler:(void(^)(void))completionHandler{
    self.handeledOnTap = YES;
    Notification *n = [[Notification alloc]initWithDict:response.notification.request.content.userInfo];
    UINavigationController *nav = [[TabBarController sharedInstance].viewControllers objectAtIndex:1];
    MapController *mapVc = nav.viewControllers.firstObject;
    mapVc.plotEarthQuake = YES;
    mapVc.n = n;
    [TabBarController sharedInstance].selectedIndex = 1;
    NSLog(@"handling with tap");
    
    self.notification = n;
    BOOL saved = [[DBHelper sharedInstance] saveNotificationObj:self.notification];
    NSLog(@"notification saved to db : %d",saved);
   
    if (n.SegmentID != nil && ![n.SegmentID isEqual:[NSNull null]]) {
        [self logPushReceived];
    }
    
}
-(void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler{
   
    
    self.notification =[[Notification alloc]initWithDict:userInfo];
    
     [[pinpoint notificationManager] interceptDidReceiveRemoteNotification:userInfo fetchCompletionHandler:completionHandler];
    
    NSLog(@"COLA Notification received %@",userInfo.description);
//    if (self.handeledOnTap) {
//        self.handeledOnTap = NO;
//    }else{
        [self handleNotification:userInfo];
        NSLog(@"handling without tap");
//    }
    completionHandler(UIBackgroundFetchResultNewData);
}
-(void)logPushReceived{
    if (self.notification.SegmentID != nil && ![self.notification.SegmentID isEqual:[NSNull null]]) {
        [Helper hitPushOpenRequest:self.notification.SegmentID];
    }
}
-(void)handleNotification:(NSDictionary *)userInfo{
    if ([TabBarController sharedInstance] != nil) {
        [self showEarthquakeAlertWithNotification:self.notification];
    }
}

//WHEN RECEIVED
-(void)showEarthquakeAlertWithNotification:(Notification *)n{
//    self.notification = [[Notification alloc]initWithDict:userInfo];
    BOOL saved = [[DBHelper sharedInstance] saveNotificationObj:n];
    NSLog(@"notification saved to db : %d",saved);
    [self logPushReceived];
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:n.title message:n.body preferredStyle:UIAlertControllerStyleAlert];
    
    [alert addAction:[UIAlertAction actionWithTitle:[Helper localized:@"alertBtnCancel"] style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        
    }]];
    [alert addAction:[UIAlertAction actionWithTitle:[Helper localized:@"alertBtnOk"] style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        UINavigationController *nav = [[TabBarController sharedInstance].viewControllers objectAtIndex:1];
        MapController *mapVc = nav.viewControllers.firstObject;
        mapVc.plotEarthQuake = YES;
        mapVc.n = n;
        [TabBarController sharedInstance].selectedIndex = 1;
    }]];
    [[Helper topMostController] presentViewController:alert animated:YES completion:nil];
    self.notification = nil;
}

#pragma MARK CLLLocationManager Delegate :

-(void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray<CLLocation *> *)locations{
    NSLog(@"location updated to");
    NSLog(@"%f,%f",locations.firstObject.coordinate.latitude,locations.firstObject.coordinate.longitude);
    if ([options objectForKey:UIApplicationLaunchOptionsLocationKey]) {
        [self registerDevice];
    }
    else if ([self shouldRegisterDeviceLocation]){
        [self registerDevice];
    }
}
-(BOOL)shouldRegisterDeviceLocation{
    return [self.lastSentLocation distanceFromLocation:self.manager.location] > 500 && [UIApplication sharedApplication].applicationState == UIApplicationStateBackground;
}
-(void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error{
    NSLog(@"location getting failed with error : %@",error.localizedDescription);
}
//CTI: Registers device at the backend, the user is anonymously segmented to grids based on the location
-(void)registerDevice{
    self.deviceToken = [[NSUserDefaults standardUserDefaults]valueForKey:@"deviceToken"];
    if (self.deviceToken == nil) {
        return;
    }
    self.registeringDevice = YES;
    
    NSString *lang = [[NSUserDefaults standardUserDefaults]valueForKey:@"lang"];
    if ([lang isEqualToString:@"es"]){
        lang = @"Block-ES-";
    }else{
        lang = @"Block-EN-";
    }
    
    __block CLLocation *loc = [[CLLocation alloc]initWithLatitude:self.manager.location.coordinate.latitude longitude:self.manager.location.coordinate.longitude];
    
    NSDictionary *body =@{};
  
    
    
    
    body = @{
                           @"DeviceID":self.deviceToken,
                           @"LatLong": [NSString stringWithFormat:@"%f,%f",self.manager.location.coordinate.latitude,self.manager.location.coordinate.longitude],
                           @"Language": lang
                           };
    
    
    //76 : 33.837730, -117.907729
    //79 : 34.490752, -117.947141
    //95 : 33.721569, -117.731910
    //41 : 32.831029, -118.371905
    //80 : 34.748279, -118.003863
    //7: 34.145803, -118.904742
    
//    self.manager.location.coordinate.latitude,self.manager.location.coordinate.longitude
    //replace static values with this
    //
    
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:@"<server_address>/registerDevice"]
                                                           cachePolicy:NSURLRequestUseProtocolCachePolicy
                                                       timeoutInterval:30.0];
    
    if(isTesting){
//        34.052235, -118.243683
        body = @{
                 @"DeviceID":self.deviceToken,
                 @"LatLong": [NSString stringWithFormat:@"%f,%f",
self.manager.location.coordinate.latitude,self.manager.location.coordinate.longitude
                              ],
                 @"Language": lang
                 };
        request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:@"<server_address>/registerDeviceDev"]
                                          cachePolicy:NSURLRequestUseProtocolCachePolicy
                                      timeoutInterval:30.0];
    }
    NSData *postData = [NSJSONSerialization dataWithJSONObject:body options:NSJSONWritingPrettyPrinted error:nil];
    [request setHTTPMethod:@"POST"];
    [request setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:postData];
    
    __weak AppDelegate *ap = self;
    NSURLSession *session = [NSURLSession sharedSession];
    NSURLSessionDataTask *dataTask = [session dataTaskWithRequest:request
                                                completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
                                                    dispatch_async(dispatch_get_main_queue(), ^{
                                                        self.registeringDevice = NO;
                                                        if (error) {
                                                            NSLog(@"%@", error.localizedDescription);
                                                        } else {
                                                            NSHTTPURLResponse *httpResponse = (NSHTTPURLResponse *) response;
                                                            id json = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:nil];
                                                            
                                                            if ([json[@"msg"] isEqual:[NSNull null]] || [json[@"msg"] isKindOfClass:[NSNull class]]) {
                                                                NSLog(@"msg is null");
                                                            }
                                                            else if (httpResponse.statusCode == 200) {
                                                                ap.lastSentLocation = loc;
                                                                if([json[@"msg"] isKindOfClass:[NSString class]]){
                                                                    
                                                                }
                                                                else
                                                                    [ap updateAWSProfile:[NSString stringWithFormat:@"%ld",(long)[json[@"msg"] longValue]]];
                                                            }else{
                                                                NSLog(@"error while sending location to server");
                                                            }
                                                        }
                                                    });
                                                }];
    [dataTask resume];
}

//CTI: Segments are updated at pinpoint

-(void)updateAWSProfile:(NSString *)code{
    
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(2 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        AWSPinpointEndpointProfile *profile = [self->pinpoint.targetingClient currentEndpointProfile];
        
        NSString *lang = [[NSUserDefaults standardUserDefaults]valueForKey:@"lang"];
        if ([lang isEqualToString:@"es"]){
            [profile addAttribute:@[@"0"] forKey:@"Block-EN-"];
            [profile addAttribute:@[code]
                           forKey:@"Block-ES-"];
        }else{
            [profile addAttribute:@[@"0"] forKey:@"Block-ES-"];
            [profile addAttribute:@[code]
                           forKey:@"Block-EN-"];
        }
        
        [[self->pinpoint targetingClient] updateEndpointProfile: profile];
    });
    
}
-(void)playSound{
    [[AVAudioSession sharedInstance]setCategory:AVAudioSessionCategoryPlayback error:nil];
    [[AVAudioSession sharedInstance]setActive:YES error:nil];
    NSString *soundFilePath = [NSString stringWithFormat:@"%@/alert.mp3",[[NSBundle mainBundle] resourcePath]];
    NSURL *soundFileURL = [NSURL fileURLWithPath:soundFilePath];
    
    _player = [[AVAudioPlayer alloc] initWithContentsOfURL:soundFileURL error:nil];
    _player.numberOfLoops = 1;
    
    [_player play];
}




// (Optional) Create a function and customize the event recording.

-(void) logEvents {
    AWSPinpointAnalyticsClient *pinpointAnalyticsClient = pinpoint.analyticsClient;
    
    AWSPinpointEvent *event = [pinpointAnalyticsClient createEventWithEventType:@"CustomEvent"];
    [event addAttribute:@"DemoAttributeValue1" forKey:@"DemoAttribute1"];
    [event addAttribute:@"DemoAttributeValue2" forKey:@"DemoAttribute2"];
    [event addMetric:[NSNumber numberWithInteger:arc4random() % 65535] forKey:@"EventName"] ;
    [pinpointAnalyticsClient recordEvent:event];
    [pinpointAnalyticsClient submitEvents];
    
}
@end
