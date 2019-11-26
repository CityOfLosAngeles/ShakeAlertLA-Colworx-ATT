//
//  ListItemCell.m
//  cityofLA
//
//  Created by Sam Sidd on 04/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import "ListItemCell.h"
#import "UIFont+FontAdditions.h"
#import "KitItem.h"
#import "Helper.h"

@implementation ListItemCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
}

-(void)setupWithItem:(id )it{
    self.item = it;
    if ([it isKindOfClass:[ListItem class]]){
        ListItem *item = (ListItem *)it;
        self.item = item;
        self.imgView.image = item.image;
        self.title.text = item.title;
        self.accessibilityLabel = item.title;
    }else if ([it isKindOfClass:[PlanListItem class]]){
        PlanListItem *item = (PlanListItem *)it;
        self.imgView.image = [UIImage imageNamed:@"off"];
        if (item.completed) {
            self.accessibilityLabel = [NSString stringWithFormat:@"Checked %@",item.plan];
            self.imgView.image = [UIImage imageNamed:@"check"];
        }else{
            self.accessibilityLabel = [NSString stringWithFormat:@"Unchecked %@",item.plan];
        }
        self.title.text = item.plan;
    }else if ([it isKindOfClass:[SecurePlaceItem class]]){
        SecurePlaceItem *item = (SecurePlaceItem *)it;
        self.imgView.image = [UIImage imageNamed:@"off"];
        if (item.completed) {
            self.imgView.image = [UIImage imageNamed:@"check"];
            self.accessibilityLabel = [NSString stringWithFormat:@"Checked %@",item.name];
        }else{
            self.accessibilityLabel = [NSString stringWithFormat:@"Unchecked %@",item.name];
        }
        self.title.text = item.name;
    }else if ([it isKindOfClass:[KitItem class]]){
        KitItem *item = (KitItem *)it;
        self.completedLbl.text = item.completedText;
        self.title.text = item.kit;
        self.progress.progress =  (float)item.completedItems / (float)item.kitSubItems.count;
        self.backgroundColor = UIColor.clearColor;
        self.contentView.backgroundColor = UIColor.clearColor;
        self.progress.progressTintColor = makePlanColor;
        self.accessibilityLabel = self.title.text;
    }else if ([it isKindOfClass:[KitSubItem class]]){
        KitSubItem *item = (KitSubItem *)it;
        self.title.text = item.item_name;
        self.imgView.image = [UIImage imageNamed:@"off"];
        if (item.completed) {
            self.accessibilityLabel = [NSString stringWithFormat:@"Checked %@",self.title.text];
            self.imgView.image = [UIImage imageNamed:@"check"];
        }else{
            self.accessibilityLabel = [NSString stringWithFormat:@"Unchecked %@",self.title.text];
        }
        self.btn.hidden = YES;
    }
    self.backgroundColor = UIColor.clearColor;
    
    NSString *lang = [[NSUserDefaults standardUserDefaults]valueForKey:@"lang"];
    if ([lang isEqualToString:@"en"]){
        self.btn.accessibilityLabel = @"Go Next";
    }else{
        
    }
    
    [self.btn addTarget:self action:@selector(nextPressed:) forControlEvents:UIControlEventTouchUpInside];
}


-(void)nextPressed:(UIButton *)btn{
    [self.delegate nextBtnPressed:btn item:self.item];
}
@end
