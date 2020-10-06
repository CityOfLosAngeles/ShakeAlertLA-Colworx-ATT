//
//  ListItem.m
//  cityofLA
//
//  Created by Sam Sidd on 04/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import "ListItem.h"
//CTI: List items initialization method with required attributes
@implementation ListItem
-(instancetype)initWithTitle:(NSString *)title
                       image:(UIImage*)image
                      action:(ListItemAction)action
                 nextVcTitle:(NSString *)nextVcTitle{
    self = [super init];
    if (self) {
        self.title = title;
        self.image = image;
        self.action = action;
        self.nextVcTitle = nextVcTitle;
        return self;
    }
    return nil;
}
-(instancetype)initWithTitle:(NSString *)title
                       image:(UIImage*)image
                      action:(ListItemAction)action
                htmlFileName:(NSString *)htmlFileName
                 nextVcTitle:(NSString *)nextVcTitle{
    self = [super init];
    if (self) {
        self.title = title;
        self.image = image;
        self.action = action;
        self.nextVcTitle = nextVcTitle;
        self.htmlFileName = htmlFileName;
        return self;
    }
    return nil;
}
-(instancetype)initWithTitle:(NSString *)title
                       image:(UIImage*)image
                      action:(ListItemAction)action
                        link:(NSString *)link
                 nextVcTitle:(NSString *)nextVcTitle{
    self = [super init];
    if (self) {
        self.title = title;
        self.image = image;
        self.action = action;
        self.nextVcTitle = nextVcTitle;
        self.linkURL = link;
        return self;
    }
    return nil;
}
@end
