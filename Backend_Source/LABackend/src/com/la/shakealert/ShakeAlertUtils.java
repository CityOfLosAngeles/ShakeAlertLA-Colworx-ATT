package com.la.shakealert;

/** @file ShakeAlertUtils.java */

/* Copyright (c) 2016 California Institute of Technology.
 * All rights reserved, November 17, 2016
 * This program is distributed WITHOUT ANY WARRANTY whatsoever.
 * Do not redistribute this program without written permission.
 * Do not remove this Copyright statement from this file.  
 */

//package org.shakealert.eew.core;

import java.util.Date;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Vector;
import java.util.HashMap;

public final class ShakeAlertUtils {

    public final static double P_WAVE_VELOCITY = 6.10; // [kilometers/second] 			//source: EEWDisplay.SeismicUtils.java
    public final static double S_WAVE_VELOCITY = 3.55; // [kilometers/second]			//source: EEWDisplay.SeismicUtils.java
    public final static double EQUATOR_RADIUS_METERS = 6378137.0; 				        //source: bbn.openmap.proj.Planet
    public final static double EQUATOR_RADIUS_KM = EQUATOR_RADIUS_METERS / 1000.0;                  //source: bbn.openmap.proj.Planet
    public final static double EQUATOR_CIRCUMFERENCE_METERS = 2 * Math.PI * EQUATOR_RADIUS_METERS;  //source: bbn.openmap.proj.Planet
    public final static double EQUATOR_CIRCUMFERENCE_KM = EQUATOR_CIRCUMFERENCE_METERS / 1000.0;    //source: bbn.openmap.proj.Planet

    /** Computes integer warning time in seconds
     *  @param originTime Event time stamp as a Date object.
     *  @param depth	Event depth in kilometers.
     *  @param eventLat Event latitude in decimal degrees.
     *  @param eventLon Event longitude in decimal degrees.
     *  @param referenceLat Observer's latitude in decimal degrees.
     *  @param referenceLon Observer's longitude in decimal degrees.
     *  @return time until shaking in seconds
     */
    public static float calculateWarningTime(Date originTime, double depth, double eventLat, double eventLon, double referenceLat, double referenceLon) {
        //****************NTP***********************************************************/
        //double ntpTimeDiffMillis = 0;
        //try { 
        //	ntpTimeDiffMillis = new SntpClient().getNTPOffset("ntp-01.caltech.edu"); 
        //} catch (IOException e) { e.printStackTrace(); }
        //******************************************************************************/
        long originTimeInMillis = originTime.getTime();
        long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
        long timeOffsetInMillis = currentTimeInMillis - originTimeInMillis;

        double epi_val = Math.pow( radianToKilometer( greatCircleDistance(eventLat, eventLon, referenceLat, referenceLon) ), 2 );
        double hypo_dist = Math.sqrt(epi_val + Math.pow(depth, 2));

        System.out.printf("Event Origin Time = %d ms\n", originTimeInMillis);
        System.out.printf("Time Difference = %d ms\n", timeOffsetInMillis);
        System.out.printf("Event Depth = %f km\n", depth);
        System.out.printf("Hypocenter Distance = %f km\n",  hypo_dist);

        return (float)( (hypo_dist / S_WAVE_VELOCITY) - (timeOffsetInMillis / 1000.0) );

    } // end method calculateWarningTime

    //Taken from EEWDisplay/SeismicUtils.java
    public static int calculateIntensity( double depth, double magnitude, double eventLat, double eventLon, double referenceLat, double referenceLon, int vsSiteCondition ) {
        double epiVal = Math.pow( radianToKilometer( greatCircleDistance(eventLat, eventLon, referenceLat, referenceLon) ), 2 );
        double epiDist = Math.sqrt(epiVal); 
        String soilType = "S";
        if(vsSiteCondition > 434) { soilType = "R"; }
        Vector<Double> res = CuaAttenuationRelation.CH2007( magnitude , epiDist, 0, "PGA", "H", "S", soilType);
        double PGA = (res.elementAt(0)).doubleValue();
        res = CuaAttenuationRelation.CH2007( magnitude, epiDist , 0, "PGV" , "H", "S", soilType );					  
        double PGV = res.elementAt(0).doubleValue();
        return (int) Math.floor( getIntensity(PGA, PGV) ); //round mmi value down
    } // end method calculateIntensity

