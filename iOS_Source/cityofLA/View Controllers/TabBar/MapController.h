//
//  QuakeMapController.h
//  cityofLA
//
//  Created by Sam Sidd on 05/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Helper.h"
#import "Notification.h"

@import GoogleMaps;

@interface MapController : UIViewController
@property (strong,nonatomic) IBOutlet GMSMapView *map;
@property (weak, nonatomic) IBOutlet UILabel *additionalView;
@property BOOL plotEarthQuake;
@property Notification *n;
@property GMSPolygon *polygon;
@end
