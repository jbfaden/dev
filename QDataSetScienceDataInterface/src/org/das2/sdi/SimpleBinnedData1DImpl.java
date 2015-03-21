
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
        this.x= SemanticOps.xtagsDataSet(source);
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
