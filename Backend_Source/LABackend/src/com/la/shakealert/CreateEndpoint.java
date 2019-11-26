package com.la.shakealert;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.pinpoint.AmazonPinpoint;
import com.amazonaws.services.pinpoint.AmazonPinpointClientBuilder;
import com.amazonaws.services.pinpoint.model.UpdateEndpointRequest;
import com.amazonaws.services.pinpoint.model.UpdateEndpointResult;
import com.amazonaws.services.pinpoint.model.EndpointDemographic;
import com.amazonaws.services.pinpoint.model.EndpointLocation;
import com.amazonaws.services.pinpoint.model.EndpointRequest;
import com.amazonaws.services.pinpoint.model.EndpointResponse;
import com.amazonaws.services.pinpoint.model.EndpointUser;
import com.amazonaws.services.pinpoint.model.GetCampaignActivitiesRequest;
import com.amazonaws.services.pinpoint.model.GetCampaignActivitiesResult;
import com.amazonaws.services.pinpoint.model.GetEndpointRequest;
import com.amazonaws.services.pinpoint.model.GetEndpointResult;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class CreateEndpoint {
	
	static String appId = "3c3b37f3f20a4abfb59cfd9269e9205d";

	public static void main(String[] args) {

		/*EndpointResponse response = createEndpoint("1","Block-EN-");

		System.out.println(response.getAddress());
		System.out.println(response.getChannelType());
		System.out.println(response.getApplicationId());
		System.out.println(response.getEndpointStatus());
		System.out.println(response.getRequestId());
		System.out.println(response.getUser());*/
		
		
		
	}

    public void createEndpoint(String Block, String BlockName) {
    	
    	AmazonPinpoint client = AmazonPinpointClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
    	
        String endpointId = UUID.randomUUID().toString();
        System.out.println("Endpoint ID: " + endpointId);
        
        try {
        	
        	new MySql().insertEndpoint(endpointId, Block, BlockName);
			
		} catch (Exception e) {
			// TODO: handle exception
			
		}

        EndpointRequest endpointRequest = createEndpointRequestData(Block,BlockName);

        UpdateEndpointRequest updateEndpointRequest = new UpdateEndpointRequest()
                .withApplicationId(appId)
                .withEndpointId(endpointId)
                .withEndpointRequest(endpointRequest);

        UpdateEndpointResult updateEndpointResponse = client.updateEndpoint(updateEndpointRequest);
        System.out.println("Update Endpoint Response: " + updateEndpointResponse.getMessageBody());

        GetEndpointRequest getEndpointRequest = new GetEndpointRequest()
                .withApplicationId(appId)
                .withEndpointId(endpointId);
        GetEndpointResult getEndpointResult = client.getEndpoint(getEndpointRequest);

        System.out.println("Got Endpoint: " + getEndpointResult.getEndpointResponse().getId());
        //return getEndpointResult.getEndpointResponse();
    }

    public EndpointRequest createEndpointRequestData(String Block, String BlockName) {

        HashMap<String, List<String>> customAttributes = new HashMap<>();
        List<String> favoriteTeams = new ArrayList<>();
        favoriteTeams.add(Block);
        customAttributes.put(BlockName, favoriteTeams);


        EndpointDemographic demographic = new EndpointDemographic()
                .withAppVersion("3.0")
                .withMake("testApple")
                .withModel("iPhone")
                .withModelVersion("7")
                .withPlatform("ios")
                .withPlatformVersion("10.1.1")
                .withTimezone("America/Los_Angeles");

        EndpointLocation location = new EndpointLocation()
                .withCity("Los Angeles")
                .withCountry("US")
                .withLatitude(34.0)
                .withLongitude(-118.2)
                .withPostalCode("90068")
                .withRegion("CA");

        Map<String,Double> metrics = new HashMap<>();
        metrics.put("health", 100.00);
        metrics.put("luck", 75.00);

        EndpointUser user = new EndpointUser()
                .withUserId(UUID.randomUUID().toString());

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        String nowAsISO = df.format(new Date());

        EndpointRequest endpointRequest = new EndpointRequest()
                .withAddress(UUID.randomUUID().toString())
                .withAttributes(customAttributes)
                .withChannelType("APNS")
                .withDemographic(demographic)
                .withEffectiveDate(nowAsISO)
                .withLocation(location)
                .withMetrics(metrics)
                .withOptOut("NONE")
                .withRequestId(UUID.randomUUID().toString())
                .withUser(user);

        return endpointRequest;
    }

}