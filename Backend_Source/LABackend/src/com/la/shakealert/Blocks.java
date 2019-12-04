package com.la.shakealert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

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

			JSONArray features = (JSONArray)jsonObject.get("features");
			GeometryFactory fact = new GeometryFactory();

			for (Object o : features) {

				JSONObject featuresItem = (JSONObject) o;
				JSONObject geometry = (JSONObject)featuresItem.get("geometry");
				JSONObject properties = (JSONObject)featuresItem.get("properties");

				JSONArray coordinatesObj = (JSONArray)geometry.get("coordinates");
				JSONArray coordinatesItems = (JSONArray)coordinatesObj.get(0);

				Coordinate[] coo = new Coordinate[coordinatesItems.size()];
				int i = 0;

				for (Object coordinatesItem : coordinatesItems) {

					JSONArray coordinates = (JSONArray)coordinatesItem;
					double Lat = Double.parseDouble(coordinates.get(1).toString());
					double Long = Double.parseDouble(coordinates.get(0).toString());

					coo[i] = new Coordinate(Lat, Long);

					i++;

				}

				Map<String, Object> map = new HashMap<String, Object>();

				map.put("Polygon", fact.createPolygon(fact.createLinearRing(coo),null));
				map.put("index", properties.get("index"));
				map.put("Block-EN", getSegmentID_ByName(segments, "Block-EN--"+properties.get("index")+""));
				map.put("Block-ES", getSegmentID_ByName(segments, "Block-ES--"+properties.get("index")+""));
				PolygonList.add(map);
				
			}
			
		}
		
		return (ArrayList) PolygonList;
	}
	
	
	//Colworx: This method for make list of block object
	public List<List> checkGeometryIntersects(List<Map> PolygonList, List<Map>earthquakeList) {

		List<List> mainList = new ArrayList<>();
		List<String> listContain = new ArrayList<>();

		for (Map earthquakeitem : earthquakeList) {

			Polygon polygonB = (Polygon)earthquakeitem.get("Polygon");
			String MMI = earthquakeitem.get("MMI").toString();
			String[] Coordinates = (String[]) earthquakeitem.get("Coordinates");
			
			List<Map> listItems = new ArrayList<>();
			
			for (Map PolygonItem : PolygonList) {
				

				Polygon polygonA = (Polygon)PolygonItem.get("Polygon");
				String index = PolygonItem.get("index").toString();
				
				if(polygonA.intersects(polygonB)) {

					if(!listContain.contains(index)) {
						
						String BlockEN = PolygonItem.get("Block-EN").toString();
						String BlockES = PolygonItem.get("Block-ES").toString();

						listContain.add(index);
						
						Map<String, Object> item = new HashMap<String, Object>();
						
						item.put("Topic", index);
						item.put("MMI", MMI);
						item.put("Polygon", Coordinates);
						item.put("Block-EN", BlockEN);
						item.put("Block-ES", BlockES);
						
						listItems.add(item);

					}

				}

			}
			
			if(!listItems.isEmpty()) {
				
				mainList.add(listItems);
				
			}

		}

		return mainList;

	}
	
	
	//Colworx: This method checks Coordinates in the blocks and return the block index.
	public static String checkGeometryContain(List<Map> PolygonList, String LatLong) {

		String index = "0";
		String[] latLong = LatLong.split(",");
		
		for (Map PolygonItem : PolygonList) {

			Polygon polygonA = (Polygon)PolygonItem.get("Polygon");
			index = PolygonItem.get("index").toString();
			GeometryFactory gf = new GeometryFactory();
			Point geom = gf.createPoint(new Coordinate(Double.parseDouble(latLong[0]), Double.parseDouble(latLong[1])));
			
			if(polygonA.contains(geom)) {

				return index;

			}
		}

		return index;

	}

	//Colworx: Get List of contours from statis xml file 2014_LaHabra_M5.1_contour.xml
	public static List<Map> readContour() {

		List<Map> PolygonList = new ArrayList<>();	
		
		try {

			GeometryFactory fact = new GeometryFactory();

			File fXmlFile = new File("/Users/Administrator/eclipse-workspace/LABackend/assets/2014_LaHabra_M5.1_contour.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("contour");
			
			//j=9; j>=0; j--
			
			for (int temp = nList.getLength()-1; temp >= 0; temp--) {

				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {


					Element eElement = (Element) nNode;

					String MMI = eElement.getElementsByTagName("MMI").item(0).getTextContent();
					String polygon = eElement.getElementsByTagName("polygon").item(0).getTextContent();

					//System.out.println("MMI: "+MMI);
					//System.out.println("polygon: "+polygon);

					String[] LongLatObj = polygon.split(" ");
					//System.out.println(LongLatObj.length);

					Coordinate[] coo = new Coordinate[LongLatObj.length];
					int i = 0;

					for (String coordinatesItem : LongLatObj) {

						double Lat = Double.parseDouble(coordinatesItem.split(",")[0]);
						double Long = Double.parseDouble(coordinatesItem.split(",")[1]);

						coo[i] = new Coordinate(Lat, Long);
						i++;

					}

					Map<String, Object> map = new HashMap<String, Object>();

					map.put("Polygon", fact.createPolygon(fact.createLinearRing(coo),null));
					map.put("MMI", MMI);
					PolygonList.add(map);

				}

			}

			return PolygonList;

		} catch (Exception e) {

			e.printStackTrace();
			return PolygonList;

		}

	}
	
	public List<Map> readContourFromStream(Document doc) {

		List<Map> PolygonList = new ArrayList<>();	
		
		try {

			GeometryFactory fact = new GeometryFactory();

//			File fXmlFile = new File("/Users/Administrator/eclipse-workspace/LABackend/assets/2014_LaHabra_M5.1_contour.xml");
//			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//			Document doc = dBuilder.parse(fXmlFile);
			
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("contour");
			
			//j=9; j>=0; j--
			
			for (int temp = nList.getLength()-1; temp >= 0; temp--) {

				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					/* This is how to declare HashMap */
					HashMap<String, String> hmap = new HashMap<String, String>();

					Element eElement = (Element) nNode;

					String MMI = eElement.getElementsByTagName("MMI").item(0).getTextContent();
					String polygon = eElement.getElementsByTagName("polygon").item(0).getTextContent();

					//System.out.println("MMI: "+MMI);
					//System.out.println("polygon: "+polygon);

					String[] LongLatObj = polygon.split(" ");
					//System.out.println(LongLatObj.length);

					Coordinate[] coo = new Coordinate[LongLatObj.length];
					int i = 0;

					for (String coordinatesItem : LongLatObj) {

						double Lat = Double.parseDouble(coordinatesItem.split(",")[0]);
						double Long = Double.parseDouble(coordinatesItem.split(",")[1]);

						coo[i] = new Coordinate(Lat, Long);
						i++;

					}

					Map<String, Object> map = new HashMap<String, Object>();

					map.put("Polygon", fact.createPolygon(fact.createLinearRing(coo),null));
					map.put("MMI", MMI);
					map.put("Coordinates", LongLatObj);
					PolygonList.add(map);

				}

			}

			return PolygonList;

		} catch (Exception e) {

			e.printStackTrace();
			return PolygonList;

		}

	}

	//First time use for add segments of blocks
	public static void addSegmentsToAWS_Pinpoint() {
		
		for (int j = 1; j < 101; j++) {
			
			new CreateSegment().createSegment("Block-EN-",String.valueOf(j));
			new CreateSegment().createSegment("Block-ES-",String.valueOf(j));
			
		}

	}
	
	public String getSegmentID_ByName (List<Map> segments, String BlockName) {
		
		String SegmentID = "";
		
		for (Map map : segments) {
			
			if(map.get("SegmentName").equals(BlockName)) {
				
//				System.out.println(map.get("SegmentID"));
//				System.out.println(map.get("SegmentName"));
				SegmentID = (String) map.get("SegmentID");
				return SegmentID;
				
			}
		}
		
		return SegmentID;
	}

}
