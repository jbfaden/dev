/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdi.data;

import com.google.common.base.Optional;

/**
 * Z as a function of X and Y, possibly containing fill values and uncertainties.
 * <img src="http://jfaden.net/~jbf/autoplot/renderings/XYZData.png" alt="XYZData.png">
 *
 * @author faden@cottagesystems.com
 */
public interface XYZData extends SimpleXYZData, MetadataSrc<XYZMetadata> {

    /**
     * get the fill detector for the Z values.
     * Note that the fill data detector only works on Z; it is assumed that X
     * and Y are the dependent variables and they are never fill.
     *
     * @return the fill detector
     */
    Optional<FillDetector> getZFillDetector();

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
     * get the uncertainty provider for Z values.
     * @return the uncertainty provider for Z values.
     */    
    Optional<UncertaintyProvider> getZUncertProvider();

    /**
     * return the metadata for the object.
     * @return the metadata 
     */    
    @Override
    XYZMetadata getMetadata();

}
