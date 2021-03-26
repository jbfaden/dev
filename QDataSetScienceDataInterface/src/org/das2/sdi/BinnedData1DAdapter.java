
package org.das2.sdi;

import org.das2.datum.Units;
import org.das2.qds.MutablePropertyDataSet;
import org.das2.qds.QDataSet;

import sdi.data.BinnedData1D;
import sdi.data.XYMetadata;

/**
 * Adapts BinnedData1D to QDataSet
 * 
 * @author faden@cottagesystems.com
 */
public class BinnedData1DAdapter extends SimpleBinnedData1DAdapter {

  /**
   * return a QDataSet for BinnedData1D
   * 
   * @param data the BinnedData1D
   * @return a QDataSet
   */
  public static QDataSet adapt(BinnedData1D data) {
    MutablePropertyDataSet x = getX(data);
    MutablePropertyDataSet y = getY(data);
    XYMetadata meta = data.getMetadata();
    x.putProperty(QDataSet.UNITS, Units.lookupUnits(meta.getXUnits().getName()));
    x.putProperty(QDataSet.LABEL, meta.getXLabel());
    x.putProperty(QDataSet.NAME, meta.getXName());
    y.putProperty(QDataSet.UNITS, Units.lookupUnits(meta.getYUnits().getName()));
    y.putProperty(QDataSet.LABEL, meta.getYLabel());
    y.putProperty(QDataSet.NAME, meta.getYName());
    y.putProperty(QDataSet.DELTA_MINUS, Adapter.getUPAdapter(y, data.getYUncertProvider(), false));
    y.putProperty(QDataSet.DELTA_PLUS, Adapter.getUPAdapter(y, data.getYUncertProvider(), true));
    y.putProperty(QDataSet.WEIGHTS, Adapter.getWeights(y, data.getFillDetector()));
    y.putProperty(QDataSet.DEPEND_0, x);
    return y;
  }
}
