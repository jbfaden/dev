/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.das2.sdi;

import java.util.Optional;
import org.virbo.dataset.QDataSet;
import sdi.data.FillDetector;
import sdi.data.UncertaintyProvider;
import sdi.data.XYData;
import sdi.data.XYMetadata;

/**
 * Adapt QDataSets to an XYData.  This will take a rank 1 QDataSet with 
 * DEPEND_0 tags, or two rank 1 QDataSets.
 * @author faden@cottagesystems.com
 */
public class XYDataImpl extends SimpleXYDataImpl implements XYData {

    public XYDataImpl( QDataSet x, QDataSet y ) {
        super( x, y );
    }
    
    public XYDataImpl( QDataSet source ) {
        super( source );
    }
    
    @Override
    public Optional<FillDetector> getFillDetector() {
        return Adapter.getFillDetector(y);
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
    public XYMetadata getMetadata() {
        return new XYMetadataImpl( x, y );
    }
    
}
