//
//  Plan.m
//  cityofLA
//
//  Created by ColWorx on 07/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import "PlanListItem.h"

@implementation PlanListItem

-(instancetype)initWithPlan:(NSString *)plan
                       info:(NSString *)info
                      notes:(NSString *)notes
                         ID:(NSInteger )ID
                  completed:(BOOL)completed{
    self = [super init];
    if (self) {
        self.ID = ID;
        self.plan = plan;
        self.info = info;
        self.notes = notes;
        self.completed = completed;
        self.action = notesVc;
        return self;
    }
    return nil;
}

@end
