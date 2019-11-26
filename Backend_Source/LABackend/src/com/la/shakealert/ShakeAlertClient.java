package com.la.shakealert;

/** @file ShakeAlertClient.java */

/*
 * Copyright (c) 2016 California Institute of Technology.
 * All rights reserved, November 17, 2016 
 * This program is distributed WITHOUT ANY WARRANTY whatsoever.
 * Do not redistribute this program without written permission.
 * Do not remove this Copyright statement from this file.  
 */

//package org.shakealert.eew.core;

//Java Imports
import java.io.IOException;
import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Logger;

//JMS and ActiveMQ Imports
import javax.jms.Connection;
import javax.jms.Session;
import org.apache.activemq.ActiveMQMessageConsumer;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.ActiveMQConnectionFactory;


/** ShakeAlert Client main class which contains the program entry point. */
public class ShakeAlertClient {

    private static final Logger LOGGER = Logger.getLogger( ShakeAlertClient.class.getName() );
    private final String propertiesFilePath = "shakealert_client.properties";
    private String healthTopic = null;
    private String eventTopic = null;
    private String serverUrl = null;
    private String host = null;
    private String port = null;
    public static Properties properties = null;
    public static boolean debugEnabled = false;

    /** Primary constructor for class ShakeAlertClient that contains the main program functionality. 
     *  @param args The command line arguments passed from the main method.
     */
    public ShakeAlertClient(String[] args) {
        try {
        	
        	//For Generate Blocks
			List<Map> Blocks = new Blocks().generateBlocks();
			System.out.println("Total Blocks: "+Blocks.size());
        	
            System.out.print( "\nShakeAlert Client Example\n---------------------------\n" );

            //Load properties from file
            System.out.printf("Loading Properties File...");
            loadProperties();
            System.out.printf("Done!\n");

            //Parse CLI Arguments
            parseCliArgs(args);

            //Set Log Debug Level
            if(debugEnabled) {
                LOGGER.setLevel( Level.ALL );
            }
            else {
                LOGGER.setLevel( Level.INFO ); //Set Default Logger Level
            }

            //If no overrides were specified in cli args then use ones found in properties file 
            if( healthTopic == null ) {
                LOGGER.log( Level.FINE, "Using health topic loaded from properties file." );
                healthTopic = properties.getProperty("health_topic");
            }
            if( eventTopic == null ) {
                LOGGER.log( Level.FINE, "Using event topic loaded from properties file." );
                eventTopic = properties.getProperty("event_topic");
            }
            if( host == null ) {
                LOGGER.log( Level.FINE, "Using host name loaded from properties file." );
                host = properties.getProperty("host");
            }
            if( port == null ) {
                LOGGER.log( Level.FINE, "Using password loaded from properties file." );
                port = properties.getProperty("port");
            }

            //Print host, port, health topic, event topic for verification
            System.out.printf("\nConnection Info:\n" );
            System.out.printf( "Using host: %s\n", host );
            System.out.printf( "Using port: %s\n", port );
            System.out.printf( "Using health topic: %s\n", healthTopic );
            System.out.printf( "Using event topic: %s\n", eventTopic );

            //Construct ActiveMQ broker host URL
            serverUrl = "ssl://" + host + ":" + port;

            //Create and Start JMS Connection to ActiveMQ broker host using constructed URL 
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory( serverUrl );
            Connection connection = connectionFactory.createConnection( 
                    properties.getProperty( "username" ), properties.getProperty( "password" ) );
            connection.start();

            //Create new JMS Session
            Session amqSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            //Create a JMS Destination for each ActiveMQ Topic (ActiveMQTopic is a JMS Destination)
            ActiveMQTopic shakealertHealthTopic = (ActiveMQTopic) amqSession.createTopic( healthTopic );
            ActiveMQTopic quakeEventTopic = (ActiveMQTopic) amqSession.createTopic( eventTopic );

            //Create new JMS MessageConsumer for each ActiveMQ Topic
            ActiveMQMessageConsumer healthMessageConsumer = 
                (ActiveMQMessageConsumer) amqSession.createConsumer(shakealertHealthTopic); 
            ActiveMQMessageConsumer quakeEventMessageConsumer = 
                (ActiveMQMessageConsumer) amqSession.createConsumer(quakeEventTopic); 

            //Register Corresponding Asynchronous MessageListeners to each MessageConsumer
            HealthMessageListener healthListener = new HealthMessageListener();
            healthListener.setDebugEnabled(debugEnabled);
            healthMessageConsumer.setMessageListener(healthListener);
            double latitude = Double.parseDouble( properties.getProperty("latitude") );
            double longitude = Double.parseDouble( properties.getProperty("longitude") );
            EventMessageListener eventListener = new EventMessageListener( latitude, longitude, Blocks );
            eventListener.setDebugEnabled(debugEnabled);
            quakeEventMessageConsumer.setMessageListener(eventListener);

            //Start consumer threads
            System.out.print("Starting consumer threads...");
            healthMessageConsumer.start();
            quakeEventMessageConsumer.start();
            System.out.println("All Consumers Started Successfully!");

            //Loop until shutdown command is entered
            System.out.print("\nEnter \"quit\" To Exit Program\n");
            
            //MyCode
            
            
            
            Scanner keyboard = new Scanner(System.in);
            String in = keyboard.nextLine();
            while(!in.equals("quit")) {
                in = keyboard.nextLine();
            }

            //Stop consumer threads, close any open connections, and exit program.
            System.out.printf("Stopping Message Consumers...");
            if(healthMessageConsumer != null) { healthMessageConsumer.close(); }
            if(quakeEventMessageConsumer != null) { quakeEventMessageConsumer.close(); }
            System.out.printf("Done\nClosing connection to ActiveMQ Message Broker...");
            if(amqSession != null) {amqSession.close(); }
            if(connection != null) { connection.close(); }
            if(keyboard != null) { keyboard.close(); }
            System.out.printf("Done.\n");

        } catch ( Exception except ) {
            //log exception as severe and print it's stack trace
            LOGGER.log(Level.SEVERE, except.toString(), except);
        }

        System.out.println("\nProgram Terminated.\n");

    } //end ctor ShakeAlertClient(String[]) 

