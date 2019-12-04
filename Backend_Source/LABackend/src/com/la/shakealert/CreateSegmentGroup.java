package com.la.shakealert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.glue.model.Segment;
import com.amazonaws.services.pinpoint.AmazonPinpoint;
import com.amazonaws.services.pinpoint.AmazonPinpointClientBuilder;
import com.amazonaws.services.pinpoint.model.AttributeDimension;
import com.amazonaws.services.pinpoint.model.AttributeType;
import com.amazonaws.services.pinpoint.model.CreateSegmentRequest;
import com.amazonaws.services.pinpoint.model.CreateSegmentResult;
import com.amazonaws.services.pinpoint.model.RecencyDimension;
import com.amazonaws.services.pinpoint.model.RecencyType;
import com.amazonaws.services.pinpoint.model.SegmentBehaviors;
import com.amazonaws.services.pinpoint.model.SegmentDemographics;
import com.amazonaws.services.pinpoint.model.SegmentDimensions;
import com.amazonaws.services.pinpoint.model.SegmentGroup;
import com.amazonaws.services.pinpoint.model.SegmentGroupList;
import com.amazonaws.services.pinpoint.model.SegmentLocation;
import com.amazonaws.services.pinpoint.model.SegmentReference;
import com.amazonaws.services.pinpoint.model.SegmentResponse;
import com.amazonaws.services.pinpoint.model.Type;
import com.amazonaws.services.pinpoint.model.WriteSegmentRequest;

public class CreateSegmentGroup {

	String appId = "3c3b37f3f20a4abfb59cfd9269e9205d";
	
	AmazonPinpoint client = AmazonPinpointClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

