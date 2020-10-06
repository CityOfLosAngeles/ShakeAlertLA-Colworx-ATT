//
//  QuakeListController.h
//  cityofLA
//
//  Created by Sam Sidd on 05/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface QuakeListController : UIViewController

@property (strong,nonatomic) IBOutlet UICollectionView *collectionView;
@property (strong,nonatomic) IBOutlet UILabel *label4;
@property (strong,nonatomic) IBOutlet UILabel *label5;
@property (strong,nonatomic) IBOutlet UILabel *label4n5;
@property (strong,nonatomic) IBOutlet UILabel *magnitudeLbl;
-(void)reload;

//Colworx -> 03-04-2019
@property BOOL apiResponse;
-(void)scrollToTop;
@end
