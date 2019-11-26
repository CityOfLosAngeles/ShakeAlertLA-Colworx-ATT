package com.la.shakealert;

/** @file CuaAttenuationRelation.java */

/* Copyright (c) 2016 California Institute of Technology.
 * All rights reserved, November 17, 2016
 * This program is distributed WITHOUT ANY WARRANTY whatsoever.
 * Do not redistribute this program without written permission.
 * Do not remove this Copyright statement from this file.  
 */

//package org.shakealert.eew.core;

import java.util.Vector;

public class CuaAttenuationRelation {

    public static Vector<Double> CH2007(double M,double Rjb,double Sigma,String IM,String ZH,String PS,String RS){

        // Cua and Heaton 2007 relationships
        // IM = {PGA, PGV, FD}, where FD = 3 sec high pass filtered displacement
        // ZH = {Z,H}, where Z=vertical, H=horizontal
        // PS = {P, S}, P=P-wave, S=S-wave
        // RS = {Rock, Soil}, where Rock is for sites w/ NEHRP class BC and above,
        // Soil is for sites w/ NEHRP class C and below

        // note: output units are PGA (cm/s/s), PGV (cm/s), FD (cm)
        // y is median ground motion level
        // up is median + sigma
        // low is median - sigma
        // sigma is in log10 

        //global IM ZH PS RS
        final double DEPTH_SQUARED = 9.0;
        double R1=Math.sqrt(Math.pow(Rjb,2) + DEPTH_SQUARED);

        Double A,B,C1,C2,D,E,S;
        A = new Double(0);
        B = S = C1 = C2 = D = E = A;
        Vector<Double> cofs = get_coeffs_CH2007(IM,ZH,PS,RS);
        double a = (cofs.elementAt(0)).doubleValue();
        double b = (cofs.elementAt(1)).doubleValue();
        double c1 = (cofs.elementAt(2)).doubleValue();
        double c2 = (cofs.elementAt(3)).doubleValue();
        double d = (cofs.elementAt(4)).doubleValue();
        double e = (cofs.elementAt(5)).doubleValue();
        double _sigma = (cofs.elementAt(6)).doubleValue();

        if(Sigma != 0.0){
            _sigma=Sigma;
        }

        //System.out.println("a "+a+" b "+b+" c1 "+c1+" d "+d+" e "+e+" _sigma "+_sigma);

        double CM=c1*Math.exp(c2*(M-5.0))*(Math.atan(M-5.0)+1.4);
        double log10Y= a*M - b*(R1+CM) - d*Math.log10(R1+CM) + e;
        double logup=log10Y + _sigma;
        double loglow=log10Y - _sigma;

        //System.out.println("R1:"+R1+" Rjb:"+Rjb);

        Double y , up , low , sigma;
        y = up = low = sigma = new Double(0);

        y= new Double(Math.pow(10,log10Y));
        up=new Double(Math.pow(10,logup));
        low=new Double(Math.pow(10,loglow));	
        sigma = new Double(_sigma);

        //System.out.println("y:"+y);

        double SCALE_PEAK = 1.10; 		    
        y *= SCALE_PEAK;

        Vector<Double> ret = new Vector<Double>();
        ret.addElement(y);
        ret.addElement(up);
        ret.addElement(low);
        ret.addElement(sigma);

        return ret;
    }

