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
public interface BinnedData2D extends SimpleBinnedData2D, Described<XYZMetadata> {
  Optional<FillDetector2D> getFillDetector();
  Optional<UncertaintyProvider> getZUncertProvider();

  @Override
  XYZMetadata getMetadata();
}