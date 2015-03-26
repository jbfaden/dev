
package org.das2.sdi;

import java.util.Optional;
import org.virbo.dataset.QDataSet;
import sdi.data.ContiguousBinnedData1D;
import sdi.data.FillDetector;
import sdi.data.UncertaintyProvider;
import sdi.data.XYMetadata;

/**
 *
 * @author faden@cottagesystems.com
 */
class ContiguousBinnedData1DImpl extends SimpleContiguousBinnedData1DImpl implements ContiguousBinnedData1D {

    public ContiguousBinnedData1DImpl(QDataSet ds) {
        super(ds);
    }

    public ContiguousBinnedData1DImpl(QDataSet xlow, QDataSet xhigh, QDataSet y) {
        super(xlow, xhigh, y);
    }

    @Override
    public Optional<FillDetector> getFillDetector() {
        return Adapter.getFillDetector(y);
    }

    @Override
    public Optional<UncertaintyProvider> getUncertProvider() {
        return Adapter.getUncertaintyProvider(y);
    }

    @Override
    public XYMetadata getMetadata() {
        return new XYMetadataImpl( x, y );
    }

}
