
package org.das2.sdi;

import java.util.Optional;

import org.das2.datum.Units;
import org.das2.qds.AbstractRank1DataSet;
import org.das2.qds.MutablePropertyDataSet;
import org.das2.qds.QDataSet;
import org.das2.qds.ops.Ops;

import sdi.data.FillDetector;
import sdi.data.XYZData;
import sdi.data.XYZMetadata;

/**
 * Adapts {@link XYZData} to an equivalent {@link QDataSet}.
 * 
 * @author faden@cottagesystems.com
 */
public class XYZDataAdapter extends SimpleXYZDataAdapter {

  /**
   * Return a {@link QDataSet} for an input instance of type {@link XYZData}. This method returns the
   * result of calling {@link #adapt(XYZData, Double)}, passing null for the fill argument.
   * 
   * @param xyzdata the XYZData
   * @return an equivalent QDataSet
   */
  public static QDataSet adapt(XYZData xyzdata) {
    return adapt(xyzdata, null);
  }

  /**
   * Return a {@link QDataSet} for an input instance of type {@link XYZData}. If the input data's
   * optional "Fill" detector (returned by the {@link XYZData#getZFillDetector()} method) is present,
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
   * @param xyzdata the XYZData
   * @param fill the value to return if fill is detected, and to use for the
   *        {@link QDataSet#FILL_VALUE} property
   * @return a QDataSet
   */
  public static QDataSet adapt(XYZData xyzdata, Double fill) {
    MutablePropertyDataSet x = getX(xyzdata);
    MutablePropertyDataSet y = getY(xyzdata);

    MutablePropertyDataSet z;
    Optional<FillDetector> fillDetector = xyzdata.getZFillDetector();
    if (fillDetector.isPresent()) {
      z = getZ(xyzdata, fillDetector.get(), fill);
    } else {
      z = getZ(xyzdata);
    }

    XYZMetadata meta = xyzdata.getMetadata();

    x.putProperty(QDataSet.UNITS, Units.lookupUnits(meta.getXUnits().getName()));
    x.putProperty(QDataSet.LABEL, meta.getXLabel());
    x.putProperty(QDataSet.NAME, meta.getXName());
    x.putProperty(QDataSet.DELTA_MINUS, Adapter.getUPAdapter(x, xyzdata.getXUncertProvider(), false));
    x.putProperty(QDataSet.DELTA_PLUS, Adapter.getUPAdapter(x, xyzdata.getXUncertProvider(), true));

    y.putProperty(QDataSet.UNITS, Units.lookupUnits(meta.getYUnits().getName()));
    y.putProperty(QDataSet.LABEL, meta.getYLabel());
    y.putProperty(QDataSet.NAME, meta.getYName());
    y.putProperty(QDataSet.DELTA_MINUS, Adapter.getUPAdapter(y, xyzdata.getYUncertProvider(), false));
    y.putProperty(QDataSet.DELTA_PLUS, Adapter.getUPAdapter(y, xyzdata.getYUncertProvider(), true));

    z.putProperty(QDataSet.UNITS, Units.lookupUnits(meta.getZUnits().getName()));
    z.putProperty(QDataSet.LABEL, meta.getZLabel());
    z.putProperty(QDataSet.NAME, meta.getZName());
    z.putProperty(QDataSet.DELTA_MINUS, Adapter.getUPAdapter(z, xyzdata.getZUncertProvider(), false));
    z.putProperty(QDataSet.DELTA_PLUS, Adapter.getUPAdapter(z, xyzdata.getZUncertProvider(), true));
    z.putProperty(QDataSet.FILL_VALUE, fill);

    return Ops.link(x, y, z);

  }

  protected static MutablePropertyDataSet getZ(XYZData xyzdata, FillDetector fillDetector, Double fill) {
    double fillToUse = fill != null ? fill.doubleValue() : QDataSet.DEFAULT_FILL_VALUE;

    AbstractRank1DataSet result = new AbstractRank1DataSet(xyzdata.size()) {
      @Override
      public double value(int i) {
        return fillDetector.isFill(i) ? fillToUse : xyzdata.getZ(i);
      }
    };

    return result;
  }

}
