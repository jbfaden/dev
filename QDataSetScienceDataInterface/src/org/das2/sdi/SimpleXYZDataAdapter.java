
package org.das2.sdi;

import org.das2.qds.AbstractRank1DataSet;
import org.das2.qds.MutablePropertyDataSet;
import org.das2.qds.QDataSet;
import org.das2.qds.ops.Ops;

import sdi.data.SimpleXYZData;

/**
 * Adapts SimpleXYZData to QDataSet
 * @author faden@cottagesystems.com
 */
public class SimpleXYZDataAdapter {

    protected static MutablePropertyDataSet getX( SimpleXYZData xyzdata ) {
        AbstractRank1DataSet result= new AbstractRank1DataSet( xyzdata.size() ) {
            @Override
            public double value(int i) {
                return xyzdata.getX(i);
            }
        };
        return result;
    }
    
    protected static MutablePropertyDataSet getY( SimpleXYZData xyzdata ) {
        AbstractRank1DataSet result= new AbstractRank1DataSet( xyzdata.size() ) {
            @Override
            public double value(int i) {
                return xyzdata.getY(i);
            }
        };
        return result;
    }
    
    protected static MutablePropertyDataSet getZ( SimpleXYZData xyzdata ) {
        AbstractRank1DataSet result= new AbstractRank1DataSet( xyzdata.size() ) {
            @Override
            public double value(int i) {
                return xyzdata.getZ(i);
            }
        };
        return result;
    }
    
    /**
     * adapt the simple XYZ data.
     * @param simpleXYZData the data
     * @return a QDataSet
     */
    public static QDataSet adapt( SimpleXYZData simpleXYZData ) {
        MutablePropertyDataSet x= getX(simpleXYZData);
        MutablePropertyDataSet y= getY(simpleXYZData);
        MutablePropertyDataSet z= getZ(simpleXYZData);
        return Ops.link( x, y, z );
    }
}
