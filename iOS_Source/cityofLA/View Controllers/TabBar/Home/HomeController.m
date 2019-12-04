//
//  HomeController.m
//  cityofLA
//
//  Created by Sam Sidd on 04/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import "HomeController.h"
#import "UIView+ViewAdditions.h"
#import "Helper.h"
#import "TabBarController.h"
#import "AppDelegate.h"
@interface HomeController () <UICollectionViewDataSource,UICollectionViewDelegate,UICollectionViewDelegateFlowLayout,UITabBarDelegate>

@end
//CTI: Controller class that manages dashboard view and functions
@implementation HomeController
-(void)viewWillAppear:(BOOL)animated{
    [ self.navigationController.navigationBar setHidden:YES];
    [self reloadItems];
}
- (void)viewDidLoad {
    [super viewDidLoad];
    [self.cardView drawDropShadowOnView:15];
    self.cardView.layer.cornerRadius = 6;
    [Helper applyAttributesOnVc:self withColor:darkBlueColor];
}
-(void)reloadItems{
    self.items = [[NSMutableArray alloc]init];
    [self.items addObject:@{@"image":[UIImage imageNamed:@"prepare"],
                            @"title":[Helper localized:@"prepare"],
                            @"lineImage":[UIImage imageNamed:@"p_line"]
                            }];
    [self.items addObject:@{@"image":[UIImage imageNamed:@"understand"],
                            @"title":[Helper localized:@"understand"],
                            @"lineImage":[UIImage imageNamed:@"u_line"]
                            }];
    [self.items addObject:@{@"image":[UIImage imageNamed:@"view"],
                            @"title":[Helper localized:@"view"],
                            @"lineImage":[UIImage imageNamed:@"v_line"]
                            }];
    [self.items addObject:@{@"image":[UIImage imageNamed:@"recover"],
                            @"title":[Helper localized:@"recover"],
                            @"lineImage":[UIImage imageNamed:@"r_line"]
                            }];
    [self.collectionView reloadData];
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.2 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        self.collectionViewHeight.constant = self.collectionView.contentSize.height;
    });
    self.bePrepared.text = [[Helper localized:@"bePrepared"] uppercaseString];
    self.getKitReady.text = [Helper localized:@"kitReady"];
    [self.aboutShakeAlert setTitle:[Helper localized:@"aboutShakeAlert"] forState:UIControlStateNormal];
    [self.termsBtn setTitle:[Helper localized:@"termsOfUse"] forState:UIControlStateNormal];
    self.topLabel.text = [Helper localized:@"homeTitle"];

    
//    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
//        UIAlertController *alert =  [UIAlertController alertControllerWithTitle:@"Terremoto! Terremoto!" message:@"está sucediendo, es posible que Muy fuerte a Fuerte una sacudida débil en su ubicación." preferredStyle:UIAlertControllerStyleAlert];
//        UIAlertAction *action = [UIAlertAction actionWithTitle:@"Descartar" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
//
//        }];
//        UIAlertAction *dismis =[UIAlertAction actionWithTitle:@"Localizar en un mapa" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
//
//        }];
//
//        [alert addAction:action];
//        [alert addAction:dismis];
//        [self presentViewController:alert animated:NO completion:nil];
//    });
//

    
}

- (IBAction)shakeBtn:(UIButton *)sender {
    [Helper pushListViewControllerWithType:AboutLA title:[Helper localized:@"aboutShakeAlert"] navColor:darkBlueColor onVc:self];
}
- (IBAction)termsBtn:(UIButton *)sender {
    [Helper pushHtmlControllerWithFile:@"terms and condition"
                                  onVc:self withColor:darkBlueColor
                        dontHideNavBar:NO
                                 title:[Helper localized:@"termsTitle"]];
}

#pragma MARK :- UICollectionView Methods
-(NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView{
    return 1;
}
-(NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section{
    return self.items.count;
}
-(UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath{
    
    UICollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"cell" forIndexPath:indexPath];
    UIImageView *imgView = [cell viewWithTag:1];
    UILabel *titleLbl = [cell viewWithTag:2];
    UIImageView *lineView = [cell viewWithTag:4];
    
    NSDictionary *item = self.items[indexPath.row];
    imgView.image = item[@"image"];
    titleLbl.text = item[@"title"];
    lineView.image = item[@"lineImage"];

    [Helper drawDropShadowOnCell:cell :6];
    return  cell;
}
-(void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.row == 0) { // Make A Plan Toggle
        [Helper pushListViewControllerWithType:Plan
                                         title:[Helper localized:@"makePlanNextVc"]
                                      navColor:makePlanColor
                                          onVc:self];
    }
    if (indexPath.row == 1){
        [self selectIndex:1];
    }
    if (indexPath.row == 2) {
        [Helper pushTwoTabsControllerOnVc:self
                                withColor:purpleLAColor
                              withTabType:quakeTabs
                                    title:[Helper localized:@"view"]];
    }
    if (indexPath.row == 3){
        [self selectIndex:2];
    }
}
-(void)selectIndex:(int)index{
    TabBarController *tabbar = (TabBarController *)self.tabBarController;
    UITabBarItem *item = [tabbar.tabBar.items objectAtIndex:index];
    [tabbar tabBar:tabbar.tabBar didSelectItem:item];;
    [tabbar setSelectedIndex:index];
}
-(CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath{
    return CGSizeMake( ScreenWidth * 0.44 , 146);
}
@end
