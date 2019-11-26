//
//  ListViewController.m
//  cityofLA
//
//  Created by Sam Sidd on 04/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import "ListViewController.h"
#import "TwoTabController.h"
#import "PlanListItem.h"
#import "SecurePlaceItem.h"
#import "DBHelper.h"
#import "KitItem.h"
#import "NSString+StringAdditions.h"
#import "UIFont+FontAdditions.h"
#import "TabBarController.h"


@interface ListViewController () <ListItemCellDelegate>

@end

@implementation ListViewController{
    BOOL dontHideNav;
    
}
- (void)viewWillAppear:(BOOL)animated{
    if (self.listType == BuildKitList) {
        self.tableView.scrollEnabled = NO;
        self.scrollView.scrollEnabled = NO;
        [self setupViewModel];
        return;
    }
    if ([self isRecoveryController]) {
        [Helper applyAttributesOnVc:self withColor:self.navColor];
        [Helper recoveryBackBtn:self];
    }else{
        [Helper setupNavBarOnPush:self withColor:self.navColor];
    }
        [self setupViewModel];
}
-(void)viewWillDisappear:(BOOL)animated{
    if (self.listType == BuildKitList) {
        
        return;
    }
    if ([self isRecoveryController]) {return;}
    if (dontHideNav) {
        dontHideNav = !dontHideNav;
        return;
    }
    [Helper resetNavBarOnPop:self];
}
- (void)viewDidLoad {
    [super viewDidLoad];
    
    if ([self isRecoveryController]) {
        self.listType = Recovery;
        self.navColor = recoveryGreenColor;
        self.title = [Helper localized:@"recoveryTitle"];
    }
    if (self.listType == SecureSubList){
        self.tableView.rowHeight = UITableViewRowAnimationAutomatic;
        self.tableView.estimatedRowHeight = 44;
    }
    if (self.listType != BuildKitList) {
    }else{
        self.bgView.hidden = YES;
    }
    [self setupViewModel];
}
-(void)goToHome:(UIButton *)btn{
    [[TabBarController sharedInstance]setSelectedIndex:0];
}
-(void)setupViewModel{
    self.viewModel = [[ListViewModel alloc]initWithListType:self.listType];
    if (self.listType == BuildKitSubItemList) {
        self.viewModel.list = self.list;
    }
    self.tableView.backgroundColor = UIColor.clearColor;
    [self.tableView reloadData];
    
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.2 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        if (self.listType == Plan) {
            self.tblHeight.constant = self.tableView.contentSize.height;
            self.tableView.scrollEnabled = NO;

            NSMutableAttributedString *planDesc = [[NSMutableAttributedString alloc]initWithString:[Helper localized:@"planDesc"] attributes:@{NSFontAttributeName : [UIFont SFRegularWithSize:14]}];
            
            NSRange range = [planDesc.mutableString rangeOfString:[Helper localized:@"notifyLa"]];
//            [planDesc replaceCharactersInRange:range withString:@"Notify LA"];
            [planDesc addAttribute: NSLinkAttributeName value:[Helper localized:@"notifyLaLink"]  range: range];
            [planDesc addAttribute: NSUnderlineStyleAttributeName value:@(NSUnderlineStyleSingle)  range: range];
            
            range = [planDesc.mutableString rangeOfString:[Helper localized:@"rylan"]];
//            [planDesc replaceCharactersInRange:range withString:@"RYLAN"];
            [planDesc addAttribute: NSLinkAttributeName value:[Helper localized:@"rylanLink"]  range: range];
            [planDesc addAttribute: NSUnderlineStyleAttributeName value:@(NSUnderlineStyleSingle)  range: range];
            
            self.textView.attributedText = planDesc;
        }else{
            self.tblHeight.constant = self.tableView.contentSize.height;
        }
    });
}
-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 1;
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.viewModel.list.count;
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    ListItemCell *cell = [tableView dequeueReusableCellWithIdentifier:@"cell"];
    if (self.listType == SecureSubList){
        cell = [tableView dequeueReusableCellWithIdentifier:@"cellSecure"];
    }
    if (self.listType == BuildKitList){
        cell = [tableView dequeueReusableCellWithIdentifier:@"cellBuildKit"];
    }
    [cell setupWithItem:self.viewModel.list[indexPath.row]];
    cell.contentView.backgroundColor = UIColor.clearColor;
    cell.delegate = self;
    
    return cell;
}
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    if (self.listType == SecureSubList) {
        SecurePlaceItem *item = self.viewModel.list[indexPath.row];
        item.completed = !item.completed;
        BOOL update = [[DBHelper sharedInstance]updateSecurePlaceItem:item];
        if (update) {
            [self.viewModel.list replaceObjectAtIndex:indexPath.row withObject:item];
            [tableView reloadRowsAtIndexPaths:@[[NSIndexPath indexPathForRow:indexPath.row inSection:0]] withRowAnimation:UITableViewRowAnimationAutomatic];;
            return;
        }
        return;
    }
    if (self.listType == BuildKitSubItemList) {
        KitSubItem *item = self.viewModel.list[indexPath.row];
        item.completed = !item.completed;
        BOOL update = [[DBHelper sharedInstance]updateKitSubItem:item];
        if (update) {
            [self.viewModel.list replaceObjectAtIndex:indexPath.row withObject:item];
            [tableView reloadRowsAtIndexPaths:@[[NSIndexPath indexPathForRow:indexPath.row inSection:0]] withRowAnimation:UITableViewRowAnimationAutomatic];
            return;
        }
        return;
    }
    ListItem *item = self.viewModel.list[indexPath.row];
    if (item.action == html){
        //If is implemented to show tabbar on the detail screens aswell
        if ([item.title isEqualToString:[Helper localized:@"partnersTitle"]] ||
            [item.title isEqualToString:[Helper localized:@"disclaimerTitle"]] ||
            [item.title isEqualToString:[Helper localized:@"earlyWorkTitle"]] ||
            [item.title isEqualToString:[Helper localized:@"getFromLA"]]) {
            [Helper pushHtmlControllerWithFile:item.htmlFileName
                                          onVc:self
                                     withColor:self.navColor
                                dontHideNavBar:YES
                             dontHideBottomBar:NO
                                         title:item.nextVcTitle];
        }else{
            //15-08-2019
            if (self.viewModel.listType == AboutLA) {
                [self openWebView];
            } else {
                [Helper pushHtmlControllerWithFile:item.htmlFileName
                                                  onVc:self
                                             withColor:self.navColor
                                        dontHideNavBar:YES
                                                 title:item.nextVcTitle];
            }
            //END 15-08-2019
        }
    }else if (item.action == shelter){
        [Helper pushTwoTabsControllerOnVc:self
                                withColor:self.navColor
                              withTabType:shelterTabs
                                    title:item.nextVcTitle];
    }else if (item.action == makePlan){
        [Helper pushListViewControllerWithType:PlanSubList
                                         title:item.title
                                      navColor:makePlanColor
                                          onVc:self
                                   nextVcTitle:item.nextVcTitle];
    }else if (item.action == notesVc){
        PlanListItem *item = self.viewModel.list[indexPath.row];
        [Helper pushNotesViewControllerWithPlanItem:item
                                              title:item.plan
                                           navColor:makePlanColor
                                               onVc:self];
        dontHideNav = YES;
    }else if (item.action == securePlace){
        [Helper pushListViewControllerWithType:SecureSubList
                                         title:item.nextVcTitle
                                      navColor:self.navColor
                                          onVc:self];
    }else if (item.action == build_kit){
        [Helper pushBuildKitViewControllerWithType:BuildKitList
                                         title:item.nextVcTitle
                                      navColor:makePlanColor
                                          onVc:self];
    }else if (item.action == build_kit_item){
        KitItem *it = (KitItem*)item;
        [Helper pushListViewControllerWithType:BuildKitSubItemList
                                         title:it.kit
                                      navColor:makePlanColor
                                          onVc:self
                                     listItems:it.kitSubItems];
    }else if (item.action == link_url){
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:item.linkURL] options:@{} completionHandler:^(BOOL success) {
            NSLog(@"%d",success);
        }];
    }
    
    
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (self.listType == SecureSubList) {
        return  UITableViewAutomaticDimension;
    }
    //15-08-2019
