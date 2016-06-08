
package org.das2.sdi;

import org.das2.datum.Units;
import org.virbo.dataset.AbstractDataSet;
import org.virbo.dataset.AbstractRank1DataSet;
import org.virbo.dataset.MutablePropertyDataSet;
import org.virbo.dataset.QDataSet;
import sdi.data.SimpleBinnedData2D;

/**
 * Adapts BinnedData2D to QDataSet
 * @author faden@cottagesystems.com
 * @see org.virbo.dataset.examples.Schemes#simpleSpectrogram() 
 */
public class SimpleBinnedData2DAdapter {
    protected static MutablePropertyDataSet getX( SimpleBinnedData2D data) {
        AbstractRank1DataSet result= new AbstractRank1DataSet( data.sizeX() ) {
            @Override
            public double value(int i) {
                return data.getXBin(i).getReference();
            }
        };
        AbstractRank1DataSet binPlus= new AbstractRank1DataSet( data.sizeX() ) {
            @Override
            public double value(int i) {
                return data.getXBin(i).getMax() - data.getXBin(i).getReference();
            }
        };
        AbstractRank1DataSet binMinus= new AbstractRank1DataSet( data.sizeX() ) {
            @Override
            public double value(int i) {
                return data.getXBin(i).getReference() - data.getXBin(i).getMin();
            }
        };
        result.putProperty( QDataSet.BIN_MINUS, binMinus );
        result.putProperty( QDataSet.BIN_PLUS, binPlus );
        return result;
    }
    
    protected static MutablePropertyDataSet getY( SimpleBinnedData2D data) {
        AbstractRank1DataSet result= new AbstractRank1DataSet( data.sizeY() ) {
            @Override
            public double value(int i) {
                return data.getYBin(i).getReference();
            }
        };
        AbstractRank1DataSet binPlus= new AbstractRank1DataSet( data.sizeY() ) {
            @Override
            public double value(int i) {
                return data.getYBin(i).getMax() - data.getYBin(i).getReference();
            }
        };
        AbstractRank1DataSet binMinus= new AbstractRank1DataSet( data.sizeY() ) {
            @Override
            public double value(int i) {
                return data.getYBin(i).getReference() - data.getYBin(i).getMin();
            }
        };

        result.putProperty( QDataSet.BIN_MINUS, binMinus );
        result.putProperty( QDataSet.BIN_PLUS, binPlus );
        return result;
    }
         
    protected static MutablePropertyDataSet getZ( SimpleBinnedData2D data ) {
        return new AbstractDataSet() {
            @Override
            public int rank() {
                return 2;
            }

            @Override
            public double value(int i, int j) {
                return data.getZ(i,j);
            }
            
            @Override
            public int length() {
                return data.sizeX();
            }
            
            @Override
            public int length(int i) {
                return data.sizeY();
            }
        };
    }
    
    public static QDataSet adapt( SimpleBinnedData2D data ) {
        MutablePropertyDataSet dep0= getX(data);
        MutablePropertyDataSet dep1= getY(data);
        MutablePropertyDataSet ds= getZ(data);
        ds.putProperty( QDataSet.DEPEND_0, dep0 );
        ds.putProperty( QDataSet.DEPEND_1, dep1 );
        return ds;
    }
}
