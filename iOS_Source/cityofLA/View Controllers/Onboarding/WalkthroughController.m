//
//  WalkthroughController.m
//  cityofLA
//
//  Created by Sam Sidd on 01/11/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import "WalkthroughController.h"
//CTI: Controller class to show tutorial wizard on App Launch
@interface WalkthroughController ()

@end

@implementation WalkthroughController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.topImageView.image = [UIImage imageNamed:self.data[@"Image"]];
    self.titleLabel.text = self.data[@"Title"];
    self.descLabel.text = self.data[@"Desc"];
}
@end
