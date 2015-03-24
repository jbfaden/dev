
package org.das2.sdi;

import com.google.common.base.Optional;
import org.virbo.dataset.QDataSet;
import sdi.data.BinnedData1D;
import sdi.data.FillDetector;
import sdi.data.UncertaintyProvider;
import sdi.data.XYMetadata;

/**
 *
 * @author faden@cottagesystems.com
 */
public class BinnedData1DImpl extends SimpleBinnedData1DImpl implements BinnedData1D {

    public BinnedData1DImpl(QDataSet ds) {
        super(ds);
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
