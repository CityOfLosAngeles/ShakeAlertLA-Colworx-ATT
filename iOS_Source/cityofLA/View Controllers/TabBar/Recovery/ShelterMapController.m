//
//  ShelterMapController.m
//  cityofLA
//
//  Created by Sam Sidd on 05/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import "ShelterMapController.h"
#import "TwoTabController.h"
#import "ShelterListItem.h"
#import "Helper.h"


@interface ShelterMapController () <GMSMapViewDelegate>

@end
//CTI: Plots the shelters pin to the mapview.
@implementation ShelterMapController{
    TwoTabController *parentVc;
}
-(void)viewDidAppear:(BOOL)animated{
    [self selectLocation];
}
- (void)viewDidLoad {
    [super viewDidLoad];
    parentVc = [TwoTabController sharedInstance];
    self.map = [[GMSMapView alloc]initWithFrame:parentVc.containerView.bounds];
    [self.view addSubview:self.map];
    self.map.delegate = self;
    self.map.isAccessibilityElement = YES;
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        [Helper moveToLAOnMap:self.map];
    });
}
-(void)reload{
    GMSCoordinateBounds *bounds = [[GMSCoordinateBounds alloc]init];
    self.markers = [[NSMutableArray alloc]init];
    for (ShelterListItem *item in parentVc.pins) {
        GMSMarker *marker = [[GMSMarker alloc]init];
        marker.position = item.coordinates;
        marker.title = item.title;
        marker.icon  = [UIImage imageNamed:@"find_pin"];
        marker.snippet = item.desc;
        marker.map   = self.map;
        marker.userData = item;
        marker.accessibilityLabel = marker.title;
        bounds = [bounds includingCoordinate:item.coordinates];
        [self.markers addObject:marker];
    }
    GMSCameraPosition *cameraPosition = [self.map cameraForBounds:bounds insets:UIEdgeInsetsMake(10,10,10,10)];
    [self.map animateToCameraPosition:cameraPosition];
    [self.map animateToZoom:10];
}
-(void)selectLocation{
    if (self.highlightLocation != nil) {
        for (GMSMarker *marker in self.markers) {
            if ([marker.title isEqualToString:self.highlightLocation]) {
                GMSCameraUpdate *markerLocation = [GMSCameraUpdate setTarget:marker.position];
                [self.map moveCamera:markerLocation];
                [self.map setSelectedMarker:marker];
                [self.map animateToZoom:10];
                self.highlightLocation = nil;
                break;
            }
        }
    }
}

-(BOOL)mapView:(GMSMapView *)mapView didTapMarker:(GMSMarker *)marker{
    return YES;
}

@end
