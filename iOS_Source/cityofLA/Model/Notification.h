//
//  Notification.h
//  cityofLA
//
//  Created by Sam Sidd on 15/11/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "NSString+Timestamp_Handling.h"
@import CoreLocation;
@import UIKit;

NS_ASSUME_NONNULL_BEGIN

@interface Notification : NSObject
@property NSNumber *ID;
@property NSString* title;
@property NSString* body;
@property NSString* Category;
@property NSString* DepthUnit;
@property NSString* DepthValue;
@property NSString* EventOriginTimeStampUnit;
@property NSString* EventOriginTimeStampValue;
@property NSString* LatitudeUnit;
@property NSString* LatitudeValue;
@property NSString* Likelihood;
@property NSString* LongitudeUnit;
@property NSString* LongitudeValue;
@property NSString* MMI;
@property NSString* MagnitudeUnit;
@property NSString* MagnitudeValue;
@property NSString* MessageOriginSystem;
@property NSString* TimeStamp;
@property NSString* Topic;
@property NSString* Type;
@property NSString* startTime;
@property NSString* SegmentID;
@property NSArray* Polygon;
@property NSArray* Colors;


@property CLLocationCoordinate2D coordinates;
@property NSString *timeToDisplay;
@property NSString *address;
@property UIImage *pinImage;
@property NSMutableArray<CLLocation *> *coordinatesArr;;
@property NSString *intensity;

-(instancetype)initWithTitle:(NSString *)title
                        body:(NSString *)body
                         lat:(NSString *)lat
                         lon:(NSString *)lon
                         mmi:(NSString *)mmi
                   magnitude:(NSString *)magnitude
                   startTime:(NSString *)startTime
                       topic:(NSString *)topic
                          ID:(NSNumber *)ID;

//WASIQ
-(instancetype)initWithTitle:(NSString *)title
                         lat:(NSString *)lat
                         lon:(NSString *)lon
                   magnitude:(NSString *)magnitude
                 updatedTime:(NSString*)updatedTime
                       place:(NSString*)place;
//END WASIQ
-(instancetype)initWithDict:(NSDictionary *)userInfo;
-(void)getAddress:(void(^)(NSString *address))completionBlock;
@end

NS_ASSUME_NONNULL_END
