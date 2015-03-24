
package org.das2.sdi;

import java.util.Optional;
import org.virbo.dataset.QDataSet;
import sdi.data.BinnedData2D;
import sdi.data.FillDetector2D;
import sdi.data.UncertaintyProvider;
import sdi.data.XYZMetadata;

/**
 *
 * @author faden@cottagesystems.com
 */
public class BinnedData2DImpl extends SimpleBinnedData2DImpl implements BinnedData2D {

    public BinnedData2DImpl(QDataSet ds) {
        super(ds);
    }
    
    @Override
    public Optional<FillDetector2D> getFillDetector() {
        return Adapter.getFillDetector2D(z);
    }

    @Override
    public Optional<UncertaintyProvider> getZUncertProvider() {
        return Optional.empty(); //TODO: UncertaintyProvider2D
    }

    @Override
    public XYZMetadata getMetadata() {
        return new XYZMetadataImpl( x, y, z );
    }

}
