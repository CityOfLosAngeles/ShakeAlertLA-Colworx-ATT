package com.la.shakealert;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.simple.parser.ParseException;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudtrail.model.Event;
import com.amazonaws.services.cloudwatchevents.model.PutEventsRequest;
import com.amazonaws.services.cloudwatchevents.model.PutEventsRequestEntry;
import com.amazonaws.services.cloudwatchevents.model.PutEventsResult;
import com.amazonaws.services.mobile.AWSMobileClient;
import com.amazonaws.services.pinpoint.AmazonPinpoint;
import com.amazonaws.services.pinpoint.AmazonPinpointClient;
import com.amazonaws.services.pinpoint.AmazonPinpointClientBuilder;
import com.amazonaws.services.pinpoint.model.EventStream;
import com.amazonaws.services.pinpoint.model.EventsBatch;
import com.amazonaws.services.pinpoint.model.EventsRequest;
import com.amazonaws.services.pinpoint.model.PutEventStreamRequest;
import com.amazonaws.services.pinpoint.model.PutEventStreamResult;
import com.amazonaws.services.pinpoint.model.SegmentReference;


public class TestingClass {

	static Timer t;

	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {


		// TODO Auto-generated method stub

		//String LatLong = "33.939439,-118.147677";

		//For Create Segment
		//new CreateSegment().createSegment("1");

		//For Create Campaign
		//new CreateCampaign().createCampaign("8d7d42597b71445b985bb4557208f727");

		//For Insert Segment
		//System.out.println(new MySql().insertSegment("Block-1","abc"));

		//For Generate Blocks
		/*List<Map> Blocks = new Blocks().generateBlocks();
		for (Map map : Blocks) {

			System.out.println(map.toString());

		}*/

		//Dont Use Again
		//new Blocks().addSegmentsToAWS_Pinpoint();

		/*for (Map map : Blocks) {

			System.out.println(map.toString());

		}*/


		//For Get Block Index
		//System.out.println(new Blocks().checkGeometryContain(Blocks, LatLong));

		//startPollingTimer();

		//		ArrayList<SegmentReference> sourceSegments = new ArrayList<SegmentReference>();
		//		sourceSegments.add(new SegmentReference().withId("4a9d2612558641fabab20ed799cec9cf"));
		//		sourceSegments.add(new SegmentReference().withId("9d4d2aa9703e44aaa117c7d097393876"));

		//For Create Group Segment
		//new CreateSegmentGroup().createSegmentGroupAndLaunchCampaign(sourceSegments, "Week");

		//For Get Segments
		/*try {

			List<Map> segments = new MySql().getSegments();
			System.out.println(segments);

		} catch (Exception e) {

			// TODO: handle exception

		}*/

		//For create segment by index
		//new CreateSegment().createSegment_ByIndexs();


		//Create EndPoints
		/*for (int o = 0; o < 5000; o++) {

			for (int i = 1; i < 101; i++) {

				try {
					
					String block = Integer.toString(i);
					new CreateEndpoint().createEndpoint(block,"Block-EN-");
					new CreateEndpoint().createEndpoint(block,"Block-ES-");
					
					System.out.println("Block-"+block);
					
				} catch (Exception e) {
					// TODO: handle exception
					
					System.out.println(e.getMessage());
				}

			}

		}*/


	}

	/*public synchronized static void startPollingTimer() {
		if (t == null) {
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					//Do your work

					System.out.println("run the code.");
				}
			};

			t = new Timer();
			t.scheduleAtFixedRate(task, 0, 5000);
		}
	}*/

}
