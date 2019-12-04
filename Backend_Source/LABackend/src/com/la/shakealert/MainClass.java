package com.la.shakealert;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;

import com.amazonaws.services.pinpoint.model.SegmentReference;
import com.google.gson.Gson;
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class MainClass {

	/*static Timer t;

	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
		// TODO Auto-generated method stub

		//startPollingTimer();

		try {

    		runCode();

		} catch (Exception e) {
			// TODO: handle exception
		}	

	}*/

	/*public synchronized static void startPollingTimer() {

        if (t == null) {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                   //Do your work

                	System.out.println("run the code.");
                	try {

                		runCode();

					} catch (Exception e) {
						// TODO: handle exception
					}		
                }
            };

            t = new Timer();
            t.scheduleAtFixedRate(task, 0, 5000);
        }
    }*/

	@SuppressWarnings("unchecked")
	public static void runCode(List<Map> Blocks, 
			Document doc,
			HashMap<String, Object> hashMap) throws FileNotFoundException, IOException, ParseException {

		//StartTime
		long startTime = System.currentTimeMillis();
		UUID uuid = UUID.randomUUID();
	    String uuidStr = uuid.toString();
	    System.out.println("Random UUID[1]: " + uuidStr);
	    hashMap.put("eventID", uuid);
	    

		//For Generate Blocks
		//List<Map> Blocks = new Blocks().generateBlocks();
		System.out.println("Total Blocks: "+Blocks.size());

		//For Get Earth Quake
		List<Map> earthquakeList = new Blocks().readContourFromStream(doc); //new Blocks().readContour(); read contour from xml file for testing
		System.out.println("EarthQuake Contour: " +earthquakeList.size());

		//For Check Intersection
		List<List> IntersectsList = new Blocks().checkGeometryIntersects(Blocks, earthquakeList);
		System.out.println("Intersects Count: "+IntersectsList.size());
		hashMap.put("IntersectsCount", IntersectsList.size());

		 try {
		    	
		    	new MySql().insertEvent(hashMap);
				
			} catch (Exception e) {
				
				// TODO: handle exception
				System.out.println("event Insert Exception: " + e.getMessage());
				
			}
		
		int i = 0;

		ArrayList<Object> Polygons = new ArrayList<Object>();
		//ArrayList<String> Colors = new ArrayList<String>();

		int ps = 0;
		for (List list : IntersectsList) {


			Map<String, Object> item = new HashMap<String, Object>();
			item = (Map<String, Object>) list.get(ps);

			if(Double.parseDouble((String) item.get("MMI")) >= 4.0){

				Polygons.add(item.get("Polygon"));
				//Colors.add(getIntensity_Color_EN((String) item.get("MMI")));
			}

			ps++;

		}

		//Intersection List
		for (List list : IntersectsList) {

			Map<String, Object> item = new HashMap<String, Object>();
			item = (Map<String, Object>) list.get(0);

			if(Double.parseDouble((String) item.get("MMI")) >= 4.0){

				hashMap.put("MMI", item.get("MMI"));
				hashMap.put("Topic", item.get("Topic"));
				hashMap.put("Polygon", item.get("Polygon"));
				hashMap.put("Polygons", Polygons);
				hashMap.put("Colors", getColors(Polygons));

				String MMI = (String) item.get("MMI");

				System.out.println("MMI: "+item.get("MMI"));
				//System.out.println("Topic: "+item.get("Topic"));

				//ArrayList<SegmentReference> sourceSegments_EN = new ArrayList<SegmentReference>();
				//ArrayList<SegmentReference> sourceSegments_ES = new ArrayList<SegmentReference>();

				ArrayList<String> withValues_EN = new ArrayList<String>();
				ArrayList<String> withValues_ES = new ArrayList<String>();

				for (int j = 0; j < list.size(); j++) {

					Map<String, Object> item2 = new HashMap<String, Object>();
					item2 = (Map<String, Object>) list.get(j);

					//System.out.println("Block-EN: "+item2.get("Block-EN"));
					//System.out.println("Block-ES: "+item2.get("Block-ES"));

					//if(i == 2) {

					if(!item2.get("Block-EN").equals("")){

						String BlockIndex = (String) item2.get("Topic");

						/*try {

								if(!new MySql().getSegment((String) item2.get("Block-EN")).equals("")) {

									new CreateCampaign().createCampaign_EN(hashMap, (String) item2.get("Block-EN"));

								}

							} catch (Exception e) {
								// TODO: handle exception
							}*/

						//for group
						//sourceSegments_EN.add(new SegmentReference().withId((String) item2.get("Block-EN")));

						withValues_EN.add(BlockIndex);

					}

					if(!item2.get("Block-ES").equals("")){


						String BlockIndex = (String) item2.get("Topic");

						/*try {

								if(!new MySql().getSegment((String) item2.get("Block-ES")).equals("")) {

									new CreateCampaign().createCampaign_ES(hashMap, (String) item2.get("Block-ES"));

								}

							} catch (Exception e) {
								// TODO: handle exception
							}*/

						//for group
						//sourceSegments_ES.add(new SegmentReference().withId((String) item2.get("Block-ES")));

						withValues_ES.add(BlockIndex);

					}
					//}

				}

				/*(i == 2) {*/

				System.out.println("Campaign RUN");
				//				new CreateSegmentGroup().createSegmentGroupAndLaunchCampaign_EN(sourceSegments_EN, MMI, hashMap);
				//				new CreateSegmentGroup().createSegmentGroupAndLaunchCampaign_ES(sourceSegments_ES, MMI, hashMap);
				new CreateSegmentGroup().createSegment_ByIndexs_EN(withValues_EN, MMI, hashMap);
				new CreateSegmentGroup().createSegment_ByIndexs_ES(withValues_ES, MMI, hashMap);

				/*}

				i++;*/

			}

		}


		//EndTime
		long endTime = System.currentTimeMillis();
		System.out.println("That took " + (endTime - startTime) + " milliseconds");

	}

	private static ArrayList<String> getColors(ArrayList<Object> Polygons) {
		
		ArrayList<String> ColorsList = new ArrayList<String>();
		
		/*
		 *  2: 1O 1Y
			3: O Y G
			4: OO Y G
			5: OO YY G
			6: OO YY GG
			7: OOO YY GG
			8: OOO YYY GG
		 * */
		
		String[] colors = {"#FFA500","#FFFF00","#7CFC00"};
		
		if(Polygons.size() == 1) {
			
			ColorsList.add(colors[0]);
			
		}else if(Polygons.size() == 2) {
			
			ColorsList.add(colors[0]);
			ColorsList.add(colors[1]);
			
		}else if(Polygons.size() == 3) {
			
			ColorsList.add(colors[0]);
			ColorsList.add(colors[1]);
			ColorsList.add(colors[2]);
			
		}else if(Polygons.size() == 4) {
			
			ColorsList.add(colors[0]);
			ColorsList.add(colors[0]);
			ColorsList.add(colors[1]);
			ColorsList.add(colors[2]);
			
		}else if(Polygons.size() == 5) {
			
			ColorsList.add(colors[0]);
			ColorsList.add(colors[0]);
			ColorsList.add(colors[1]);
			ColorsList.add(colors[1]);
			ColorsList.add(colors[2]);
			
		}else if(Polygons.size() == 6) {
			
			ColorsList.add(colors[0]);
			ColorsList.add(colors[0]);
			ColorsList.add(colors[1]);
			ColorsList.add(colors[1]);
			ColorsList.add(colors[2]);
			ColorsList.add(colors[2]);
			
		}else if(Polygons.size() == 7) {
			
			ColorsList.add(colors[0]);
			ColorsList.add(colors[0]);
			ColorsList.add(colors[0]);
			ColorsList.add(colors[1]);
			ColorsList.add(colors[1]);
			ColorsList.add(colors[2]);
			ColorsList.add(colors[2]);
			
		}else if(Polygons.size() == 8) {
			
			ColorsList.add(colors[0]);
			ColorsList.add(colors[0]);
			ColorsList.add(colors[0]);
			ColorsList.add(colors[1]);
			ColorsList.add(colors[1]);
			ColorsList.add(colors[1]);
			ColorsList.add(colors[2]);
			ColorsList.add(colors[2]);
			
		}
		
		return ColorsList;
	}

	private static String getIntensity_Color_EN(String MMI) {

		String[] intensity = {"#dfe6fe","#7efbdf","#95f879","#f7f835","#fdca2c","#ff701e","#ec2416","#c81d11"};

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

		return "#ffffff";

	}

}
