//
//  ListViewModel.m
//  cityofLA
//
//  Created by Sam Sidd on 04/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import "ListViewModel.h"
#import "Helper.h"
#import "DBHelper.h"

@implementation ListViewModel
//CTI: Initialization of List View Models
-(instancetype)initWithListType:(ListType)listType{
    self = [super init];
    self.listType = listType;
    if (self.listType == AboutLA){
        self.list = [self getLAItems];
    }
    else if (self.listType == Plan){
        self.list = [self getPlanItems];
    }
    else if (self.listType == Recovery){
        self.list = [self getRecoveryItems];
    }
    else if (self.listType == PlanSubList){
        self.list = [[DBHelper sharedInstance]getPlans];
    }
    else if (self.listType == SecureSubList){
        self.list = [[DBHelper sharedInstance]getSecurePlaceItems];
    }
    else if (self.listType == BuildKitList){
        self.list = [[DBHelper sharedInstance]getKitItems];
    }
    if (self) {
        return self;
    }
    return nil;
}

//CTI: Setup pages with HTML contents
-(NSMutableArray *)getLAItems{    
    ListItem *partners = [[ListItem alloc]initWithTitle:[Helper localized:@"partnersTitle"]
                                                  image:[UIImage imageNamed:@"partners"]
                                                 action:html
                                           htmlFileName:@"partner"
                                            nextVcTitle:[Helper localized:@"partnersNextVc"]];
    
    ListItem *disclaimer = [[ListItem alloc]initWithTitle:[Helper localized:@"disclaimerTitle"]
                                                    image:[UIImage imageNamed:@"disclaimer"]
                                                   action:html
                                             htmlFileName:@"disclaimer"
                                              nextVcTitle:[Helper localized:@"disclaimerNextVc"]];

    ListItem *help = [[ListItem alloc]initWithTitle:[Helper localized:@"earlyWorkTitle"]
                                              image:[UIImage imageNamed:@"help"]
                                             action:html
                                       htmlFileName:@"how_does_early_warning_work"
                                        nextVcTitle:[Helper localized:@"earlyWorkNextVc"]];
    
    ListItem *info = [[ListItem alloc]initWithTitle:[Helper localized:@"getFromLA"]
                                              image:[UIImage imageNamed:@"info"]
                                             action:html
                                       htmlFileName:@"where_am_i_going_to_get_from_shakealert_la"
                                        nextVcTitle:[Helper localized:@"getFromLANextVc"]];
    
    //15-08-2019
    ListItem *tutorial = [[ListItem alloc]initWithTitle:[Helper localized:@"tutorialTitle"]
                                                    image:[UIImage imageNamed:@"tutorial"]
                                                   action:html
                                             htmlFileName:@"disclaimer"
                                              nextVcTitle:[Helper localized:@"disclaimerNextVc"]];
    //END 15-08-2018
    return @[partners,disclaimer,help,info, tutorial].mutableCopy;
}

//CTI: Plan pages List Items
-(NSMutableArray *)getPlanItems{
    ListItem *make_a_plan = [[ListItem alloc]initWithTitle:[Helper localized:@"makePlanTitle"]
                                                  image:[UIImage imageNamed:@"make_a_plan"]
                                                 action:makePlan
                                            nextVcTitle:[Helper localized:@"makePlanTitle"]];
    
    ListItem *build_a_kit = [[ListItem alloc]initWithTitle:[Helper localized:@"buildKitTitle"]
                                                    image:[UIImage imageNamed:@"build_a_kit"]
                                                   action:build_kit
                                              nextVcTitle:[Helper localized:@"buildKitNextVc"]];
    
    ListItem *secure = [[ListItem alloc]initWithTitle:[Helper localized:@"secureTitle"]
                                              image:[UIImage imageNamed:@"secure_your_place"]
                                             action:securePlace
                                        nextVcTitle:[Helper localized:@"secureNextVc"]];
    
    ListItem *protective = [[ListItem alloc]initWithTitle:[Helper localized:@"protectiveTitle"]
                                              image:[UIImage imageNamed:@"protective_action"]
                                             action:html
                                       htmlFileName:@"protective_action"
                                        nextVcTitle:[Helper localized:@"protectiveNextVc"]];
    
    ListItem *find_out_more = [[ListItem alloc]initWithTitle:[Helper localized:@"findOutMore"]
                                                       image:[UIImage imageNamed:@"find_out"]
                                                      action:link_url
                                                        link:@"https://www.earthquakecountry.org"
                                                 nextVcTitle:[Helper localized:@"findOutMore"]];
    return @[make_a_plan,build_a_kit,secure,protective,find_out_more].mutableCopy;
}

//CTI: Recovery page items with their respective HTML pages
-(NSMutableArray *)getRecoveryItems{
    ListItem *shaking = [[ListItem alloc]initWithTitle:[Helper localized:@"shakingTitle"]
                                                  image:[UIImage imageNamed:@"shaking"]
                                                 action:html
                                           htmlFileName:@"when_shaking_stops"
                                            nextVcTitle:[Helper localized:@"shakingNextVc"]];
    
    ListItem *shelterItem = [[ListItem alloc]initWithTitle:[Helper localized:@"shelterTitle"]
                                                 image:[UIImage imageNamed:@"shelter"]
                                                action:shelter
                                           nextVcTitle:[Helper localized:@"shelterNextVc"]];

    ListItem *volunteer = [[ListItem alloc]initWithTitle:[Helper localized:@"v_helpTitle"]
                                              image:[UIImage imageNamed:@"v_help"]
                                             action:html
                                       htmlFileName:@"volunteer_to_help"
                                        nextVcTitle:[Helper localized:@"v_helpNextVc"]];
    
    ListItem *access = [[ListItem alloc]initWithTitle:[Helper localized:@"accessDisasterTitle"]
                                              image:[UIImage imageNamed:@"access"]
                                             action:html
                                       htmlFileName:@"access_disaster_services"
                                        nextVcTitle:[Helper localized:@"accessDisasterNextVc"]];
    return @[shaking,shelterItem,volunteer,access].mutableCopy;
}
@end
