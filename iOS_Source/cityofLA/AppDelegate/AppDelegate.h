//
//  AppDelegate.h
//  cityofLA
//
//  Created by Sam Sidd on 8/2/18.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DBHelper.h"
#import "SVProgressHUD.h"
#import <AudioToolbox/AudioToolbox.h>
#import <AVFoundation/AVFoundation.h>
@import CoreLocation;
@import GoogleMaps;
@import GooglePlaces;

//CTI: isTesting 1 points all keys to development environment. 0  is production
#define isTesting 1


@interface AppDelegate : UIResponder <UIApplicationDelegate>
@property (strong, nonatomic) UIWindow *window;

+(AppDelegate *)sharedInstance;
@property CLLocationManager *manager;
@property NSString *deviceToken;
-(void)registerDevice;
@property BOOL registeringDevice;
-(void)handleNotification:(NSDictionary *)userInfo;
@property AVAudioPlayer *player;
    
@property Notification *notification;
-(void)showEarthquakeAlertWithNotification:(Notification *)n;
@property BOOL handeledOnTap;
-(void) logEvents;

@property CLLocation *lastSentLocation;
@end

