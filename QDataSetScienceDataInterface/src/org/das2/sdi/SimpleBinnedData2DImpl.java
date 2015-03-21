
package org.das2.sdi;

import org.virbo.dataset.QDataSet;
import org.virbo.dataset.SemanticOps;
import org.virbo.dataset.examples.Schemes;
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
        if ( !Schemes.isSimpleSpectrogram(ds) ) throw new IllegalArgumentException("scheme");
        this.x= SemanticOps.xtagsDataSet(ds);
        this.y= SemanticOps.ytagsDataSet(ds);
        this.z= ds;        
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
