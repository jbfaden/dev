package sdi.util;

import sdi.data.Units;
import sdi.data.XYZMetadata;

/**
 * Mostly concrete implementation of {@link XYZMetadata} that by default forwards all its methods to
 * the corresponding methods of another metadata instance. Extend this class to wrap a metadata
 * instance, overrirding methods to change one or more attributes as desired.
 * 
 * @author James Peachey, JHUAPL
 *
 */
public abstract class ForwardingXYZMetadata implements XYZMetadata {

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
  public Units getZUnits() {
    return getOrig().getZUnits();
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
  public String getZName() {
    return getOrig().getZName();
  }

  @Override
  public String getXLabel() {
    return getOrig().getXLabel();
  }

  @Override
  public String getYLabel() {
    return getOrig().getYLabel();
  }

  @Override
  public String getZLabel() {
    return getOrig().getZLabel();
  }

  /**
   * Return the original {@link XYZMetadata} instance so its properties are available to the
   * forwarding intance. May not return null.
   * 
   * @return the metadata
   */
  protected abstract XYZMetadata getOrig();

  @Override
  public String toString() {
    return getOrig().toString();
  }

}
