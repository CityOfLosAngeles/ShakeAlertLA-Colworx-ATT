//
//  BuildKitController.h
//  cityofLA
//
//  Created by Sam Sidd on 08/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ListViewModel.h"

@interface BuildKitController : UIViewController

@property (strong,nonatomic) IBOutlet UITextView *textView;
@property (strong,nonatomic) IBOutlet UIView *controllerView;
@property UIColor *navColor;
@property ListType listType;

@property (strong,nonatomic) IBOutlet NSLayoutConstraint *controllerHeight;
@end
