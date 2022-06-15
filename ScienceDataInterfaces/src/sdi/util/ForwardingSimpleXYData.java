package sdi.util;

import sdi.data.SimpleXYData;

public class ForwardingSimpleXYData implements SimpleXYData {
  private final SimpleXYData data;

  public ForwardingSimpleXYData(SimpleXYData data) {
    super();
    this.data = data;
  }

  @Override
  public int size() {
    return data.size();
  }

  @Override
  public double getX(int i) {
    return data.getX(i);
  }

  @Override
  public double getY(int i) {
    return data.getY(i);
  }

  @Override
  public String toString() {
    return data.toString();
  }

}
