
package org.das2.sdi;

import org.das2.datum.Units;
import org.das2.qds.MutablePropertyDataSet;
import org.das2.qds.QDataSet;

import sdi.data.BinnedData2D;
import sdi.data.XYZMetadata;

/**
 * Adapts BinnedData2D to QDataSet
 * 
 * @author faden@cottagesystems.com
 */
public class BinnedData2DAdapter extends SimpleBinnedData2DAdapter {

  /**
   * return a QDataSet for BinnedData1D
   * 
   * @param data the BinnedData1D
   * @return a QDataSet
   */
  public static QDataSet adapt(BinnedData2D data) {

    MutablePropertyDataSet dep0 = getX(data);
    MutablePropertyDataSet dep1 = getY(data);
    MutablePropertyDataSet ds = getZ(data);

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


    return ds;
  }

  private static void addUnits(Object property, Units u) {
    MutablePropertyDataSet qds = (MutablePropertyDataSet) property;
    qds.putProperty(QDataSet.UNITS, u);
  }
}
