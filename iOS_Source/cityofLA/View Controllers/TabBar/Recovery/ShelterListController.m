//
//  ShelterListController.m
//  cityofLA
//
//  Created by Sam Sidd on 05/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import "ShelterListController.h"
#import "Helper.h"
#import "ShelterListItem.h"
#import "ShelterMapController.h"

@interface ShelterListController () <UICollectionViewDataSource,UICollectionViewDelegate>

@end

@implementation ShelterListController{
    TwoTabController *parentVc;
    UIRefreshControl *refresh;
}
- (void)viewDidLoad {
    [super viewDidLoad];
    parentVc = [TwoTabController sharedInstance];
    refresh = [[UIRefreshControl alloc]init];
    [refresh addTarget:self action:@selector(refreshList:) forControlEvents:UIControlEventValueChanged];
    [self.collectionView addSubview:refresh];
    self.textLabel.text = [Helper localized:@"shelterListText"];
}
-(void)refreshList:(UIRefreshControl *)refresh{
    if (refresh.state == 0) {
        [parentVc loadShelter];
    }
}
-(NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView{
    return 1;
}
-(NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section{
    return parentVc.pins.count;
}
-(UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath{
    UICollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"cell" forIndexPath:indexPath];
    UILabel *titleLbl = [cell viewWithTag:1];
    ShelterListItem *item = parentVc.pins[indexPath.row];
   __block UILabel *dLbl = [cell viewWithTag:2];
    [Helper drawDropShadowOnCell:cell :6];
    cell.contentView.layer.cornerRadius = 10;
    titleLbl.text = item.title;
    dLbl.text = item.desc;
    [item getAddress:^(NSString *address) {
        dLbl.text = address;
    }];
    return cell;
}
-(void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath{
    ShelterListItem *item = parentVc.pins[indexPath.row];
    ShelterMapController *mapVc = parentVc.viewControllers.lastObject;
    mapVc.highlightLocation = item.title;
    [parentVc mapPressed:parentVc.mapBtn];
}
-(void)reload{
    NSDateFormatter *fm = [[NSDateFormatter alloc]init];
    [fm setDateFormat:@"MMM dd, hh:mm a"];
    NSString *dateStr = [fm stringFromDate:[NSDate date]];
    
    self.dateLbl.text = [NSString stringWithFormat:@"%@: %@",[Helper localized:@"refreshed"],dateStr];
    [self.collectionView reloadData];
    [refresh endRefreshing];
    
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.2 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        self.collectionHeight.constant = self.collectionView.contentSize.height;
    });
    
}

@end
