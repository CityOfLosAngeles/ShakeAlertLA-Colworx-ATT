package com.la.shakealert;

/** @file HealthMessageListener.java */

/* Copyright (c) 2016 California Institute of Technology.
 * All rights reserved, November 17, 2016
 * This program is distributed WITHOUT ANY WARRANTY whatsoever.
 * Do not redistribute this program without written permission.
 * Do not remove this Copyright statement from this file.  
 */

//package org.shakealert.eew.core;

//Java Imports
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

//Apache ActiveMQ and Java Messaging Service imports
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

//XML Parser Related Imports
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.xml.sax.InputSource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class HealthMessageListener implements MessageListener, ExceptionListener {

    private static final Logger LOGGER = Logger.getLogger( ShakeAlertClient.class.getName() );
    private boolean debugEnabled = false;

    /** Implements interface MessageListener's onMessage(Message) method. This method will parse a ShakeAlert system 
     *  health message and print a system health report.
     */
    @Override 
    public void onMessage(Message message) {
        try {
            if(message instanceof TextMessage) {
                if(debugEnabled) {
                    System.out.println( "\n\n*****Begin XML Health Message*****" );
                    System.out.println( ( (TextMessage)message ).getText() );
                    System.out.println( "*****End XML Health Message*****\n" );
                }
                processMessageText( ( (TextMessage)message ).getText() ); 
            }
            else {
                System.out.println("\nHealthMessageListener: Non-Text Health Message Received:\n" + message);
            }
        } catch (Exception except) {
            LOGGER.log( Level.SEVERE, except.toString(), except);
        }
    } // end method onMessage(Message)

    /** A private helper method to parse the XML message text and print the health status to console. 
     *  @param messageText The XML text string from a ShakeAlert system health message
     */
    private void processMessageText(String messageText) {

        try {
            //Build Document object from message text input source
            InputSource messageInputSource = new InputSource( new StringReader( messageText ) );
            XPath xpath = XPathFactory.newInstance().newXPath();
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document healthDoc = docBuilder.parse(messageInputSource);

            //Get a list of all component nodes
            NodeList components = (NodeList) xpath.evaluate("/system_status/component", healthDoc, XPathConstants.NODESET);

            //Create and initialize active server counter
            int activeComponentCount = 0;
            int totalComponentCount = 0;

            //For each node check if its status attribute is on and print row in status table
            if( debugEnabled ) { System.out.printf( "%-30s%-10s\n", "ShakeAlert Component", "Status"); }
            for(int nodeIndex = 0; nodeIndex < components.getLength(); nodeIndex++) {
                Node currentNode = components.item(nodeIndex);
                Node nameAttrib = currentNode.getAttributes().getNamedItem("name");
                Node statusAttrib = currentNode.getAttributes().getNamedItem("status");

                //If the node is component named onsite or elarms 
                if( !nameAttrib.getNodeValue().equals("dm") ) {
                    if( statusAttrib.getNodeValue().equals("ON") ) {
                        if(debugEnabled) { System.out.printf( "%-30s%-10s\n", nameAttrib.getNodeValue(), "ONLINE" ); }
                        activeComponentCount++;
                    }
                    else {
                        if(debugEnabled) { System.out.printf( "%-30s%-10s\n", nameAttrib.getNodeValue(), "OFFLINE" ); }
                    }
                    totalComponentCount++;
                }
            }

            //Print ShakeAlert system health status
            if(debugEnabled) {
                System.out.printf( "\n\n%d of %d Total ShakeAlert Components Online\n", activeComponentCount, totalComponentCount);
            }
            //System.out.printf( "\nShakeAlert Health Level (HEALTH TOPIC): %3.2f\n\n", ( activeComponentCount / (float) totalComponentCount ) );

        } catch ( Exception except ) { LOGGER.log( Level.SEVERE, except.toString(), except); }
    } // end method processMessageText

    public synchronized void onException(JMSException jmsExcept) {
        LOGGER.log( Level.SEVERE, jmsExcept.toString(), jmsExcept);
    } // end method onException(JMSException)

    public void setDebugEnabled(boolean isEnabled) {
        debugEnabled = isEnabled;
    } // end method setDebugEnabled(boolean)

    //HealthMessageListener Unit Test Main
    public static void main(String[] args) {
        HealthMessageListener hml = new HealthMessageListener();
        try {
            java.io.FileReader fileReader = new java.io.FileReader(args[0]);
            char[] buffer = new char[100*1024];
            fileReader.read(buffer);
            fileReader.close();
            String string = new String(buffer).trim();
            hml.processMessageText( string );
        } catch (Exception e) {
            e.printStackTrace();
        }
    } // end main(String[])

} //end class HealthMessageListener
//end file HealthMessageListener.java
