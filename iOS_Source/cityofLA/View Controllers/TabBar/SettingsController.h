//
//  SettingsController.h
//  cityofLA
//
//  Created by Sam Sidd on 05/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Helper.h"
#import "TabBarController.h"
@interface SettingsController : UIViewController
@property (strong,nonatomic) IBOutlet UIButton *eng;
@property (strong,nonatomic) IBOutlet UIButton *spa;

@property (strong,nonatomic) IBOutlet UILabel *changeSettings;
@property (strong,nonatomic) IBOutlet UILabel *push;
@property (strong,nonatomic) IBOutlet UILabel *appAllows;

@property (strong,nonatomic) IBOutlet UILabel *phoneSettings;
@property (strong,nonatomic) IBOutlet UILabel *phoneSettingsDetail;

//15-08-2018
@property (weak, nonatomic) IBOutlet UILabel *locationSettings;
@property (weak, nonatomic) IBOutlet UILabel *locationSettingsDetail;
@property (weak, nonatomic) IBOutlet UILabel *DNDTitle;
@property (weak, nonatomic) IBOutlet UILabel *DNDHelper;

//END 15-08-2019
@end
