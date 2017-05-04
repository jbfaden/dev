package sdi.functions;

import java.util.Optional;

import sdi.data.FillDetector;
import sdi.data.XYMetadata;

/**
 * Interface representing a univariate function of double returning double, with associated metadata and fill
 * detection.
 * 
 * @author James.Peachey@jhuapl.edu
 *
 */
public interface ScalarFunctionOfOneArg extends SimpleScalarFunctionOfOneArg, Source<XYMetadata> {
    /**
     * return the fill detector indicating if a value is valid or fill
     * (non-valid measurement). This is Optional, in case all the data are valid.
     *
     * @return the fill detector
     */
    Optional<FillDetector> getFillDetector();
}
