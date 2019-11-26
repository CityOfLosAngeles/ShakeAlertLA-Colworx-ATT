# LAEEW_Android

#### version 1.0 build 1.4
## Deployment Environment

Android Studio 3.2 <br />
Targeted OS : 27 <br />
Windows 10 <br />

### Github version
Git version 2.16.3.windows.1

### Elements that should NOT be open sourced
Files:<br />

google-services.json <br />
awsconfiguration.json <br />
Keystore folder contains .jks file and credentials(Required for Release build)<br />

### General code Information

All API Constants are present in `ConfigConstants.java`.
<br />

The code is structured on MVC pattern. <br /><br />

### API'S
-Glide<br />
-SmartLocation<br />
-Android SQLiteAssetHelper<br />
-Amazon Pinpoint<br />
-BottomNavigationViewEx<br />
-VolleyPlus<br />
-FloatingActionButton<br />

#### Models: 
All Model classes are present under the package `Models` <br />
#### Interfaces/XML: 
XML files are available in group `res`

#### Controllers: 
App Icons and Launch images are also categorized in different image assets and can be easily switched from the Project Settings <br />
RegisterDevice and PushRate services are getting called from RecentEarthquakeService.java class, URL's are available in ConfigConstants.java file.<br /> <br />

-google-services.json and awsconfiguration.json files contains URL's of Amazon Pinpoint and Firbase Cloud Messanging.<br />
-Keystore folder contains .jks file and credentials(Required for Release build).<br />
<br /><br />
