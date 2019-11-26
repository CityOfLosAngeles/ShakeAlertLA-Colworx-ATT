//
//  NSString+Timestamp_Handling.m
//  Hanger
//
//  Created by Sam Sidd on 31/10/2018.
//  Copyright © 2018 AT&T. All rights reserved.
//

#import "NSString+Timestamp_Handling.h"

@implementation NSString (Timestamp_Handling)
-(NSDate *)getDate{
    NSDateFormatter *fm = [[NSDateFormatter alloc]init];
    [fm setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
    
    NSString *dateStr = self;
    dateStr = [dateStr stringByReplacingOccurrencesOfString:@"T" withString:@" "];
    if ([dateStr containsString:@"."]) {
        dateStr = [dateStr substringToIndex:[dateStr rangeOfString:@"."].location];
    }
    return [fm dateFromString:dateStr];
}
-(NSString *)dateOnly{
    NSDateFormatter *fm = [[NSDateFormatter alloc]init];
    [fm setDateFormat:@"yyyy-MM-dd"];
    NSString *dateStr = self;
    dateStr = [dateStr stringByReplacingOccurrencesOfString:@"T" withString:@" "];
    if ([dateStr containsString:@" "]) {
        dateStr = [dateStr substringToIndex:[dateStr rangeOfString:@" "].location];
    }
    NSDate *date = [fm dateFromString:dateStr];
    [fm setDateFormat:@"MM/dd/yyyy"];
    dateStr = [fm stringFromDate:date];
    return dateStr;
}
-(NSString *)timeOnly{
    NSDateFormatter *fm = [[NSDateFormatter alloc]init];
    [fm setDateFormat:@"HH:mm"];
    NSString *dateStr = self;
    dateStr = [dateStr stringByReplacingOccurrencesOfString:@"T" withString:@" "];
    if ([dateStr containsString:@" "]) {
        dateStr = [dateStr substringFromIndex:[dateStr rangeOfString:@" "].location];
    }
    if ([dateStr containsString:@"."]) {
        dateStr = [dateStr substringToIndex:[dateStr rangeOfString:@"."].location];
    }
    return dateStr;
}
-(NSTimeInterval )getInterval{
    NSDate *date = [self getDate];
    return [date timeIntervalSince1970];
}
-(NSString *)getEarthQuakeTime{
    
    NSTimeInterval intervalInSec = [self doubleValue] / 1000;
    NSDate *date = [NSDate dateWithTimeIntervalSince1970:intervalInSec];
    NSDateFormatter *fm = [[NSDateFormatter alloc]init];
    [fm setDateFormat:@"MMMM dd, yyyy, hh:mm a"];
    return [fm stringFromDate:date];;
}
    
    
-(NSString *)getRecentEarthQuakeTime {
//    NSTimeInterval intervalInSec = [self doubleValue] / 1000;
//    NSDate *date = [NSDate dateWithTimeIntervalSince1970:intervalInSec];
//    NSDateFormatter *fm = [[NSDateFormatter alloc]init];
//    [fm setTimeZone:[NSTimeZone timeZoneWithAbbreviation:@"America/Los_Angeles"]];
//    [fm setDateFormat:@"MMMM dd, yyyy, HH:mm:ss"];
//    return [fm stringFromDate:date];
    
    
//    NSTimeInterval intervalInSec = [self doubleValue] / 1000;
//    NSDate *date = [NSDate dateWithTimeIntervalSince1970:intervalInSec];
//    NSDateFormatter *fm = [[NSDateFormatter alloc]init];
//    [fm setTimeZone:[NSTimeZone timeZoneWithAbbreviation:@"UTC"]];
//    [fm setDateFormat:@"MMMM dd, yyyy, HH:mm:ss"];
//    NSString *strDate =  [fm stringFromDate:date];;
//
//
//    NSDate* ts_utc;
//    NSDateFormatter* df_utc = [[NSDateFormatter alloc] init];
//    [df_utc setTimeZone:[NSTimeZone timeZoneWithName:@"UTC"]];
//    [df_utc setDateFormat:@"MMMM dd, yyyy, HH:mm:ss"];
//    ts_utc = [df_utc dateFromString:strDate];
//
//    NSTimeZone *destinationTimeZone = [NSTimeZone timeZoneWithName:@"PST"];
//    NSInteger timeZoneHourOffset = (NSInteger) ([destinationTimeZone secondsFromGMTForDate:ts_utc] / 3600.0); //geting the time offset between UTC and PST
//    NSDate *pstDate = [ts_utc dateByAddingTimeInterval:timeZoneHourOffset * 60 * 60]; //applying that offset into ts_utc NSDate
//
//    NSDateFormatter *fm2 = [[NSDateFormatter alloc]init];
//    [fm2 setTimeZone:[NSTimeZone timeZoneWithAbbreviation:@"UTC"]];
//    [fm2 setDateFormat:@"MMMM dd, yyyy, HH:mm:ss"];
//    return [fm2 stringFromDate:pstDate];
    
    NSTimeInterval intervalInSec = [self doubleValue] / 1000;
    NSDate *date = [NSDate dateWithTimeIntervalSince1970:intervalInSec];
    
    NSTimeZone *destinationTimeZone = [NSTimeZone timeZoneWithName:@"PST"];
    NSInteger timeZoneHourOffset = (NSInteger) ([destinationTimeZone secondsFromGMTForDate:date] / 3600.0); //geting the time offset between UTC and PST
    NSDate *pstDate = [date dateByAddingTimeInterval:timeZoneHourOffset * 60 * 60]; //applying that offset into ts_utc NSDate
    
    NSDateFormatter *fm2 = [[NSDateFormatter alloc]init];
    [fm2 setTimeZone:[NSTimeZone timeZoneWithAbbreviation:@"UTC"]];
    [fm2 setDateFormat:@"MMMM dd, yyyy, hh:mm:ss a"];
    return [fm2 stringFromDate:pstDate];

}
@end
