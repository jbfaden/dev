
package org.das2.sdi;

import org.das2.datum.Units;
import org.virbo.dataset.MutablePropertyDataSet;
import org.virbo.dataset.QDataSet;
import sdi.data.BinnedData1D;
import sdi.data.SimpleBinnedData1D;
import sdi.data.XYMetadata;

/**
 * Adapts BinnedData1D to QDataSet
 * @author faden@cottagesystems.com
 */
public class BinnedData1DAdapter {
    private static MutablePropertyDataSet getX( BinnedData1D xydata ) {
        MutablePropertyDataSet result= SimpleBinnedData1DAdapter.getX( (SimpleBinnedData1D)xydata );
        XYMetadata meta= xydata.getMetadata();
        result.putProperty( QDataSet.UNITS, Units.lookupUnits( meta.getXUnits().getName() ) );
        result.putProperty( QDataSet.LABEL, meta.getXLabel() );
        result.putProperty( QDataSet.NAME, meta.getXName() );
        return result;
    }
    
    
    private static MutablePropertyDataSet getY( BinnedData1D xydata ) {
        MutablePropertyDataSet result= SimpleBinnedData1DAdapter.getY( (SimpleBinnedData1D)xydata );
        XYMetadata meta= xydata.getMetadata();
        result.putProperty( QDataSet.UNITS, Units.lookupUnits( meta.getYUnits().getName() ) );
        result.putProperty( QDataSet.LABEL, meta.getYLabel() );
        result.putProperty( QDataSet.NAME, meta.getYName() );
        result.putProperty( QDataSet.DELTA_MINUS, Adapter.getUPAdapter( result, xydata.getYUncertProvider(), false ) );
        result.putProperty( QDataSet.DELTA_PLUS, Adapter.getUPAdapter( result, xydata.getYUncertProvider(), true ) );
        result.putProperty( QDataSet.WEIGHTS, Adapter.getWeights( result, xydata.getFillDetector() ) );
        return result;
    }
     
    /**
     * return a QDataSet for BinnedData1D
     * @param data the BinnedData1D
     * @return a QDataSet
     */
    public static QDataSet adapt( BinnedData1D data ) {
        MutablePropertyDataSet dep0= getX(data);
        MutablePropertyDataSet ds= getY(data);
        ds.putProperty( QDataSet.DEPEND_0, dep0 );
        return ds;
    }
}
