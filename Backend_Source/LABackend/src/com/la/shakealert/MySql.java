package com.la.shakealert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySql {

	static Connection con;

	public MySql() throws ClassNotFoundException, SQLException {

		// TODO Auto-generated constructor stub
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/labackend_pro_db","admin","");

	}

	public Boolean insertSegment(String segmentName, String segmentId) {

		Boolean resp = false;

		try{

			//here sonoo is the database name, root is the username and root is the password
			Statement stmt = con.createStatement();
			//ResultSet rs = stmt.executeQuery("select * from appusers");
			stmt.execute("INSERT INTO `segments` (`SegmentID`, `SegmentName`, `CreatedAt`) VALUES ('"+segmentId+"', '"+segmentName+"', NOW())");
			resp = true;

			//System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));

			//			while(rs.next()) {
			//				
			//				System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3)+"  "+rs.getString(4)+"  "+rs.getString(5));
			//				
			//			}
			con.close();
			return resp;

		} catch (Exception e){ 

			System.out.println(e);
			resp = false;
			return resp;

		}

	}

	public void insertCampaign(HashMap<String, Object> hashMap) {

		try{
			
			System.out.println("hashMap: "+hashMap);
			
			//here sonoo is the database name, root is the username and root is the password
			Statement stmt = con.createStatement();
			stmt.execute("INSERT INTO `campaigns` (`CampaignID`, `CampaignName`, `CreatedAt`, `eventID`, `time`, `Intensity`,`SegmentID`) VALUES ('"+hashMap.get("CampaignID").toString()+"', '"+hashMap.get("CampaignName").toString()+"', NOW(), '"+hashMap.get("eventID").toString()+"', '"+hashMap.get("time").toString()+"', '"+hashMap.get("Intensity").toString()+"','"+hashMap.get("SegmentID").toString()+"')");
			con.close();

		} catch (Exception e){ 

			System.out.println(e);

		}

	}
	
	public void updateCampaign(String val1, String val2, String val3) {
		try {
			
			Statement stmt = con.createStatement();
			stmt.execute("UPDATE `campaigns` SET SuccessfulEndpointCount = '"+val2+"', TotalEndpointCount = '"+val3+"' WHERE `CampaignID` = '"+val1+"'");
			con.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
	}
	
	public void insertEvent(HashMap<String, Object> hashMap) {

		try{
			
			//System.out.println("hashMap: "+hashMap);

			//here sonoo is the database name, root is the username and root is the password
			Statement stmt = con.createStatement();
			stmt.execute("INSERT INTO `events` (`eventID`, `CreatedAt`, `Likelihood`, `EventOriginTimeStampUnit`, `LatitudeValue`, `DepthUnit`, `DepthValue`, `EventOriginTimeStampValue`, `LongitudeValue`, `IntersectsCount`, `MagnitudeValue`) VALUES ('"+hashMap.get("eventID").toString()+"',NOW(),'"+hashMap.get("Likelihood").toString()+"','"+hashMap.get("EventOriginTimeStampUnit").toString()+"','"+hashMap.get("LatitudeValue").toString()+"','"+hashMap.get("DepthUnit").toString()+"','"+hashMap.get("DepthValue").toString()+"','"+hashMap.get("EventOriginTimeStampValue").toString()+"','"+hashMap.get("LongitudeValue").toString()+"','"+hashMap.get("IntersectsCount").toString()+"', '"+hashMap.get("MagnitudeValue").toString()+"')");
			con.close();

		} catch (Exception e){ 

			System.out.println(e);

		}

	}


	public String getSegment(String SegmentID) {

		String resp = "";

		try{

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select segments.SegmentID from segments,connecteduser where segments.SegmentID = '"+SegmentID+"' and connecteduser.SID = segments.SID LIMIT 1");

			if(rs.first()) {

				resp = rs.getString("SegmentID");

			}

			con.close();
			return resp;

		} catch (Exception e){ 

			System.out.println(e);
			return resp;

		}

	}

	public List<Map> getSegments() {

		List<Map> result = new ArrayList<>();

		try{

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT `SID`,`SegmentID`,`SegmentName`,`CreatedAt` FROM `segments`");

			while(rs.next()) {

				Map<String, String> map = new HashMap<String, String>();

				map.put("SID", rs.getString("SID"));
				map.put("SegmentID", rs.getString("SegmentID"));
				map.put("SegmentName", rs.getString("SegmentName"));
				result.add(map);

			}

			con.close();
			return result;

		} catch (Exception e){ 

			System.out.println(e);
			return result;

		}

	}
	
	
	public void insertEndpoint(String Endpoint,String Block,String Type) {

		try{

			//here sonoo is the database name, root is the username and root is the password
			Statement stmt = con.createStatement();
			stmt.execute("INSERT INTO `createendpoints`(`Endpoint`, `Block`, `Type`, `CreatedAt`) VALUES ('"+Endpoint+"','"+Block+"','"+Type+"',NOW())");
			con.close();

		} catch (Exception e){ 

			System.out.println(e);

		}

	}

}
