//
//  BuildKitController.m
//  cityofLA
//
//  Created by Sam Sidd on 08/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import "BuildKitController.h"
#import "NSString+StringAdditions.h"
#import "UIFont+FontAdditions.h"
#import "Helper.h"
#import "ListViewController.h"


@interface BuildKitController ()

@end

@implementation BuildKitController
-(void)viewWillAppear:(BOOL)animated{
    [Helper setupNavBarOnPush:self withColor:self.navColor];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setup];
    
}

//CTI: This method setup the "Build a kit" items.
-(void)setup{
    NSMutableAttributedString *build_a_kit = [[NSMutableAttributedString alloc]initWithString:[Helper localized:@"buildAtHome"] attributes:@{NSFontAttributeName : [UIFont SFRegularWithSize:14]}];
    
    NSRange range = [build_a_kit.mutableString rangeOfString:[Helper localized:@"sampleLinkKey"]];
    [build_a_kit addAttribute: NSLinkAttributeName value:@"https://www.ready.gov/build-a-kit"  range: range];
    [build_a_kit addAttribute: NSUnderlineStyleAttributeName value:@(NSUnderlineStyleSingle)  range: range];

//    build_a_kit = build_a_kit.identifyLinks;
    
    NSAttributedString *underTheBed = [[NSMutableAttributedString alloc]initWithString:[Helper localized:@"underTheBed"]
                                                                            attributes:@{NSFontAttributeName : [UIFont SFBoldWithSize:14]}];
    NSAttributedString *sturdyShoes = [[NSMutableAttributedString alloc]initWithString:[Helper localized:@"sturdyShoes"] attributes:@{NSFontAttributeName : [UIFont SFRegularWithSize:14]}];
    
    NSAttributedString *somethings = [[NSMutableAttributedString alloc]initWithString:[Helper localized:@"someThingsTo"]
                                                                           attributes:@{NSFontAttributeName : [UIFont SFBoldWithSize:14]}];
    NSAttributedString *eachItem = [[NSMutableAttributedString alloc]initWithString:[Helper localized:@"eachItemWill"] attributes:@{NSFontAttributeName : [UIFont SFRegularWithSize:14]}];
    
    [build_a_kit appendAttributedString:underTheBed];
    [build_a_kit appendAttributedString:sturdyShoes];
    [build_a_kit appendAttributedString:somethings];
    [build_a_kit appendAttributedString:eachItem];
    
    self.textView.attributedText = build_a_kit;
    
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.2 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        ListViewController *listVc = [self.storyboard instantiateViewControllerWithIdentifier:@"list"];
        listVc.listType = BuildKitList;
        float height = self.view.frame.size.height - (self.textView.frame.size.height + self.textView.frame.origin.y);
        self.controllerHeight.constant = height + 100;
        
        [self.view setNeedsLayout];
        [self.view layoutIfNeeded];
        
        CGRect frame = self.controllerView.bounds;
        frame.size.height = height + 100;
        listVc.view.frame = frame;
        listVc.view.backgroundColor = UIColor.clearColor;
        self.controllerView.backgroundColor = UIColor.clearColor;
        [self addChildViewController:listVc];
        [self.controllerView addSubview:listVc.view];
        
    });

}

@end
