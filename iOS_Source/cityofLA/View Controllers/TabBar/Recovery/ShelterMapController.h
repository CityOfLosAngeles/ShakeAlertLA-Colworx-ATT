//
//  ShelterMapController.h
//  cityofLA
//
//  Created by Sam Sidd on 05/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import <UIKit/UIKit.h>
@import GoogleMaps;
@interface ShelterMapController : UIViewController

@property (strong,nonatomic) IBOutlet GMSMapView *map;
-(void)reload;
@property NSString *highlightLocation;
@property NSMutableArray<GMSMarker *> *markers;
@end
