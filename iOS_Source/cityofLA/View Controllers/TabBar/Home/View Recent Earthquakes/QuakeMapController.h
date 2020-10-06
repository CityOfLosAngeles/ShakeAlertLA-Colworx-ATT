//
//  QuakeMapController.h
//  cityofLA
//
//  Created by Sam Sidd on 05/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import <UIKit/UIKit.h>
@import GoogleMaps;

@interface QuakeMapController : UIViewController
@property (strong,nonatomic) IBOutlet GMSMapView *mapView;
@property NSMutableArray<GMSMarker *> *markers;
@property GMSCoordinateBounds *bounds;
-(void)reload;

//    Colworx
@property float zoomLevel;
@property BOOL firstLoaded;
//END Colworx
@end
