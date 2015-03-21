/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.das2.sdi;

import org.das2.datum.Units;
import org.virbo.dataset.QDataSet;
import org.virbo.dataset.examples.Schemes;
import sdi.data.SimpleXYData;
import sdi.data.XYData;
import sdi.data.XYMetadata;

/**
 * Tests to exercise the code.
 * @author jbf
 */
public class Test {
    
    private static void test1() {
        System.err.println("==test1==");
        QDataSet ds= Schemes.simpleSpectrogram().slice(0).trim(0,10);
        SimpleXYData xyds= Adapter.adapt( ds, SimpleXYData.class );
        for ( int i=0; i<xyds.size(); i++ ) {
            System.err.printf("%f %f\n", xyds.getX(i), xyds.getY(i) );
        }   
    }
    
    private static void test2() {
        System.err.println("==test2==");
        QDataSet ds= Schemes.scalarTimeSeries().trim(0,10);
        XYData xyds= Adapter.adapt( ds, XYData.class );
        XYMetadata m= xyds.getMetadata();
        Units u= Units.lookupUnits(m.getXUnits().getName());
        for ( int i=0; i<xyds.size(); i++ ) {
            System.err.printf("%s %f\n", u.createDatum( xyds.getX(i) ), xyds.getY(i) );
        }   
    }
    
    
    public static void main( String[] args ) {
        test1();
        test2();
    }
}
