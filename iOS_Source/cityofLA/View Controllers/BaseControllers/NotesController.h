//
//  NotesController.h
//  cityofLA
//
//  Created by Sam Sidd on 08/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MDTextField.h"
#import "PlanListItem.h"

@interface NotesController : UIViewController
@property (strong,nonatomic) IBOutlet UIScrollView *scrollView;
@property (strong,nonatomic) IBOutlet UILabel *recommendationsLbl;
@property (strong,nonatomic) IBOutlet UITextView *recommendationTextView;
@property (strong,nonatomic) IBOutlet UILabel *notesLabel;
@property (strong,nonatomic) IBOutlet MDTextField *notesField;
@property (strong,nonatomic) IBOutlet UILabel *completeLabel;
@property (strong,nonatomic) IBOutlet UISwitch *completeSwitch;
@property (strong,nonatomic) IBOutlet UIButton *tickbtn;

@property (strong,nonatomic) IBOutlet NSLayoutConstraint *notesHeight;

@property PlanListItem *planListItem;
@property UIColor *navColor;
@end
