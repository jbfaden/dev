package sdi.util;

import sdi.data.Units;
import sdi.data.XYMetadata;

/**
 * Mostly concrete implementation of {@link XYMetadata} that by default forwards all its methods to
 * the corresponding methods of another metadata instance. Extend this class to wrap a metadata
 * instance, overrirding methods to change one or more attributes as desired.
 * 
 * @author James Peachey, JHUAPL
 *
 */
public abstract class ForwardingXYMetadata implements XYMetadata {

  @Override
  public String getName() {
    return getOrig().getName();
  }

  @Override
  public Units getXUnits() {
    return getOrig().getXUnits();
  }

  @Override
  public Units getYUnits() {
    return getOrig().getYUnits();
  }

  @Override
  public String getXName() {
    return getOrig().getXName();
  }

  @Override
  public String getYName() {
    return getOrig().getYName();
  }

  @Override
  public String getXLabel() {
    return getOrig().getXLabel();
  }

  @Override
  public String getYLabel() {
    return getOrig().getYLabel();
  }

  /**
   * Return the original {@link XYMetadata} instance so its properties are available to the
   * forwarding intance. May not return null.
   * 
   * @return the metadata
   */
  protected abstract XYMetadata getOrig();

  @Override
  public String toString() {
    return getOrig().toString();
  }

}
