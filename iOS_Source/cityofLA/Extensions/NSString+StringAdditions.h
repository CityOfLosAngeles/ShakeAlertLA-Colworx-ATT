//
//  NSString+StringAdditions.h
//  cityofLA
//
//  Created by Sam Sidd on 05/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import <Foundation/Foundation.h>
@import UIKit;
@import CoreLocation;

@interface NSString (StringAdditions)
-(NSMutableAttributedString *)identifyLinks;
- (CGFloat)heightForString:(NSString *)text font:(UIFont *)font maxWidth:(CGFloat)maxWidth ;
@end


@interface NSMutableAttributedString (AttrStringAdditions)
-(NSMutableAttributedString *)identifyLinks;

@end
