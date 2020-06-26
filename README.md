### About ShakealertLA	
ShakeAlertLA alerts you that an earthquake has been detected and that you may soon feel shaking. You can also use this app to prepare for an earthquake, get details on recent earthquakes, and find help after an earthquake. This app is brought to you by Mayor Eric Garcetti and the City of Los Angeles, and built on the ShakeAlert system developed by the U.S. Geological Survey.

### Interface design choices

While building the UI of the app we consider the WDA (4.5) Accessibility when choosing app colors and elements, overall the UI of the app is user friendly
* For iOS, we consider the APPLE Human Interface guidelines and used iOS native controllers. And for Android, Material UI have been used
* For Earthquake Alerts and Warnings, app using RED color banners 
* We have used blue color branding for Garcetti Mayor logos 
* Each section of the app has its own color representation, examples are follows
  * History Earthquakes - Purple
  * Earthquakes Alert Map – Red
  * Recovery – Green
  * Prepare and Plan – Tilled green
* Setup wizards helps guide the users and a quick walk through the app


### Setting up local development environment	
Before you start, ensure you have the following installed:
*	Xcode 10 or above
*	macOS Mojave or above
*	Java SDK minimum 10
*	Android Studio 3.2 or above
*	OpenSSL library to communicate with USGS server over SSL/TLS protocol
*	TomCat Java Server
*	Eclipse IDE

### Then follow these steps to set up and run your server/application 

### Clone the code repository
•	git clone https://github.com/Colworx/CityofLA.git	

### Steps for Backend source deployment	
In order to provide a simple development user experience, you will need to emulate some of that complexity through the creation steps below.

1.	After cloning the repository, cd to this directory [Backend_Source/LaWebApi](https://github.com/Colworx/CityofLA/Backend_Source/LaWebApi)
2.	Then, Create a database in PHPMyAdmin and upload the database script file from here [CityofLA/Database/LaWebApi](https://github.com/Colworx/CityofLA/Database) to MySQL, this will create all the tables and relations between the entities

#### 3. Install and setup Apache Tomcat server 
*	Download Apache Tomcat v7.0 from this link https://tomcat.apache.org/download-70.cgi 
*	Open the source [/LaWebApi](https://github.com/Colworx/CityofLA/Backend_Source/LaWebApi) in Eclipse Environment
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
* Update the directory path in Block.java file [LABackend/assets/boxes_10.json](https://github.com/Colworx/CityofLA/Backend_Source/LABackend/assets/boxes_10.json)

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

### Steps to Run the app source

#### 1. Load the Sources
* To run the iOS project, Launch the Xcode and open the source [CityofLA/iOS_Source](https://github.com/Colworx/CityofLA/iOS_Source)
* For Android,  Launch Android studio and open the source [CityofLA/Android_Source](https://github.com/Colworx/CityofLA/Android_Source)

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
public ConfigConstants {


}
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
```json
{
need code
}
```

#### 5. AWS Pinpoint Configuration
* Setup AWS Pinpoint on AWS console and generate awsconfiguration.json file. This file should be placed in the root folder of the application source

For iOS and Android
```json
{
  "UserAgent": "MobileHub/1.0",
  "Version": "1.0",
  "CredentialsProvider": {

    "CognitoIdentity": {
      "Default": {
        "PoolId": "",
        "Region": "us-east-1"
      }
    }
  },
  "IdentityManager": {
    "Default": {}
  },
  "PinpointAnalytics": {
    "Default": {
      "AppId": "",
      "Region": "us-east-1"
    }
  },
  "PinpointTargeting": {
    "Default": {
      "Region": "us-east-1"
    }
  }
}
```

