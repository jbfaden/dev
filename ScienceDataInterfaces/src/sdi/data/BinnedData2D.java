/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdi.data;

import com.google.common.base.Optional;

/**
 * two-index table of numbers, each tagged with an X and a Y bin, and the
 * Z value may be fill or have uncertainties.
 * @author jbf
 */
public interface BinnedData2D extends SimpleBinnedData2D, MetadataSrc<XYZMetadata> {

    Optional<FillDetector2D> getFillDetector();

    Optional<UncertaintyProvider> getZUncertProvider();

    /**
     * return the metadata for the object.
     * @return the metadata 
     */    
    @Override
    XYZMetadata getMetadata();
}
