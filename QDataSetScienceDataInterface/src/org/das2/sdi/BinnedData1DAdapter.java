
package org.das2.sdi;

import java.util.Optional;

import org.das2.datum.Units;
import org.das2.qds.AbstractRank1DataSet;
import org.das2.qds.MutablePropertyDataSet;
import org.das2.qds.QDataSet;

import sdi.data.BinnedData1D;
import sdi.data.FillDetector;
import sdi.data.XYMetadata;

/**
 * Adapts {@link BinnedData1D} to an equivalent {@link QDataSet}.
 * 
 * @author faden@cottagesystems.com
 */
public class BinnedData1DAdapter extends SimpleBinnedData1DAdapter {

  /**
   * Return a {@link QDataSet} for an input instance of type {@link BinnedData1D}. This method returns
   * the result of calling {@link #adapt(BinnedData1D, Double)}, passing null for the fill argument.
   * 
   * @param data the BinnedData1D
   * @return an equivalent QDataSet
   */
  public static QDataSet adapt(BinnedData1D data) {
    return adapt(data, null);
  }

  /**
   * Return a {@link QDataSet} for an input instance of type {@link BinnedData1D}. If the input data's
   * optional "Fill" detector (returned by the {@link BinnedData1D#getFillDetector()} method) is
   * present, the output data set will return one specific special value whenever Fill is detected by
   * the detector. The special value used is controlled by the specified fill argument.
   * <p>
   * If the specified fill argument is non-null, its value is used as the special "Fill" value. If the
   * fill argument is null, {@link QDataSet#DEFAULT_FILL_VALUE} will be used instead.
   * <p>
   * The fill argument is also always assigned to the output data set's {@link QDataSet#FILL_VALUE}
   * property AS-IS, whether or not it is null, and whether or not the fill detector is present. This
   * gives the caller complete control over the output data set's Fill handling.
   * 
   * @param data the BinnedData1D
   * @param fill the value to return if fill is detected, and to use for the
   *        {@link QDataSet#FILL_VALUE} property
   * @return a QDataSet
   */
  public static QDataSet adapt(BinnedData1D data, Double fill) {
    MutablePropertyDataSet x = getX(data);

    MutablePropertyDataSet y;
    Optional<FillDetector> fillDetector = data.getFillDetector();
    if (fillDetector.isPresent()) {
      y = getY(data, fillDetector.get(), fill);
    } else {
      y = getY(data);
    }

    XYMetadata meta = data.getMetadata();
    x.putProperty(QDataSet.UNITS, Units.lookupUnits(meta.getXUnits().getName()));
    x.putProperty(QDataSet.LABEL, meta.getXLabel());
    x.putProperty(QDataSet.NAME, meta.getXName());
    y.putProperty(QDataSet.UNITS, Units.lookupUnits(meta.getYUnits().getName()));
    y.putProperty(QDataSet.LABEL, meta.getYLabel());
    y.putProperty(QDataSet.NAME, meta.getYName());
    y.putProperty(QDataSet.DELTA_MINUS, Adapter.getUPAdapter(y, data.getYUncertProvider(), false));
    y.putProperty(QDataSet.DELTA_PLUS, Adapter.getUPAdapter(y, data.getYUncertProvider(), true));
    y.putProperty(QDataSet.DEPEND_0, x);
    y.putProperty(QDataSet.FILL_VALUE, fill);

    return y;
  }

  protected static MutablePropertyDataSet getY(BinnedData1D data, FillDetector fillDetector, Double fill) {
    double fillToUse = fill != null ? fill.doubleValue() : QDataSet.DEFAULT_FILL_VALUE;

    AbstractRank1DataSet result = new AbstractRank1DataSet(data.size()) {
      @Override
      public double value(int i) {
        return fillDetector.isFill(i) ? fillToUse : data.getY(i);
      }
    };

    return result;
  }
}
