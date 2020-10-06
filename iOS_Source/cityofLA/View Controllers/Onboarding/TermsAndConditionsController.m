//
//  TermsAndConditionsController.m
//  cityofLA
//
//  Created by Sam Sidd on 03/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import "AppDelegate.h"
#import "TermsAndConditionsController.h"
#import "UIView+ViewAdditions.h"
#import "Helper.h"
#import "UIFont+FontAdditions.h"

@interface TermsAndConditionsController () <UIWebViewDelegate>

@end

@implementation TermsAndConditionsController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.title = [Helper localized:@"termsTitle"];
    [Helper applyAttributesOnVc:self withColor:darkBlueColor];
    self.agreedBtn.userInteractionEnabled = NO;
    self.agreedBtn.titleLabel.numberOfLines = 0;
    [self.agreedBtn setTitle:[Helper localized:@"termsBtnTitle"] forState:UIControlStateNormal];
    self.agreedBtn.titleLabel.textAlignment = NSTextAlignmentCenter;
    [self.agreedBtn curveView];
    
    NSString *htmlFilename = @"terms and condition";
    NSString *lang = [[NSUserDefaults standardUserDefaults]valueForKey:@"lang"];
    if ([lang isEqualToString:@"es"]){
        htmlFilename = [htmlFilename stringByAppendingString:@"_es"];
    }
    NSString *htmlFile = [[NSBundle mainBundle] pathForResource:htmlFilename ofType:@"html"];
    NSString* htmlString = [NSString stringWithContentsOfFile:htmlFile encoding:NSUTF8StringEncoding error:nil];
    [self.webView loadHTMLString:htmlString baseURL: [[NSBundle mainBundle] bundleURL]];
    self.webView.backgroundColor = UIColor.whiteColor;
    self.webView.opaque = NO;
    self.webView.delegate = self;
    [Helper showHudWithMessage:@"Loading"];
}
-(void)webViewDidFinishLoad:(UIWebView *)webView{
    [Helper hideHud];
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        self.agreedBtn.userInteractionEnabled = YES;
    });
    
}
-(IBAction)backPressed:(id)sender{
    [self.navigationController dismissViewControllerAnimated:self completion:nil];
}
-(IBAction)agreedPressed:(id)sender{
    [[NSUserDefaults standardUserDefaults]setBool:YES forKey:@"onboarded"];
    UITabBarController *tabBar = [[UIStoryboard storyboardWithName:@"Main" bundle:nil]instantiateViewControllerWithIdentifier:@"tabBar"];
    [self presentViewController:tabBar animated:YES completion:nil];
}
-(UIStatusBarStyle)preferredStatusBarStyle{
    return UIStatusBarStyleLightContent;
}
@end
