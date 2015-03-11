/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.das2.sdi;

import org.virbo.dataset.QDataSet;
import org.virbo.dataset.SemanticOps;
import sdi.data.FillDetector;
import sdi.data.UncertaintyProvider;
import sdi.data.XYData;
import sdi.data.XYMetadata;

/**
 *
 * @author jbf
 */
public class XYDataImpl implements XYData {

    QDataSet y;
    QDataSet x;
    
    public XYDataImpl( QDataSet source ) {
        this.y= source;
        this.x= SemanticOps.xtagsDataSet(source);
    }
    
    @Override
    public com.google.common.base.Optional<FillDetector> getFillDetector() {
        return Adapter.getFillDetector(y);
    }

    @Override
    public com.google.common.base.Optional<UncertaintyProvider> getXUncertProvider() {
        return Adapter.getUncertaintyProvider(x);
    }

    @Override
    public com.google.common.base.Optional<UncertaintyProvider> getYUncertProvider() {
        return Adapter.getUncertaintyProvider(y);
    }

    @Override
    public XYMetadata getMetadata() {
        return new XYMetadataImpl( x, y );
    }

    @Override
    public int size() {
        return x.length();
    }

    @Override
    public double getX(int i) {
        return x.value(i);
    }

    @Override
    public double getY(int i) {
        return y.value(i);
    }

    @Override
    public Object getData() {
        return this;
    }
    
}
