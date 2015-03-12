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
public interface ContiguousBinnedData1D extends SimpleContiguousBinnedData1D, Described<XYMetadata> {
  Optional<FillDetector> getFillDetector();
  Optional<UncertaintyProvider> getUncertProvider();

  @Override
  XYMetadata getMetadata();
}