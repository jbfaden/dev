
package org.das2.sdi;

import java.util.Optional;

import org.das2.datum.Units;
import org.das2.qds.AbstractDataSet;
import org.das2.qds.MutablePropertyDataSet;
import org.das2.qds.QDataSet;

import sdi.data.BinnedData2D;
import sdi.data.FillDetector2D;
import sdi.data.SimpleBinnedData2D;
import sdi.data.XYZMetadata;

/**
 * Adapts {@link BinnedData2D} to an equivalent {@link QDataSet}.
 * 
 * @author faden@cottagesystems.com
 */
public class BinnedData2DAdapter extends SimpleBinnedData2DAdapter {

  /**
   * Return a {@link QDataSet} for an input instance of type {@link BinnedData2D}. This method returns
   * the result of calling {@link #adapt(BinnedData2D, Double)}, passing null for the fill argument.
   * 
   * @param data the BinnedData2D
   * @return an equivalent QDataSet
   */
  public static QDataSet adapt(BinnedData2D data) {
    return adapt(data, null);
  }

  /**
   * Return a {@link QDataSet} for an input instance of type {@link BinnedData2D}. If the input data's
   * optional "Fill" detector (returned by the {@link BinnedData2D#getFillDetector()} method) is
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
   * @param data the BinnedData2D
   * @param fill the value to return if fill is detected, and to use for the
   *        {@link QDataSet#FILL_VALUE} property
   * @return a QDataSet
   */
  public static QDataSet adapt(BinnedData2D data, Double fill) {

    MutablePropertyDataSet dep0 = getX(data);
    MutablePropertyDataSet dep1 = getY(data);

    MutablePropertyDataSet ds;
    Optional<FillDetector2D> fillDetector = data.getFillDetector();
    if (fillDetector.isPresent()) {
      ds = getZ(data, fillDetector.get(), fill);
    } else {
      ds = getZ(data);
    }

    XYZMetadata md = data.getMetadata();
    Units xUnits = Units.lookupUnits(md.getXUnits().getName());
    Units yUnits = Units.lookupUnits(md.getYUnits().getName());
    Units zUnits = Units.lookupUnits(md.getZUnits().getName());

    addUnits(dep0.property(QDataSet.BIN_PLUS), xUnits);
    addUnits(dep0.property(QDataSet.BIN_MINUS), xUnits);

    addUnits(dep1.property(QDataSet.BIN_PLUS), yUnits);
    addUnits(dep1.property(QDataSet.BIN_MINUS), yUnits);


    ds.putProperty(QDataSet.DEPEND_0, dep0);
    ds.putProperty(QDataSet.DEPEND_1, dep1);

    MutablePropertyDataSet xBinMinus = (MutablePropertyDataSet) dep0.property(QDataSet.BIN_MINUS);
    xBinMinus.putProperty(QDataSet.UNITS, xUnits.getOffsetUnits());
    MutablePropertyDataSet xBinPlus = (MutablePropertyDataSet) dep0.property(QDataSet.BIN_PLUS);
    xBinPlus.putProperty(QDataSet.UNITS, xUnits.getOffsetUnits());
    dep0.putProperty(QDataSet.UNITS, xUnits);
    dep0.putProperty(QDataSet.LABEL, md.getXLabel());
    dep0.putProperty(QDataSet.NAME, md.getXName());
    MutablePropertyDataSet yBinMinus = (MutablePropertyDataSet) dep1.property(QDataSet.BIN_MINUS);
    yBinMinus.putProperty(QDataSet.UNITS, yUnits.getOffsetUnits());
    MutablePropertyDataSet yBinPlus = (MutablePropertyDataSet) dep1.property(QDataSet.BIN_PLUS);
    yBinPlus.putProperty(QDataSet.UNITS, yUnits.getOffsetUnits());
    dep1.putProperty(QDataSet.UNITS, yUnits);
    dep1.putProperty(QDataSet.LABEL, md.getYLabel());
    dep1.putProperty(QDataSet.NAME, md.getYName());
    ds.putProperty(QDataSet.UNITS, zUnits);
    ds.putProperty(QDataSet.LABEL, md.getZLabel());
    ds.putProperty(QDataSet.NAME, md.getZName());
    ds.putProperty(QDataSet.TITLE, md.getName());
    ds.putProperty(QDataSet.FILL_VALUE, fill);

    return ds;
  }

  protected static MutablePropertyDataSet getZ(SimpleBinnedData2D data, FillDetector2D fillDetector, Double fill) {
    double fillToUse = fill != null ? fill.doubleValue() : QDataSet.DEFAULT_FILL_VALUE;

    return new AbstractDataSet() {
      @Override
      public int rank() {
        return 2;
      }

      @Override
      public double value(int i, int j) {
        return fillDetector.isFill(i, j) ? fillToUse : data.getZ(i, j);
      }

      @Override
      public int length() {
        return data.sizeX();
      }

      @SuppressWarnings("unused")
      @Override
      public int length(int i) {
        return data.sizeY();
      }
    };
  }

  private static void addUnits(Object property, Units u) {
    MutablePropertyDataSet qds = (MutablePropertyDataSet) property;
    qds.putProperty(QDataSet.UNITS, u);
  }
}
