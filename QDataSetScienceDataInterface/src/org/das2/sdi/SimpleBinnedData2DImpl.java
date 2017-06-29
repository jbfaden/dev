
package org.das2.sdi;

import org.das2.qds.DataSetUtil;
import org.das2.qds.QDataSet;
import org.das2.qds.SemanticOps;
import org.das2.qds.examples.Schemes;
import org.das2.qds.ops.Ops;

import sdi.data.Bin;
import sdi.data.SimpleBinnedData2D;

/**
 *
 * @author faden@cottagesystems.com
 */
class SimpleBinnedData2DImpl implements SimpleBinnedData2D {

    QDataSet x;
    QDataSet y;
    QDataSet z;
    
    /**
     * create the SimpleBinnedData2DImpl with x (rank 1), y (rank 1) and 
     * z (rank 2) datasets.
     * @param x rank 1 dataset independent parameter
     * @param y rank 1 dataset independent parameter
     * @param z rank 2 dataset dependent parameter
     */
    public SimpleBinnedData2DImpl( QDataSet x, QDataSet y, QDataSet z ) {
        if ( !Schemes.isSimpleSpectrogram(z) ) throw new IllegalArgumentException("data cannot be converted to SimpleBinnedData2D");
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
        if ( (QDataSet) y.property(QDataSet.BIN_MINUS)==null 
                || (QDataSet) y.property(QDataSet.BIN_PLUS)==null ) {
            QDataSet cadence= DataSetUtil.guessCadence(y,null);
            if ( cadence!=null ) {
                cadence= Ops.divide(cadence,2);
                y= Ops.putProperty( y, QDataSet.BIN_PLUS, cadence );
                y= Ops.putProperty( y, QDataSet.BIN_MINUS, cadence );
            } else {
                throw new IllegalArgumentException("source y must have BIN_PLUS and BIN_MINUS");
            }
        }
        this.x= x;
        this.y= y;
        this.z= z;
    }
    
    public SimpleBinnedData2DImpl( QDataSet ds ) {
        this( SemanticOps.xtagsDataSet(ds), SemanticOps.ytagsDataSet(ds), ds );  
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
