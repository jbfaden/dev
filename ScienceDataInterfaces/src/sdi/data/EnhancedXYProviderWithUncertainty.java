/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdi.data;

import com.google.common.base.Optional;


/**
 * TODO: I don't think this class is needed.
 * @author jbf
 */
public interface EnhancedXYProviderWithUncertainty extends XYData {
    Optional<UncertaintyProvider> getUncertProviderX();
    Optional<UncertaintyProvider> getUncertProviderY();
}
