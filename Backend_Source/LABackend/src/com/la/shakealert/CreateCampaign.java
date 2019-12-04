package com.la.shakealert;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.pinpoint.AmazonPinpoint;
import com.amazonaws.services.pinpoint.AmazonPinpointClientBuilder;
import com.amazonaws.services.pinpoint.model.Action;
import com.amazonaws.services.pinpoint.model.CampaignResponse;
import com.amazonaws.services.pinpoint.model.CreateCampaignRequest;
import com.amazonaws.services.pinpoint.model.CreateCampaignResult;
import com.amazonaws.services.pinpoint.model.DeleteCampaignRequest;
import com.amazonaws.services.pinpoint.model.DeleteCampaignResult;
import com.amazonaws.services.pinpoint.model.GetCampaignActivitiesRequest;
import com.amazonaws.services.pinpoint.model.GetCampaignActivitiesResult;
import com.amazonaws.services.pinpoint.model.GetCampaignRequest;
import com.amazonaws.services.pinpoint.model.GetCampaignResult;
import com.amazonaws.services.pinpoint.model.Message;
import com.amazonaws.services.pinpoint.model.MessageConfiguration;
import com.amazonaws.services.pinpoint.model.Schedule;
import com.amazonaws.services.pinpoint.model.WriteCampaignRequest;
import com.amazonaws.services.s3.model.GetBucketAnalyticsConfigurationRequest;
import com.google.gson.Gson;

public class CreateCampaign {

	String appId = "3c3b37f3f20a4abfb59cfd9269e9205d";

