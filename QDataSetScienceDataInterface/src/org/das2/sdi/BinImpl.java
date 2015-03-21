
package org.das2.sdi;

import org.virbo.dataset.QDataSet;
import sdi.data.Bin;

/**
 * Implementation looks at BIN_PLUS and BIN_MINUS properties.
 * @author faden@cottagesystems.com
 */
public class BinImpl implements Bin {

    QDataSet ref;
    QDataSet minus;
    QDataSet plus;
    int i0;
    
    public BinImpl( QDataSet ds, int i0 ) {
        this.ref= ds;
        this.i0= i0;
        this.minus= (QDataSet) ds.property(QDataSet.BIN_MINUS);
        this.plus= (QDataSet) ds.property(QDataSet.BIN_PLUS);
    }
    
    @Override
    public double getMin() {
        return ref.value(i0) - minus.value(i0);
    }

    @Override
    public double getMax() {
        return ref.value(i0) + plus.value(i0);
    }

    @Override
    public double getReference() {
        return ref.value(i0);
    }
    
}
