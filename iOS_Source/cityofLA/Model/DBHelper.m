//
//  DBHelper.m
//  cityofLA
//
//  Created by ColWorx on 07/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import "DBHelper.h"
#import "KitItem.h"


@implementation DBHelper{
    FMDatabase *db;
}
DBHelper *helper;

static NSString *updatePlanItem = @"UPDATE Plans "
"SET "
"Notes=?, "
"Completed=? "
"where "
"ID=?";
static NSString *updateSecurePlaceItem = @"UPDATE Secure_Place "
"SET "
"Completed=? "
"where "
"ID=?";
static NSString *updateKitSubItem = @"UPDATE Items "
"SET "
"Completed=? "
"where "
"Item_ID=?";

static NSString *saveNotification = @"INSERT INTO Notifications "
"(title, "
"body, "
"LatitudeValue, "
"LongitudeValue, "
"MMI, "
"MagnitudeValue, "
"startTime, "
"Topic) "
"VALUES ";
    
    

+(DBHelper *)sharedInstance{
    if (helper == nil){
        helper = [[DBHelper alloc] init];
    }
    return helper;
}


-(void)initialize{
    NSString *documentsDirectory = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) firstObject] ;
    NSString *dbPath = [documentsDirectory stringByAppendingPathComponent:dbNameWithExt];
    
    if (![[NSFileManager defaultManager] fileExistsAtPath:dbPath]) {
        NSURL *url = [[NSBundle mainBundle]URLForResource:dbName withExtension:dbExt];
        NSData *data = [NSData dataWithContentsOfURL:url];
        [data writeToFile:dbPath atomically:YES];
    }
    db = [FMDatabase databaseWithPath:dbPath];
    NSLog(@"Db opening : %d",[db open]) ;
}
#pragma MARK PLAN Methods :-
-(NSMutableArray *)getPlans{
    FMResultSet *results = [db executeQuery:@"SELECT * FROM Plans"];
    NSMutableArray *plans = [[NSMutableArray alloc]init];
    
    NSString *lang = [[NSUserDefaults standardUserDefaults]valueForKey:@"lang"];
    NSString *infoColumn = @"Info";
    NSString *planColumn = @"Plan";
    if (lang != nil && [lang isEqualToString:@"es"]) {
        infoColumn = @"Info_es";
        planColumn = @"Plan_es";
    }
    while ([results next]) {
        PlanListItem *plan = [[PlanListItem alloc]initWithPlan:[results stringForColumn:planColumn]
                                          info:[results stringForColumn:infoColumn]
                                         notes:[results stringForColumn:@"Notes"]
                                         ID:[results intForColumn:@"ID"]
                                     completed:[results intForColumn:@"Completed"]];
        [plans addObject:plan];
        
    }
    return plans;
}
-(BOOL)updatePlanItem:(PlanListItem *)planListItem{
    NSNumber *completed = planListItem.completed ? [NSNumber numberWithInteger:1] : [NSNumber numberWithInteger:0];
    return  [db executeUpdate:updatePlanItem,planListItem.notes,completed,[NSNumber numberWithInteger:planListItem.ID]];
}
#pragma MARK SECURE PLACE Methods :-

-(NSMutableArray *)getSecurePlaceItems{
    FMResultSet *results = [db executeQuery:@"SELECT * FROM Secure_Place"];
    NSMutableArray *securePlaceItems = [[NSMutableArray alloc]init];
    NSString *lang = [[NSUserDefaults standardUserDefaults]valueForKey:@"lang"];
    NSString *NameColumn = @"Name";
    if (lang != nil && [lang isEqualToString:@"es"]) {
        NameColumn = @"Name_es";
    }
    while ([results next]) {
        SecurePlaceItem *item = [[SecurePlaceItem alloc]initWithName:[results stringForColumn:NameColumn]
                                                                  ID:[results intForColumn:@"ID"]
                                                           completed:[results intForColumn:@"Completed"]];
        [securePlaceItems addObject:item];
    }
    return securePlaceItems;
}
-(BOOL)updateSecurePlaceItem:(SecurePlaceItem *)item{
    NSNumber *completed = item.completed ? [NSNumber numberWithInt:1] : [NSNumber numberWithInt:0];
    return  [db executeUpdate:updateSecurePlaceItem,completed,[NSNumber numberWithInteger:item.ID]];
}