    public static String getIntensityText( int level ) {
        String text = "No";
        if (level > 1) {
            switch (level) {
                case 2: /* 2 and 3 are the same */
                case 3: { text = "Weak";         break; }
                case 4: { text = "Light";        break; }
                case 5: { text = "Moderate";     break; }
                case 6: { text = "Strong";       break; }
                case 7: { text = "Very Strong";  break; }
                case 8: { text = "Severe";       break; }
                case 9: { text = "Violent";      break; }
                default:{ text = "Extreme";      break; }
            } // switch on intensity
        }
        return text;
    } // getIntensityText

    /** Calculates shaking intensity value on the MMI Scale. Sourced from UserDisplay ShakeAlertUtils.java. 
     *  @param pga Peak ground acceleration input.
     *  @param pgv Peak ground velocity input.
     *  @return Shaking Intensity (MMI) value.
     */
    private static double getIntensity(double pga, double pgv){
        //CLEANUP Constants in this function should be moved to struct
        double ammi = 0; //# Intensity from acceleration
        double vmmi = 0; //# Intensity from velocity
        double mmi = 0;

        if (pga > 0) {
            if (Math.log10(pga)<=1.57)
                ammi=1.78+1.55*Math.log10(pga);
            else
                ammi=(-1.6)+3.7*Math.log10(pga);
            if (ammi < 1) ammi = 1;
        }

        if (pgv > 0) {
            if (Math.log10(pgv)<=0.53)
                vmmi=3.78+1.47*Math.log10(pgv);
            else
                vmmi=2.89+3.16*Math.log10(pgv);
            if (vmmi < 1) vmmi = 1;
        }

        if (pga > 0 && pgv > 0) mmi = (ammi+vmmi) / 2;
        else if (pga > 0) mmi = ammi;
        else if (pgv > 0) mmi = vmmi;

        if (mmi < 1) mmi = 1;
        if (mmi > 9.5) mmi = 10;

        return mmi;
    } // end method getIntensity

    /** Computes the arc distance between two coordinates given in decimal degrees
     *  @param lat1 Latitude value of coordinate 1 in decimal degrees.
     *  @param lon1 Longitude value of coordinate 1 in decimal degrees.
     *  @param lat2 Latitude value of coordinate 2 in decimal degrees.
     *  @param lon2 Longitude value of coordinate 2 in decimal degrees.
     *  @return double arc distance between (lat1, lon1) and (lat2, lon2) in radians.
     */
    public static double greatCircleDistance( double lat1, double lon1, double lat2, double lon2 ) {
        double phi1 = deg2rad(lat1);
        double phi2 = deg2rad(lat2);
        double lambda1 = deg2rad(lon1);
        double lambda2 = deg2rad(lon2);
        double phiDiff = Math.sin( (phi2 - phi1) / 2 );	//latitudinal angle
        double lambdaDiff = Math.sin( (lambda2 - lambda1) / 2 ); //longitudinal angle
        double r = Math.sqrt( Math.pow(phiDiff, 2) + Math.cos(phi1) * Math.cos(phi2) * Math.pow(lambdaDiff, 2) ); //sphere radius
        return 2 * Math.asin(r); //return delta-sigma (central angle between coord1 and coord2) = 2 * arcsin( r )
    } // end method greatCircleDistance

    //convert decimal degrees to radians
    public static double deg2rad(double deg) { return (deg * Math.PI / 180.0); }	

