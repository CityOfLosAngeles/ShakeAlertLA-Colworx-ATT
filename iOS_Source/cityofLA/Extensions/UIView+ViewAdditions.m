//
//  UIView+ViewAdditions.m
//  cityofLA
//
//  Created by Sam Sidd on 03/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import "UIView+ViewAdditions.h"

@implementation UIView (ViewAdditions)

-(void)curveView{
    float size = MIN(self.bounds.size.width, self.bounds.size.height);
    self.layer.cornerRadius = size / 2;
}
-(void)drawDropShadowOnView:(float)shadowSize{
    
    self.layer.cornerRadius = 1.0f;
    self.layer.borderWidth = 0.0f;
    self.layer.borderColor = [UIColor darkGrayColor].CGColor;
    
    if ([self isKindOfClass:[UITableView class]]) {
        self.layer.masksToBounds = YES;
    }
    else{
        self.layer.masksToBounds = NO;
    }
    self.layer.shadowOffset = CGSizeMake(0, 0);
    self.layer.shadowRadius = shadowSize;
    self.layer.shadowOpacity = 0.1;
}

@end
