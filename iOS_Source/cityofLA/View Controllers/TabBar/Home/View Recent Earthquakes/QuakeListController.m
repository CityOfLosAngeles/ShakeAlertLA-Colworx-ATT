//
//  QuakeListController.m
//  cityofLA
//
//  Created by Sam Sidd on 05/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import "QuakeListController.h"
#import "Helper.h"
#import "EarthquakeDetailController.h"
#import "Notification.h"
#import "TwoTabController.h"
@interface QuakeListController () <UICollectionViewDataSource,UICollectionViewDelegate> {
    NSMutableArray *earthQuakeList;
}
@end

@implementation QuakeListController{
    TwoTabController *parentVc;
//    WASIQ
    UIRefreshControl *refresh;
}
- (void)viewDidLoad {
    [super viewDidLoad];
    parentVc = [TwoTabController sharedInstance];
    
    self.label4.text = [Helper localized:@"label4"];
    self.label5.text = [Helper localized:@"label5"];
    self.label4n5.text = [Helper localized:@"label4n5"];
    self.magnitudeLbl.text = [Helper localized:@"magnitude:"];
    
//    WASIQ
    refresh = [[UIRefreshControl alloc]init];
    [refresh addTarget:self action:@selector(refreshList:) forControlEvents:UIControlEventValueChanged];
    [self.collectionView addSubview:refresh];
//    END WASIQ
}

//    WASIQ
-(void)refreshList:(UIRefreshControl *)refresh{
    if (refresh.state == 0) {
        [self.collectionView setBounces:NO];
        parentVc.boundedAnnotation = NO;
        [[parentVc boundedAnnotations] removeAllObjects];
        parentVc.radius = 800;
        [parentVc loadRecentEarthQuakes: 800];
        [TwoTabController sharedInstance].popedViewController = NO;
    }
}

//method reload
-(void)reload{
    
    [refresh endRefreshing];
    [self.collectionView setBounces:YES];
    [self.collectionView reloadData];
    [self.collectionView setContentOffset:CGPointZero animated:YES];
}

//WASIQ
-(void) scrollToTop {
    [self.collectionView setContentOffset:CGPointZero animated:YES];
//    [collectionView setContentOffset:CGPointZero animated:YES];
}
//END WASIQ
#pragma MARK UICollectionView Methods:
-(NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView{
    return 1;
}
-(NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section{
    //WASIQ
    if ([TwoTabController sharedInstance].boundedAnnotation) {
        return [TwoTabController sharedInstance].boundedAnnotations.count;
    } else {
        return [TwoTabController sharedInstance].notifications.count;
    }
    //END WASIQ
}
-(UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath{
   __block UICollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"cell" forIndexPath:indexPath];
    Notification *n;
    if ([TwoTabController sharedInstance].boundedAnnotation) {
        n = [TwoTabController sharedInstance].boundedAnnotations[indexPath.row];
    } else {
        n = [TwoTabController sharedInstance].notifications[indexPath.row];
    }
    
    UILabel *titleLbl       = [cell viewWithTag:1];
    UILabel *descLbl        = [cell viewWithTag:2];
   __block UILabel *locationLbl    = [cell viewWithTag:3];
    UILabel *magnitudeLbl   = [cell viewWithTag:4];
    UILabel *dateLbl        = [cell viewWithTag:5];
    UIImageView *imgview    = [cell viewWithTag:6];

    
    titleLbl.text = n.title;
    locationLbl.text = n.address;
    magnitudeLbl.text = [NSString stringWithFormat:@"%@: %@", [Helper localized:@"magnitude"],n.MagnitudeValue];
//    dateLbl.text = n.timeToDisplay;
    imgview.image = n.pinImage;
    dateLbl.text = [NSString stringWithFormat:@"%@ (PST)", n.timeToDisplay];
    [Helper drawDropShadowOnCell:cell :10];

//        Notification *n = [TwoTabController sharedInstance].notifications[indexPath.row];
//        titleLbl.text = n.title;
//        descLbl.text = n.body;
//        locationLbl.text = n.address;
//        if (n.address == nil) {
//            [n getAddress:^(NSString * _Nonnull address) {
//                cell = [collectionView cellForItemAtIndexPath:indexPath];
//                locationLbl    = [cell viewWithTag:3];
//                locationLbl.text = address;
//            }];
//        }else{
//            locationLbl.text = n.address;
//        }
//        magnitudeLbl.text = [NSString stringWithFormat:@"%@ : %@", [Helper localized:@"magnitude"],n.MagnitudeValue];
//        dateLbl.text = n.timeToDisplay;
//        imgview.image = n.pinImage;
//        [Helper drawDropShadowOnCell:cell :10];
//    }
    return cell;
}
-(void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath{
    
    //WASIQ
    Notification *n;
    if ([TwoTabController sharedInstance].boundedAnnotation) {
        n = [TwoTabController sharedInstance].boundedAnnotations[indexPath.row];
    } else {
        n = [TwoTabController sharedInstance].notifications[indexPath.row];
    }
    //END WASIQ
    
    EarthquakeDetailController *detailVc = [self.storyboard instantiateViewControllerWithIdentifier:@"earthquakeDetail"];
    detailVc.n = n;
    detailVc.fromQuakeList = YES;
    parentVc.navigationController.interactivePopGestureRecognizer.delegate = nil;
    [parentVc.navigationController pushViewController:detailVc animated:YES];
}
@end
