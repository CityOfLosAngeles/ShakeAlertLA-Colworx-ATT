package com.la.shakealert;

/** @file EventMessageListener.java */

/* Copyright (c) 2016 California Institute of Technology.
 * All rights reserved, November 17, 2016
 * This program is distributed WITHOUT ANY WARRANTY whatsoever.
 * Do not redistribute this program without written permission.
 * Do not remove this Copyright statement from this file.  
 */

//package org.shakealert.eew.core;

//Java Imports
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

//JMS and Apache ActiveMQ Imports
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

//XML Parser Related: Xerces, W3C, SAX Imports
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import org.xml.sax.InputSource;
import org.w3c.dom.Document;


/** @class EventMessageListener */
public class EventMessageListener implements MessageListener, ExceptionListener {

	private final int VS_SITE_CONDITION_PROPERTY = 372;
	private final int DEFAULT_SYSTEM_LATENCY = 4 * 1000; //default latency in milliseconds used by UserDisplay
	private static final Logger LOGGER = Logger.getLogger( ShakeAlertClient.class.getName() );
	private boolean debugEnabled = false;
	private double lat;
	private double lon;
	private List<Map> Blocks;

	EventMessageListener(double lat, double lon, List<Map> Blocks) {
		this.lat = lat;
		this.lon = lon;
		this.Blocks = Blocks;
	}

	/** Called when a new message from the parent consumer's topic 
	 * 
	 *  @param message The JMS Message received from the consumer's specified topic
	 */
	@Override
	public void onMessage(Message message) {
		try {
			if(message instanceof TextMessage) {
				processMessageText( ( (TextMessage)message).getText() ); 
			}
			else {
				System.out.println("\nEventMessageConsumer: Non-Text Event Message Received:\n\n" + message);
			}
		} catch (Exception except) {
			LOGGER.log( Level.SEVERE, except.toString(), except);
		}
	} // end method onMessage(Message)

