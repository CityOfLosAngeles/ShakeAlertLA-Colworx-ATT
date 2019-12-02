//
//  EarthquakeInfo.m
//  cityofLA
//
//  Created by Sam Sidd on 22/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import "EarthquakeInfo.h"
#import "Helper.h"
#import "EarthquakeDetailController.h"

@implementation EarthquakeInfo

- (void)drawRect:(CGRect)rect {
    [super drawRect: rect];
}
//CTI: Info tip view for push notification on the map view
-(void)setupWithNotification:(Notification *)n{
    [self.detailBtn setTitle:[Helper localized:@"clickForDetail"] forState:UIControlStateNormal];
    self.n = n;
    self.titleLbl.text = self.n.title;
    self.descLbl.text = self.n.body;
    self.magnitudeLbl.text = [NSString stringWithFormat:@"%@: %@",[Helper localized:@"magnitude"], self.n.MagnitudeValue];
    self.dateLbl.text= self.n.timeToDisplay;
    if (self.n.address == nil) {
        [self.n getAddress:^(NSString * _Nonnull address) {
            self.addressLbl.text = self.n.address;
        }];
    }else{
        self.addressLbl.text = self.n.address;
    }
    
}
@end
