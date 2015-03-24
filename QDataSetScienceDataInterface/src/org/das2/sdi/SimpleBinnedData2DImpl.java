
package org.das2.sdi;

import org.virbo.dataset.DataSetUtil;
import org.virbo.dataset.QDataSet;
import org.virbo.dataset.SemanticOps;
import org.virbo.dataset.examples.Schemes;
import org.virbo.dsops.Ops;
import sdi.data.Bin;
import sdi.data.SimpleBinnedData2D;

/**
 *
 * @author faden@cottagesystems.com
 */
public class SimpleBinnedData2DImpl implements SimpleBinnedData2D {

    QDataSet x;
    QDataSet y;
    QDataSet z;
    
    public SimpleBinnedData2DImpl( QDataSet ds ) {
        if ( !Schemes.isSimpleSpectrogram(ds) ) throw new IllegalArgumentException("data cannot be converted to SimpleBinnedData2D");
        this.x= SemanticOps.xtagsDataSet(ds);
        this.y= SemanticOps.ytagsDataSet(ds);
        this.z= ds;  
        if ( (QDataSet) x.property(QDataSet.BIN_MINUS)==null 
                || (QDataSet) x.property(QDataSet.BIN_PLUS)==null ) {
            QDataSet cadence= DataSetUtil.guessCadenceNew(x,null);
            if ( cadence!=null ) {
                cadence= Ops.divide(cadence,2);
                x= Ops.putProperty( x, QDataSet.BIN_PLUS, cadence );
                x= Ops.putProperty( x, QDataSet.BIN_MINUS, cadence );
            } else {
                throw new IllegalArgumentException("source x must have BIN_PLUS and BIN_MINUS");
            }
        }
        if ( (QDataSet) y.property(QDataSet.BIN_MINUS)==null 
                || (QDataSet) y.property(QDataSet.BIN_PLUS)==null ) {
            QDataSet cadence= DataSetUtil.guessCadenceNew(y,null);
            if ( cadence!=null ) {
                cadence= Ops.divide(cadence,2);
                y= Ops.putProperty( y, QDataSet.BIN_PLUS, cadence );
                y= Ops.putProperty( y, QDataSet.BIN_MINUS, cadence );
            } else {
                throw new IllegalArgumentException("source y must have BIN_PLUS and BIN_MINUS");
            }
        }        
    }
    
    @Override
    public int sizeX() {
        return x.length();
    }

    @Override
    public Bin getXBin(int i) {
        return new BinImpl( x, i );
    }

    @Override
    public int sizeY() {
        return y.length();
    }

    @Override
    public Bin getYBin(int j) {
        return new BinImpl( y, j );
    }

    @Override
    public double getZ(int i, int j ) {
        return z.value( i, j );
    }
    
}
