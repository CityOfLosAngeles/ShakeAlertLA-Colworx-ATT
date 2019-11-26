//
//  ListItem.h
//  cityofLA
//
//  Created by Sam Sidd on 04/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import <Foundation/Foundation.h>
@import UIKit;

typedef NS_ENUM(NSInteger, ListItemAction){
    html,
    sublist,
    custom,
    shelter,
    link_url,
    build_kit,
    makePlan,
    notesVc,
    securePlace,
    build_kit_item,
};

@interface ListItem : NSObject
-(instancetype)initWithTitle:(NSString *)title
                       image:(UIImage*)image
                      action:(ListItemAction)action
                 nextVcTitle:(NSString *)nextVcTitle;
-(instancetype)initWithTitle:(NSString *)title
                       image:(UIImage*)image
                      action:(ListItemAction)action
                htmlFileName:(NSString *)htmlFileName
                 nextVcTitle:(NSString *)nextVcTitle;
-(instancetype)initWithTitle:(NSString *)title
                       image:(UIImage*)image
                      action:(ListItemAction)action
                        link:(NSString *)link
                 nextVcTitle:(NSString *)nextVcTitle;
@property UIImage *image;
@property NSString *title;
@property ListItemAction action;
@property NSString *nextVcTitle;
@property NSString *htmlFileName;
@property NSString *linkURL;
@end
