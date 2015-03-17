
package sdi.data;

import com.google.common.base.Optional;

/**
 *
 * @author faden@cottagesystems.com
 */
public interface XYData extends SimpleXYData, MetadataSrc<XYMetadata> {

    Optional<FillDetector> getFillDetector();

    Optional<UncertaintyProvider> getXUncertProvider();

    Optional<UncertaintyProvider> getYUncertProvider();

    @Override
    XYMetadata getMetadata();
}
