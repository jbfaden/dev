
package org.das2.sdi;

import java.util.Optional;

import org.das2.qds.QDataSet;
import org.das2.qds.examples.Schemes;

import sdi.data.BinnedData2D;
import sdi.data.FillDetector2D;
import sdi.data.UncertaintyProvider2D;
import sdi.data.XYZMetadata;

/**
 * Implements a BinnedData2D with a rank 2 dataset.
 * @see Schemes#simpleSpectrogram() 
 * @author faden@cottagesystems.com
 */
class BinnedData2DImpl extends SimpleBinnedData2DImpl implements BinnedData2D {

    public BinnedData2DImpl( QDataSet x, QDataSet y, QDataSet z ) {
        super( x,y,z );
    }
    
    public BinnedData2DImpl(QDataSet ds) {
        super( ds );
    }
    
    @Override
    public Optional<FillDetector2D> getFillDetector() {
        return Adapter.getFillDetector2D(z);
    }

    @Override
    public Optional<UncertaintyProvider2D> getZUncertProvider() {
        return Adapter.getUncertaintyProvider2D(z);
    }

    @Override
    public XYZMetadata getMetadata() {
        return new XYZMetadataImpl( x, y, z );
    }

}
