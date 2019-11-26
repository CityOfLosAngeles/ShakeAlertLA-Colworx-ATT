//
//  Notification.m
//  cityofLA
//
//  Created by Sam Sidd on 15/11/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//
#import "Helper.h"
#import "Notification.h"
#if TARGET_IOS_SIMULATOR
#import <objc/objc-runtime.h>
#else
#import <objc/runtime.h>
#endif
@implementation Notification

-(instancetype)initWithTitle:(NSString *)title
                        body:(NSString *)body
                         lat:(NSString *)lat
                         lon:(NSString *)lon
                         mmi:(NSString *)mmi
                   magnitude:(NSString *)magnitude
                   startTime:(NSString *)startTime
                       topic:(NSString *)topic
                          ID:(NSNumber *)ID{
    self = [super init];
    if (self) {
        self.title = title;
        self.body = body;
        self.LatitudeValue = lat;
        self.LongitudeValue = lon;
        self.startTime = startTime;
        self.MagnitudeValue = [NSString stringWithFormat:@"%.01f",magnitude.floatValue];
        self.MMI = mmi;
        self.ID = ID;
        
        self.coordinates = CLLocationCoordinate2DMake(lat.doubleValue, lon.doubleValue);
        self.timeToDisplay = [self.startTime getEarthQuakeTime];
        [self getAddress:^(NSString *address) {}];
        if ([self.MMI floatValue]<= 4.0) {
            self.pinImage = [UIImage imageNamed:@"green_pin"];
        }else if ([self.MMI floatValue] >= 5.0) {
            self.pinImage = [UIImage imageNamed:@"red_pin"];
        }else{
            self.pinImage = [UIImage imageNamed:@"yellow_pin"];
        }
        
        return self;
    }
    return nil;
}

//WASIQ
-(instancetype)initWithTitle:(NSString *)title
                         lat:(NSString *)lat
                         lon:(NSString *)lon
                   magnitude:(NSString *)magnitude
                 updatedTime:(NSString*)updatedTime
                       place:(NSString*)place {
    self = [super init];
    if (self) {
        self.title = title;
        self.LatitudeValue = lat;
        self.LongitudeValue = lon;
        self.startTime = updatedTime;
        self.MagnitudeValue = [NSString stringWithFormat:@"%.01f",magnitude.floatValue];
        self.address = place;
        self.coordinates = CLLocationCoordinate2DMake(lat.doubleValue, lon.doubleValue);
        self.timeToDisplay = [self.startTime getRecentEarthQuakeTime];
        
        if ([self.MagnitudeValue floatValue]<= 4.0) {
            self.pinImage = [UIImage imageNamed:@"green_pin"];
        }else if ([self.MagnitudeValue floatValue] >= 5.0) {
            self.pinImage = [UIImage imageNamed:@"red_pin"];
        }else{
            self.pinImage = [UIImage imageNamed:@"yellow_pin"];
        }
        
        return self;
    }
    return nil;
}

//END WASIQ

