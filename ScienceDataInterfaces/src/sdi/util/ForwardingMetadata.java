package sdi.util;

import sdi.data.Metadata;
import sdi.data.Units;

/**
 * Mostly concrete implementation of {@link Metadata} that by default forwards all its methods to
 * the corresponding methods of another metadata instance. Extend this class to wrap a metadata
 * instance, overrirding methods to change one or more attributes as desired.
 * 
 * @author James Peachey, JHUAPL
 *
 */
public abstract class ForwardingMetadata implements Metadata {

  @Override
  public Units getUnits() {
    return getOrig().getUnits();
  }

  @Override
  public String getName() {
    return getOrig().getName();
  }

  @Override
  public String getLabel() {
    return getOrig().getLabel();
  }

  /**
   * Return the original {@link Metadata} instance so its properties are available to the forwarding
   * intance. May not return null.
   * 
   * @return the metadata
   */
  protected abstract Metadata getOrig();

  @Override
  public String toString() {
    return getOrig().toString();
  }

}
