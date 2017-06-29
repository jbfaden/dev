
package org.das2.sdi;

import org.das2.datum.Units;
import org.das2.qds.MutablePropertyDataSet;
import org.das2.qds.QDataSet;
import org.das2.qds.ops.Ops;

import sdi.data.XYZData;
import sdi.data.XYZMetadata;

/**
 * Adapts XYZData to QDataSet
 * @author faden@cottagesystems.com
 */
public class XYZDataAdapter extends SimpleXYZDataAdapter {
     
    /**
     * return a QDataSet for the xyzdata
     * @param xyzdata the xyzdata
     * @return a QDataSet
     */
    public static QDataSet adapt( XYZData xyzdata ) {
        MutablePropertyDataSet x= getX(xyzdata);
        MutablePropertyDataSet y= getY(xyzdata);
        MutablePropertyDataSet z= getZ(xyzdata);
        XYZMetadata meta= xyzdata.getMetadata();
        
        x.putProperty( QDataSet.UNITS, Units.lookupUnits( meta.getXUnits().getName() ) );
        x.putProperty( QDataSet.LABEL, meta.getXLabel() );
        x.putProperty( QDataSet.NAME, meta.getXName() );
        x.putProperty( QDataSet.DELTA_MINUS, Adapter.getUPAdapter( x, xyzdata.getXUncertProvider(), false ) );
        x.putProperty( QDataSet.DELTA_PLUS, Adapter.getUPAdapter( x, xyzdata.getXUncertProvider(), true ) );        
        
        y.putProperty( QDataSet.UNITS, Units.lookupUnits( meta.getYUnits().getName() ) );
        y.putProperty( QDataSet.LABEL, meta.getYLabel() );
        y.putProperty( QDataSet.NAME, meta.getYName() );
        y.putProperty( QDataSet.DELTA_MINUS, Adapter.getUPAdapter( y, xyzdata.getYUncertProvider(), false ) );
        y.putProperty( QDataSet.DELTA_PLUS, Adapter.getUPAdapter( y, xyzdata.getYUncertProvider(), true ) );
        
        z.putProperty( QDataSet.UNITS, Units.lookupUnits( meta.getZUnits().getName() ) );
        z.putProperty( QDataSet.LABEL, meta.getZLabel() );
        z.putProperty( QDataSet.NAME, meta.getZName() );
        z.putProperty( QDataSet.DELTA_MINUS, Adapter.getUPAdapter( z, xyzdata.getZUncertProvider(), false ) );
        z.putProperty( QDataSet.DELTA_PLUS, Adapter.getUPAdapter( z, xyzdata.getZUncertProvider(), true ) );
        z.putProperty( QDataSet.WEIGHTS, Adapter.getWeights( z, xyzdata.getZFillDetector() ) );
        
        return Ops.link( x,y,z );
    }

}