//    return 60;
    
    if (self.listType == AboutLA) {
        if (indexPath.row == self.viewModel.list.count - 1 ) {
            return 80;
        }
    }
    return 60;
    //END 15-08-2019
}
-(BOOL)isRecoveryController{
    for (UINavigationController *nav in self.tabBarController.viewControllers) {
        if ([nav.viewControllers.firstObject isEqual:self]){
            return YES;
        }
    }
    return NO;
}

#pragma ListItemCellDelegate

-(void)nextBtnPressed:(UIButton *)btn item:(id)Item{
    NSInteger index = [self.viewModel.list indexOfObject:Item];
    NSIndexPath *indexPath = [NSIndexPath indexPathForRow:index inSection:0];
    [self tableView:self.tableView didSelectRowAtIndexPath:indexPath];
}



//15-08-2019
-(void) openWebView {
  
    NSString *url = [NSString stringWithFormat:@"https://www.youtube.com/watch?v=0zs-88RG9xU"];
    NSURL *inputURL = [NSURL URLWithString:url];
    NSString *scheme = inputURL.scheme;
    NSString *chromeScheme = nil;
    if ([scheme isEqualToString:@"http"]) {
        chromeScheme = @"googlechrome";
    } else if ([scheme isEqualToString:@"https"]) {
        chromeScheme = @"googlechromes";
    }
    if (chromeScheme) {
        NSString *absoluteString = [inputURL absoluteString];
        NSRange rangeForScheme = [absoluteString rangeOfString:@":"];
        NSString *urlNoScheme =
        [absoluteString substringFromIndex:rangeForScheme.location];
        NSString *chromeURLString = [chromeScheme stringByAppendingString:urlNoScheme];
        NSURL *urlNOSCHEME = [NSURL URLWithString:absoluteString];
        // Open the URL with Chrome.
        [[UIApplication sharedApplication] openURL:urlNOSCHEME options:@{} completionHandler:nil];
    }
}
@end
//END 15-08-2019