#pragma MARK BUILD A KIT Methods :-
-(NSMutableArray *)getKitItems{
    FMResultSet *results = [db executeQuery:@"SELECT * FROM Kits"];
    NSMutableArray *kitItems = [[NSMutableArray alloc]init];
    NSString *lang = [[NSUserDefaults standardUserDefaults]valueForKey:@"lang"];
    NSString *kit = @"Kit";
    if (lang != nil && [lang isEqualToString:@"es"]) {
        kit = @"Kits_es";
    }
    while ([results next]) {
        NSInteger kit_id = [results intForColumn:@"ID"];
        KitItem *item = [[KitItem alloc]initWithId:[results intForColumn:@"ID"]
                                        andKitName:[results stringForColumn:kit]
                                             items:[self getSubKitItems:kit_id]];
        [kitItems addObject:item];
    }
    return kitItems;
}
-(NSMutableArray *)getSubKitItems:(NSInteger)kit_id{
    FMResultSet *results = [db executeQuery:@"SELECT * FROM Items where Kit_ID=?",[NSNumber numberWithInteger:kit_id]];
    NSMutableArray *kitSubItems = [[NSMutableArray alloc]init];
    NSString *lang = [[NSUserDefaults standardUserDefaults]valueForKey:@"lang"];
    NSString *Item_Name = @"Item_Name";
    if (lang != nil && [lang isEqualToString:@"es"]) {
        Item_Name = @"Item_Name_es";
    }
    while ([results next]) {
        KitSubItem *kitSubItem = [[KitSubItem alloc]initWithId:[results intForColumn:@"Item_ID"]
                                                         kitId:kit_id
                                                     item_name:[results stringForColumn:Item_Name]
                                                     completed:[results intForColumn:@"Completed"]];
        [kitSubItems addObject:kitSubItem];
    }
    return kitSubItems;
}
-(BOOL)updateKitSubItem:(KitSubItem *)item{
    NSNumber *completed = item.completed ? [NSNumber numberWithInt:1] : [NSNumber numberWithInt:0];
    return  [db executeUpdate:updateKitSubItem,completed,[NSNumber numberWithInteger:item.item_id]];
}
-(NSMutableArray *)getNotifications{
    FMResultSet *results = [db executeQuery:@"SELECT * FROM Notifications"];
    NSMutableArray *notifications = [[NSMutableArray alloc]init];
    while ([results next]) {
        
        NSInteger ID = [results intForColumn:@"ID"];
        NSString *title = [results stringForColumn:@"title"];
        NSString *body = [results stringForColumn:@"body"];
        NSString *lat = [results stringForColumn:@"LatitudeValue"];
        NSString *lon = [results stringForColumn:@"LongitudeValue"];
        NSString *mmi = [results stringForColumn:@"MMI"];
        NSString *magnitude = [results stringForColumn:@"MagnitudeValue"];
        NSString *startTime = [results stringForColumn:@"startTime"];
        NSString *topic = [results stringForColumn:@"Topic"];
        
        Notification *n = [[Notification alloc]initWithTitle:title
                                                        body:body
                                                         lat:lat
                                                         lon:lon
                                                         mmi:mmi
                                                   magnitude:magnitude
                                                   startTime:startTime
                                                       topic:topic
                                                          ID:[NSNumber numberWithInteger:ID]];
        [notifications addObject:n];
    }
    return notifications;
}
-(BOOL)saveNotificationObj:(Notification *)n{
    NSString *insertNotificationQuery = [NSString stringWithFormat:@"INSERT INTO Notifications  (title , body, LatitudeValue, LongitudeValue, MMI, MagnitudeValue,startTime, Topic) VALUES ('%@','%@','%@','%@','%@', '%@','%@','%@')",n.title,n.body,n.LatitudeValue,n.LongitudeValue,n.MMI,n.MagnitudeValue,n.startTime,n.Topic];
   return [db executeUpdate:insertNotificationQuery];
}
@end
