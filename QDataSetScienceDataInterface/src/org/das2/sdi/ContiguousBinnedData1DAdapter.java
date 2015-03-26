
package org.das2.sdi;

import org.das2.datum.Units;
import org.virbo.dataset.MutablePropertyDataSet;
import org.virbo.dataset.QDataSet;
import sdi.data.ContiguousBinnedData1D;
import sdi.data.XYMetadata;

/**
 * Adapts ContiguousBinnedData1D to QDataSet.
 * @author faden@cottagesystems.com
 */
public class ContiguousBinnedData1DAdapter extends SimpleContiguousBinnedData1DAdapter {
    
    /**
     * Adapt the ContiguousBinnedData1D to QDataSet.  QDataSet doesn't have
     * an explicit constraint on the bins like ContiguousBinnedData1D, so we just
     * have to line things up.
     * @param data the data
     * @return a QDataSet
     */
    public static QDataSet adapt( ContiguousBinnedData1D data ) {
        MutablePropertyDataSet result = SimpleContiguousBinnedData1DAdapter.adapt( data );
        result.putProperty( QDataSet.DELTA_MINUS, Adapter.getUPAdapter( result, data.getUncertProvider(), false ) );
        result.putProperty( QDataSet.DELTA_PLUS, Adapter.getUPAdapter( result, data.getUncertProvider(), true ) );
        result.putProperty( QDataSet.WEIGHTS, Adapter.getFillDetector( result ) );
        XYMetadata meta= data.getMetadata();
        result.putProperty( QDataSet.UNITS, Units.lookupUnits( meta.getYUnits().getName() ) );
        result.putProperty( QDataSet.LABEL, meta.getYLabel() );
        result.putProperty( QDataSet.NAME, meta.getYName() );        
        MutablePropertyDataSet dep0= (MutablePropertyDataSet)result.property(QDataSet.DEPEND_0);
        dep0.putProperty( QDataSet.UNITS, Units.lookupUnits( meta.getXUnits().getName() ) );
        dep0.putProperty( QDataSet.LABEL, meta.getXLabel() );
        dep0.putProperty( QDataSet.NAME, meta.getXName() );        
        return result;
    }
}
