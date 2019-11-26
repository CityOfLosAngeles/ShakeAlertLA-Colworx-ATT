package com.la.webapi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.google.gson.Gson;

@Path("/")
public class api {

	@POST
	@Path("/registerDevice")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registerDevice(String data) { 

		String DeviceID = "";
		String LatLong = "";
		String Language = "";

		try {

			JSONObject obj = new JSONObject(data);
			DeviceID =  obj.get("DeviceID").toString();
			LatLong =  obj.get("LatLong").toString();
			Language =  obj.get("Language").toString();

			if(DeviceID.trim().equals("") || LatLong.trim().equals("") || Language.trim().equals("")) {

				return Response.status(400).entity("{\"code\":400,\"msg\":\"DeviceID, LatLong and Language values should be must.\"}").build();

			} else {

				//For Generate Blocks
				List<Map> Blocks = new Blocks().generateBlocks();
				//System.out.println("Total Blocks: "+Blocks.size());

				//For Check Contain in Block and get Block index
				String index = new Blocks().checkGeometryContain(Blocks, LatLong);
				//System.out.println("Block: "+index);
				
				if((index.equals("0")) || (index.equals("101"))) {
					
					System.out.println("Pro: {\"code\":200,\"msg\":101}");
					return Response.status(200).entity("{\"code\":200,\"msg\":101}").build();
					
				}else {
					
					//For insert and connect device
					//new MySql().InsertDevice(DeviceID, LatLong, index, Language);
					System.out.println("Pro: {\"code\":200,\"msg\":"+index+"}");
					return Response.status(200).entity("{\"code\":200,\"msg\":"+index+"}").build();
					
				}

			}

		} catch (Exception e) {
			// TODO: handle exception

			return Response.status(400).entity("{\"code\":400,\"msg\":\""+e.getMessage().toString()+"\"}").build();
			
		}

	}
	
	
	@POST
	@Path("/registerDeviceDev")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registerDeviceDev(String data) { 

		String DeviceID = "";
		String LatLong = "";
		String Language = "";

		try {

			JSONObject obj = new JSONObject(data);
			DeviceID =  obj.get("DeviceID").toString();
			LatLong =  obj.get("LatLong").toString();
			Language =  obj.get("Language").toString();

			if(DeviceID.trim().equals("") || LatLong.trim().equals("") || Language.trim().equals("")) {

				return Response.status(400).entity("{\"code\":400,\"msg\":\"DeviceID, LatLong and Language values should be must.\"}").build();

			} else {

				//For Generate Blocks
				List<Map> Blocks = new Blocks().generateBlocks();
				//System.out.println("Total Blocks: "+Blocks.size());

				//For Check Contain in Block and get Block index
				String index = new Blocks().checkGeometryContain(Blocks, LatLong);
				//System.out.println("Block: "+index);
				
				if((index.equals("0")) || (index.equals("101"))) {
					
					System.out.println("Dev: {\"code\":200,\"msg\":101}");
					return Response.status(200).entity("{\"code\":200,\"msg\":101}").build();
					
				}else {
					
					//For insert and connect device
					new MySqlDev().InsertDevice(DeviceID, LatLong, index, Language);
					System.out.println("Dev: {\"code\":200,\"msg\":"+index+"}");
					return Response.status(200).entity("{\"code\":200,\"msg\":"+index+"}").build();
					
				}

			}

		} catch (Exception e) {
			// TODO: handle exception

			return Response.status(400).entity("{\"code\":400,\"msg\":\""+e.getMessage().toString()+"\"}").build();
			
		}

	}
	
	
	@POST
	@Path("/pushOpenRate")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response pushOpenRate(String data) { 

		String SegmentID = "";
		String DeviceID = "";

		try {

			JSONObject obj = new JSONObject(data);
			SegmentID =  obj.get("SegmentID").toString();
			DeviceID =  obj.get("DeviceID").toString();

			if(SegmentID.trim().equals("") || DeviceID.trim().equals("")) {

				return Response.status(400).entity("{\"code\":400,\"msg\":\"SegmentID, DeviceID values should be must.\"}").build();

			} else {

				//For insert 
				if(new MySql().insertSegmentID(SegmentID,DeviceID)) {
				
					return Response.status(200).entity("{\"code\":200,\"msg\":\"Success\"}").build();
					
				}else {
					
					return Response.status(400).entity("{\"code\":400,\"msg\":\"Error\"}").build();
					
				}
				
			}

		} catch (Exception e) {
			// TODO: handle exception

			return Response.status(400).entity("{\"code\":400,\"msg\":\""+e.getMessage().toString()+"\"}").build();
			
		}

	}
	
	@POST
	@Path("/pushOpenRateDev")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response pushOpenRateDev(String data) { 

		String SegmentID = "";
		String DeviceID = "";

		try {

			JSONObject obj = new JSONObject(data);
			SegmentID =  obj.get("SegmentID").toString();
			DeviceID =  obj.get("DeviceID").toString();

			if(SegmentID.trim().equals("") || DeviceID.trim().equals("")) {

				return Response.status(400).entity("{\"code\":400,\"msg\":\"SegmentID, DeviceID values should be must.\"}").build();

			} else {

				//For insert 
				if(new MySqlDev().insertSegmentID(SegmentID,DeviceID)) {
					
					//System.out.println("{\"code\":200,\"msg\":\"Success\"}");
				
					return Response.status(200).entity("{\"code\":200,\"msg\":\"Success\"}").build();
					
				}else {
					
					return Response.status(400).entity("{\"code\":400,\"msg\":\"Error\"}").build();
					
				}
				
			}

		} catch (Exception e) {
			// TODO: handle exception

			return Response.status(400).entity("{\"code\":400,\"msg\":\""+e.getMessage().toString()+"\"}").build();
			
		}

	}
	
	
	@GET
	@Path("/getEvents")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getEvents() { 

		try {
			
			Gson gson = new Gson(); 
			String jsonBody = gson.toJson(new MySql().GetEvents()); 
			return Response.status(200).entity("{\"code\":200,\"msg\":"+jsonBody+"}").build();

		} catch (Exception e) {
			// TODO: handle exception

			return Response.status(400).entity("{\"code\":400,\"msg\":\""+e.getMessage().toString()+"\"}").build();
			
		}

	}
	
	
	@GET
	@Path("/getEventsDev")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getEventsDev() { 

		try {
			
			Gson gson = new Gson(); 
			String jsonBody = gson.toJson(new MySqlDev().GetEvents()); 
			return Response.status(200).entity("{\"code\":200,\"msg\":"+jsonBody+"}").build();

		} catch (Exception e) {
			// TODO: handle exception

			return Response.status(400).entity("{\"code\":400,\"msg\":\""+e.getMessage().toString()+"\"}").build();
			
		}

	}

}
