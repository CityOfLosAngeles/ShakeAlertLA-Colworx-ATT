//
//  UIFont+FontAdditions.m
//  cityofLA
//
//  Created by Sam Sidd on 03/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import "UIFont+FontAdditions.h"
@import UIKit;

@implementation UIFont (FontAdditions)

+(UIFont*)SFBoldWithSize:(CGFloat)size{
    return [UIFont fontWithName:@"SFUIDisplay-Bold" size:size];
}
+(UIFont*)SFHeavyWithSize:(CGFloat)size{
    return [UIFont fontWithName:@"SFUIDisplay-Heavy" size:size];
}
+(UIFont*)SFRegularWithSize:(CGFloat)size{
    return [UIFont fontWithName:@"SFUIDisplay-Regular" size:size];
}
+(UIFont*)SFThinWithSize:(CGFloat)size{
    return [UIFont fontWithName:@"SFUIDisplay-Thin" size:size];
}

@end
