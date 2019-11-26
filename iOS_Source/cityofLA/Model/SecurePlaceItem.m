//
//  SecurePlaceItem.m
//  cityofLA
//
//  Created by Sam Sidd on 08/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import "SecurePlaceItem.h"

@implementation SecurePlaceItem
-(instancetype)initWithName:(NSString *)name ID:(NSInteger)ID completed:(BOOL)completed{
    self = [super init];
    if (self) {
        self.ID = ID;
        self.name = name;
        self.completed = completed;
        return self;
    }
    return nil;
}
@end
