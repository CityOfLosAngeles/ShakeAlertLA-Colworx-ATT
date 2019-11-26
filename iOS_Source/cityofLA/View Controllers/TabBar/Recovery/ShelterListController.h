//
//  ShelterListController.h
//  cityofLA
//
//  Created by Sam Sidd on 05/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ShelterListController : UIViewController
@property (strong,nonatomic) IBOutlet UICollectionView *collectionView;
@property (strong,nonatomic) IBOutlet UILabel *textLabel;
@property (strong,nonatomic) IBOutlet UILabel *dateLbl;

@property (strong,nonatomic) IBOutlet NSLayoutConstraint *collectionHeight;
-(void)reload;
@end
