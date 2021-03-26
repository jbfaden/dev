
package org.das2.sdi;

import java.util.Optional;

import org.das2.datum.Units;
import org.das2.qds.AbstractRank1DataSet;
import org.das2.qds.MutablePropertyDataSet;
import org.das2.qds.QDataSet;

import sdi.data.ContiguousBinnedData1D;
import sdi.data.FillDetector;
import sdi.data.XYMetadata;

/**
 * Adapts {@link ContiguousBinnedData1D} to an equivalent {@link QDataSet}.
 * 
 * @author faden@cottagesystems.com
 */
public class ContiguousBinnedData1DAdapter extends SimpleContiguousBinnedData1DAdapter {

  /**
   * Return a {@link QDataSet} for an input instance of type {@link ContiguousBinnedData1D}. This
   * method returns the result of calling {@link #adapt(ContiguousBinnedData1D, Double)}, passing null
   * for the fill argument.
   * 
   * @param data the ContiguousBinnedData1D
   * @return an equivalent QDataSet
   */
  public static QDataSet adapt(ContiguousBinnedData1D data) {
    return adapt(data, null);
  }

  /**
   * Return a {@link QDataSet} for an input instance of type {@link ContiguousBinnedData1D}.
   * <p>
   * QDataSet doesn't have an explicit constraint on the bins like ContiguousBinnedData1D, so we just
   * have to line things up.
   * <p>
   * If the input data's optional "Fill" detector (returned by the
   * {@link ContiguousBinnedData1D#getFillDetector()} method) is present, the output data set will
   * return one specific special value whenever Fill is detected by the detector. The special value
   * used is controlled by the specified fill argument.
   * <p>
   * If the specified fill argument is non-null, its value is used as the special "Fill" value. If the
   * fill argument is null, {@link QDataSet#DEFAULT_FILL_VALUE} will be used instead.
   * <p>
   * The fill argument is also always assigned to the output data set's {@link QDataSet#FILL_VALUE}
   * property AS-IS, whether or not it is null, and whether or not the fill detector is present. This
   * gives the caller complete control over the output data set's Fill handling.
   * 
   * @param data the ContiguousBinnedData1D
   * @param fill the value to return if fill is detected, and to use for the
   *        {@link QDataSet#FILL_VALUE} property
   * @return a QDataSet
   */
  public static QDataSet adapt(ContiguousBinnedData1D data, Double fill) {
    MutablePropertyDataSet dep0 = getX(data);

    MutablePropertyDataSet ds;
    Optional<FillDetector> fillDetector = data.getFillDetector();
    if (fillDetector.isPresent()) {
      ds = getY(data, fillDetector.get(), fill);
    } else {
      ds = getY(data);
    }

    XYMetadata meta = data.getMetadata();

    dep0.putProperty(QDataSet.UNITS, Units.lookupUnits(meta.getXUnits().getName()));
    dep0.putProperty(QDataSet.LABEL, meta.getXLabel());
    dep0.putProperty(QDataSet.NAME, meta.getXName());

    ds.putProperty(QDataSet.DEPEND_0, dep0);
    ds.putProperty(QDataSet.DELTA_MINUS, Adapter.getUPAdapter(ds, data.getUncertProvider(), false));
    ds.putProperty(QDataSet.DELTA_PLUS, Adapter.getUPAdapter(ds, data.getUncertProvider(), true));
    ds.putProperty(QDataSet.UNITS, Units.lookupUnits(meta.getYUnits().getName()));
    ds.putProperty(QDataSet.LABEL, meta.getYLabel());
    ds.putProperty(QDataSet.NAME, meta.getYName());
    ds.putProperty(QDataSet.FILL_VALUE, fill);

    return ds;
  }

  protected static MutablePropertyDataSet getY(ContiguousBinnedData1D data, FillDetector fillDetector, Double fill) {
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
