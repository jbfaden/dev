
package org.das2.sdi;

import org.das2.qds.QDataSet;

import sdi.data.Bin;

/**
 * Implementation looks at BIN_PLUS and BIN_MINUS properties.
 * @author faden@cottagesystems.com
 */
class BinImpl implements Bin {

    double min;
    double max;
    double ref;
            
    int i0;
    
    public BinImpl( QDataSet ds, int i0 ) {
        //TODO: check units!
        this.ref= ds.value(i0);
        QDataSet minus= (QDataSet) ds.property(QDataSet.BIN_MINUS);
        if ( minus.rank()==0 ) {
            min= ref - minus.value();
        } else {
            min= ref - minus.value(i0);
        }
        QDataSet plus= (QDataSet) ds.property(QDataSet.BIN_PLUS);
        if ( plus.rank()==0 ) {
            min= ref + plus.value();
        } else {
            min= ref + plus.value(i0);
        }
    }
    
    @Override
    public double getMin() {
        return min;
    }

    @Override
    public double getMax() {
        return max;
    }

    @Override
    public double getReference() {
        return ref;
    }
    
}
