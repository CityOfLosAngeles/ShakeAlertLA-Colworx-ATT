//
//  HTMLViewController.m
//  cityofLA
//
//  Created by Sam Sidd on 04/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import "HTMLViewController.h"


@interface HTMLViewController () <UIWebViewDelegate>

@end

@implementation HTMLViewController

-(void)viewWillAppear:(BOOL)animated{
    [Helper setupNavBarOnPush:self withColor:self.navColor];
//    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(focused:) name:UIAccessibilityElementFocusedNotification object:nil];
}
-(void)viewWillDisappear:(BOOL)animated{
    if (self.dontHideNavBar){return;}
    [Helper resetNavBarOnPop:self];
    [Helper hideHud];
    [Helper hideHud];
    [Helper hideHud];
}
-(void)viewDidDisappear:(BOOL)animated{
    [Helper hideHud];
}
- (void)viewDidAppear:(BOOL)animated{
    [Helper multineLineTitle:self.navigationController];
    self.title = self.title;
}

- (void)webViewDidFinishLoad:(UIWebView *)webView {
    [Helper hideHud];
    // Disable user selection
    [webView stringByEvaluatingJavaScriptFromString:@"document.documentElement.style.webkitUserSelect='none';"];
    // Disable callout
    
}
- (void)viewDidLoad {
    [super viewDidLoad];    
    NSString *lang = [[NSUserDefaults standardUserDefaults]valueForKey:@"lang"];
    if ([lang isEqualToString:@"es"]){
        self.htmlFilename = [self.htmlFilename stringByAppendingString:@"_es"];
    }
    NSString *htmlFile = [[NSBundle mainBundle] pathForResource:self.htmlFilename ofType:@"html"];
    
    [self.webView loadRequest: [NSURLRequest requestWithURL:[NSURL fileURLWithPath:htmlFile]] ];
    self.webView.backgroundColor = UIColor.whiteColor;
    self.webView.opaque   = NO;
    self.webView.delegate = self;
    self.webView.scrollView.showsHorizontalScrollIndicator = false;

    //mail client button
    if ([self.title isEqualToString:[Helper localized:@"disclaimerNextVc"]]) {
        UIButton *btn = [[UIButton alloc]init];
        [btn setImage:[UIImage imageNamed:@"feedback"] forState:UIControlStateNormal];
        [btn addTarget:self action:@selector(openMail:) forControlEvents:UIControlEventTouchUpInside];
        self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc]initWithCustomView:btn];
    }
}
-(void)openMail:(UIButton *)btn{
    //15-08-2019

    NSString *recipients = @"mailto:early-earthquake-warning@lacity.org";
    //END 15-08-2019
    
    NSString *email = [NSString stringWithFormat:@"%@", recipients];
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:email] options:@{} completionHandler:^(BOOL success) {
        if (success == NO) {
            [Helper showHudWithError:@"Email not configured"];
        }
        NSLog(@"open mail : %d",success);
    }];
}
- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType {
    if (navigationType == UIWebViewNavigationTypeLinkClicked ) {
        [[UIApplication sharedApplication] openURL:[request URL] options:@{} completionHandler:nil];
        return NO;
    }
    
    return YES;
}

@end
