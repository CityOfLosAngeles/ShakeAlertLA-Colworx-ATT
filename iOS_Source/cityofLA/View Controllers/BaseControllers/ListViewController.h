//
//  ListViewController.h
//  cityofLA
//
//  Created by Sam Sidd on 04/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Helper.h"




@interface ListViewController : UIViewController
@property (weak,nonatomic) IBOutlet UITableView *tableView;
@property (weak,nonatomic) IBOutlet UIImageView *bgView;
@property (weak,nonatomic) IBOutlet UITextView *textView;
@property (weak,nonatomic) IBOutlet NSLayoutConstraint *tblHeight;
@property (weak,nonatomic) IBOutlet UIScrollView *scrollView;
@property (weak,nonatomic) IBOutlet NSLayoutConstraint *txtViewHeight;
@property ListType listType;
@property ListViewModel *viewModel;
@property UIColor *navColor;
@property NSMutableArray *list;
-(void)goToHome:(UIButton *)btn;
@end