	/** Parses the contents a ShakeAlert message and sends obtained data to the
	 *  methods that will calculate warning time and shaking intensity values to be displayed. 
	 *  
	 *  @param messageText The XML content String obtained from ActiveMQ broker.
	 */
	private void processMessageText(String messageText) {
		try {

			//StartTime
			long startTime = System.currentTimeMillis();
			
			//System.out.println("Complete Message:"+messageText);

			//Build Document object from message text input source
			InputSource messageInputSource = new InputSource( new StringReader( messageText ) );
			XPath xpath = XPathFactory.newInstance().newXPath();
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document eventDoc = docBuilder.parse(messageInputSource);


			//Extract Event Message Metadata Strings
			String messageCategory = xpath.evaluate("/event_message/@category", eventDoc);
			String originSystem = xpath.evaluate("/event_message/@orig_sys", eventDoc);
			String messageType = xpath.evaluate("/event_message/@message_type", eventDoc);
			String timeStamp = xpath.evaluate("/event_message/@timestamp", eventDoc);

			if( messageType.equals("delete") ) {
				System.out.println("EVENT DELETE MESSAGE: The last reported event has been canceled.");
				return;
			}
			else if( messageType.equals("new") ) {

				System.out.println("NEW EVENT MESSAGE: ");

			}
			else if( messageType.equals("update") ) {
				System.out.println("EVENT UPDATE MESSAGE: ");


			}
			else {
				if( debugEnabled ) { System.out.println("DEBUG: Message type unknown!"); }
				return;
			}


			//Extract Magnitude Strings
			String magUnitsStr = xpath.evaluate("//mag/@units", eventDoc);
			String magValueStr = xpath.evaluate("//mag", eventDoc);

			//Extract Latitude Data Strings
			String latUnitsStr = xpath.evaluate("//lat/@units", eventDoc);
			String latValueStr = xpath.evaluate("//lat", eventDoc);

			//Extract Longitude Data Strings
			String lonUnitsStr = xpath.evaluate("//lon/@units", eventDoc);
			String lonValueStr = xpath.evaluate("//lon", eventDoc);

			//Extract Depth Data Strings
			String depthUnitsStr = xpath.evaluate("//depth/@units", eventDoc);
			String depthValueStr = xpath.evaluate("//depth", eventDoc);

			//Extract Origin Time Data Strings
			String originTimeUnitsStr = xpath.evaluate("//orig_time/@units", eventDoc);
			String originTimeValueStr = xpath.evaluate("//orig_time", eventDoc);

			//Extract Likelihood Value String
			String likelihoodValueStr = xpath.evaluate("//likelihood", eventDoc);

			if( timeStamp.equals("") ) {
				timeStamp = "none";
			}

			//Print out parsed data strings
			/*System.out.printf( "\n\n-------------------------------------------------------------------------------------\n" );
			System.out.printf( "Message Category: %s\n", messageCategory );
			System.out.printf( "Message Type: %s\n", messageType );
			System.out.printf( "Message Origin System: %s\n", originSystem );
			System.out.printf( "Message Time Stamp: %s\n", timeStamp );
			System.out.printf( "-------------------------------------------------------------------------------------\n" );
			System.out.printf( "Magnitude: %s %s\n", magValueStr, magUnitsStr );
			System.out.printf( "Latitude: %s %s\n", latValueStr, latUnitsStr );
			System.out.printf( "Longitude: %s %s\n", lonValueStr, lonUnitsStr );
			System.out.printf( "Depth: %s %s\n", depthValueStr, depthUnitsStr );
			System.out.printf( "Event Origin Time Stamp: %s %s\n", originTimeValueStr, originTimeUnitsStr );
			System.out.printf( "Likelihood: %s\n", likelihoodValueStr );
			System.out.printf( "-------------------------------------------------------------------------------------\n");*/

			if( messageType.equals("new") ) {

				try {
					
					//List<Map> PushDetails = new ArrayList<>();
					
					/* This is how to declare HashMap */
					HashMap<String, Object> hmap = new HashMap<String, Object>();
					hmap.put("Category", messageCategory);
					hmap.put("Type", messageCategory);
					hmap.put("MessageOriginSystem", messageCategory);
					hmap.put("TimeStamp", messageCategory);
					hmap.put("MagnitudeUnit", magUnitsStr);
					hmap.put("MagnitudeValue", magValueStr);
					hmap.put("LatitudeUnit", latUnitsStr);
					hmap.put("LatitudeValue", latValueStr);
					hmap.put("LongitudeUnit", lonUnitsStr);
					hmap.put("LongitudeValue", lonValueStr);
					hmap.put("DepthUnit", depthUnitsStr);
					hmap.put("DepthValue", depthValueStr);
					hmap.put("EventOriginTimeStampUnit", originTimeUnitsStr);
					hmap.put("EventOriginTimeStampValue", originTimeValueStr);
					hmap.put("Likelihood", likelihoodValueStr);
					hmap.put("startTime", String.valueOf(startTime));

					new MainClass();
					MainClass.runCode(Blocks, eventDoc, hmap);

				} catch (Exception e) {
					// TODO: handle exception

				}
			}

			String timeFormatStr = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; 

			if( !originTimeValueStr.contains(".") ) {
				System.out.println("NO FRACTIONAL SEC");
				timeFormatStr = "yyyy-MM-dd'T'HH:mm:ss'Z'";
			}

			//Parse Strings to numeric and object values
			SimpleDateFormat dateParser = new SimpleDateFormat( timeFormatStr );
			dateParser.setTimeZone( TimeZone.getTimeZone(originTimeUnitsStr) );

			Date originTime = null; 
			if(originTimeValueStr != "" && originTimeValueStr != null ) {
				originTime = dateParser.parse(originTimeValueStr);
			}

			Date messageTime = null;
			if( timeStamp != "none" && timeStamp != null ) {
				messageTime = dateParser.parse(timeStamp);
			}
			else {  // no message timestamp found
				messageTime = new Date( originTime.getTime() + DEFAULT_SYSTEM_LATENCY );
			}

			double magValue = Double.parseDouble(magValueStr);
			double latValue = Double.parseDouble(latValueStr);
			double lonValue = Double.parseDouble(lonValueStr);
			double depthValue = Double.parseDouble(depthValueStr);

			//Time diff between message timestamp and event time
			long systemLatencyMillis = messageTime.getTime() - originTime.getTime();

			/*if( debugEnabled ) {
				System.out.printf( "\n\nDEBUG PRINTOUT OF PARSED VALUES: \n" );
				System.out.printf( "------------------------------------------------------------------------------------\n" );
				System.out.printf( "Date originTime.toString = %s milliseconds\n", originTime );
				System.out.printf( "long originTime = %d milliseconds\n", originTime.getTime() );
				System.out.printf( "double magValue = %f\n", magValue );
				System.out.printf( "double latValue = %f degrees\n", latValue );
				System.out.printf( "double lonValue = %f degrees\n", lonValue );
				System.out.printf( "double depthValue = %f kilometers\n", depthValue );
				System.out.printf( "long systemLatencyMillis = %d milliseconds\n", systemLatencyMillis );
				System.out.printf( "------------------------------------------------------------------------------------\n" );
				System.out.printf( "END DEBUG PRINTOUT OF PARSED VALUES\n\n" );
			}*/

			//Calculate and display integer warning time in seconds and shaking intensity
			float timeUntilShaking = ShakeAlertUtils.calculateWarningTime( 
					originTime, depthValue, latValue, lonValue, lat, lon );

			int shakingIntensity = ShakeAlertUtils.calculateIntensity( 
					depthValue, magValue, latValue, lonValue, lat, lon, VS_SITE_CONDITION_PROPERTY );

			String intensityLevelStr = ShakeAlertUtils.getIntensityText( shakingIntensity );

			System.out.printf( "WARNING: %.1f Seconds Until %s Shaking (%d)\n\n", 
					timeUntilShaking, intensityLevelStr, shakingIntensity );

			//Get intensity for all radii
			HashMap<String, Double> intensityMap = ShakeAlertUtils.cuaAllDist( magValue );

			System.out.printf( "Shaking Intensities: \n");
			for( String key : ShakeAlertUtils.Mmi2GMData.MMI ) {
				System.out.print( " {" + key + ", " + intensityMap.get(key) + "} " );
			}
			System.out.println("\n");

			System.out.printf(" Shaking intensity from lookup table = %s (%d)\n\n", ShakeAlertUtils.getLocalMmiIntensity(latValue, lonValue, lat, lon, magValue), ShakeAlertUtils.getLocalNumericMmiIntensity(latValue, lonValue, lat, lon, magValue) );

		} catch ( Exception except ) {
			LOGGER.log( Level.SEVERE, except.toString(), except);
		}

	} // end method processMessageText()

	@Override
	public synchronized void onException(JMSException jmsExcept) {
		System.out.println("JMS Exception occured. Terminating client.");
	} //end method onException

	public void setDebugEnabled(boolean isEnabled) {
		debugEnabled = isEnabled;
	} //end method setDebugEnabled

	/** EventMessageListener Unit Test Main. */
	/* public static void main(String[] args) {
        EventMessageListener eml = new EventMessageListener(34.0201613, -118.6919214);
        eml.setDebugEnabled(true);
        try {
            java.io.FileReader fileReader = new java.io.FileReader(args[0]);
            char[] buffer = new char[100*1024];
            fileReader.read(buffer);
            fileReader.close();
            String string = new String(buffer).trim();
            eml.processMessageText( string );
        } catch (Exception e) {
            e.printStackTrace();
        }
    } */// end main(String[])


} //end class EventMessageListener
// end file EventMessageListener.java