    /** Program main entry point. */
    public static void main(String[] args) {
        new ShakeAlertClient(args); //Run the ShakeAlertClient
    } // end main(String[])

    /** Loads program properties from a specified properties file into the ShakeAlertClient's Properties member. 
     *  Any IOExceptions that may occur during this procedure will be caught and their stack trace printed to the
     *  log with a debug level of severe.
     */
    private void loadProperties() {
        FileInputStream propLoaderInStream = null;
        try {
            propLoaderInStream = new FileInputStream(propertiesFilePath);
            properties = new Properties();
            properties.load(propLoaderInStream);
            propLoaderInStream.close();
        } catch(IOException propFileIOExcept) {
            //log exception as severe and print it's stack trace
            LOGGER.log( Level.SEVERE, propFileIOExcept.toString(), propFileIOExcept);
        }
    } //end method loadProperties

    /** Parses and loads command line options and overrides. 
     *  Options := { -debug | -host=your_host_name | -port=your_port_number | -health_topic=your_health_topic | -event_topic=your_event_topic }
     *  @param args Command line arguments array to be processed for optional parameters and overrides.
     */
    private void parseCliArgs( String[] args ) {
        if( args == null || args.length == 0 ) return;
        if( args[0].equals("-help") ) {
            System.out.printf( "Options:\n\t-debug\n\t-host=your_host_name\n\t-port=your_port_number,\n\t"
                    + "health_topic=your_health_topic\n\tevent_topic=your_event_topic" );
            return;
        }
        for( String currentArg: args ) {
            if( currentArg.equals("-debug") ) { 
                debugEnabled = true; 
                System.out.printf("Debug Flag Enabled!\n");
            }
            else if( currentArg.contains("-host=") ) {
                host = currentArg.split( "=" )[1];
                if(debugEnabled) { System.out.printf("Host name command line override found: %s\n", host); }
            }
            else if( currentArg.contains("-port=") ) {
                port = currentArg.split( "=" )[1];
                if(debugEnabled) { System.out.printf("Port number command line override found: %s\n", port); };
            }
            else if( currentArg.contains("-topic=") ) {
                eventTopic = currentArg.split( "=" )[1];
                if(debugEnabled) { System.out.printf("Event topic number overriden in command line: %s\n", port); };
            }
            else {
                System.out.println("Ignoring unrecognised argument: " + currentArg);
            }
        }
    } //end method parseCliArgs( String[] )


// static byte[] HmacSHA256(String data, byte[] key) throws Exception {
//    String algorithm="HmacSHA256";
//    Mac mac = Mac.getInstance(algorithm);
//    mac.init(new SecretKeySpec(key, algorithm));
//    return mac.doFinal(data.getBytes("UTF8"));
//}
//
//static byte[] getSignatureKey(String key, String dateStamp, String regionName, String serviceName) throws Exception {
//    byte[] kSecret = ("AWS4" + key).getBytes("UTF8");
//    byte[] kDate = HmacSHA256(dateStamp, kSecret);
//    byte[] kRegion = HmacSHA256(regionName, kDate);
//    byte[] kService = HmacSHA256(serviceName, kRegion);
//    byte[] kSigning = HmacSHA256("aws4_request", kService);
//    return kSigning;
//}

} // end class ShakeAlertClient
// end ShakeAlertClient.java

