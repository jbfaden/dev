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
public interface XYData extends SimpleXYData, MetadataSrc<XYMetadata> {
  Optional<FillDetector> getFillDetector();
  Optional<UncertaintyProvider> getXUncertProvider();
  Optional<UncertaintyProvider> getYUncertProvider();

  @Override
  XYMetadata getMetadata();
}
