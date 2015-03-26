package org.das2.sdi;

import org.virbo.dataset.AbstractDataSet;
import org.virbo.dataset.AbstractRank1DataSet;
import org.virbo.dataset.MutablePropertyDataSet;
import org.virbo.dataset.QDataSet;
import sdi.data.SimpleContiguousBinnedData1D;


/**
 *
 * @author faden@cottagesystems.com
 */
public class SimpleContiguousBinnedData1DAdapter {
    
    /**
     * This returns the infrequently-used "Bins" dataset, which is 
     * i,j(0=low,1=high) &rarr; BinBoundary(i,j)
     * @param data the data 
     * @return the QDataSet equivalent
     */
    protected static MutablePropertyDataSet getX( SimpleContiguousBinnedData1D data ) {
        MutablePropertyDataSet result= new AbstractDataSet() {
            @Override
            public double value(int i0,int i1) {
                if ( i1==0 ) {
                    return data.getXBinLo(i0);
                } else {
                    if ( i0<data.size()-1 ) {
                        return data.getXBinLo(i0+1);
                    } else {
                        return data.getLastXBinHi();
                    }
                }
            }
            @Override
            public int rank() {
                return 2;
            }
            @Override
            public int length() {
                return data.size();
            }
            @Override
            public int length(int i) {
                return 2;
            }
        };
        result.putProperty( QDataSet.BINS_1, QDataSet.VALUE_BINS_MIN_MAX );
        return result;
    }
    
    protected static MutablePropertyDataSet getY( SimpleContiguousBinnedData1D data ) {
        AbstractRank1DataSet result= new AbstractRank1DataSet( data.size() ) {
            @Override
            public double value(int i) {
                return data.getY(i);
            }
        };
        return result;
    }
         
    /**
     * Adapt the SimpleContiguousBinnedData1D to QDataSet.  QDataSet doesn't have
     * an explicit constraint on the bins like SimpleContiguousBinnedData1D, so we just
     * have to line things up.
     * @param data the data
     * @return a QDataSet
     */
    public static MutablePropertyDataSet adapt( SimpleContiguousBinnedData1D data ) {
        MutablePropertyDataSet dep0= getX(data);
        MutablePropertyDataSet ds= getY(data);
        ds.putProperty( QDataSet.DEPEND_0, dep0 );
        return ds;
    }
}
