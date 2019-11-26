//
//  DBHelper.h
//  cityofLA
//
//  Created by ColWorx on 07/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import <Foundation/Foundation.h>
#define dbNameWithExt @"ShakeAlertLA.db"
#define dbName @"ShakeAlertLA"
#define dbExt @"db"
#import "PlanListItem.h"
#import "SecurePlaceItem.h"
#import "KitItem.h"
#import "Notification.h"
@import FMDB;
@interface DBHelper : NSObject
+(DBHelper *)sharedInstance;
-(void)initialize;

-(NSMutableArray *)getPlans;
-(BOOL)updatePlanItem:(PlanListItem *)planListItem;


-(NSMutableArray *)getSecurePlaceItems;
-(BOOL)updateSecurePlaceItem:(SecurePlaceItem *)item;

-(NSMutableArray *)getKitItems;
-(BOOL)updateKitSubItem:(KitSubItem *)item;

-(NSMutableArray *)getNotifications;
-(BOOL)saveNotificationObj:(Notification *)n;
@end
