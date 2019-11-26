//
//  SecurePlaceItem.h
//  cityofLA
//
//  Created by Sam Sidd on 08/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SecurePlaceItem : NSObject

-(instancetype)initWithName:(NSString *)name ID:(NSInteger)ID completed:(BOOL)completed;
@property NSString *name;
@property NSInteger ID;
@property BOOL completed;
@end
