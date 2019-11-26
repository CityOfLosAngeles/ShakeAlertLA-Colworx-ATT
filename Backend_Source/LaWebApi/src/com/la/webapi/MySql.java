package com.la.webapi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.PreparedStatement;

public class MySql {

	/*public MySql() throws ClassNotFoundException, SQLException {

		// TODO Auto-generated constructor stub
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/labackend_pro_db","admin","");

	}*/

	public Boolean InsertDevice(String DeviceID, String LatLong, String Index, String Language){

		Boolean resp = false;

		try{
			
			Connection con;
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/labackend_pro_db","admin","");

			List<Map> Device = GetDevice(DeviceID);
			//System.out.println(Device.toString());
			Statement stmt = con.createStatement();

			if(Device.size() == 0) {

				stmt.execute("INSERT INTO `users` (`DeviceID`, `LatLong`, `CreatedAt`) VALUES ('"+DeviceID+"', '"+LatLong+"', NOW())");

				ResultSet rs = stmt.executeQuery("select UID from users where DeviceID = '"+DeviceID+"'");
				int UID = 0;
				if(rs.first()) {

					UID = rs.getInt("UID");

				}

				Language = Language + "-"+ Index;

				rs = stmt.executeQuery("select SID from segments where SegmentName = '"+Language+"'");
				int SID = 0;
				if(rs.first()) {

					SID = rs.getInt("SID");

				}

				stmt.execute("INSERT INTO `connecteduser`(`SID`, `UID`) VALUES ("+SID+", "+UID+")");
				resp = true;
				
				System.out.println("Insert New Record.");

			} else {

				stmt.execute("UPDATE `users` SET `LatLong` = '"+LatLong+"', UpdatedAt = NOW() WHERE DeviceID = '"+DeviceID+"'");

				ResultSet rs = stmt.executeQuery("select UID from users where DeviceID = '"+DeviceID+"'");
				int UID = 0;
				if(rs.first()) {

					UID = rs.getInt("UID");

				}

				Language = Language + "-"+ Index;

				rs = stmt.executeQuery("select SID from segments where SegmentName = '"+Language+"'");
				int SID = 0;
				if(rs.first()) {

					SID = rs.getInt("SID");

				}

				stmt.execute("DELETE FROM `connecteduser` WHERE `UID` = "+UID+"");
				stmt.execute("INSERT INTO `connecteduser`(`SID`, `UID`) VALUES ("+SID+", "+UID+")");
				resp = true;
				System.out.println("Update Record.");
			}

			con.close();
			return resp;

		} catch (Exception e){ 

			System.out.println(e);
			resp = false;
			return resp;

		}
	}
	
	public boolean insertSegmentID(String val1,String val2) {
		try {
			
			Connection con;
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/labackend_pro_db","admin","");
			//Statement stmt = con.createStatement();
			String query = "INSERT INTO `campaignsdetails`(`SegmentID`,`DeviceID`) VALUES (?,?)";
			//stmt.execute("INSERT INTO `campaignsdetails`(`SegmentID`,`DeviceID`) VALUES ('"+val1+"','"+val2+"')");
			PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement(query);
			preparedStmt.setString(1, val1);
			preparedStmt.setString(2, val2);
			
			//execute the preparedstatement
		    preparedStmt.execute();
		      
			//UpdateCampaign(val1);
			con.close();
			return true;
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
			return false;
		}
	}
	
	

	public static List<Map> GetDevice(String DeviceID) {

		List<Map> result = new ArrayList<>();

		try{
			Connection con;
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/labackend_pro_db","admin","");

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * from users where DeviceID = '"+DeviceID+"'");

			if(rs.first()) {

				Map<String, String> map = new HashMap<String, String>();

				map.put("UID", rs.getString("UID"));
				map.put("DeviceID", rs.getString("DeviceID"));
				map.put("LatLong", rs.getString("LatLong"));

				result.add(map);
			}

			//			while(rs.next()) {
			//
			//				System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3)+"  "+rs.getString(4)+"  "+rs.getString(5));
			//
			//			}

			return result;

		} catch (Exception e){ 

			System.out.println(e);
			return result;

		}
	}
	
