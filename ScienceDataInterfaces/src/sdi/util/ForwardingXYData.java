package sdi.util;

import java.util.Optional;
import sdi.data.FillDetector;
import sdi.data.UncertaintyProvider;
import sdi.data.XYData;
import sdi.data.XYMetadata;

public class ForwardingXYData implements XYData {
  private final XYData data;

  public ForwardingXYData(XYData data) {
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
  public Optional<FillDetector> getFillDetector() {
    return data.getFillDetector();
  }

  @Override
  public Optional<UncertaintyProvider> getXUncertProvider() {
    return data.getXUncertProvider();
  }

  @Override
  public Optional<UncertaintyProvider> getYUncertProvider() {
    return data.getYUncertProvider();
  }

  @Override
  public XYMetadata getMetadata() {
    return data.getMetadata();
  }

  @Override
  public String toString() {
    return data.toString();
  }

}