    private static Vector<Double> get_coeffs_CH2007(String IM,String ZH,String PS,String RS) {
        Double A,B,C1,C2,D,E,Sigma;
        A = B= C1 = C2 = D = E = Sigma = new Double(0.0);
        double a,b,c1,c2,d,e,sigma;
        a = b = c1 = c2 = d = e = sigma = 0;	

        if(IM.compareTo("PGA")==0 && ZH.compareTo("H")==0 && PS.compareTo("P")==0 && RS.compareTo("R")==0){
            a = 0.72;
            b=  3.3e-3;
            c1= 1.6;
            c2= 1.05;
            d = 1.2;
            e =-1.06;
            sigma=0.31;
        }
        else if(IM.compareTo("PGA")==0 && ZH.compareTo("H")==0 && PS.compareTo("P")==0 && RS.compareTo("S")==0){
            a=0.74;
            b= 3.3e-3;
            c1= 2.41;
            c2= 0.95;
            d = 1.26;
            e =-1.05;
            sigma= 0.29;
        }
        else if(IM.compareTo("PGV")==0 && ZH.compareTo("H")==0 && PS.compareTo("P")==0 && RS.compareTo("R")==0){
            a=0.80;
            b=8.4e-4;
            c1=0.76;
            c2=1.03;
            d=1.24;
            e=-3.103;
            sigma=0.27;
        }
        else if(IM.compareTo("PGV")==0 && ZH.compareTo("H")==0 && PS.compareTo("P")==0 && RS.compareTo("S")==0){
            a=0.84;
            b=5.4e-4;
            c1=1.21;
            c2=0.97;
            d= 1.28;
            e= -3.13;
            sigma=0.26;
        }    
        else if( IM.compareTo("FD")==0 && ZH.compareTo("H")==0 && PS.compareTo("P")==0 && RS.compareTo("R")==0){
            a=0.95;
            b=1.7e-7;
            c1=2.16;
            c2=1.08;
            d=1.27;
            e=-4.96;
            sigma=0.28;
        }
        else if( IM.compareTo("FD")==0 && ZH.compareTo("H")==0 && PS.compareTo("P")==0 && RS.compareTo("S")==0){
            a=0.94;
            b=5.17e-7;
            c1=2.26;
            c2=1.02;
            d=1.16;
            e=-5.01;
            sigma=0.3;
        }
        else if( IM.compareTo("PGA")==0 && ZH.compareTo("H")==0 && PS.compareTo("S")==0 && RS.compareTo("R")==0){
            a=0.733;
            b=7.216e-4;
            d=1.48;
            c1=1.16;
            c2=0.96;
            e=-0.4202;
            sigma=0.3069;
        }
        else if( IM.compareTo("PGA")==0 && ZH.compareTo("H")==0 && PS.compareTo("S")==0 && RS.compareTo("S")==0){
            a=0.709;
            b=2.3878e-3;
            d=1.4386;
            c1=1.722;
            c2=0.9560;
            e=-2.4525e-2;
            sigma=0.3261;
        }
        else if( IM.compareTo("PGV")==0 && ZH.compareTo("H")==0 && PS.compareTo("S")==0 && RS.compareTo("R")==0){
            a=0.861988;
            b=5.578e-4;
            d=1.36760;
            c1=0.8386;
            c2=0.98;
            e=-2.58053;
            sigma=0.2773;
        }	    
        else if( IM.compareTo("PGV")==0 && ZH.compareTo("H")==0 && PS.compareTo("S")==0 && RS.compareTo("S")==0){
            a=0.88649;
            b=8.4e-4;
            d=1.4729;
            c1=1.39;
            c2=0.95;
            e=-2.2498;
            sigma=0.3193;
        }	    
        else if( IM.compareTo("FD")==0 && ZH.compareTo("H")==0 && PS.compareTo("S")==0 && RS.compareTo("R")==0){
            a=1.03;
            b=1.01e-7;
            c1=1.09;
            c2=1.13;
            d=1.43;
            e=-4.34;
            sigma=0.27;
        }	    
        else if( IM.compareTo("FD")==0 && ZH.compareTo("H")==0 && PS.compareTo("S")==0 && RS.compareTo("S")==0){
            a=1.08;
            b=1.2e-6;
            c1=1.95;
            c2=1.09;
            d=1.56;
            e=-4.1;
            sigma=0.32;
        }	    
        else if( IM.compareTo("PGA")==0 && ZH.compareTo("Z")==0 && PS.compareTo("P")==0 && RS.compareTo("R")==0){
            a=0.74;
            b=4.01e-3;
            c1=1.75;
            c2=1.09;
            d=1.2;
            e=-0.96;
            sigma=0.29;
        }	    
        else if( IM.compareTo("PGA")==0 && ZH.compareTo("Z")==0 && PS.compareTo("P")==0 && RS.compareTo("S")==0){
            a=0.74;
            b=5.17e-7;
            c1=2.03;
            c2=0.97;
            d=1.2;
            e=-0.77;
            sigma=0.31;
        }	    
        else if( IM.compareTo("PGV")==0 && ZH.compareTo("Z")==0 && PS.compareTo("P")==0 && RS.compareTo("R")==0){
            a=0.82;
            b=8.54e-4;
            c1=1.14;
            c2=1.11;
            d=1.36;
            e=-2.90057;
            sigma=0.26;
        }	    
        else if( IM.compareTo("PGV")==0 && ZH.compareTo("Z")==0 && PS.compareTo("P")==0 && RS.compareTo("S")==0){
            a=0.81;
            b=2.65e-6;
            c1=1.4;
            c2=1.0;
            d=1.48;
            e=-2.55;
            sigma=0.30;
        }	    
        else if( IM.compareTo("FD")==0 && ZH.compareTo("Z")==0 && PS.compareTo("P")==0 && RS.compareTo("R")==0){
            a=0.96;
            b=1.98e-6;
            c1=1.66;
            c2=1.16;
            d=1.34;
            e=-4.79;
            sigma=0.28;
        }	    
        else if( IM.compareTo("FD")==0 && ZH.compareTo("Z")==0 && PS.compareTo("P")==0 && RS.compareTo("S")==0){
            a=0.93;
            b=1.09e-7;
            c1=1.5;
            c2=1.04;
            d=1.23;
            e=-4.74;
            sigma=0.31;
        }	    
        else if( IM.compareTo("PGA")==0 && ZH.compareTo("Z")==0 && PS.compareTo("S")==0 && RS.compareTo("R")==0){
            a= 0.78;
            b=2.7e-3;
            c1=1.76;
            c2=1.11;
            d=1.38;
            e=-0.75;
            sigma=0.30;
        }	    
        else if( IM.compareTo("PGA")==0 && ZH.compareTo("Z")==0 && PS.compareTo("S")==0 && RS.compareTo("S")==0){
            a=0.75;
            b=2.47e-3;
            c1=1.59;
            c2=1.01;
            d=1.47;
            e=-0.36;
            sigma=0.30;
        }	    
        else if( IM.compareTo("PGV")==0 && ZH.compareTo("Z")==0 && PS.compareTo("S")==0 && RS.compareTo("R")==0){
            a=0.90;
            b=1.03e-3;
            c1=1.39;
            c2=1.09;
            d= 1.51;
            e=-2.78;
            sigma=0.25;
        }	    
        else if( IM.compareTo("PGV")==0 && ZH.compareTo("Z")==0 && PS.compareTo("S")==0 && RS.compareTo("S")==0){
            a=0.88;
            b=5.41e-4;
            c1=1.53;
            c2=1.04;
            d=1.48;
            e=-2.54;
            sigma=0.27;
        }	    
        else if( IM.compareTo("FD")==0 && ZH.compareTo("Z")==0 && PS.compareTo("S")==0 && RS.compareTo("R")==0){
            a=1.04;
            b=1.12e-5;
            c1=1.38;
            c2=1.18;
            d=1.37;
            e=-4.74;
            sigma=0.25;
        }	    
        else if( IM.compareTo("FD")==0 && ZH.compareTo("Z")==0 && PS.compareTo("S")==0 && RS.compareTo("S")==0){
            a=1.04;
            b=4.92e-6;
            c1=1.55;
            c2=1.08;
            d=1.36;
            e=-4.57;
            sigma=0.28;
        }	    

        A = new Double(a);
        B = new Double(b);
        C1 = new Double(c1);
        C2 = new Double(c2);
        D = new Double(d);
        E = new  Double(e);
        Sigma = new Double(sigma);

        Vector<Double> ret = new Vector<Double>();
        ret.addElement(A);
        ret.addElement(B);
        ret.addElement(C1);
        ret.addElement(C2);
        ret.addElement(D);
        ret.addElement(E);
        ret.addElement(Sigma);

        return ret;

    }
} // end class CuaAttenuationRelation
// end file CuaAttenuationRelation.java
