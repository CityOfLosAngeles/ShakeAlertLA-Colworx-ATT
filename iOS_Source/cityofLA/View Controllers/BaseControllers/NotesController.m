//
//  NotesController.m
//  cityofLA
//
//  Created by Sam Sidd on 08/10/2018.
//  Copyright © 2018 CityOfLa. All rights reserved.
//

#import "NotesController.h"
#import "Helper.h"
#import "DBHelper.h"
#import "UIFont+FontAdditions.h"
#import "NSString+StringAdditions.h"


@interface NotesController () <MDTextFieldDelegate>

@end

@implementation NotesController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setupViews];
}
- (void)viewDidAppear:(BOOL)animated{
    [Helper multineLineTitle:self.navigationController];
    self.title = self.planListItem.plan;
}
-(void)setupViews{
    self.recommendationsLbl.text = [Helper localized:@"recommendation"];
    self.notesLabel.text         = [Helper localized:@"notes"];
    self.completeLabel.text      = [Helper localized:@"completed"];
    
    NSMutableAttributedString *attr = [[NSMutableAttributedString alloc]initWithString:self.planListItem.info
                                                                            attributes:@{NSFontAttributeName:[UIFont SFRegularWithSize:14]}];
    if ([self.planListItem.info containsString:@"</a>"]) {
        NSMutableAttributedString *htmlStr = [[NSMutableAttributedString alloc] initWithData:[self.planListItem.info dataUsingEncoding:NSUTF8StringEncoding]
                                                                       options:@{NSDocumentTypeDocumentAttribute: NSHTMLTextDocumentType,
                                                                                 NSCharacterEncodingDocumentAttribute: @(NSUTF8StringEncoding)}
                                                            documentAttributes:nil error:nil];
        
        [htmlStr addAttributes:@{NSFontAttributeName:[UIFont SFRegularWithSize:14]} range:NSMakeRange(0, htmlStr.length)];
        self.recommendationTextView.attributedText = htmlStr;
        
    }else{
        self.recommendationTextView.attributedText = attr.identifyLinks;
    }
    self.notesField.text = self.planListItem.notes;
    self.completeSwitch.on = self.planListItem.completed;
    
    self.notesField.label = [Helper localized:@"additionalNotes"];
    self.notesField.labelsFont = [UIFont SFRegularWithSize:12];
    self.notesField.normalColor = self.navColor;
    self.notesField.highlightColor = self.navColor;
    self.notesField.singleLine = NO;
    self.notesField.inputTextFont = [UIFont SFRegularWithSize:14];
    self.notesField.floatingLabel = NO;
    self.notesField.returnKeyType = UIReturnKeyDone;
    self.notesField.delegate = self;
    self.notesField.maxVisibleLines = 100;
    self.notesField.text = self.planListItem.notes;
    
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{

        if (self->_notesField.text.length > 0) {
           CGFloat height = [self.notesField.text heightForString:self.notesField.text font:self.notesField.inputTextFont maxWidth:self.notesField.textView.frame.size.width];
            height += self.notesField.textView.frame.origin.y;
            CGRect frame = self.notesField.textView.frame;
            frame.size.height = height;
            self.notesField.textView.frame = frame;

            CGRect dividerFrame = self.notesField.dividerHolder.frame;
            dividerFrame.origin.y = height + 5;
            self.notesField.dividerHolder.frame = dividerFrame;
            self.notesHeight.constant = height + 30;
        }
    });
    self.completeSwitch.tintColor = self.navColor;
    self.completeSwitch.onTintColor = self.navColor;
    [Helper setupNavBarOnPush:self withColor:self.navColor];
}
#pragma MARK MDTextFieldDelegate:

-(void)textFieldDidEndEditing:(MDTextField *)textField{
    self.planListItem.notes = textField.text;
    BOOL updated = [[DBHelper sharedInstance]updatePlanItem:self.planListItem];
    if (updated) {
        NSLog(@"plan item updated");
    }
}
- (void)textFieldDidChange:(MDTextField *)textField{
    if (textField.text.length == 0){
        self.notesHeight.constant = 70;
    }else{
        self.notesHeight.constant = textField.dividerHolder.frame.origin.y + textField.dividerHolder.frame.size.height + 8;
    }
}
-(IBAction)tickPressed:(UISwitch *)s{
    [self.notesField endEditing:YES];
    self.planListItem.notes = self.notesField.text;
    self.planListItem.completed = s.on;
    BOOL updated = [[DBHelper sharedInstance]updatePlanItem:self.planListItem];
    if (updated) {
        NSLog(@"plan item updated");
    }
    [self.navigationController popViewControllerAnimated:YES];
}
-(NSMutableAttributedString *)addLinkAttributes{
    NSString *yourString = self.recommendationTextView.text;
    NSDataDetector* detector = [NSDataDetector dataDetectorWithTypes:NSTextCheckingTypeLink error:nil];
    NSArray* matches = [detector matchesInString:yourString options:0 range:NSMakeRange(0,  yourString.length)];
    NSMutableAttributedString *attrText =  [[NSMutableAttributedString alloc] initWithString:yourString];
    
    for (int index = 0 ; index < matches.count; index ++) {
        NSTextCheckingResult *textResult = [matches objectAtIndex : index];
        NSRange testRange = textResult.range;
        NSURL *testUrl = textResult.URL ;
        [attrText addAttribute:NSLinkAttributeName value:testUrl  range: testRange];
        [attrText addAttribute:NSFontAttributeName
                                  value:[UIFont SFRegularWithSize:14] range:NSMakeRange(0,yourString.length)];

    }
    return attrText;
}
-(void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event{
    [self.notesField endEditing:YES];
    [self.notesField resignFirstResponder];
}
@end
