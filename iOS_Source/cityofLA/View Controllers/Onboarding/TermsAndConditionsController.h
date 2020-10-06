//
//  TermsAndConditionsController.h
//  cityofLA
//
//  Created by Sam Sidd on 03/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import <UIKit/UIKit.h>
//CTI: Controller class to show T&C
@interface TermsAndConditionsController : UIViewController
@property (weak,nonatomic) IBOutlet UIWebView *webView;
@property (weak,nonatomic) IBOutlet UIButton *agreedBtn;

@end
