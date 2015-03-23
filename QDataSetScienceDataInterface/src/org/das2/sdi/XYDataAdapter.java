
package org.das2.sdi;

import org.das2.datum.Units;
import org.virbo.dataset.MutablePropertyDataSet;
import org.virbo.dataset.QDataSet;
import sdi.data.SimpleXYData;
import sdi.data.XYData;
import sdi.data.XYMetadata;

/**
 * Given an XYData, provide a QDataSet that preserves as much information
 * as possible.
 * @author faden@cottagesystems.com
 */
public class XYDataAdapter {
    private static MutablePropertyDataSet getX( XYData xydata ) {
        MutablePropertyDataSet result= SimpleXYDataAdapter.getX( (SimpleXYData)xydata );
        XYMetadata meta= xydata.getMetadata();
        result.putProperty( QDataSet.UNITS, Units.lookupUnits( meta.getXUnits().getName() ) );
        result.putProperty( QDataSet.LABEL, meta.getXLabel() );
        result.putProperty( QDataSet.NAME, meta.getXName() );
        result.putProperty( QDataSet.DELTA_MINUS, Adapter.getUPAdapter( result, xydata.getXUncertProvider(), true ) );
        result.putProperty( QDataSet.DELTA_MINUS, Adapter.getUPAdapter( result, xydata.getXUncertProvider(), false ) );        
        return result;
    }
    
    
    private static MutablePropertyDataSet getY( XYData xydata ) {
        MutablePropertyDataSet result= SimpleXYDataAdapter.getY( (SimpleXYData)xydata );
        XYMetadata meta= xydata.getMetadata();
        result.putProperty( QDataSet.UNITS, Units.lookupUnits( meta.getYUnits().getName() ) );
        result.putProperty( QDataSet.LABEL, meta.getYLabel() );
        result.putProperty( QDataSet.NAME, meta.getYName() );
        result.putProperty( QDataSet.DELTA_MINUS, Adapter.getUPAdapter( result, xydata.getYUncertProvider(), true ) );
        result.putProperty( QDataSet.DELTA_PLUS, Adapter.getUPAdapter( result, xydata.getYUncertProvider(), false ) );
        result.putProperty( QDataSet.WEIGHTS, Adapter.getWeights( result, xydata.getFillDetector() ) );
        return result;
    }
     
    /**
     * return a QDataSet for the xydata
     * @param xydata the xydata
     * @return a QDataSet
     */
    public static QDataSet adapt( XYData xydata ) {
        MutablePropertyDataSet dep0= getX(xydata);
        MutablePropertyDataSet ds= getY(xydata);
        ds.putProperty( QDataSet.DEPEND_0, dep0 );
        return ds;
    }

}
