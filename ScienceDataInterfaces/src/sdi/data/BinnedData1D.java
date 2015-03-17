
package sdi.data;

import com.google.common.base.Optional;

/**
 *
 * @author faden@cottagesystems.com
 */
public interface BinnedData1D extends SimpleBinnedData1D, MetadataSrc<XYMetadata> {

    Optional<FillDetector> getFillDetector();

    Optional<UncertaintyProvider> getYUncertProvider();

    @Override
    XYMetadata getMetadata();
}
