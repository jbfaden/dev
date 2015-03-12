/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdi.data;

import com.google.common.base.Optional;

/**
 *
 * @author jbf
 */
public interface XYZData extends SimpleXYZData, Described<XYZMetadata> {

    /**
     * note that the fill data detector only works on Z; it is assumed that X
     * and Y are the dependent variables and they are never fill
     *
     * @return the fill detector 
     */
    Optional<FillDetector> getZFillDetector();

    
    Optional<UncertaintyProvider> getXUncertProvider();

    Optional<UncertaintyProvider> getYUncertProvider();

    Optional<UncertaintyProvider> getZUncertProvider();

    @Override
    XYZMetadata getMetadata();

}
