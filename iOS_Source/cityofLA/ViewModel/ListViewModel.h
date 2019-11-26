//
//  ListViewModel.h
//  cityofLA
//
//  Created by Sam Sidd on 04/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef NS_ENUM(NSUInteger, ListType) {
    AboutLA,
    Plan,
    Recovery,
    PlanSubList,
    SecureSubList,
    BuildKitList,
    BuildKitSubItemList,
};

@interface ListViewModel : NSObject
-(instancetype)initWithListType:(ListType)listType;
@property ListType listType;
@property NSMutableArray *list;


@end