	AmazonPinpoint client = AmazonPinpointClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

	
	//Colworx: This method for send push notification to EN devices.
	public void createCampaign_EN(HashMap<String, Object> hashMap, String SegmentID) {

		try {

			String MMI = (String) hashMap.get("MMI");
			String Index = (String) hashMap.get("Topic");
			hashMap.put("SegmentID", SegmentID);

			String Intensity = getIntensity_EN(MMI);
			Boolean runCode = true;

			if((MMI.equals("1.0000")) || (MMI.equals("0.0000")) || (MMI.equals("0")) || (Intensity.equals("")) || (SegmentID.equals(""))) {

				runCode = false;

			}

			if(runCode) {

				Schedule schedule = new Schedule()
						.withStartTime("IMMEDIATE");

				//hashMap.put("Intensity", Intensity);
				//hashMap.put("Language", "EN");

				Gson gson = new Gson(); 
				String jsonBody = gson.toJson(hashMap); 

				System.out.println("jsonBody: "+jsonBody);

				//String jsonBody = "{\"MMI\":"+MMI+",\"startTime\":"+startTime+"}";

				Message defaultMessage = new Message();
				Message APNSMessage = new Message();
				Message GCMMessage = new Message();

				defaultMessage.withAction(Action.OPEN_APP);
				GCMMessage.withAction(Action.OPEN_APP);
				//APNSMessage.withAction(Action.OPEN_APP);

				String body = "";

				if(Double.parseDouble((String) hashMap.get("MMI")) >= 4.0 && Double.parseDouble((String) hashMap.get("MMI")) <= 5.0){

					defaultMessage.withBody("An earthquake is happening, you may feel weak shaking at your location!");
					GCMMessage.withBody("An earthquake is happening, you may feel weak shaking at your location!");
					//APNSMessage.withBody("An earthquake is happening, you may feel weak shaking at your location!");
					body = "An earthquake is happening, you may feel weak shaking at your location!";

				}else {

					defaultMessage.withBody("Expect "+Intensity+" shaking. Drop, cover, and hold on. Protect yourself now!");
					GCMMessage.withBody("Expect "+Intensity+" shaking. Drop, cover, and hold on. Protect yourself now!");
					//APNSMessage.withBody("Expect "+Intensity+" shaking. Drop, cover, and hold on. Protect yourself now!");
					body = "Expect "+Intensity+" shaking. Drop, cover, and hold on. Protect yourself now!";

				}

				defaultMessage.withTitle("Earthquake! Earthquake!");
				defaultMessage.withJsonBody(jsonBody);

				GCMMessage.withTitle("Earthquake! Earthquake!");
				GCMMessage.withJsonBody(jsonBody);

				//APNSMessage.withTitle("TEST: Earthquake! Earthquake!");
				//APNSMessage.withJsonBody(jsonBody);

				//System.out.println("defaultMessage.getRawContent() before : "+defaultMessage.getRawContent());

				//APNSMessage.withRawContent("{\"aps\":{\"alert\":{\"title\":\"Earthquake! Earthquake!\",\"body\":\""+body+"\"},\"sound\":\"alert.mp3\"},\"acme1\":"+jsonBody+"}");
				APNSMessage.withRawContent("{\"aps\":{\"alert\":{\"title\":\"Earthquake! Earthquake!\",\"body\":\""+body+"\"},\"sound\":{\"name\":\"alert.mp3\",\"critical\":1,\"volume\":1}},\"acme1\":"+jsonBody+"}");
				//GCMMessage.withRawContent("{ \"notification\": {\"title\":\"TEST: Earthquake! Earthquake!\",\"body\":\""+body+"\"},\"data\":\""+jsonBody+"\"}");

				System.out.println("defaultMessage.getRawContent() : "+APNSMessage.getRawContent());

				MessageConfiguration messageConfiguration = new MessageConfiguration();
				//.withDefaultMessage(defaultMessage);
				messageConfiguration.withAPNSMessage(APNSMessage);
				messageConfiguration.withGCMMessage(GCMMessage);

				WriteCampaignRequest request = new WriteCampaignRequest()
						.withDescription("Earthquake")
						.withSchedule(schedule)
						.withSegmentId(SegmentID)
						.withName("CampaignEN-"+Index)
						.withMessageConfiguration(messageConfiguration);

				//Colworx: This method is use for create campaign to send push notification on devices.
				CreateCampaignRequest createCampaignRequest = new CreateCampaignRequest()
						.withApplicationId(appId).withWriteCampaignRequest(request);

				CreateCampaignResult result = client.createCampaign(createCampaignRequest);
				String CampaignID = result.getCampaignResponse().getId();

				System.out.println("Campaign ID EN: " + CampaignID);

				hashMap.put("CampaignID", CampaignID);
				hashMap.put("CampaignName", "CampaignEN-"+Intensity+"-"+Index);
				hashMap.put("Intensity", Intensity);

				//Start Time
				long startTime = Long.parseLong(hashMap.get("startTime").toString());

				//End Time
				long endTime = System.currentTimeMillis(); 

				hashMap.put("time", (endTime - startTime));
				
				//Colworx: This method is use for add sent campaign details
				new MySql().insertCampaign(hashMap);

				try {

					//Timer
					new Timer().schedule(new TimerTask() {          
						@Override
						public void run() {
							// this code will be executed after 2 seconds   
							
							try {
								
								GetCampaignActivitiesRequest getCampaignActivitiesRequest = new GetCampaignActivitiesRequest();
								getCampaignActivitiesRequest.withApplicationId(appId);
								getCampaignActivitiesRequest.withCampaignId(CampaignID);

								GetCampaignActivitiesResult getResult = client.getCampaignActivities(getCampaignActivitiesRequest);
								System.out.println("Get Campaign Result EN: " + getResult);

								String GetCampaignID = getResult.getActivitiesResponse().getItem().get(0).getCampaignId().toString();
								String GetSuccessfulEndpointCount = getResult.getActivitiesResponse().getItem().get(0).getSuccessfulEndpointCount().toString();
								String GetTotalEndpointCount = getResult.getActivitiesResponse().getItem().get(0).getTotalEndpointCount().toString();
								new MySql().updateCampaign(GetCampaignID, GetSuccessfulEndpointCount, GetTotalEndpointCount);
								
							} catch (ClassNotFoundException | SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}, 120000);

				} catch (Exception e) {
					// TODO: handle exception
				}

			}

		} catch (Exception e) {
			// TODO: handle exception

			System.out.println("Campaign Not Send EN");
		}

	}

	//Colworx: This method for send push notification to ES devices.
	public void createCampaign_ES(HashMap<String, Object> hashMap, String SegmentID) {

		try {

			String MMI = (String) hashMap.get("MMI");
			String Index = (String) hashMap.get("Topic");
			hashMap.put("SegmentID", SegmentID);

			String Intensity = getIntensity_ES(MMI);
			Boolean runCode = true;

			if((MMI.equals("1.0000")) || (MMI.equals("0.0000")) || (MMI.equals("0")) || (Intensity.equals("")) || (SegmentID.equals(""))) {

				runCode = false;

			}

			if(runCode) {

				Schedule schedule = new Schedule()
						.withStartTime("IMMEDIATE");

				//hashMap.put("Intensity", Intensity);
				//hashMap.put("Language", "ES");

				Gson gson = new Gson(); 
				String jsonBody = gson.toJson(hashMap); 

				System.out.println("jsonBody: "+jsonBody);

				//String jsonBody = "{\"MMI\":"+MMI+",\"startTime\":"+startTime+"}";


				Message defaultMessage = new Message();
				Message APNSMessage = new Message();
				Message GCMMessage = new Message();

				defaultMessage.withAction(Action.OPEN_APP);
				GCMMessage.withAction(Action.OPEN_APP);
				//APNSMessage.withAction(Action.OPEN_APP);

				String body = "";

				if(Double.parseDouble((String) hashMap.get("MMI")) >= 4.0 && Double.parseDouble((String) hashMap.get("MMI")) <= 5.0){

					defaultMessage.withBody("Un terremoto está sucediendo, es posible que sienta una sacudida débil en su ubicación.");
					GCMMessage.withBody("Un terremoto está sucediendo, es posible que sienta una sacudida débil en su ubicación.");
					//APNSMessage.withBody("Un terremoto está sucediendo, es posible que sienta una sacudida débil en su ubicación.");
					body = "Un terremoto está sucediendo, es posible que sienta una sacudida débil en su ubicación.";
				}
				else {

					defaultMessage.withBody("está sucediendo, es posible que "+Intensity+" una sacudida débil en su ubicación.");
					GCMMessage.withBody("está sucediendo, es posible que "+Intensity+" una sacudida débil en su ubicación.");
					//APNSMessage.withBody("está sucediendo, es posible que "+Intensity+" una sacudida débil en su ubicación.");
					body = "está sucediendo, es posible que "+Intensity+" una sacudida débil en su ubicación.";
				}

				defaultMessage.withTitle("Terremoto! Terremoto!");
				defaultMessage.withJsonBody(jsonBody);

				GCMMessage.withTitle("Terremoto! Terremoto!");
				GCMMessage.withJsonBody(jsonBody);

				//APNSMessage.withTitle("TEST: Terremoto! Terremoto!");
				//APNSMessage.withJsonBody(jsonBody);

				//APNSMessage.withRawContent("{\"aps\":{\"alert\":{\"title\":\"Terremoto! Terremoto!\",\"body\":\""+body+"\"},\"sound\":\"alert.mp3\"},\"acme1\":"+jsonBody+"}");
				APNSMessage.withRawContent("{\"aps\":{\"alert\":{\"title\":\"Terremoto! Terremoto!\",\"body\":\""+body+"\"},\"sound\":{\"name\":\"alert.mp3\",\"critical\":1,\"volume\":1}},\"acme1\":"+jsonBody+"}");
				
				MessageConfiguration messageConfiguration = new MessageConfiguration()
						//.withDefaultMessage(defaultMessage);
						.withAPNSMessage(APNSMessage).withGCMMessage(GCMMessage);

				WriteCampaignRequest request = new WriteCampaignRequest()
						.withDescription("Terremoto")
						.withSchedule(schedule)
						.withSegmentId(SegmentID)
						.withName("CampaignES-"+Index)
						.withMessageConfiguration(messageConfiguration);

				CreateCampaignRequest createCampaignRequest = new CreateCampaignRequest()
						.withApplicationId(appId).withWriteCampaignRequest(request);

				CreateCampaignResult result = client.createCampaign(createCampaignRequest);
				String CampaignID = result.getCampaignResponse().getId();

				System.out.println("Campaign ID ES: " + CampaignID);

				hashMap.put("CampaignID", CampaignID);
				hashMap.put("CampaignName", "CampaignES-"+Intensity+"-"+Index);
				hashMap.put("Intensity", Intensity);

				//Start Time
				long startTime = Long.parseLong(hashMap.get("startTime").toString());

				//End Time
				long endTime = System.currentTimeMillis(); 

				hashMap.put("time", (endTime - startTime));

				new MySql().insertCampaign(hashMap);

				try {

					//Timer
					new Timer().schedule(new TimerTask() {          
						@Override
						public void run() {
							// this code will be executed after 2 seconds   

							try {
								GetCampaignActivitiesRequest getCampaignActivitiesRequest = new GetCampaignActivitiesRequest();
								getCampaignActivitiesRequest.withApplicationId(appId);
								getCampaignActivitiesRequest.withCampaignId(CampaignID);

								GetCampaignActivitiesResult getResult = client.getCampaignActivities(getCampaignActivitiesRequest);
								System.out.println("Get Campaign Result ES: " + getResult);
								String GetCampaignID = getResult.getActivitiesResponse().getItem().get(0).getCampaignId().toString();
								String GetSuccessfulEndpointCount = getResult.getActivitiesResponse().getItem().get(0).getSuccessfulEndpointCount().toString();
								String GetTotalEndpointCount = getResult.getActivitiesResponse().getItem().get(0).getTotalEndpointCount().toString();
								new MySql().updateCampaign(GetCampaignID, GetSuccessfulEndpointCount, GetTotalEndpointCount);
								
							} catch (ClassNotFoundException | SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}, 120000);

				} catch (Exception e) {
					// TODO: handle exception
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Campaign Not Send ES");

		}

	}

	//Colworx: This method for get Intensity msg in EN from MMI value.
	private static String getIntensity_EN(String MMI) {

		//		String[] intensity = {"Weak","Light","Moderate","Strong","Very Strong","Servere","Violent","Extreme"};
		String[] intensity = {"Weak","Light","Moderate to Light","Strong to Moderate","Very Strong to Strong","Servere to very Strong","Violent to Servere","Extreme to Violent"};
		if((MMI.equals("2.0000")) || (MMI.equals("3.0000"))) {

			return intensity[0];

		} else if(MMI.equals("4.0000")) {

			return intensity[1];

		}else if(MMI.equals("5.0000")) {

			return intensity[2];

		}else if(MMI.equals("6.0000")) {

			return intensity[3];

		}else if(MMI.equals("7.0000")) {

			return intensity[4];

		}else if(MMI.equals("8.0000")) {

			return intensity[5];

		}else if(MMI.equals("9.0000")) {

			return intensity[6];

		}else if(MMI.equals("10.0000")) {

			return intensity[7];
		}

		return "";

	}

	//Colworx: This method for get Intensity msg in ES from MMI value.
	private static String getIntensity_ES(String MMI) {

		String[] intensity = {"Débiles","Ligero","Moderar a Ligero","Fuerte a Moderar","Muy fuerte a Fuerte","Grave a Muy fuerte","Violento a Grave","Extremo a Violento"};

		if((MMI.equals("2.0000")) || (MMI.equals("3.0000"))) {

			return intensity[0];

		} else if(MMI.equals("4.0000")) {

			return intensity[1];

		}else if(MMI.equals("5.0000")) {

			return intensity[2];

		}else if(MMI.equals("6.0000")) {

			return intensity[3];

		}else if(MMI.equals("7.0000")) {

			return intensity[4];

		}else if(MMI.equals("8.0000")) {

			return intensity[5];

		}else if(MMI.equals("9.0000")) {

			return intensity[6];

		}else if(MMI.equals("10.0000")) {

			return intensity[7];
		}

		return "";

	}

}
