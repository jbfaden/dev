
package org.das2.sdi;

import org.das2.datum.Units;
import org.virbo.dataset.MutablePropertyDataSet;
import org.virbo.dataset.QDataSet;
import sdi.data.XYData;
import sdi.data.XYMetadata;

/**
 * Given an XYData, provide a QDataSet that preserves as much information
 * as possible.
 * @author faden@cottagesystems.com
 */
public class XYDataAdapter extends SimpleXYDataAdapter {
     
    /**
     * return a QDataSet for the xydata
     * @param xydata the xydata
     * @return a QDataSet
     */
    public static QDataSet adapt( XYData xydata ) {
        MutablePropertyDataSet x= getX(xydata);
        MutablePropertyDataSet y= getY(xydata);
        y.putProperty( QDataSet.DEPEND_0, x );
        XYMetadata meta= xydata.getMetadata();
        x.putProperty( QDataSet.UNITS, Units.lookupUnits( meta.getXUnits().getName() ) );
        x.putProperty( QDataSet.LABEL, meta.getXLabel() );
        x.putProperty( QDataSet.NAME, meta.getXName() );
        x.putProperty( QDataSet.DELTA_MINUS, Adapter.getUPAdapter( x, xydata.getXUncertProvider(), false ) );
        x.putProperty( QDataSet.DELTA_PLUS, Adapter.getUPAdapter( x, xydata.getXUncertProvider(), true ) );        
        y.putProperty( QDataSet.UNITS, Units.lookupUnits( meta.getYUnits().getName() ) );
        y.putProperty( QDataSet.LABEL, meta.getYLabel() );
        y.putProperty( QDataSet.NAME, meta.getYName() );
        y.putProperty( QDataSet.DELTA_MINUS, Adapter.getUPAdapter( y, xydata.getYUncertProvider(), false ) );
        y.putProperty( QDataSet.DELTA_PLUS, Adapter.getUPAdapter( y, xydata.getYUncertProvider(), true ) );
        y.putProperty( QDataSet.WEIGHTS, Adapter.getWeights( y, xydata.getFillDetector() ) );
        return y;
    }

}