    public static double radianToKilometer(double radian) { return (radian * EQUATOR_RADIUS_KM); }

    //convert radians to decimal degrees
    public static double rad2deg(double rad) { return (rad * 180 / Math.PI); }	

    /** Finds the distance for a given magnitude at which ground motion is predicted to decrease to provided level. */
    public static HashMap<String, Double> cuaAllDist( double magnitude ) {

        HashMap<String, Double> intensityRadiiMap = new HashMap<String, Double>(); //map of intensity strings to min radius values

        //TODO: This should be replaced with a static method GrndMotionData.getGoundMotionBounds()
        Mmi2GMData mmiData = new Mmi2GMData(); //Ground Motion Data Structure

        //final String[] gmList = { "PGV"}; // velocity only
        //final String[] gmList = { "PGA"}; //acceleration only
        final String[] gmList = { "PGA", "PGV" }; //average acceleration and velocity

        final String[] siteSubstrateTypes = { "S" }; // soil only
        //final String[] siteSubstrateTypes = { "R" }; // rock only
        //final String[] siteSubstrateTypes = { "R", "S" }; // average soil and rock

        for( int mmiIndex = 0; mmiIndex < Mmi2GMData.MMI.length; mmiIndex++ ) {

            double rTol = 1.0;   //min radius in km
            double rFinal = 0.0; //final radius in km

            //for each substraitType in siteSubstraitTypes eg. Soil and/or Rock 
            for (String substrateType : siteSubstrateTypes ) {

                double aTol = 0;

                //for each ground motion type in ground motion list eg. acceleration and velocity 
                for( String gmType : gmList ) {
                    double rStart = 0.0;
                    double rEnd = 10000.0; //end radius in km

                    aTol= mmiData.gmBounds.get(gmType).get(mmiIndex).doubleValue();
                    double rCheck = 0;
                    double y = 0;
                    if ( aTol > CuaAttenuationRelation.CH2007( magnitude, rCheck, 0, gmType, "H", "S", substrateType ).get(0).doubleValue() ) {
                        rEnd = 0;
                    }

                    while ( (rEnd - rStart) > rTol ) {
                        rCheck =  rStart +  ( rEnd - rStart ) / 2.0;
                        y = CuaAttenuationRelation.CH2007( magnitude, rCheck, 0, gmType, "H", "S", substrateType ).get(0).doubleValue();
                        if( y >= aTol ) { rStart = rCheck; } 
                        else { rEnd = rCheck; }
                    } // end while loop

                    rFinal += rEnd;

                } // end for gmType : gmList

            } // for substraitTypes in siteSubstraitTypes

            //Update rFinal to be average intensity radius for all ground motion and substrate types
            rFinal /= (float) (siteSubstrateTypes.length * gmList.length);
            rFinal = Math.ceil(rFinal);
            intensityRadiiMap.put(Mmi2GMData.MMI[mmiIndex], rFinal);

        } // end for each mmi element in Mmi2GM.MMI

        return intensityRadiiMap; //return HashMap of Intensities to Radii

    } //end method cuaAllDist( double )

    /** Inner class to hold MMI to PGA/PGV info following Worden et al. 2012 */
    static class Mmi2GMData {
        //PGA = peak ground acceleration
        //PGV = peak ground velocity
        //TODO: Move pga/pgv constants into a better struct to be used by the getIntensity function from the UserDisplay
        static final String[] MMI = { "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X+" };
        final float PGAC1 = 1.78f;   
        final float PGAC2 = 1.55f;   
        final float PGAC3 = -1.60f;   
        final float PGAC4 = 3.70f;   
        final float PGVC1 = 3.78f;   
        final float PGVC2 = 1.47f;   
        final float PGVC3 = 2.89f;   
        final float PGVC4 = 3.16f;   
        final float PGAT1 = 1.57f;
        final float PGAT2 = 4.22f;
        final float PGVT1 = 0.53f;
        final float PGVT2 = 4.56f;
        private HashMap<String, Vector<Double>> gmBounds;

