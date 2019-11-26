//
//  NSString+StringAdditions.m
//  cityofLA
//
//  Created by Sam Sidd on 05/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import "NSString+StringAdditions.h"
#import "UIFont+FontAdditions.h"
@import UIKit;

@implementation NSString (StringAdditions)
-(NSMutableAttributedString *)identifyLinks{

    NSDataDetector* detector = [NSDataDetector dataDetectorWithTypes:NSTextCheckingTypeLink error:nil];
    NSArray* matches = [detector matchesInString:self options:0 range:NSMakeRange(0,  self.length)];
    NSMutableAttributedString *attrText =  [[NSMutableAttributedString alloc] initWithString:self];
    
    for (int index = 0 ; index < matches.count; index ++) {
        NSTextCheckingResult *textResult = [matches objectAtIndex : index];
        NSRange testRange = textResult.range;
        NSURL *testUrl = textResult.URL ;
        [attrText addAttribute:NSLinkAttributeName value:testUrl  range: testRange];
        [attrText addAttribute:NSFontAttributeName
                         value:[UIFont SFRegularWithSize:14] range:NSMakeRange(0,self.length)];
        
    }
    return attrText;
}
- (CGFloat)heightForString:(NSString *)text font:(UIFont *)font maxWidth:(CGFloat)maxWidth {
    if (![text isKindOfClass:[NSString class]] || !text.length) {
        // no text means no height
        return 0;
    }
    
    NSStringDrawingOptions options = NSStringDrawingUsesLineFragmentOrigin | NSStringDrawingUsesFontLeading;
    NSDictionary *attributes = @{ NSFontAttributeName : font };
    CGSize size = [text boundingRectWithSize:CGSizeMake(maxWidth, CGFLOAT_MAX) options:options attributes:attributes context:nil].size;
    CGFloat height = ceilf(size.height) + 1; // add 1 point as padding
    
    return height;
}



@end


@implementation NSMutableAttributedString (AttrStringAdditions)
-(NSMutableAttributedString *)identifyLinks{
    NSDataDetector* detector = [NSDataDetector dataDetectorWithTypes:NSTextCheckingTypeLink error:nil];
    NSArray* matches = [detector matchesInString:self.mutableString options:0 range:NSMakeRange(0,  self.length)];
    
    for (int index = 0 ; index < matches.count; index ++) {
        NSTextCheckingResult *textResult = [matches objectAtIndex : index];
        NSRange testRange = textResult.range;
        NSURL *testUrl = textResult.URL ;
        [self addAttribute:NSLinkAttributeName value:testUrl  range: testRange];
        [self addAttribute:NSFontAttributeName
                         value:[UIFont SFRegularWithSize:14] range:NSMakeRange(0,self.length)];
        
    }
    return self;
}
@end
