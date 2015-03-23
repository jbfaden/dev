
package org.das2.sdi;

import org.virbo.dataset.AbstractRank1DataSet;
import org.virbo.dataset.MutablePropertyDataSet;
import org.virbo.dataset.QDataSet;
import sdi.data.SimpleBinnedData1D;

/**
 *
 * @author faden@cottagesystems.com
 */
public class SimpleBinnedData1DAdapter {
    
    private static MutablePropertyDataSet getX( SimpleBinnedData1D data ) {
        AbstractRank1DataSet result= new AbstractRank1DataSet( data.size() ) {
            @Override
            public double value(int i) {
                return data.getXBin(i).getReference();
            }
        };
        AbstractRank1DataSet binPlus= new AbstractRank1DataSet( data.size() ) {
            @Override
            public double value(int i) {
                return data.getXBin(i).getMax() - data.getXBin(i).getReference();
            }
        };
        AbstractRank1DataSet binMinus= new AbstractRank1DataSet( data.size() ) {
            @Override
            public double value(int i) {
                return data.getXBin(i).getReference() - data.getXBin(i).getMin();
            }
        };
        result.putProperty( QDataSet.BIN_MINUS, binMinus );
        result.putProperty( QDataSet.BIN_PLUS, binPlus );
        return result;
    }
    
    private static MutablePropertyDataSet getY( SimpleBinnedData1D data ) {
        AbstractRank1DataSet result= new AbstractRank1DataSet( data.size() ) {
            @Override
            public double value(int i) {
                return data.getY(i);
            }
        };
        return result;
    }
         
    public static QDataSet adapt( SimpleBinnedData1D data ) {
        MutablePropertyDataSet dep0= getX(data);
        MutablePropertyDataSet ds= getY(data);
        ds.putProperty( QDataSet.DEPEND_0, dep0 );
        return ds;
    }
}
