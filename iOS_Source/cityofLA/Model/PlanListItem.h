//
//  Plan.h
//  cityofLA
//
//  Created by ColWorx on 07/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ListItem.h"

@interface PlanListItem : NSObject
-(instancetype)initWithPlan:(NSString *)plan
                       info:(NSString *)info
                      notes:(NSString *)notes
                         ID:(NSInteger )ID
                  completed:(BOOL)completed;
@property NSString *plan;
@property NSInteger ID;
@property NSString *info;
@property NSString *notes;
@property BOOL completed;
@property ListItemAction action;
@end
