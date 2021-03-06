
package org.das2.sdi;

import java.util.Optional;

import org.das2.qds.QDataSet;

import sdi.data.BinnedData1D;
import sdi.data.FillDetector;
import sdi.data.UncertaintyProvider;
import sdi.data.XYMetadata;

/**
 *
 * @author faden@cottagesystems.com
 */
class BinnedData1DImpl extends SimpleBinnedData1DImpl implements BinnedData1D {

    public BinnedData1DImpl( QDataSet x, QDataSet y ) {
        super( x, y );
    }
    
    public BinnedData1DImpl(QDataSet ds) {
        super( ds );
    }
    
    @Override
    public Optional<FillDetector> getFillDetector() {
        return Adapter.getFillDetector(y);
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