	public static List<Map> GetEvents() {

		List<Map> result = new ArrayList<>();

		try{
			
			Connection con;
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/labackend_pro_db","admin","");

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT `eventID`, `eventLocation`, `CreatedAt`, `Likelihood`, `EventOriginTimeStampUnit`, `LatitudeValue`, `DepthUnit`, `DepthValue`, `EventOriginTimeStampValue`, `LongitudeValue`, `IntersectsCount`, `MagnitudeValue` FROM `events` order by CreatedAt desc LIMIT 38");

				while(rs.next()) {
	
					Map<String, Object> map = new HashMap<String, Object>();

					List<Map> Campaigns = GetCampaigns(rs.getString("eventID"));
					
					map.put("eventID", rs.getString("eventID"));
					map.put("CreatedAt", rs.getString("CreatedAt"));
					map.put("Likelihood", rs.getString("Likelihood"));
					map.put("EventOriginTimeStampUnit", rs.getString("EventOriginTimeStampUnit"));
					map.put("LatitudeValue", rs.getString("LatitudeValue"));
					map.put("DepthUnit", rs.getString("DepthUnit"));
					map.put("DepthValue", rs.getString("DepthValue"));
					map.put("EventOriginTimeStampValue", rs.getString("EventOriginTimeStampValue"));
					map.put("LongitudeValue", rs.getString("LongitudeValue"));
					map.put("IntersectsCount", rs.getString("IntersectsCount"));
					map.put("MagnitudeValue", rs.getString("MagnitudeValue"));
					map.put("Campaigns", Campaigns);
					result.add(map);
	
				}

			return result;

		} catch (Exception e){ 

			System.out.println(e);
			return result;

		}
	}
	
	public static List<Map> GetCampaigns(String EventID) {

		List<Map> result = new ArrayList<>();

		try{
			
			Connection con;
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/labackend_pro_db","admin","");

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT `PID`, `CampaignID`, `CampaignName`, `CreatedAt`, `eventID`, `time`, `Intensity`, `SuccessfulEndpointCount`, `TotalEndpointCount` FROM `campaigns` WHERE `eventID` = '"+EventID+"'");

			while(rs.next()) {
				
				Map<String, Object> map = new HashMap<String, Object>();

				map.put("PID", rs.getString("PID"));
				map.put("CampaignID", rs.getString("CampaignID"));
				map.put("CampaignName", rs.getString("CampaignName"));
				map.put("CreatedAt", rs.getString("CreatedAt"));
				map.put("eventID", rs.getString("eventID"));
				map.put("time", rs.getString("time"));
				map.put("Intensity", rs.getString("Intensity"));
				map.put("SuccessfulEndpointCount", rs.getString("SuccessfulEndpointCount"));
				map.put("TotalEndpointCount", rs.getString("TotalEndpointCount"));
				
				/*String TotalOpen = GetTotalOpen(rs.getString("CampaignID"));
				if(!TotalOpen.equals("")) {
					
					double OpenRate = (Integer.parseInt(TotalOpen)/Integer.parseInt(rs.getString("TotalEndpointCount")))*100;
					map.put("OpenRate", String.valueOf(OpenRate)+"%");
					
				}else {
					
					map.put("OpenRate", "0");
					
				}*/
				
				result.add(map);

			}

			return result;

		} catch (Exception e){ 

			System.out.println(e);
			return result;

		}
	}
	
	
	private static String GetTotalOpen(String SegmentID) {

		try{
			
			String OpenRate = "";
			Connection con;
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/labackend_pro_db","admin","");

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select count(*) as TotalOpen from (SELECT * FROM `campaignsdetails` WHERE `SegmentID` = '"+SegmentID+"' GROUP  BY DeviceID) as TotalOpen");

			while(rs.first()) {
				
				OpenRate = rs.getString("TotalOpen");
				
			}
			
			con.close();
			
			return OpenRate;

		} catch (Exception e){ 

			System.out.println(e);
			return "";

		}
	}

}
