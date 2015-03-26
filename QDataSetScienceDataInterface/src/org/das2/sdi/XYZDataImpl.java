
package org.das2.sdi;

import java.util.Optional;
import org.virbo.dataset.QDataSet;
import sdi.data.FillDetector;
import sdi.data.UncertaintyProvider;
import sdi.data.XYZData;
import sdi.data.XYZMetadata;

/**
 *
 * @author faden@cottagesystems.com
 */
class XYZDataImpl extends SimpleXYZDataImpl implements XYZData {
            
    public XYZDataImpl( QDataSet x, QDataSet y, QDataSet z ) {
        super( x,y,z );
    }
    
    public XYZDataImpl( QDataSet ds ) {
        super( ds );
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
    
}
