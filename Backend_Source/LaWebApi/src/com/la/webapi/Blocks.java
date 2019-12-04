package com.la.webapi;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class Blocks {

	public ArrayList generateBlocks() throws FileNotFoundException, IOException, ParseException {

		JSONParser parser = new JSONParser();
		JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(
				"/Users/Administrator/eclipse-workspace/LABackend/assets/boxes_10.json"));

		JSONArray features = (JSONArray)jsonObject.get("features");
		List<Map> PolygonList = new ArrayList<>();
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
			PolygonList.add(map);
		}

		return (ArrayList) PolygonList;
	}

	public String checkGeometryContain(List<Map> PolygonList, String LatLong) {

		String index = "0";
		String[] latLong = LatLong.split(",");

		for (Map PolygonItem : PolygonList) {

			Polygon polygonA = (Polygon)PolygonItem.get("Polygon");
			index = PolygonItem.get("index").toString();
			GeometryFactory gf = new GeometryFactory();
			
			double Lat = Double.parseDouble(latLong[0].trim());
			double Long = Double.parseDouble(latLong[1].trim());
			Point geom = gf.createPoint(new Coordinate(Lat,Long));
			
			if(polygonA.contains(geom)) {
				
				return index;

			}
		}

		return index;

	}
}
