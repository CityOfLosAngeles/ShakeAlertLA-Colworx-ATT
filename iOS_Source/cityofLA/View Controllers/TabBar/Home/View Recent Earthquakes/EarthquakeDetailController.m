//
//  EarthquakeDetailController.m
//  cityofLA
//
//  Created by Sam Sidd on 22/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import "EarthquakeDetailController.h"
#import "Helper.h"

//WASIQ
#import "TwoTabController.h"
//END WASIQ
@interface EarthquakeDetailController ()

@end

@implementation EarthquakeDetailController
-(void)viewWillAppear:(BOOL)animated{
    [self.navigationController.navigationBar setHidden:NO];
    self.navigationController.navigationBar.prefersLargeTitles = YES;
    [Helper customBackBtnOnVc:self];
}
- (void)viewWillDisappear:(BOOL)animated{
    
}
- (void)viewDidLoad {
    [super viewDidLoad];
    self.title = [Helper localized:@"details"];
    [Helper drawDropShadowOnView:self.cardView];
    self.magnitudeTitle.text =   [Helper localized:@"magnitude"];
    self.timeTitle.text      =   [Helper localized:@"time"];
    self.locationTitle.text  =   [Helper localized:@"location"];
    self.latitudeTitle.text  =   [Helper localized:@"latitude"];
    self.longitudeTitle.text =   [Helper localized:@"longitude"];
    self.intensityTitle.text =   [Helper localized:@"intensity"];
    [self setup];

}
-(void)setup{
    self.titleLbl.text = self.n.title;
    self.magnitude.text = self.n.MagnitudeValue;
//    self.time.text = self.n.timeToDisplay;
    self.time.text = [NSString stringWithFormat:@"%@ (PST)",self.n.timeToDisplay];
    self.location.text = self.n.address;
    self.latitude.text = self.n.LatitudeValue;
    self.longitude.text = self.n.LongitudeValue;
    
    [Helper drawDropShadowOnView:self.cardView];
    //WASIQ
    if(_fromQuakeList) {
        [TwoTabController sharedInstance].popedViewController = YES;
    } else {
        [TwoTabController sharedInstance].popedViewController = YES;
    }
    //END WASIQ
}
@end
