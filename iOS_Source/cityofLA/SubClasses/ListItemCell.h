//
//  ListItemCell.h
//  cityofLA
//
//  Created by Sam Sidd on 04/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import "ListItem.h"
#import <UIKit/UIKit.h>
#import "PlanListItem.h"
#import "SecurePlaceItem.h"

@protocol ListItemCellDelegate <NSObject>

-(void)nextBtnPressed:(UIButton *)btn item:(id)Item;

@end

@interface ListItemCell : UITableViewCell
@property PlanListItem *planItem;
@property id item;
@property (strong,nonatomic) IBOutlet UIImageView *imgView;
@property (strong,nonatomic) IBOutlet UILabel *title;
@property (strong,nonatomic) IBOutlet UIButton *btn;
@property (strong,nonatomic) IBOutlet UIProgressView *progress;
@property (strong,nonatomic) IBOutlet UILabel *completedLbl;
-(void)setupWithItem:(id )it;
@property id<ListItemCellDelegate> delegate;
@end
