
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
    
    public SimpleBinnedData1DImpl( QDataSet source ) {
        this.y= source;
        if ( y.rank()!=1 ) {
            throw new IllegalArgumentException("source is not rank 1: "+y );
        }
        this.x= SemanticOps.xtagsDataSet(source);
        if ( x.rank()!=1 ) {
            throw new IllegalArgumentException("source x is not rank 1: "+x );
        }
        if ( (QDataSet) x.property(QDataSet.BIN_MINUS)==null 
                || (QDataSet) x.property(QDataSet.BIN_PLUS)==null ) {
            throw new IllegalArgumentException("source x must have BIN_PLUS and BIN_MINUS");
        }
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
