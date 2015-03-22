
package sdi.data;

import com.google.common.base.Optional;

/**
 * <p>
 * Y as a function of X, possibly containing fill values and uncertainties.
 * <br>X(i) &rarr; Y(i) where 0 &le; i &lt; <code>size()</code>
 * </p>
 * <p>The figure below represents an XYData with getMetadata().getXUnits() is 
 * Units("seconds since 2015-03-17T00:00Z") and getMetadata().getYLabel() is "Density"</p>
 * <br>
 * <img src="http://jfaden.net/~jbf/autoplot/renderings/XYData.png" alt="XYData.png">
 * @author faden@cottagesystems.com
 */
public interface XYData extends SimpleXYData, MetadataSrc<XYMetadata> {

    /**
     * return the fill detector indicating if a value is valid or fill
     * (non-valid measurement). This is Optional, in case all the data is valid.
     *
     * @return the fill detector
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