        public Mmi2GMData() {
            //initialize gmBounds structure eg. gmBounds = { "acc" = { a_1, a_2,..., a_8 } }, "vel" = { v_1, v_2,..., v_9 } }
            gmBounds = new HashMap< String, Vector<Double>>();
        gmBounds.put( "PGA", new Vector<Double>() );
        gmBounds.put( "PGV", new Vector<Double>() );

        //For each MMI in integer range [2,11) -> populate gmBounds map with values calculated using PGV* and PGA*
        for(int mmi = 2; mmi < 11; mmi++) {
            //PGA (Acceleration) Ground Motion Bounds
            if( mmi > PGAT2 ) { 
                gmBounds.get("PGA").add( Math.pow( 10.0, (mmi - this.PGAC3) / this.PGAC4 ) );
            }
            else { 
                gmBounds.get("PGA").add( Math.pow( 10.0, (mmi - this.PGAC1) / this.PGAC2 ) );
            }

            //PGV (Velocity) Ground Motion Bounds
            if( mmi > PGVT2 ) { 
                gmBounds.get("PGV").add( Math.pow( 10.0, (mmi - this.PGVC3) / this.PGVC4 ) );
            }
            else {
                gmBounds.get("PGV").add(  Math.pow( 10.0, (mmi - this.PGVC1) / this.PGVC2 ) );
            }
        } //end for

    } // end Constructor Mmi2GMData()

} //end nested inner class Mmi2GMData

/** Returns local MMI string given distance from the epicenter and magnitude. */
static String getLocalMmiIntensity( double eventLat, double eventLon, double referenceLat, double referenceLon, double magnitude ) { 
    HashMap<String, Double> radii = cuaAllDist( magnitude );
    double epi_distance = radianToKilometer( greatCircleDistance(eventLat, eventLon, referenceLat, referenceLon) );
    String mmi = "I";
    for( int mmiIndex = 0;  mmiIndex < Mmi2GMData.MMI.length; mmiIndex++) {
        double radius = radii.get( Mmi2GMData.MMI[mmiIndex] );
        if( epi_distance > radius ) {
            return mmi;
        }
        else {
            mmi = Mmi2GMData.MMI[mmiIndex] ;
        }
    }
    return mmi;
}

/** Returns the local MMI Intensity for a given coordinate pair and magnitude. */
static int getLocalNumericMmiIntensity( double eventLat, double eventLon, double referenceLat, double referenceLon, double magnitude ) {
    String mmiString = getLocalMmiIntensity( eventLat, eventLon, referenceLat, referenceLon, magnitude );
    if( mmiString.equals( "I") ) { return 1; };
    for(int index = 0; index < Mmi2GMData.MMI.length; index++ ) {
        if( mmiString.equals(Mmi2GMData.MMI[index]) ) {
            return index + 2;
        }
    }
    return -1;
}

//ShakeAlertUtils class testing main
public static void main( String[] args ) {

    if( args[0].equals( "mmi_radii" ) ) {
        HashMap<String, Double> intenseRadii;
        for(double mag = 2.5; mag <= 8.0; mag += 0.1 ) {
            intenseRadii = cuaAllDist(mag);
            System.out.printf( "Magnitude %1.1f { MMI, Radius } =", mag );
            for( String key : Mmi2GMData.MMI ) {
                System.out.printf( " { %s, %f } ", key, intenseRadii.get(key) );
            }
            System.out.println();
        } // end for( magnitude_i -> magnitude_f )
    }
    else if( args[0].equals( "test_mmi_radii") ) {
        String outputFilePath = null;
        try { 
            outputFilePath = args[1]; 
            runMmiRadiiTest( outputFilePath );
        } catch( ArrayIndexOutOfBoundsException nullArgExcept ) { 
            runMmiRadiiTest( outputFilePath );
        }
    }

} // end test method main

public static void runMmiRadiiTest( String outputPath ) {
    PrintWriter writer = null; 
    try {
        if( outputPath == null ) {
            writer = new PrintWriter( System.out );
        }
        else {
            File outputFile = new File( outputPath );
            writer = new PrintWriter( outputFile );
        }
    }
    catch( IOException ioExcept ) {
        ioExcept.printStackTrace();
    } // end try catch

    final double initMagnitude = 2.5; //starting test magnitude
    final double finalMagnitude = 8.0; //end test magnitude
    final double finalDistance = 3000.00; //end test distance in km
    final double magIncrement = 0.2;  //value magnitude will be increased for each test iteration
    final int vs30Condition = 372; //from UserDisplay default settings. At 372 UD will use Soil as substrate
    final double referenceLat = 38.139;
    final double referenceLon = -118.126;

    /** Increasing both latitude and longitude values by this amount results in a distance increase of 1km. */
    final double latLonIncrement = 0.00707; 

    System.out.printf("Running test...\n");

    writer.println("Magnitude,DistanceFromEpi,UserDisplayMMI,RadiiMapMMI,Difference");
    int errorCount = 0;
    int total = 0; //total entries processed = magnitude count * distance count
    int meanErrorDist = 0; //average distance in km where an error occurred
    int meanErrorMag = 0;

    for( double currentMag = initMagnitude; currentMag <= finalMagnitude; currentMag += magIncrement ) {
        double currentEventLat = referenceLat + latLonIncrement; 
        double currentEventLon = referenceLon + latLonIncrement;
        for( double currentDist = ShakeAlertUtils.radianToKilometer( ShakeAlertUtils.greatCircleDistance(currentEventLat, currentEventLon, referenceLat, referenceLon) ); currentDist <= finalDistance; currentDist += ShakeAlertUtils.radianToKilometer( ShakeAlertUtils.greatCircleDistance(currentEventLat, currentEventLon, referenceLat, referenceLon) ) ) {
            int originalIntensity = ShakeAlertUtils.calculateIntensity( 0, currentMag, currentEventLat, currentEventLon, referenceLat, referenceLon, vs30Condition );
            int mapIntensity = ShakeAlertUtils.getLocalNumericMmiIntensity( currentEventLat, currentEventLon, referenceLat, referenceLon, currentMag );
            int difference = Math.abs(mapIntensity - originalIntensity);

            if( difference > 0 && originalIntensity > 2 ) {
                double tmpdist = ShakeAlertUtils.radianToKilometer( ShakeAlertUtils.greatCircleDistance(currentEventLat, currentEventLon, referenceLat, referenceLon));
                writer.printf("%2.1f,%f,%f,%d,%d,%d\n", currentMag, currentDist, tmpdist, originalIntensity, mapIntensity, difference);
                errorCount++;
                meanErrorDist += currentDist;
                meanErrorMag += currentMag;

            } // end if

            currentEventLon += latLonIncrement;
            currentEventLon += latLonIncrement;
            total++;

        } // end for( all distances )
    } // end for( all magnitudes )

    if( errorCount > 0 ) {
        //calculate and display error stats
        meanErrorDist /= errorCount;
        meanErrorMag /= errorCount;
        System.out.println( "Failed Test Cases Found.");
        writer.printf("Tests cases failed / total test cases: %d/%d = %f\n", errorCount, total, errorCount / (double)total );
        writer.printf("Mean distance of failure in km: %d \n", meanErrorDist);
        writer.printf("Mean magnitude of failure: %d \n", meanErrorMag);
    }
    else {
        System.out.println("All " + total + " Test Cases Passed.");
    } // end if else

    //close output writer
    writer.close();
} //end runMmiRadiiTest

} // end class ShakeAlertUtils
// end file ShakeAlertUtils.java

