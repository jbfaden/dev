
package sdi.data;

import com.google.common.base.Optional;

/**
 * XY data where the X dimension has bins associated with it and Y
 * is a dependent variable, and also possibly containing fill and 
 * uncertainties.
 * @author faden@cottagesystems.com
 */
public interface BinnedData1D extends SimpleBinnedData1D, MetadataSrc<XYMetadata> {

    /**
     * return the fill detector indicating if a value is valid or fill
     * (non-valid measurement). This is Optional, in case all the data is valid.
     *
     * @return the fill detector
     */    
    Optional<FillDetector> getFillDetector();

    /**
     * return the uncertainty provider, indicating the 1-sigma confidence
     * interval for the data.
     *
     * @return the uncertainty provider
     */    
    Optional<UncertaintyProvider> getYUncertProvider();

    /**
     * return the metadata for the object.
     * @return the metadata 
     */    
    @Override
    XYMetadata getMetadata();
}
