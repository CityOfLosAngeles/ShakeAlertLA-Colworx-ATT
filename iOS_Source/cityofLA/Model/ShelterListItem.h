//
//  ShelterListItem.h
//  cityofLA
//
//  Created by Sam Sidd on 08/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import <Foundation/Foundation.h>
@import CoreLocation;

@interface ShelterListItem : NSObject
-(instancetype)initWithDict:(NSMutableDictionary *)dict;
@property NSString *title;
@property NSString *desc;
@property CLLocationCoordinate2D coordinates;
-(void)getAddress:(void(^)(NSString *address))completionBlock;
@end
