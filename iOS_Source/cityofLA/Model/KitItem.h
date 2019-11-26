//
//  KitItem.h
//  cityofLA
//
//  Created by Sam Sidd on 08/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ListItem.h"
@class KitSubItem;


@interface KitItem : NSObject
-(instancetype)initWithId:(NSInteger )_id
               andKitName:(NSString *)kit
                    items:(NSMutableArray *)kitSubItems;
@property NSInteger _id;
@property NSString *kit;
@property NSMutableArray<KitSubItem*> *kitSubItems;
@property NSInteger completedItems;
@property NSString *completedText;
@property ListItemAction *action;
@end


@interface KitSubItem : NSObject
-(instancetype)initWithId:(NSInteger)item_id
                    kitId:(NSInteger)kit_id
                item_name:(NSString *)name
                completed:(BOOL)completed;
@property NSInteger item_id;
@property NSInteger item_kit_id;
@property NSString *item_name;
@property BOOL completed;
@end


