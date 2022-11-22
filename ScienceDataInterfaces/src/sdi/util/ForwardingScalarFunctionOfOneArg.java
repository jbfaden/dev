package sdi.util;

import sdi.data.XYMetadata;
import sdi.functions.ScalarFunctionOfOneArg;

/**
 * Mostly concrete implementation of {@link ScalarFunctionOfOneArg} that by default forwards all its
 * methods to the corresponding methods of another scalar function instance. Extend this class to
 * wrap a scalar function instance, overrirding methods to change one or more attributes as desired.
 * 
 * @author James Peachey, JHUAPL
 *
 */
public abstract class ForwardingScalarFunctionOfOneArg implements ScalarFunctionOfOneArg {

  @Override
  public double evaluate(double t) {
    return getOrig().evaluate(t);
  }

  @Override
  public XYMetadata get() {
    return getOrig().get();
  }

  /**
   * Return the original {@link ScalarFunctionOfOneArg} instance so its properties are available to
   * the forwarding intance. May not return null.
   * 
   * @return the metadata
   */
  protected abstract ScalarFunctionOfOneArg getOrig();

  @Override
  public String toString() {
    return getOrig().toString();
  }

}
