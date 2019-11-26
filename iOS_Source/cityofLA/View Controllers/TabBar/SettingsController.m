//
//  SettingsController.m
//  cityofLA
//
//  Created by Sam Sidd on 05/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import "SettingsController.h"
#import "AppDelegate.h"

@interface SettingsController ()

@end

@implementation SettingsController
-(void)viewWillAppear:(BOOL)animated{
    [self setupViews];
//    AppDelegate *appD = (AppDelegate *)[UIApplication sharedApplication].delegate;
//    [appD logEvents];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    
}
-(void)setupViews{
    NSString *lang = [[NSUserDefaults standardUserDefaults]valueForKey:@"lang"];
    if (lang == nil){
        [[NSUserDefaults standardUserDefaults]setObject:@"en" forKey:@"lang"];
        lang = [[NSUserDefaults standardUserDefaults]valueForKey:@"lang"];
    }
    
    if ([lang isEqualToString:@"en"]){
        self.eng.selected = YES;
        self.spa.selected = NO;
    }else{
        self.eng.selected = NO;
        self.spa.selected = YES;
    }
    
    self.title = [Helper localized:@"settingsTitle"];
    self.changeSettings.text = [Helper localized:@"changeLanguage"];
    self.push.text = [Helper localized:@"push"];
    self.appAllows.text = [Helper localized:@"appAllows"];
    self.phoneSettings.text = [Helper localized:@"phoneSettings"];
    self.phoneSettingsDetail.text = [Helper localized:@"phoneSettingsDetail"];
    [self.eng setTitle:[Helper localized:@"english"] forState:UIControlStateNormal];
    [self.spa setTitle:[Helper localized:@"spanish"] forState:UIControlStateNormal];
    [Helper applyAttributesOnVc:self withColor:darkBlueColor];
    
    //15-09-2019
    self.locationSettings.text = [Helper localized:@"locationSettings"];
    self.locationSettingsDetail.text = [Helper localized:@"locationSettingDetails"];
    //END 15-09-2019
    
    //SAM DND
    self.DNDTitle.text =[Helper localized:@"DNDTitle"];
    self.DNDHelper.text =[Helper localized:@"DNDHelper"];
}
-(IBAction)englishPressed:(UIButton *)btn{
    NSString *lang = [[NSUserDefaults standardUserDefaults]valueForKey:@"lang"];
    if ([lang isEqualToString:@"en"]) {
        return;
    }
    btn.selected = YES;
    self.spa.selected = NO;
    [[NSUserDefaults standardUserDefaults]setObject:@"en" forKey:@"lang"];
    UITabBarController *tabBar = [[UIStoryboard storyboardWithName:@"Main" bundle:nil]instantiateViewControllerWithIdentifier:@"tabBar"];
    [tabBar setSelectedIndex:0];
    [AppDelegate sharedInstance].window.rootViewController = tabBar;
    [[AppDelegate sharedInstance]registerDevice];
//    [[TabBarController sharedInstance]setTabItemsTitle];
//    [self setupViews];
}
-(IBAction)spanishPressed:(UIButton *)btn{
    NSString *lang = [[NSUserDefaults standardUserDefaults]valueForKey:@"lang"];
    if ([lang isEqualToString:@"es"]) {
        return;
    }
    btn.selected = YES;
    self.eng.selected = NO;
    [[NSUserDefaults standardUserDefaults]setObject:@"es" forKey:@"lang"];
//    [[TabBarController sharedInstance]setTabItemsTitle];
//    [self setupViews];
    UITabBarController *tabBar = [[UIStoryboard storyboardWithName:@"Main" bundle:nil]instantiateViewControllerWithIdentifier:@"tabBar"];
    [AppDelegate sharedInstance].window.rootViewController = tabBar;
    [[AppDelegate sharedInstance]registerDevice];
}
@end
