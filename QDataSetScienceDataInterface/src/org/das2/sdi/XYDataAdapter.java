
package org.das2.sdi;

import java.util.Optional;

import org.das2.datum.Units;
import org.das2.qds.AbstractRank1DataSet;
import org.das2.qds.MutablePropertyDataSet;
import org.das2.qds.QDataSet;

import sdi.data.FillDetector;
import sdi.data.XYData;
import sdi.data.XYMetadata;

/**
 * Adapts {@link XYData} to an equivalent {@link QDataSet}.
 * 
 * @author faden@cottagesystems.com
 */
public class XYDataAdapter extends SimpleXYDataAdapter {

  /**
   * Return a {@link QDataSet} for an input instance of type {@link XYData}. This method returns the
   * result of calling {@link #adapt(XYData, Double)}, passing null for the fill argument.
   * 
   * @param xydata the XYData
   * @return an equivalent QDataSet
   */
  public static QDataSet adapt(XYData xydata) {
    return adapt(xydata, null);
  }

  /**
   * Return a {@link QDataSet} for an input instance of type {@link XYData}. If the input data's
   * optional "Fill" detector (returned by the {@link XYData#getFillDetector()} method) is present,
   * the output data set will return one specific special value whenever Fill is detected by the
   * detector. The special value used is controlled by the specified fill argument.
   * <p>
   * If the specified fill argument is non-null, its value is used as the special "Fill" value. If the
   * fill argument is null, {@link QDataSet#DEFAULT_FILL_VALUE} will be used instead.
   * <p>
   * The fill argument is also always assigned to the output data set's {@link QDataSet#FILL_VALUE}
   * property AS-IS, whether or not it is null, and whether or not the fill detector is present. This
   * gives the caller complete control over the output data set's Fill handling.
   * 
   * @param xydata the XYData
   * @param fill the value to return if fill is detected, and to use for the
   *        {@link QDataSet#FILL_VALUE} property
   * @return a QDataSet
   */
  public static QDataSet adapt(XYData xydata, Double fill) {
    MutablePropertyDataSet x = getX(xydata);

    MutablePropertyDataSet y;
    Optional<FillDetector> fillDetector = xydata.getFillDetector();
    if (fillDetector.isPresent()) {
      y = getY(xydata, fillDetector.get(), fill);
    } else {
      y = getY(xydata);
    }

    y.putProperty(QDataSet.DEPEND_0, x);
    XYMetadata meta = xydata.getMetadata();
    x.putProperty(QDataSet.UNITS, Units.lookupUnits(meta.getXUnits().getName()));
    x.putProperty(QDataSet.LABEL, meta.getXLabel());
    x.putProperty(QDataSet.NAME, meta.getXName());
    x.putProperty(QDataSet.DELTA_MINUS, Adapter.getUPAdapter(x, xydata.getXUncertProvider(), false));
    x.putProperty(QDataSet.DELTA_PLUS, Adapter.getUPAdapter(x, xydata.getXUncertProvider(), true));
    y.putProperty(QDataSet.UNITS, Units.lookupUnits(meta.getYUnits().getName()));
    y.putProperty(QDataSet.LABEL, meta.getYLabel());
    y.putProperty(QDataSet.NAME, meta.getYName());
    y.putProperty(QDataSet.DELTA_MINUS, Adapter.getUPAdapter(y, xydata.getYUncertProvider(), false));
    y.putProperty(QDataSet.DELTA_PLUS, Adapter.getUPAdapter(y, xydata.getYUncertProvider(), true));
    y.putProperty(QDataSet.TITLE, meta.getName());
    y.putProperty(QDataSet.FILL_VALUE, fill);

    return y;
  }

  protected static MutablePropertyDataSet getY(XYData xydata, FillDetector fillDetector, Double fill) {
    double fillToUse = fill != null ? fill.doubleValue() : QDataSet.DEFAULT_FILL_VALUE;

    AbstractRank1DataSet result = new AbstractRank1DataSet(xydata.size()) {
      @Override
      public double value(int i) {
        return fillDetector.isFill(i) ? fillToUse : xydata.getY(i);
      }
    };

    return result;
  }

}
