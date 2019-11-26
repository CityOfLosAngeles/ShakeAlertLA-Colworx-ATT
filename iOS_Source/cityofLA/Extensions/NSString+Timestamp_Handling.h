//
//  NSString+Timestamp_Handling.h
//  Hanger
//
//  Created by Sam Sidd on 31/10/2018.
//  Copyright © 2018 AT&T. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NSString (Timestamp_Handling)
-(NSDate *)getDate;
-(NSTimeInterval )getInterval;
-(NSString *)dateOnly;
-(NSString *)timeOnly;
-(NSString *)getEarthQuakeTime;
-(NSString *)getRecentEarthQuakeTime;
@end

NS_ASSUME_NONNULL_END