	//Colworx: This method for create one segment from segments list for separate campaign before send the notification to devices. EN
	public void createSegmentGroupAndLaunchCampaign_EN(ArrayList<SegmentReference> sourceSegments, String Intensity, HashMap<String, Object> hashMap) {

		//ArrayList<SegmentGroup> SegmentGroups = new ArrayList<SegmentGroup>();

		try {

			/*System.out.println("sourceSegments:" +sourceSegments.toString());
			System.out.println("Intensity:" +Intensity.toString());
			System.out.println("hashMap:" +hashMap.toString());*/

			Map<String, AttributeDimension> segmentAttributes = new HashMap<>();
			//segmentAttributes.put(keyName, new AttributeDimension().withAttributeType(AttributeType.INCLUSIVE).withValues(keyValue));

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

			//ArrayList<SegmentReference> sourceSegments = new ArrayList<SegmentReference>();

			//sourceSegments.add(new SegmentReference().withId("4a9d2612558641fabab20ed799cec9cf"));
			//sourceSegments.add(new SegmentReference().withId("9d4d2aa9703e44aaa117c7d097393876"));

			/*ArrayList<SegmentReference> sourceSegmentsList = new ArrayList<SegmentReference>();

			int count = 1;
			int RefCount = sourceSegments.size();

			for (SegmentReference segmentReference : sourceSegments) {

				if(RefCount < 5 && count != 5) {

					SegmentGroup segmentGroup = new SegmentGroup();
					segmentGroup.withSourceSegments(sourceSegmentsList);
					segmentGroup.withType("ANY");
					segmentGroup.withSourceType("ANY");

					SegmentGroups.add(segmentGroup);
					sourceSegmentsList = new ArrayList<SegmentReference>();
					count = 1;

				}else if(count == 5) {

					SegmentGroup segmentGroup = new SegmentGroup();
					segmentGroup.withSourceSegments(sourceSegmentsList);
					segmentGroup.withType("ANY");
					segmentGroup.withSourceType("ANY");

					SegmentGroups.add(segmentGroup);

					sourceSegmentsList = new ArrayList<SegmentReference>();
					count = 1;

				}

				sourceSegmentsList.add(segmentReference);
				count++;
				RefCount--;

			}*/


			SegmentGroup segmentGroup = new SegmentGroup();
			segmentGroup.withSourceSegments(sourceSegments);
			segmentGroup.withType("ANY");
			segmentGroup.withSourceType("ANY");

			SegmentGroupList segmentGroupsList = new SegmentGroupList();
			segmentGroupsList.withInclude("ANY");
			segmentGroupsList.withGroups(segmentGroup);

			WriteSegmentRequest writeSegmentRequest = new WriteSegmentRequest()
					.withName("Segment-" +Intensity)
					.withDimensions(dimensions)
					.withSegmentGroups(segmentGroupsList);

			CreateSegmentRequest createSegmentRequest = new CreateSegmentRequest()
					.withApplicationId(appId).withWriteSegmentRequest(writeSegmentRequest);

			CreateSegmentResult createSegmentResult = client.createSegment(createSegmentRequest);

			String SegmentID = createSegmentResult.getSegmentResponse().getId();

			System.out.println("Segment ID: " + SegmentID);

			//return createSegmentResult.getSegmentResponse();

			new CreateCampaign().createCampaign_EN(hashMap, SegmentID);

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

	}

	//Colworx: This method for create one segment from segments list for separate campaign before send the notification to devices. ES
	public void createSegmentGroupAndLaunchCampaign_ES(ArrayList<SegmentReference> sourceSegments, String Intensity, HashMap<String, Object> hashMap) {

		//ArrayList<SegmentGroup> SegmentGroups = new ArrayList<SegmentGroup>();

		try {

			Map<String, AttributeDimension> segmentAttributes = new HashMap<>();

			SegmentBehaviors segmentBehaviors = new SegmentBehaviors();
			SegmentDemographics segmentDemographics = new SegmentDemographics();
			SegmentLocation segmentLocation = new SegmentLocation();

			SegmentDimensions dimensions = new SegmentDimensions()
					.withAttributes(segmentAttributes)
					.withBehavior(segmentBehaviors)
					.withDemographic(segmentDemographics)
					.withLocation(segmentLocation);

			/*ArrayList<SegmentReference> sourceSegmentsList = new ArrayList<SegmentReference>();

			int count = 1;
			int RefCount = sourceSegments.size();

			for (SegmentReference segmentReference : sourceSegments) {

				if(RefCount < 5 && count != 5) {

					SegmentGroup segmentGroup = new SegmentGroup();
					segmentGroup.withSourceSegments(sourceSegmentsList);
					segmentGroup.withType("ANY");
					segmentGroup.withSourceType("ANY");

					SegmentGroups.add(segmentGroup);
					sourceSegmentsList = new ArrayList<SegmentReference>();
					count = 1;

				}else if(count == 5) {

					SegmentGroup segmentGroup = new SegmentGroup();
					segmentGroup.withSourceSegments(sourceSegmentsList);
					segmentGroup.withType("ANY");
					segmentGroup.withSourceType("ANY");

					SegmentGroups.add(segmentGroup);

					sourceSegmentsList = new ArrayList<SegmentReference>();
					count = 1;

				}

				sourceSegmentsList.add(segmentReference);
				count++;
				RefCount--;

			}*/


			SegmentGroup segmentGroup = new SegmentGroup();
			segmentGroup.withSourceSegments(sourceSegments);
			segmentGroup.withType("ANY");
			segmentGroup.withSourceType("ANY");

			SegmentGroupList segmentGroupsList = new SegmentGroupList();
			segmentGroupsList.withInclude("ANY");
			segmentGroupsList.withGroups(segmentGroup);

			WriteSegmentRequest writeSegmentRequest = new WriteSegmentRequest()
					.withName("Segment-" +Intensity)
					.withDimensions(dimensions)
					.withSegmentGroups(segmentGroupsList);

			CreateSegmentRequest createSegmentRequest = new CreateSegmentRequest()
					.withApplicationId(appId).withWriteSegmentRequest(writeSegmentRequest);

			CreateSegmentResult createSegmentResult = client.createSegment(createSegmentRequest);

			String SegmentID = createSegmentResult.getSegmentResponse().getId();

			System.out.println("Segment ID ES: " + SegmentID);

			//return createSegmentResult.getSegmentResponse();

			new CreateCampaign().createCampaign_ES(hashMap, SegmentID);

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

	}

	public void createSegment_ByIndexs_EN(ArrayList<String> withValues,String Intensity, HashMap<String, Object> hashMap) {

		try {

			Map<String, AttributeDimension> segmentAttributes = new HashMap<>();
			segmentAttributes.put("Block-EN-", new AttributeDimension().withAttributeType(AttributeType.INCLUSIVE).withValues(withValues));

			SegmentBehaviors segmentBehaviors = new SegmentBehaviors();
			SegmentDemographics segmentDemographics = new SegmentDemographics();
			SegmentLocation segmentLocation = new SegmentLocation();

			RecencyDimension recencyDimension = new RecencyDimension();
			recencyDimension.withDuration("DAY_30").withRecencyType("ACTIVE");
			recencyDimension.withRecencyType("ANY");
			recencyDimension.withRecencyType(RecencyType.ACTIVE);
			segmentBehaviors.setRecency(recencyDimension);

			SegmentDimensions dimensions = new SegmentDimensions()
					.withAttributes(segmentAttributes)
					.withBehavior(segmentBehaviors)
					.withDemographic(segmentDemographics)
					.withLocation(segmentLocation);

			WriteSegmentRequest writeSegmentRequest = new WriteSegmentRequest()
					.withName("Segment-EN-" +Intensity)
					.withDimensions(dimensions);

			CreateSegmentRequest createSegmentRequest = new CreateSegmentRequest()
					.withApplicationId(appId).withWriteSegmentRequest(writeSegmentRequest);

			CreateSegmentResult createSegmentResult = client.createSegment(createSegmentRequest);
			String SegmentID = createSegmentResult.getSegmentResponse().getId();
			System.out.println("Segment ID EN: " + SegmentID);

			new CreateCampaign().createCampaign_EN(hashMap, SegmentID);

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());

		}

	}
	
	public void createSegment_ByIndexs_ES(ArrayList<String> withValues,String Intensity, HashMap<String, Object> hashMap) {

		try {

			Map<String, AttributeDimension> segmentAttributes = new HashMap<>();
			segmentAttributes.put("Block-ES-", new AttributeDimension().withAttributeType(AttributeType.INCLUSIVE).withValues(withValues));

			SegmentBehaviors segmentBehaviors = new SegmentBehaviors();
			SegmentDemographics segmentDemographics = new SegmentDemographics();
			SegmentLocation segmentLocation = new SegmentLocation();

			RecencyDimension recencyDimension = new RecencyDimension();
			recencyDimension.withDuration("DAY_30").withRecencyType("ACTIVE");
			recencyDimension.withRecencyType("ANY");
			recencyDimension.withRecencyType(RecencyType.ACTIVE);
			segmentBehaviors.setRecency(recencyDimension);

			SegmentDimensions dimensions = new SegmentDimensions()
					.withAttributes(segmentAttributes)
					.withBehavior(segmentBehaviors)
					.withDemographic(segmentDemographics)
					.withLocation(segmentLocation);

			WriteSegmentRequest writeSegmentRequest = new WriteSegmentRequest()
					.withName("Segment-ES-" +Intensity)
					.withDimensions(dimensions);

			CreateSegmentRequest createSegmentRequest = new CreateSegmentRequest()
					.withApplicationId(appId).withWriteSegmentRequest(writeSegmentRequest);

			CreateSegmentResult createSegmentResult = client.createSegment(createSegmentRequest);
			String SegmentID = createSegmentResult.getSegmentResponse().getId();
			System.out.println("Segment ID ES: " + SegmentID);

			new CreateCampaign().createCampaign_ES(hashMap, SegmentID);

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());

		}

	}

}
