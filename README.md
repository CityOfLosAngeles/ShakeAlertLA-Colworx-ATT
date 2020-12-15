# ShakeAlertLA
ShakeAlertLA has been archived. You are free to fork and modify the code for your own use, but development on the open source repository has stopped and issues and pull requests will not be accepted.

_This project is provided as is under the terms of the [Apache 2.0]('LICENSE') license. The City of Los Angeles is not responsible for any ways in which the software might be used by third parties._

ShakeAlertLA is an application that was developed in partnership with the City of Los Angeles to provide early warning of strong earthquake shaking to the citizens of the Los Angeles area.

The code below was built entirely by AT&T and contractors, the City of LA has no detailed knowledge of how it works and is providing this source code for other organizations who might be interested in implementing similar solutions.

The following documentation provides an overview of the ShakeAlertLA application:


1. [ShakeAlertLA Open Source Code Review presentation](https://drive.google.com/file/d/1d12d4wDPXvyLe0XyMXjydRonMNZMD0lz/view?usp=sharing)
2. [Recording of the ShakeAlertLA Open Source Code Review presentation](https://drive.google.com/file/d/1RzGAMU7kazqrx1zKyk-VfHr3SRnJUzs5/view?usp=sharing) with the City of L.A., AT&T and other municipalities about the code 
3. [Colworx ShakeAlertLA Source Code guide](https://drive.google.com/file/d/1kZMKiicvBHyfzpc-3w9coCvlFp1ZKPnn/view?usp=sharing) provided by the developer

Continue to read this README file for further guidance on how to modify this code for your own development.

## About ShakealertLA	
ShakeAlertLA alerts you that an earthquake has been detected and that you may soon feel shaking. You can also use this app to prepare for an earthquake, get details on recent earthquakes, and find help after an earthquake. This app is brought to you by Mayor Eric Garcetti and the City of Los Angeles, and built on the ShakeAlert system developed by the U.S. Geological Survey.

## Interface design choices

The designs of the app are inspired with Google's Material design. The UI and UX is user-friendly and easy to understand. For Accessibility, The colors, contrast, shadows, and layout are designed allowing users of diverse abilities to navigate, understand, and use the UI.


* For iOS, Apple Human Interface guideline (AHI) is used with iOS native controls and elements. For Android, Material UI have been used
* For Earthquake Alerts and Warnings, Red color is used.
* Garcetti Mayor Blue color is used through-out the app.
* Each section of the app has its own color representation, examples are follows
  * History Earthquakes - Purple
  * Earthquakes Alert Map – Red
  * Recovery – Green
  * Prepare and Plan – Teal green
* Setup wizards helps guide the user with a quick walk through of the app

## Data storage

* For Data storage, ShakeAlertLA uses MySQL database on the backend
* In the database, only grid numbers, events, anonymous device ID and campaign IDs are stored. There is no actual personal information that can be traced to any individual.
* The data at rest and transmission is secured and encrypted
* For Example- DeviceID: 6191fe1721ab8210, Grid: EN-54, Location: 34.912, -118.982 Language: EN
* There is no hard cache or offline mode 

## Server-side logic and Push Notifications

### 1. Web Service to Register the Devices into the Grid (Blocks)	
* An API is used to POST anonymous data with location co-ordinates to the server.
* When a user launches the app, it passes the Device Token and location coordinates to this web service for device registration
* This Endpoint then verify the coordinates and returns the Grid number to the client side app (the app continuously check for significant change in user location to send back the updated coordinates to the server)
* The data at rest and transmission is secured and encrypted 

### 2. When ShakeAlert Sends an Alert

#### Obtain the USGS Active MQ Listener
You can request to obtain an account from USGS to receive alerts from the USGS. Details are here https://www.shakealert.org/implementation/partners/ 
* From the receiver code you can send the message payload to this class as shown below for grid processing. [CityofLA/Backend_Source](https://github.com/Colworx/CityofLA/tree/master/Backend_Source/LABackend)

```java
public class MainClass {	

	@SuppressWarnings("unchecked")
	public static void runCode(List<Map> Blocks, 
			Document doc,
			HashMap<String, Object> hashMap) throws FileNotFoundException, IOException, ParseException {
```

#### Active MQ listener receives a new Event

* When an event is received at MQ listener, the backend then checks the MAGNITUDE and MMI values and only read the polygon values from the contour where (Mag >= 4.5 && MMI>= 3.0)

* Below is an example of Event received from USGS.
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

* Backend Java code process the message and checks for intersection using polygons from contour data set.

#### Polygon Intersection with the Grid

* Polygons are used to get the Intersection points

* Polygon and Intersection Example
```xml
<polygon number="8">38.8550,-122.7722 38.8491,-122.7541 38.8350,-122.7466 38.8209,-122.7541 38.8150,-122.7722 38.8209,-122.7903 38.8350,-122.7978 38.8491,-122.7903 38.8550,-122.7722</polygon>
```
![Intersection Image](http://ftp2.colworx.com/partner/LA/Screen%20Shot%202020-06-26%20at%209.07.40%20PM.png)

#### Campaign for sending Push Notification

* A push campaign is created for each MMI grids having value greater than or equal to 3.

## Setting up local development environment	
Before you start, ensure you have the following installed:
*	Xcode 10 or above
*	macOS Mojave or above
*	Java SDK minimum 10
*	Android Studio 3.2 or above
*	OpenSSL library to communicate with USGS server over SSL/TLS protocol
*	TomCat Java Server
*	Eclipse IDE

## Then follow these steps to set up and run your server/application 

### Clone the code repository
•	git clone https://github.com/Colworx/CityofLA.git	

### Steps for Backend source deployment	
In order to provide a simple development user experience, you will need to emulate some of that complexity through the creation steps below.

1.	After cloning the repository, cd to this directory [Backend_Source/LaWebApi](https://github.com/Colworx/CityofLA/tree/master/Backend_Source/LaWebApi)
2.	Then, Create a database in PHPMyAdmin and upload the database script file from here [CityofLA/Database/LaWebApi](https://github.com/Colworx/CityofLA/tree/master/Database) to MySQL, this will create all the tables and relations between the entities

#### 3. Install and setup Apache Tomcat server 
*	Download Apache Tomcat v7.0 from this link https://tomcat.apache.org/download-70.cgi 
*	Open the source [/LaWebApi](https://github.com/Colworx/CityofLA/tree/master/Backend_Source/LaWebApi) in Eclipse Environment
*	Click on Servers Tab And Create a new server by selecting “Tomcat v7.0 Server”
*	Select Apache installation Directory and click Finish
*	Once you have finished the installation, you should see Tomcat v7.0 Server at localhost [Stopped, Republish] under Servers tab. Double click on it verify HTTP ports information. By default HTTP port is 8080.

#### 4. Setting up database connections
* Update the connection string in MySQL.java file

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

#### 5. Setting up Blocks path located only in LA	
* Update the directory path in Block.java file [LABackend/assets/boxes_10.json](https://github.com/cityoflosangeles/ShakeAlertLA-Colworx/tree/master/Backend_Source/LABackend/assets/boxes_10.json)

```java
public class Blocks {

	List<Map> segments;
	
	//Colworx: This method for generates 101 blocks from boxes_10.json file and return polygon list. 
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
* Now right click on Server and click Start, It should be up and running on port 8080 and you could visit default page using URL: http://localhost:8080/

## Steps to Run the app source

#### 1. Load the Sources
* To run the iOS project, Launch the Xcode and open the source [CityofLA/iOS_Source](https://github.com/CityOfLosAngeles/ShakeAlertLA-Colworx/tree/master/iOS_Source)
* For Android,  Launch Android studio and open the source [CityofLA/Android_Source](https://github.com/CityOfLosAngeles/ShakeAlertLA-Colworx/tree/master/Android_Source)

#### 2. Update API URLs
* Replace placeholder URLs with real API URLs in the AppDelegate for iOS and ConfigConstants.java for Android
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

#### 3. Generate Google Map API Key
* Go to the Google Cloud Platform Console
* Click the project drop-down and select or create the project for which you want to add an API key
* Click the menu button and select APIs & Services > Credentials
* On the Credentials page, click Create credentials > API key
* The API key created dialog displays your newly created API key
* Click Close

#### 4. Add the API key to your request
* Place the Google Map API key under the file AppDelegate for iOS and google-services.json for Android

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

This repository contains the codebase for ShakeAlertLA's messaging gateway. The gateway uses Amazon AWS to send messages from the USGS EEW ActiveMQ message gateway to APNS/GPNS. The Messaging gateway provides Earthquake Early Warning notifications from the USGS for users inside LA County.
For code issues, please open a github issue. For other issues, email early-earthquake-warning@lacity.org.


## Related Projects 
* [MyShake](https://myshake.berkeley.edu/)
* [USGS ShakeAlert](https://www.usgs.gov/natural-hazards/earthquake-hazards/shakealert)
