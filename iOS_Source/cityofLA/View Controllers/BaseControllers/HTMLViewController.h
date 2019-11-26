//
//  HTMLViewController.h
//  cityofLA
//
//  Created by Sam Sidd on 04/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Helper.h"
#import "HomeController.h"
#import "NSString+StringAdditions.h"
@import WebKit;

@interface HTMLViewController : UIViewController
@property (strong,nonatomic) IBOutlet UIWebView *webView;
@property NSString *htmlFilename;
@property UIColor *navColor;
@property BOOL dontHideNavBar;
@property (strong, nonatomic) IBOutlet WKWebView *web;
@property (strong, nonatomic) IBOutlet UITextView *textView;

@end