-(void)getAddress:(void(^)(NSString *address))completionBlock{
    __block CLPlacemark* placemark;
    __block NSString *address = nil;
    __weak Notification *weakSelf = self;
    CLGeocoder* geocoder = [CLGeocoder new];
    CLLocation *location = [[CLLocation alloc]initWithLatitude:self.coordinates.latitude longitude:self.coordinates.longitude];
    [geocoder reverseGeocodeLocation:location completionHandler:^(NSArray *placemarks, NSError *error){
        if (error == nil && [placemarks count] > 0){
            placemark = [placemarks lastObject];
            address = [NSString stringWithFormat:@"%@, %@", placemark.locality,placemark.postalCode];
            weakSelf.address = address;
            completionBlock(address);
        }else{
            completionBlock(@"");
            NSLog(@"%@",error.localizedDescription);;
            NSLog(@"%lu",(unsigned long)[placemarks count]);
        }
    }];
}
-(instancetype)initWithDict:(NSDictionary *)userInfo{
    self = [super init];
    if (self) {
        NSDictionary *json = userInfo[@"acme1"];
        self.title = userInfo[@"aps"][@"alert"][@"title"];
        self.body = userInfo[@"aps"][@"alert"][@"body"];
        self.MMI = json[@"MMI"];
        self.startTime = json[@"startTime"];
        
        self.LatitudeValue = json[@"LatitudeValue"];
        self.LongitudeValue = json[@"LongitudeValue"];
        
        self.MagnitudeValue = [NSString stringWithFormat:@"%.02f",[json[@"MagnitudeValue"] floatValue] ];
        self.MMI = json[@"MMI"];
        
        self.Polygon = json[@"Polygons"];
        self.Colors  = json[@"Colors"];
        self.SegmentID = json[@"SegmentID"];
        
        self.coordinates = CLLocationCoordinate2DMake(self.LatitudeValue.doubleValue, self.LongitudeValue.doubleValue);
        self.timeToDisplay = [self.startTime getEarthQuakeTime];
        [self getAddress:^(NSString *address) {}];
        self.coordinatesArr = [Helper getLocationsArray:self.Polygon];
        return self;
    }
    return nil;
}

- (NSMutableArray *)propertyNames {
    NSMutableArray *propNames = [[NSMutableArray alloc]init];;
    unsigned int outCount, i;
    objc_property_t *properties = class_copyPropertyList([Notification class], &outCount);
    for (i = 0; i < outCount; i++) {
        objc_property_t property = properties[i];
        NSString *propertyName = [NSString stringWithCString:property_getName(property) encoding:NSUTF8StringEncoding];
        [propNames addObject:propertyName];
    }
    free(properties);
    return propNames;
}


+(NSString*)getIntensity_EN:(NSString *)MMI {
    
    NSArray *intensity = @[@"Weak",@"Light",@"Moderate",@"Strong",@"Very Strong",@"Servere",@"Violent",@"Extreme"];
    
    if([MMI isEqualToString:@"2.0000"] || [MMI isEqualToString:@"3.0000"]) {
        
        return intensity[0];
        
    } else if([MMI isEqualToString:@"4.0000"]) {
        
        return intensity[1];
        
    }else if([MMI isEqualToString:@"5.0000"]) {
        
        return intensity[2];
        
    }else if([MMI isEqualToString:@"6.0000"]) {
        
        return intensity[3];
        
    }else if([MMI isEqualToString:@"7.0000"]) {
        
        return intensity[4];
        
    }else if([MMI isEqualToString:@"8.0000"]) {
        
        return intensity[5];
        
    }else if([MMI isEqualToString:@"9.0000"]) {
        
        return intensity[6];
        
    }else if([MMI isEqualToString:@"10.0000"]) {
        
        return intensity[7];
    }
    
    return @"";
    
}

+(NSString*)getIntensity_ES:(NSString *)MMI {
    
    NSArray *intensity = @[@"Débiles",@"Ligero",@"Moderar",@"Fuerte",@"Muy fuerte",@"Grave",@"Violento",@"Extremo"];
    
    if(([MMI isEqualToString:@"2.0000"]) || ([MMI isEqualToString:@"3.0000"])) {
        
        return intensity[0];
        
    } else if([MMI isEqualToString:@"4.0000"]) {
        
        return intensity[1];
        
    }else if([MMI isEqualToString:@"5.0000"]) {
        
        return intensity[2];
        
    }else if([MMI isEqualToString:@"6.0000"]) {
        
        return intensity[3];
        
    }else if([MMI isEqualToString:@"7.0000"]) {
        
        return intensity[4];
        
    }else if([MMI isEqualToString:@"8.0000"]) {
        
        return intensity[5];
        
    }else if([MMI isEqualToString:@"9.0000"]) {
        
        return intensity[6];
        
    }else if([MMI isEqualToString:@"10.0000"]) {
        
        return intensity[7];
    }
    
    return @"";
    
}
@end
