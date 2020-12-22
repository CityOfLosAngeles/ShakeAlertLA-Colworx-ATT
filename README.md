# ShakeAlertLA
_ShakeAlertLA has been archived. You are free to fork and modify the code for your own use, but development on the open source repository has stopped and issues and pull requests will not be accepted._

_This project is provided as is under the terms of the [Apache 2.0](./LICENSE) license. The City of Los Angeles is not responsible for any ways in which the software might be used by third parties._

ShakeAlertLA is an application that was developed in partnership with the City of Los Angeles to provide early warning of strong earthquake shaking to the citizens of the Los Angeles area.

The code below was built in collaboration between the City of Los Angeles and by AT&T and its contractors. The City of L.A. is providing this source code for other organizations who might be interested in implementing similar solutions. Note that there are components of the code (as noted below) that are redacted since there is a separate application process with the U.S. Geological Survey to have access to the ShakeAlert messaging feed and provide public notifications.

The following documentation provides an overview of the ShakeAlertLA application:


1. [ShakeAlertLA Open Source Code Review presentation](https://drive.google.com/file/d/1d12d4wDPXvyLe0XyMXjydRonMNZMD0lz/view?usp=sharing) with the City of L.A., AT&T and other municipalities about the code.
2. [Colworx ShakeAlertLA Source Code guide](https://drive.google.com/file/d/1kZMKiicvBHyfzpc-3w9coCvlFp1ZKPnn/view?usp=sharing) provided by AT&T and its developer Colworx.

## Measures taken by AT&T to secure the code

AT&T has undertaken, with the concurrence and purview of the City of L.A., industry appropriate reviews for security for applications of the nature of ShakeAlertLA including the details below. 

iOS Specific:
* ImmuniWeb Scan
* AWS instance hardened

Android Specific:
* Quixxi for Android scan

Port scanners:
* Port Checker Open Port Checker ([www.portcheckers.com](https://www.portcheckers.com/)) and You Get Signal ([www.yougetsignal.com/tools/open-ports](https://www.yougetsignal.com/tools/open-ports/))

Pen Test:
* Completed by AT&T Cybersecurity
* NOTE: AT&T does not control the U.S. Geological Survey environment

Authentication/Authorization:
* The app has no login, the information endpoints are open, and public and no actual, usable data is stored or transmitted.
* GUID is encrypted for push notification, language setting, and last anonymous location via lat/long. 

AWS Specific:
* Hardened the solution and encrypted the transmission per standard AWS deployment ([docs.aws.amazon.com/AWSEC2/latest/UserGuide/ec2-security.html](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/ec2-security.html)).

#### Continue to read this README file for further guidance on how to modify this code for your own development.

## About ShakealertLA	
ShakeAlertLA alerts you that an earthquake has been detected and that you may soon feel shaking. You can also use this app to prepare for an earthquake, get details on recent earthquakes, and find help after an earthquake. This app is brought to you by Mayor Eric Garcetti and the City of Los Angeles, and built on the ShakeAlert system developed by the U.S. Geological Survey.

## Interface design choices

The designs of the app are inspired with Google's Material design and user-centric design sessions with first responders, the public, and experts. The user interface (UI) and user experience (UX) is user-friendly and easy to understand. For acessibility, the colors, contrast, shadows, and layout are designed to allow users of diverse abilities to navigate, understand, and use the UI.


* For iOS, the Apple Human Interface guideline (AHI) is used with iOS native controls and elements. For Android, Material-UI has been used. For earthquake alerts and warnings, the color red is used. There are four main sections to the app, and each section of the app has its own color representation, for example:
  * Prepare for an earthquake – teal green
  * Earthquake alert map – red
  * View recent earthquakes - purple
  * Recover from an earthquake – green
* There are also setup wizards that help guide people with a quick walk through of the app.

## Data storage

* For data storage, ShakeAlertLA uses a MySQL database on the backend.
* In the database, only grid numbers, events, anonymous device ID, and campaign IDs are stored. There is no actual personal information that can be traced to any individual.
* The data at rest and transmission is secured and encrypted.
* An example of a data display would be: DeviceID: 6191fe1721ab8210, Grid: EN-54, Location: 34.912, -118.982 Language: EN
* There is no hard cache or offline mode.

## Server-side logic and push notifications

### 1. Web service to register the devices into the grid (blocks)	
* An API is used to POST anonymous data with location co-ordinates to the server.
* When a user launches the app, it passes the device token and location coordinates to this web service for device registration.
* This endpoint then verifies the coordinates and returns the grid number to the client side app (the app continuously checks for significant changes in the user location to send back the updated coordinates to the server).
* The data at rest and in transmission is secured and encrypted.

### 2. When ShakeAlert sends an alert

#### Obtain the USGS Active MQ listener
To create an alert, you must be subscribed to the U.S. Geological Survey (USGS) ShakeAlert message feed. ShakeAlert is managed and operated by the USGS. You can request an account to receive alerts directly and solely from the USGS. Details are here [www.shakealert.org/implementation/partners](https://www.shakealert.org/implementation/partners/). This application process can take some time to be approved and requires interaction with the USGS ShakeAlert team, so make plans accordingly.
* From the receiver code you can send the message payload to this class as shown below for grid processing. [Backend_Source/LABackend](./Backend_Source/LABackend)

```java
public class MainClass {	

	@SuppressWarnings("unchecked")
	public static void runCode(List<Map> Blocks, 
			Document doc,
			HashMap<String, Object> hashMap) throws FileNotFoundException, IOException, ParseException {
```

#### Active MQ listener receives a new event

* When an event is received at MQ listener, the backend then checks the magnitude and modified Mercalli intensity (MMI) values and only reads the polygon values from the contour where the magnitude is greater than or equal to 4.5 and the MMI is greater than or equal to 3.0 *at that location*. Therefore, a magnitude 7.0 quake in Oregon is unlikely to trigger an alert in Los Angeles if the thresholds are not met for a grid in Los Angeles. These values are set by the USGS and cannot be lowered except by them. Developers can choose to have higher limits. The experience of the City of L.A. is that people downloading ShakeAlertLA want to have notifications to the maximum extent allowed.

* Below is an example of an event received from USGS.
```XML
<?xml version="1.0" encoding="UTF-8" standalone="no" ?>
<event_message alg_vers="1.1.1 2019-04-17" category="live" instance="eqinfo2gm-contour@eew-uw-prod2" message_type="update" orig_sys="eqinfo2gm" ref_id="0" ref_src="" timestamp="2020-06-25T04:09:15.928Z" version="2">

  <core_info id="6576">
    <mag units="Mw">3.6514</mag>
    <mag_uncer units="Mw">0.2665</mag_uncer>
    <lat units="deg">38.8350</lat>
    <lat_uncer units="deg">0.0656</lat_uncer>
    <lon units="deg">-122.7722</lon>
    <lon_uncer units="deg">0.0656</lon_uncer>
    <depth units="km">8.0000</depth>
    <depth_uncer units="km">5.0000</depth_uncer>
    <orig_time units="UTC">2020-06-25T04:09:07.308Z</orig_time>
    <orig_time_uncer units="sec">0.8431</orig_time_uncer>
    <likelihood>0.9653</likelihood>
    <num_stations>8</num_stations>
  </core_info>

  <contributors>
    <contributor alg_instance="epic@eew-uw-prod1" alg_name="epic" alg_version="3.2.0-2019-06-06" category="live" event_id="42679" version="2"/>
    <contributor alg_instance="epic@eew-uw-prod2" alg_name="epic" alg_version="3.2.0-2019-06-06" category="live" event_id="39318" version="2"/>
    <contributor alg_instance="epic@eew-bk-prod2" alg_name="epic" alg_version="3.2.0-2019-06-06" category="live" event_id="43748" version="2"/>
    <contributor alg_instance="epic@eew-nc-prod1" alg_name="epic" alg_version="3.2.0-2019-06-06" category="live" event_id="22584" version="1"/>
    <contributor alg_instance="epic@eew-nc-prod2" alg_name="epic" alg_version="3.2.0-2019-06-06" category="live" event_id="22346" version="1"/>
    <contributor alg_instance="epic@eew-bk-prod1" alg_name="epic" alg_version="3.2.0-2019-06-06" category="live" event_id="43650" version="1"/>
    <contributor alg_instance="epic@eew-ci-prod2" alg_name="epic" alg_version="3.2.0-2019-06-06" category="live" event_id="38824" version="2"/>
    <contributor alg_instance="epic@eew-ci-prod1" alg_name="epic" alg_version="3.2.0-2019-06-06" category="live" event_id="39494" version="1"/>
    <contributor alg_instance="sa@eew-uw-prod2.ess.washington.edu" alg_name="sa" alg_version="2.3.23 2020-04-01" category="live" event_id="7174" version="4"/>
  </contributors>

  <gm_info>
    <gmcontour_pred number="3">
      <contour>
        <MMI units="">2</MMI>
        <PGA units="cm/s/s">1.3865</PGA>
        <PGV units="cm/s">0.0615</PGV>
        <polygon number="8">39.0251,-122.7722 38.9693,-122.5993 38.8347,-122.5281 38.7004,-122.6000 38.6449,-122.7722 38.7004,-122.9444 38.8347,-123.0163 38.9693,-122.9451 39.0251,-122.7722</polygon>
      </contour>
      <contour>
        <MMI units="">3</MMI>
        <PGA units="cm/s/s">6.1249</PGA>
        <PGV units="cm/s">0.2947</PGV>
        <polygon number="8">38.9099,-122.7722 38.8879,-122.7042 38.8350,-122.6761 38.7821,-122.7043 38.7601,-122.7722 38.7821,-122.8401 38.8350,-122.8683 38.8879,-122.8402 38.9099,-122.7722</polygon>
      </contour>
      <contour>
        <MMI units="">4</MMI>
        <PGA units="cm/s/s">27.0557</PGA>
        <PGV units="cm/s">1.4114</PGV>
        <polygon number="8">38.8550,-122.7722 38.8491,-122.7541 38.8350,-122.7466 38.8209,-122.7541 38.8150,-122.7722 38.8209,-122.7903 38.8350,-122.7978 38.8491,-122.7903 38.8550,-122.7722</polygon>
      </contour>
    </gmcontour_pred>
  </gm_info>

</event_message>
```

* When a message is received, backend Java code processes the message and checks for an intersection point using polygons from the contour data set. An intersection point is a place in the geography of the Los Angeles area in which at least one potential device exists.

#### Polygon intersection with the grid

* Polygons are used to get the intersection points. An example of a polygon and intersection point is:

```xml
<polygon number="8">38.8550,-122.7722 38.8491,-122.7541 38.8350,-122.7466 38.8209,-122.7541 38.8150,-122.7722 38.8209,-122.7903 38.8350,-122.7978 38.8491,-122.7903 38.8550,-122.7722</polygon>
```
![Intersection Image](http://ftp2.colworx.com/partner/LA/Screen%20Shot%202020-06-26%20at%209.07.40%20PM.png)

#### Campaign for sending push notifications

* A push campaign is created for each grid in which an event has an MMI value greater than or equal to 3 AND a magnitude greater than or equal to 4.5 at a location in the L.A. geography.

## Setting up local development environment	
Before you start, ensure you have the following installed:
*	Xcode 10 or above
*	macOS Mojave or above
*	Java SDK minimum 10
*	Android Studio 3.2 or above
*	OpenSSL library to communicate with USGS server over SSL/TLS protocol
*	TomCat Java Server
*	Eclipse IDE

## Then follow these steps to set up and run your server/application:

### Clone the code repository
•	git clone https://github.com/CityOfLosAngeles/ShakeAlertLA-Colworx-ATT.git	

### Steps for backend source deployment	
In order to provide a simple development user experience, you will need to emulate some of that complexity through the creation steps below.

1.	After cloning the repository, cd to this directory [Backend_Source/LaWebApi](./Backend_Source/LaWebApi).
2.	Then, create a database in PHPMyAdmin and upload the database script file from here [Backend_Source/LaWebApi](./Backend_Source/LaWebApi) to MySQL, this will create all the tables and relations between the entities.

#### 3. Install and setup Apache Tomcat server 
*	Download Apache Tomcat v7.0 from this link [tomcat.apache.org/download-70.cgi](https://tomcat.apache.org/download-70.cgi).
*	Open the source [Backend_Source/LaWebApi](./Backend_Source/LaWebApi) in the Eclipse environment.
*	Click on the Servers tab and create a new server by selecting “Tomcat v7.0 Server.”
*	Select the Apache installation directory and click Finish.
*	Once you have finished the installation, you should see Tomcat v7.0 Server at localhost [Stopped, Republish] under the Servers tab. Double click on it to verify the HTTP ports information. By default, the HTTP port is 8080.

#### 4. Setting up database connections
* Update the connection string in MySQL.java file:

```java
public class MySql {

	public Boolean InsertDevice(String DeviceID, String LatLong, String Index, String Language){

		Boolean resp = false;

		try{
			
			Connection con;
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/labackend_pro_db","admin","");

			List<Map> Device = GetDevice(DeviceID);
			//System.out.println(Device.toString());
			Statement stmt = con.createStatement();
```

#### 5. Setting up blocks path located only in Los Angeles

* Update the directory path in Block.java file [LABackend/assets/boxes_10.json](./Backend_Source/LABackend/assets/boxes_10.json)

```java
public class Blocks {

	List<Map> segments;
	
	//Colworx: This method for generates 101 blocks from boxes_10.json file and returns the polygon list. 
	public ArrayList generateBlocks() throws FileNotFoundException, IOException, ParseException {
		
		List<Map> PolygonList = new ArrayList<>();
		
		try {
			
			segments = new MySql().getSegments();
			
		} catch (Exception e) {
			// TODO: handle exception
			
			return (ArrayList) PolygonList;
		}
		
		if(segments.size() != 0) {
			
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(
					"/Users/Administrator/eclipse-workspace/LABackend/assets/boxes_10.json"));

```

#### 6. Run the server
* Now right click on Server and click Start. It should be up and running on port 8080 and you can visit the default page at http://localhost:8080/.

## Steps to Run the app source

#### 1. Load the sources
* To run the iOS project, launch the Xcode and open the source [iOS_Source](./iOS_Source)
* For Android, launch Android Studio and open the source [Android_Source](./Android_Source)

#### 2. Update API URLs
* Replace placeholder URLs with real API URLs in the AppDelegate for iOS and ConfigConstants.java for Android.

* For Android 
```java
public class ConfigConstants {

    public static final String API_BASE_URL = "";

    public static final boolean IN_DEV = false; // true = DEV and false = PRO

    public static final String HOST_URL = "http://localhost:8080/"; // Update host url get from web service

    public static final String PUSH_OPEN_RATE_URL = HOST_URL+"LaWebApi/rest/pushOpenRate";
    public static final String PUSH_OPEN_RATE_DEV_URL = HOST_URL+"LaWebApi/rest/pushOpenRateDev";

    public static final String REGISTER_URL = HOST_URL+"LaWebApi/rest/registerDevice";
    public static final String  REGISTER_DEV_URL = HOST_URL+"LaWebApi/rest/registerDeviceDev";
    public static final boolean PRINT_LOGS = false;

    public static final String LANGUAGE_SPANISH = "es";//"es-rUS";
    public static final String LANGUAGE_ENGLISH = "en";//"es-rUS";

}
```
* For iOS
```objective c
//inside registerDevice method of AppDelegate.m
NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:@"<server_address>/registerDevice"]
                                                           cachePolicy:NSURLRequestUseProtocolCachePolicy
                                                       timeoutInterval:30.0];
```

#### 3. Generate the Google Map API key
* Go to the Google Cloud Platform Console.
* Click the project drop-down and select or create the project for which you want to add an API key.
* Click the menu button and select APIs & Services > Credentials.
* On the Credentials page, click Create Credentials > API key.
* The API key created dialog displays your newly created API key.
* Click Close.

#### 4. Add the API key to your request
* Place the Google Map API key under the file AppDelegate for iOS and under google-services.json for Android

* For Android 
```json
{
  "project_info": {
    "project_number": "",
    "firebase_url": "https://shakealert-la-dev.firebaseio.com",
    "project_id": "shakealert-la-dev",
    "storage_bucket": "shakealert-la-dev.appspot.com"
  },
  "client": [
    {
      "client_info": {
        "mobilesdk_app_id": "",
        "android_client_info": {
          "package_name": ""
        }
      },
      "oauth_client": [
        {
          "client_id": "",
          "client_type": 3
        }
      ],
      "api_key": [
        {
          "current_key": ""
        }
      ],
      "services": {
        "appinvite_service": {
          "other_platform_oauth_client": [
            {
              "client_id": "",
              "client_type": 3
            }
          ]
        }
      }
    }
  ],
  "configuration_version": "1"
}
```

* For iOS 
```objective c
//in Helper.h
#define google_maps_api_key @""
```

## Messaging Gateway

This repository contains the codebase for ShakeAlertLA's messaging gateway. The gateway uses Amazon AWS to send messages from the USGS earthquake early warning ActiveMQ message gateway to APNS/GPNS. The messaging gateway provides earthquake early warning notifications from the USGS for users in Los Angeles County.

## Backend updates

The backend source code also uses AWS Pinpoint SDK to send pushes through GCM and APNS server. If you are using a different provider than AWS, de-integrate AWS Pinpoint and make sure to replace the keys in the “sendMessage” method of your push notification provider. Note that "View recent earthquakes" on the main view are populated by prior push notifications, but are limited to events that qualify for the threshold as set by the USGS for the location at which they occurred. However, if the threshold wasn't met for a location in Los Angeles, this would not have resulted in a push notification via ShakeAlertLA. They are captured on this map for clarity and completeness. It is important to note that you  sync your event thresholds with the USGS ShakeAlert decision engine thresholds at a bare minimum.

## Client side integration

### Deintegrate the shelter map layer (which is set to LA County)

You will need to identify a map layer that reflects shelter locations in your area. The one used for ShakeAlertLA links to our map layer of shelters that are activated in real time for an active emergency. Also, not that the ShakeAlertLA ayer is not active unless a real event occurs, so you will need to find a test layer for your area to validate.

### Deintegrate the historical earthquake view

An open USGS API populates historical quakes, but the attributes sent to the API are for Los Angeles specific coordinates, and only request events larger than 2.5 magnitude are shown, as the design intention is to not flood the view with a massive amount of insignificant event data.

### All other views are static and require updating local bundle assets

These views include, but are not limited to, logos, artwork, colors, and branding. You should revise any and all labels and instructions in the codebase if you plan on changing any or all of parameters that describe the current thresholds for events. These would include both magnitude and MMI if you want to raise the limits above the threshold established by USGS (not recommended)> These are manually maintained associations.

### Make A Plan is derived from a local database

You can use this as a reference for your app, but feel free to modify as needed. This information is aggregated from the City of L.A. emergency information, Earthquake Country Alliance, and USGS. Nothing in this plan is stored remotely, and is all part of the local bundle and unique to the device.


## Related Projects 
* [MyShake](https://myshake.berkeley.edu/)
* [USGS ShakeAlert](https://www.usgs.gov/natural-hazards/earthquake-hazards/shakealert)
