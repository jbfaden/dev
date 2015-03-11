/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.das2.sdi;

import org.virbo.dataset.QDataSet;
import org.virbo.dataset.SemanticOps;
import sdi.data.Bin;
import sdi.data.SimpleBinnedData2D;

/**
 *
 * @author jbf
 */
public class SimpleBinnedData2DImpl implements SimpleBinnedData2D {

    QDataSet x;
    QDataSet y;
    QDataSet z;
    
    public SimpleBinnedData2DImpl( QDataSet ds ) {
        this.x= SemanticOps.xtagsDataSet(ds);
        this.y= SemanticOps.ytagsDataSet(ds);
        this.z= ds;        
    }
    
    @Override
    public int sizeX() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Bin getXBin(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int sizeY() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Bin getYBin(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getZ(int i, int i1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
