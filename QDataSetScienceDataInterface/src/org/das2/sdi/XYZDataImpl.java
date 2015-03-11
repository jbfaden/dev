/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.das2.sdi;

import com.google.common.base.Optional;
import org.virbo.dataset.DataSetOps;
import org.virbo.dataset.QDataSet;
import org.virbo.dataset.SemanticOps;
import sdi.data.FillDetector;
import sdi.data.UncertaintyProvider;
import sdi.data.XYZData;
import sdi.data.XYZMetadata;

/**
 *
 * @author jbf
 */
public class XYZDataImpl implements XYZData {

    QDataSet x;
    QDataSet y;
    QDataSet z;
            
    public XYZDataImpl( QDataSet ds ) {
        if ( ds.rank()==2 ) {
            x= SemanticOps.xtagsDataSet(ds);
            y= SemanticOps.ytagsDataSet(ds);
            z= DataSetOps.unbundleDefaultDataSet(ds);
        } else if ( ds.rank()==1 ) {
            x= SemanticOps.xtagsDataSet(ds);
            y= SemanticOps.ytagsDataSet(ds);
            z= (QDataSet) ds.property(QDataSet.PLANE_0);
        }
    }
    
    @Override
    public Optional<FillDetector> getZFillDetector() {
        return Adapter.getFillDetector(z);
    }

    @Override
    public Optional<UncertaintyProvider> getXUncertProvider() {
        return Adapter.getUncertaintyProvider(x);
    }

    @Override
    public Optional<UncertaintyProvider> getYUncertProvider() {
        return Adapter.getUncertaintyProvider(y);
    }

    @Override
    public Optional<UncertaintyProvider> getZUncertProvider() {
        return Adapter.getUncertaintyProvider(z);
    }

    @Override
    public XYZMetadata getMetadata() {
        return new XYZMetadataImpl(x, y, z);
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getX(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getY(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getZ(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
