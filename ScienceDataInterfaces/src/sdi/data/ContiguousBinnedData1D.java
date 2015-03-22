package sdi.data;

import com.google.common.base.Optional;

/**
 * <p>
 * The data values lay in contiguous bins.</p>
 * <img src="http://jfaden.net/~jbf/autoplot/renderings/ContiguousBinnedData1D.png" alt="ContiguousBinnedData1D.png">
 * <p>These bins may contain fill data.</p>
 *
 * @author jbf
 */
public interface ContiguousBinnedData1D extends SimpleContiguousBinnedData1D, MetadataSrc<XYMetadata> {

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
    Optional<UncertaintyProvider> getUncertProvider();

    /**
     * return the metadata for the object.
     * @return the metadata 
     */
    @Override
    XYMetadata getMetadata();
}
