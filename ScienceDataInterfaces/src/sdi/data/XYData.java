
package sdi.data;

import com.google.common.base.Optional;

/**
 * Y as a function of X, possibly containing fill values and uncertainties.
 * @author faden@cottagesystems.com
 */
public interface XYData extends SimpleXYData, MetadataSrc<XYMetadata> {

    /**
     * get the fill detector for the values. 
     * @return the fill detector.
     */
    Optional<FillDetector> getFillDetector();

    /**
     * get the uncertainty provider for X values.
     * @return the uncertainty provider for X values.
     */
    Optional<UncertaintyProvider> getXUncertProvider();

    /**
     * get the uncertainty provider for Y values.
     * @return the uncertainty provider for Y values.
     */
    Optional<UncertaintyProvider> getYUncertProvider();

    /**
     * return the metadata for the object.
     * @return the metadata 
     */
    @Override
    XYMetadata getMetadata();
}
