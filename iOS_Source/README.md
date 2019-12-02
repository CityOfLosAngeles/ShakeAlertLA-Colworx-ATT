# LAEEW_iOS
iOS LA Early Earthquake Warning for iOS

version 1.2 build 2.1 <br />
Deployment Environment <br />
Xcode 10.0 <br />
Targeted OS : iOS 11 <br />
Mac OS : mac OS High Sierra 10.13.6 <br />
<br />
<br />
### Elements that should NOT be open sourced
Files<br />

AppDelegate.m<br />
Helper.m<br />
aws-configuration.json<br />


### Code Structure
The code uses MVC acrchiture and each class in the code base is categorized in their respective directory. (Model, Views, and Controller)

Helper classes contains common methods and utility functions that are used across the app.


#### Third-Party Plugin
CocoaPods 1.3.1
<br />
#### Github version
Git version 2.11.0 (Apple Git-81)

The code is structured on MVVM pattern. 

#### Models:
All Model classes are present under the group Model <br />

#### View Models:
All View Model classes are present under the group View Model <br />

#### Interfaces/Storyboard:
XIB and storyboard files are available in group View <br />

#### View Controllers:
All app view controllers are present under the Controller group.<br />
