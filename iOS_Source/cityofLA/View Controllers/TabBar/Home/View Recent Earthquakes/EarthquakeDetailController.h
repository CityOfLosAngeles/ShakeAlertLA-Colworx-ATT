//
//  EarthquakeDetailController.h
//  cityofLA
//
//  Created by Sam Sidd on 22/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Notification.h"

@interface EarthquakeDetailController : UIViewController
@property (strong,nonatomic) IBOutlet UIImageView *backgroundImgView;
@property (strong,nonatomic) IBOutlet UILabel *titleLbl;
@property (strong,nonatomic) IBOutlet UIView *cardView;
@property (strong,nonatomic) IBOutlet UILabel *magnitude;
@property (strong,nonatomic) IBOutlet UILabel *time;
@property (strong,nonatomic) IBOutlet UILabel *location;
@property (strong,nonatomic) IBOutlet UILabel *latitude;
@property (strong,nonatomic) IBOutlet UILabel *longitude;
@property (strong,nonatomic) IBOutlet UILabel *intensity;
    
@property (strong,nonatomic) IBOutlet UILabel *magnitudeTitle;
@property (strong,nonatomic) IBOutlet UILabel *timeTitle;
@property (strong,nonatomic) IBOutlet UILabel *locationTitle;
@property (strong,nonatomic) IBOutlet UILabel *latitudeTitle;
@property (strong,nonatomic) IBOutlet UILabel *longitudeTitle;
@property (strong,nonatomic) IBOutlet UILabel *intensityTitle;
    
@property Notification *n;

//Colworx
@property BOOL fromQuakeList;
@end
