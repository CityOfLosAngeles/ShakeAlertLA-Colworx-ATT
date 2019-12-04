package com.la.shakealert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.pinpoint.AmazonPinpoint;
import com.amazonaws.services.pinpoint.AmazonPinpointClientBuilder;
import com.amazonaws.services.pinpoint.model.AttributeDimension;
import com.amazonaws.services.pinpoint.model.AttributeType;
import com.amazonaws.services.pinpoint.model.CreateSegmentRequest;
import com.amazonaws.services.pinpoint.model.CreateSegmentResult;
import com.amazonaws.services.pinpoint.model.GetSegmentRequest;
import com.amazonaws.services.pinpoint.model.GetSegmentResult;
import com.amazonaws.services.pinpoint.model.RecencyDimension;
import com.amazonaws.services.pinpoint.model.RecencyType;
import com.amazonaws.services.pinpoint.model.SegmentBehaviors;
import com.amazonaws.services.pinpoint.model.SegmentDemographics;
import com.amazonaws.services.pinpoint.model.SegmentDimensions;
import com.amazonaws.services.pinpoint.model.SegmentGroup;
import com.amazonaws.services.pinpoint.model.SegmentGroupList;
import com.amazonaws.services.pinpoint.model.SegmentLocation;
import com.amazonaws.services.pinpoint.model.SegmentResponse;
import com.amazonaws.services.pinpoint.model.WriteSegmentRequest;

public class CreateSegment {
	
	String appId = "3c3b37f3f20a4abfb59cfd9269e9205d";
	
	AmazonPinpoint client = AmazonPinpointClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
	
	//Colworx: This method for generates segments of block.
	 public SegmentResponse createSegment(String keyName, String index) {
	    	
		 	//keyName: BlockIndex
		 	//keyValue: 1 to 100
		    String keyValue = index;
	    	
	        Map<String, AttributeDimension> segmentAttributes = new HashMap<>();
	        segmentAttributes.put(keyName, new AttributeDimension().withAttributeType(AttributeType.INCLUSIVE).withValues(keyValue));

	        SegmentBehaviors segmentBehaviors = new SegmentBehaviors();
	        SegmentDemographics segmentDemographics = new SegmentDemographics();
	        SegmentLocation segmentLocation = new SegmentLocation();
	        
//	        RecencyDimension recencyDimension = new RecencyDimension();
//	        recencyDimension.withDuration("DAY_30").withRecencyType("ACTIVE");
//	        segmentBehaviors.setRecency(recencyDimension);

	        SegmentDimensions dimensions = new SegmentDimensions()
	                .withAttributes(segmentAttributes)
	                .withBehavior(segmentBehaviors)
	                .withDemographic(segmentDemographics)
	                .withLocation(segmentLocation);


	        WriteSegmentRequest writeSegmentRequest = new WriteSegmentRequest()
	                .withName(keyName + keyValue).withDimensions(dimensions);

	        CreateSegmentRequest createSegmentRequest = new CreateSegmentRequest()
	                .withApplicationId(appId).withWriteSegmentRequest(writeSegmentRequest);

	        CreateSegmentResult createSegmentResult = client.createSegment(createSegmentRequest);

	        System.out.println("Segment ID: " + createSegmentResult.getSegmentResponse().getId());
	        
	        try {
	        	
	        	new MySql().insertSegment(keyName+"-"+keyValue,createSegmentResult.getSegmentResponse().getId());
				
			} catch (Exception e) {
				// TODO: handle exception
				
			}

	        return createSegmentResult.getSegmentResponse();
	        
	    }
	 
	 public SegmentResponse createSegment_ByIndexs() {
	    
		    ArrayList<String> withValues = new ArrayList<String>();
		    withValues.add("76");
		    withValues.add("77");
		    withValues.add("78");
		    withValues.add("79");
		    withValues.add("80");
		    withValues.add("91");
		    withValues.add("92");
		    withValues.add("93");
		    withValues.add("94");
		    withValues.add("95");
		    withValues.add("95");
		    withValues.add("96");
		    withValues.add("97");
		    withValues.add("98");
		    withValues.add("99");
		    withValues.add("100");
		    
	        Map<String, AttributeDimension> segmentAttributes = new HashMap<>();
	        segmentAttributes.put("Block-EN-", new AttributeDimension().withAttributeType(AttributeType.INCLUSIVE).withValues(withValues));
	        

	        SegmentBehaviors segmentBehaviors = new SegmentBehaviors();
	        SegmentDemographics segmentDemographics = new SegmentDemographics();
	        SegmentLocation segmentLocation = new SegmentLocation();
	        
	        
	        RecencyDimension recencyDimension = new RecencyDimension();
	        //recencyDimension.withDuration("DAY_30").withRecencyType("ACTIVE");
	        //recencyDimension.withRecencyType("ANY");
	        //recencyDimension.withRecencyType(RecencyType.ACTIVE);
	        segmentBehaviors.setRecency(recencyDimension);

	        SegmentDimensions dimensions = new SegmentDimensions()
	                .withAttributes(segmentAttributes)
	                .withBehavior(segmentBehaviors)
	                .withDemographic(segmentDemographics)
	                .withLocation(segmentLocation);
	        
	        /* 
	        SegmentGroup segmentGroup = new SegmentGroup();
			segmentGroup.withType("ANY");
			segmentGroup.withDimensions(dimensions);
			segmentGroup.withSourceType("ANY");
			
			SegmentGroupList segmentGroupsList = new SegmentGroupList();
			segmentGroupsList.withInclude("ANY");
			segmentGroupsList.withGroups(segmentGroup);*/
//.withSegmentGroups(segmentGroupsList)

	        WriteSegmentRequest writeSegmentRequest = new WriteSegmentRequest()
	                .withName("Group Indexs 15").withDimensions(dimensions);

	        CreateSegmentRequest createSegmentRequest = new CreateSegmentRequest()
	                .withApplicationId(appId).withWriteSegmentRequest(writeSegmentRequest);

	        CreateSegmentResult createSegmentResult = client.createSegment(createSegmentRequest);

	        System.out.println("Segment ID: " + createSegmentResult.getSegmentResponse().getId());
	        
	        /*try {
	        	
	        	new MySql().insertSegment(keyName+"-"+keyValue,createSegmentResult.getSegmentResponse().getId());
				
			} catch (Exception e) {
				// TODO: handle exception
				
			}*/

	        return createSegmentResult.getSegmentResponse();
	        
	    }

}
