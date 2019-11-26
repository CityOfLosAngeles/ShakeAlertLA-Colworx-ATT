//
//  QuakeMapController.m
//  cityofLA
//
//  Created by Sam Sidd on 05/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import "QuakeMapController.h"
#import "EarthquakeInfo.h"
#import "EarthquakeDetailController.h"
#import "TwoTabController.h"
#import "Helper.h"

//WASIQ
#import "QuakeListController.h"
//End WASIQ
//dont rename
@interface QuakeMapController () <GMSMapViewDelegate>

@end

@implementation QuakeMapController
{
    TwoTabController *parentVc;
    NSMutableArray *earthQuakeList;
}

-(void)viewDidAppear:(BOOL)animated{
    [parentVc hideMenu];
    _firstLoaded = YES;
    if(!parentVc.popedViewController) {
        [self reload];
    }

}
- (void)viewDidLoad {
    [super viewDidLoad];
    parentVc = [TwoTabController sharedInstance];
    self.mapView = [[GMSMapView alloc]initWithFrame:parentVc.containerView.bounds];
    [self.view addSubview:self.mapView];
    self.mapView.delegate = self;
//    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
//        [Helper moveToLAOnMap:self.mapView];
//    });
    
    _bounds = [[GMSCoordinateBounds alloc] init];
    
}


-(void)reload{
    if (![Helper isConnectedToNetwork]) {
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            [Helper moveToLAOnMap:self.mapView];
        });
        return;
    }
    [self.mapView clear];
    _mapView.userInteractionEnabled = YES;
    GMSCoordinateBounds *bounds = [[GMSCoordinateBounds alloc]init];
    self.markers = [[NSMutableArray alloc]init];
    for (Notification *n in parentVc.notifications) {
        GMSMarker *marker = [[GMSMarker alloc]init];
        marker.position = n.coordinates;
        marker.title = n.title;
        marker.icon  = n.pinImage;
        marker.snippet = n.title;
        marker.map   = self.mapView;
        marker.userData = n;
        
        [self.markers addObject:marker];
        bounds = [bounds includingCoordinate:n.coordinates];
    }
    GMSCameraUpdate *update = [GMSCameraUpdate fitBounds:bounds withPadding:100];
    
    
    [self.mapView animateWithCameraUpdate:update];
}

-(UIView *)mapView:(GMSMapView *)mapView markerInfoWindow:(GMSMarker *)marker{
    EarthquakeInfo *infoWindow = (EarthquakeInfo *)[[[NSBundle mainBundle]loadNibNamed:@"EarthquakeInfo" owner:nil options:@{}] objectAtIndex:0];
    [infoWindow setupWithNotification:marker.userData];
    return infoWindow;
}
-(void)mapView:(GMSMapView *)mapView didTapInfoWindowOfMarker:(GMSMarker *)marker{
    EarthquakeDetailController *detailVc = [self.storyboard instantiateViewControllerWithIdentifier:@"earthquakeDetail"];
    detailVc.n = marker.userData;
    [parentVc.navigationController pushViewController:detailVc animated:YES];
}

//WASIQ
-(void)mapView:(GMSMapView *)mapView idleAtCameraPosition:(GMSCameraPosition *)position
{
    
    GMSVisibleRegion region = self.mapView.projection.visibleRegion;
    double distance = ([self checkForDistance:LACoordinates :region.farRight] / 1000);
    NSLog(@"DISTANCE DISTANCE: %f", distance);
//    [self drawLine:LACoordinates :region.farRight];
    
    if (distance > 4000 && _firstLoaded == NO) {
        [parentVc loadRecentEarthQuakes:4000];
        
        _firstLoaded = YES;
        return;
    }
    
    if (_zoomLevel == 0) {
        return;
    }
    if (_firstLoaded == YES) {
        parentVc.popedViewController = YES;
        [self getBoundedAnnotations];
        _zoomLevel = position.zoom;
        _firstLoaded = NO;
        return;
    }
    if (_zoomLevel == position.zoom) {
        return;
    }
    if (distance < parentVc.radius) {
        [self getBoundedAnnotations];
        return;
    }
    
    if (distance > parentVc.radius) {
        mapView.userInteractionEnabled = NO;
        NSLog(@"Distance: %f", distance);
        parentVc.radius = distance;
        
        [parentVc loadRecentEarthQuakes:distance];

        _firstLoaded = YES;
        return;
    }
    
    
}
- (void)mapView:(GMSMapView *)mapView didChangeCameraPosition:(GMSCameraPosition *)position {
    NSLog(@"didChangeCameraPosition -> Zoom Level: %f", position.zoom);
    if (_zoomLevel == position.zoom) {
        [self getBoundedAnnotations];
        _firstLoaded = YES;
        return;
    }
    if (position.zoom > _zoomLevel) {
        _zoomLevel = position.zoom;
        if (_firstLoaded) {
            return;
        }
        _firstLoaded = NO;
    }
}
-(double)checkForDistance:(CLLocationCoordinate2D )to :(CLLocationCoordinate2D )from {
    
    CLLocation *toCLL =[[CLLocation alloc] initWithLatitude:to.latitude longitude:to.longitude];
    CLLocation *fromCLL = [[CLLocation alloc] initWithLatitude:from.latitude longitude:from.longitude];
    double distanceMeter = [toCLL distanceFromLocation:fromCLL];
    
    return distanceMeter;
}
-(void) drawLine: (CLLocationCoordinate2D)to :(CLLocationCoordinate2D)from {
    GMSMutablePath *path = [GMSMutablePath path];
    [path addCoordinate:to];
    [path addCoordinate:from];

    GMSPolyline *line = [GMSPolyline polylineWithPath:path];
    line.map = self.mapView;

}

-(void) getBoundedAnnotations {
    [parentVc.boundedAnnotations removeAllObjects];
    GMSProjection *projection = _mapView.projection;
    for(Notification *n in parentVc.notifications) {
        if ([projection containsCoordinate:n.coordinates]) {
            [parentVc.boundedAnnotations addObject:n];
//            NSLog(@"Contain Within Screen: \n Title: %@", n.title);
        }
    }
    parentVc.boundedAnnotation = YES;
    [parentVc.viewControllers.firstObject performSelector:@selector(reload) withObject:nil afterDelay:0];
}
//END WASIQ
@end
