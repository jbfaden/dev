
package org.das2.sdi;

import org.virbo.dataset.QDataSet;
import org.virbo.dataset.SemanticOps;
import sdi.data.Bin;
import sdi.data.SimpleBinnedData1D;

/**
 *
 * @author faden@cottagesystems.com
 */
public class SimpleBinnedData1DImpl implements SimpleBinnedData1D {

    QDataSet y;
    QDataSet x;
    
    /**
     * create the SimpleBinnedData1DImpl with x and y rank 1 datasets.
     * @param x
     * @param y 
     */
    public SimpleBinnedData1DImpl( QDataSet x, QDataSet y ) {
        this.y= y;
        if ( y.rank()!=1 ) {
            throw new IllegalArgumentException("source y is not rank 1: "+y );
        }
        this.x= x;
        if ( x.rank()!=1 ) {
            throw new IllegalArgumentException("source x is not rank 1: "+x );
        }
        if ( (QDataSet) x.property(QDataSet.BIN_MINUS)==null 
                || (QDataSet) x.property(QDataSet.BIN_PLUS)==null ) {
            throw new IllegalArgumentException("source x must have BIN_PLUS and BIN_MINUS");
        }
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
