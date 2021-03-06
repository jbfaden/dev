
package org.das2.sdi;

import org.das2.qds.AbstractRank1DataSet;
import org.das2.qds.MutablePropertyDataSet;
import org.das2.qds.QDataSet;

import sdi.data.SimpleXYData;

/**
 * Adapts SimpleXYData to QDataSet
 * @author faden@cottagesystems.com
 */
public class SimpleXYDataAdapter {

    protected static MutablePropertyDataSet getX( SimpleXYData xydata ) {
        AbstractRank1DataSet result= new AbstractRank1DataSet( xydata.size() ) {
            @Override
            public double value(int i) {
                return xydata.getX(i);
            }
        };
        return result;
    }
    
    protected static MutablePropertyDataSet getY( SimpleXYData xydata ) {
        AbstractRank1DataSet result= new AbstractRank1DataSet( xydata.size() ) {
            @Override
            public double value(int i) {
                return xydata.getY(i);
            }
        };
        return result;
    }
         
    /**
     * adapt the simple XY data.
     * @param simpleXYData the data
     * @return a QDataSet
     */
    public static QDataSet adapt( SimpleXYData simpleXYData ) {
        MutablePropertyDataSet dep0= getX(simpleXYData);
        MutablePropertyDataSet ds= getY(simpleXYData);
        ds.putProperty( QDataSet.DEPEND_0, dep0 );
        return ds;       
    }
}
