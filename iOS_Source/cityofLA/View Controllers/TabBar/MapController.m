//
//  QuakeMapController.m
//  cityofLA
//
//  Created by Sam Sidd on 05/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import "MapController.h"
#import "cityofLA-Swift.h"
#import "AppDelegate.h"
#import "EarthquakeDetailController.h"
#import "UIColorHelper.h"
#import "EarthquakeInfo.h"

@interface MapController () <GMSMapViewDelegate>

@end

@implementation MapController{
    BOOL firstLocationUpdate;
}
- (void)viewDidLoad {
    [super viewDidLoad];
    [Helper applyAttributesOnVc:self withColor:quakeRedColor];
    [[self additionalView] setBackgroundColor:quakeRedColor];
     [[self additionalView] setText:[Helper localized:@"earthQuakeAlertMap"]];
    self.map.delegate = self;
    self.map.myLocationEnabled = YES;
    [Helper moveToLAOnMap:self.map];
}
-(void)viewWillAppear:(BOOL)animated{
    self.title = [Helper localized:@"quakeMapTitle"];
    [self.map addObserver:self
               forKeyPath:@"myLocation"
                  options:NSKeyValueObservingOptionNew
                  context:NULL];
}
-(void)viewDidAppear:(BOOL)animated{
    if (self.plotEarthQuake) {
//        self.plotEarthQuake = NO;
        [self moveToEarthQuakeLocation];
    }
}
-(void)moveToEarthQuakeLocation{
    [self.map clear];
    GMSCoordinateBounds *bounds = [[GMSCoordinateBounds alloc]init];
  
    for (int i = (int)self.n.Polygon.count - 1;i >= 0 ; i--) {
        
        NSArray *polygonArr = self.n.Polygon[i];
        GMSMutablePath *rect = [GMSMutablePath path];
        for (NSString *latLong in polygonArr) {
            NSArray *comps = [latLong componentsSeparatedByString:@","];
            CLLocationCoordinate2D coordinate =  CLLocationCoordinate2DMake([[comps firstObject] doubleValue], [[comps lastObject] doubleValue]);
            CLLocation *loc = [[CLLocation alloc]initWithLatitude:coordinate.latitude longitude:coordinate.longitude];
            [rect addCoordinate:loc.coordinate];
            bounds = [bounds includingCoordinate:loc.coordinate];
        }
        GMSPolygon *polygon = [GMSPolygon polygonWithPath:rect];
        polygon.fillColor   = [Helper colorFromHexString:self.n.Colors[i]];
        polygon.strokeColor = [UIColorHelper colorWithRGBA:self.n.Colors[i]];
        polygon.strokeWidth = 2;
        polygon.map = self.map;
        
    }
//    GMSMutablePath *rect = [GMSMutablePath path];
//
//    for (CLLocation *loc in self.n.coordinatesArr) {
//        [rect addCoordinate:loc.coordinate];
//
//    }
//
//    self.polygon = [GMSPolygon polygonWithPath:rect];
//    self.polygon.fillColor = [UIColor colorWithRed:255.0 green:0 blue:0 alpha:0.05];
//    self.polygon.strokeColor = [UIColor redColor];
//    self.polygon.strokeWidth = 2;
//    self.polygon.map = self.map;
    
    GMSMarker *marker = [[GMSMarker alloc]init];
    marker.position = self.n.coordinates; // CLLocationCoordinate2DMake(34.0407,-118.2468);
//    marker.position = CLLocationCoordinate2DMake([AppDelegate sharedInstance].manager.location.coordinate.latitude, [AppDelegate sharedInstance].manager.location.coordinate.longitude);
    marker.title = self.n.title;
    marker.snippet = self.n.body;
    marker.map = self.map;
    marker.userData = self.n;
    
    
    [self.n getAddress:^(NSString * _Nonnull address) {
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            self.map.selectedMarker = marker;
            GMSCameraUpdate *quakeUpdate = [GMSCameraUpdate fitBounds:bounds withPadding:30];
            [self.map animateWithCameraUpdate:quakeUpdate];
        });
    }];
    
    
}
-(UIView *)mapView:(GMSMapView *)mapView markerInfoWindow:(GMSMarker *)marker{
    
    EarthquakeInfo *info = [[[NSBundle mainBundle]loadNibNamed:@"EarthquakeInfo" owner:nil options:@{}] firstObject];
    [info setupWithNotification:self.n];
    return info;
    
}
-(void)mapView:(GMSMapView *)mapView didTapInfoWindowOfMarker:(GMSMarker *)marker{
    EarthquakeDetailController *detailVc = [[UIStoryboard storyboardWithName:@"TwoTabs" bundle:nil] instantiateViewControllerWithIdentifier:@"earthquakeDetail"];
    detailVc.n = marker.userData;
    [self.navigationController pushViewController:detailVc animated:YES];
}
- (void)dealloc {
    [self.map removeObserver:self
                  forKeyPath:@"myLocation"
                     context:NULL];
}

#pragma mark - KVO updates

- (void)observeValueForKeyPath:(NSString *)keyPath
                      ofObject:(id)object
                        change:(NSDictionary *)change
                       context:(void *)context {
    
    if (self.plotEarthQuake) {
        return;
    }
    
    if (!firstLocationUpdate) {
        firstLocationUpdate = YES;
        CLLocation *location = [change objectForKey:NSKeyValueChangeNewKey];
        self.map.camera = [GMSCameraPosition cameraWithTarget:location.coordinate
                                                         zoom:14];
    }
}

@end
