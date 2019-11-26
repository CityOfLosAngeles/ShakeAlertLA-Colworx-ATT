//
//  ShelterListItem.m
//  cityofLA
//
//  Created by Sam Sidd on 08/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import "ShelterListItem.h"
#import "Helper.h"

@implementation ShelterListItem

-(instancetype)initWithDict:(NSMutableDictionary *)dict{
    self = [super init];
    if (self) {
        self.title = [dict valueForKey:@"attributes"][@"FacilityName"];
        NSDictionary *geometry = [dict valueForKey:@"geometry"];
        float x = [geometry[@"x"] floatValue];
        float y = [geometry[@"y"] floatValue];
        self.coordinates = CLLocationCoordinate2DMake(y, x);
        return self;
    }
    return nil;
}

-(void)getAddress:(void(^)(NSString *address))completionBlock{
    __block CLPlacemark* placemark;
    __block NSString *address = nil;
   __weak ShelterListItem *weakSelf = self;
    CLGeocoder* geocoder = [CLGeocoder new];
    CLLocation *location = [[CLLocation alloc]initWithLatitude:self.coordinates.latitude longitude:self.coordinates.longitude];
    [geocoder reverseGeocodeLocation:location completionHandler:^(NSArray *placemarks, NSError *error){
        if (error == nil && [placemarks count] > 0){
            placemark = [placemarks lastObject];
            address = [NSString stringWithFormat:@"%@, %@ %@", placemark.name, placemark.postalCode, placemark.locality];
            weakSelf.desc = address;
            completionBlock(address);
        }else{
            completionBlock(@"");
            NSLog(@"%@",error.localizedDescription);;
            NSLog(@"%lu",(unsigned long)[placemarks count]);
        }
    }];
}

@end
