//
//  EarthquakeInfo.h
//  cityofLA
//
//  Created by Sam Sidd on 22/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Notification.h"

@interface EarthquakeInfo : UIView

@property  (strong,nonatomic) IBOutlet UILabel *titleLbl;
@property  (strong,nonatomic) IBOutlet UILabel *descLbl;
@property  (strong,nonatomic) IBOutlet UILabel *magnitudeLbl;
@property  (strong,nonatomic) IBOutlet UILabel *dateLbl;
@property  (strong,nonatomic) IBOutlet UILabel *addressLbl;
@property  (strong,nonatomic) IBOutlet UIButton *detailBtn;
@property Notification *n;
-(void)setupWithNotification:(Notification *)n;
@end
