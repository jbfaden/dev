
package org.das2.sdi;

import org.das2.datum.Units;
import org.virbo.dataset.MutablePropertyDataSet;
import org.virbo.dataset.QDataSet;
import sdi.data.BinnedData2D;
import sdi.data.XYZMetadata;

/**
 * Adapts BinnedData2D to QDataSet
 * @author faden@cottagesystems.com
 */
public class BinnedData2DAdapter extends SimpleBinnedData2DAdapter {
    
    /**
     * return a QDataSet for BinnedData1D
     * @param data the BinnedData1D
     * @return a QDataSet
     */
    public static QDataSet adapt( BinnedData2D data ) {
        
        MutablePropertyDataSet dep0= getX(data);
        MutablePropertyDataSet dep1= getY(data);
        MutablePropertyDataSet ds= getZ(data);
        ds.putProperty( QDataSet.DEPEND_0, dep0 );
        ds.putProperty( QDataSet.DEPEND_1, dep1 );        
        XYZMetadata meta= data.getMetadata();
        dep0.putProperty( QDataSet.UNITS, Units.lookupUnits( meta.getXUnits().getName() ) );
        dep0.putProperty( QDataSet.LABEL, meta.getXLabel() );
        dep0.putProperty( QDataSet.NAME, meta.getXName() );
        dep1.putProperty( QDataSet.UNITS, Units.lookupUnits( meta.getYUnits().getName() ) );
        dep1.putProperty( QDataSet.LABEL, meta.getYLabel() );
        dep1.putProperty( QDataSet.NAME, meta.getYName() );
        ds.putProperty( QDataSet.UNITS, Units.lookupUnits( meta.getZUnits().getName() ) );
        ds.putProperty( QDataSet.LABEL, meta.getZLabel() );
        ds.putProperty( QDataSet.NAME, meta.getZName() );
        
        return ds;
    }
}
