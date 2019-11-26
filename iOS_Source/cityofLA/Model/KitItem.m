//
//  KitItem.m
//  cityofLA
//
//  Created by Sam Sidd on 08/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import "KitItem.h"

@implementation KitItem
-(instancetype)initWithId:(NSInteger )_id
               andKitName:(NSString *)kit
                    items:(NSMutableArray *)kitSubItems{
    self = [super init];
    if (self) {
        self._id = _id;
        self.kit = kit;
        self.action = build_kit_item;
        self.kitSubItems = kitSubItems;
        [self calculateCompletedItems];
        return self;
    }
    return nil;
}
-(void)calculateCompletedItems{
    self.completedItems = 0;
    for (KitSubItem *item in self.kitSubItems) {
        if (item.completed) {self.completedItems++;}
    }
    self.completedText = [NSString stringWithFormat:@"%ld/%lu",(long)self.completedItems,(unsigned long)self.kitSubItems.count];
}
@end

@implementation KitSubItem

-(instancetype)initWithId:(NSInteger)item_id
                    kitId:(NSInteger)kit_id
                item_name:(NSString *)name
                completed:(BOOL)completed{
    self = [super init];
    if (self) {
        self.item_id = item_id;
        self.item_kit_id = kit_id;
        self.item_name = name;
        self.completed = completed;
        return self;
    }
    return nil;
}

@end
