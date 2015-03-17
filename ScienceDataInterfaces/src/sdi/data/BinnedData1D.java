
package sdi.data;

import com.google.common.base.Optional;

/**
 * XY data where the X dimension has bins associated with it and Y
 * is a dependent variable, and also possibly containing fill and 
 * uncertainties.
 * @author faden@cottagesystems.com
 */
public interface BinnedData1D extends SimpleBinnedData1D, MetadataSrc<XYMetadata> {

    Optional<FillDetector> getFillDetector();

    Optional<UncertaintyProvider> getYUncertProvider();

    /**
     * return the metadata for the object.
     * @return the metadata 
     */    
    @Override
    XYMetadata getMetadata();
}
