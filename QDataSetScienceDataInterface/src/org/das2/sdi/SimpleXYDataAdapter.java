
package org.das2.sdi;

import org.virbo.dataset.AbstractRank1DataSet;
import org.virbo.dataset.MutablePropertyDataSet;
import org.virbo.dataset.QDataSet;
import sdi.data.SimpleXYData;

/**
 *
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
