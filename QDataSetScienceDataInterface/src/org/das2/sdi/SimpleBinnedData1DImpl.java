
package org.das2.sdi;

import org.das2.qds.DataSetUtil;
import org.das2.qds.QDataSet;
import org.das2.qds.SemanticOps;
import org.das2.qds.ops.Ops;

import sdi.data.Bin;
import sdi.data.SimpleBinnedData1D;

/**
 *
 * @author faden@cottagesystems.com
 */
class SimpleBinnedData1DImpl implements SimpleBinnedData1D {

    QDataSet y;
    QDataSet x;
    
    /**
     * create the SimpleBinnedData1DImpl with x and y rank 1 datasets.
     * The x dataset property BIN_PLUS and BIN_MINUS will be used to derive the
     * bins, or when they are not available, then DataSetUtil.guessCadence
     * will be used to calculate the boundaries.
     * @param x the independent parameter in a rank 1 QDataSet
     * @param y the dependent parameter in a rank 1 QDataSet
     */
    public SimpleBinnedData1DImpl( QDataSet x, QDataSet y ) {
        this.y= y;
        if ( y.rank()!=1 ) {
            throw new IllegalArgumentException("source y is not rank 1: "+y );
        }
        if ( x.rank()!=1 ) {
            throw new IllegalArgumentException("source x is not rank 1: "+x );
        }
        if ( (QDataSet) x.property(QDataSet.BIN_MINUS)==null 
                || (QDataSet) x.property(QDataSet.BIN_PLUS)==null ) {
            QDataSet cadence= DataSetUtil.guessCadence(x,null);
            if ( cadence!=null ) {
                cadence= Ops.divide(cadence,2);
                x= Ops.putProperty( x, QDataSet.BIN_PLUS, cadence );
                x= Ops.putProperty( x, QDataSet.BIN_MINUS, cadence );
            } else {
                throw new IllegalArgumentException("source x must have BIN_PLUS and BIN_MINUS");
            }
        }
        this.x= x;
    }
    
    /**
     * create the SimpleBinnedData1DImpl, using known schemes.
     * @param source the dataset
     * @see SemanticOps#xtagsDataSet(org.virbo.dataset.QDataSet) 
     */
    public SimpleBinnedData1DImpl( QDataSet source ) {
        this( SemanticOps.xtagsDataSet(source), source );
    }
    
    @Override
    public int size() {
        return x.length();
    }

    @Override
    public Bin getXBin(int i) {
        return new BinImpl( x, i );
    }

    @Override
    public double getY(int i) {
        return y.value(0);
    }
    
}
