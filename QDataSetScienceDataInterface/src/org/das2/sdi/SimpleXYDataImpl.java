
package org.das2.sdi;

import org.das2.qds.QDataSet;
import org.das2.qds.SemanticOps;

import sdi.data.SimpleXYData;

/**
 *
 * @author faden@cottagesystems.com
 */
class SimpleXYDataImpl implements SimpleXYData {

    QDataSet y;
    QDataSet x;
    
    /**
     * create the SimpleXYDataImpl with x and y rank 1 datasets.
     * @param x rank 1 dataset
     * @param y rank 1 dataset
     */
    public SimpleXYDataImpl( QDataSet x, QDataSet y ) {
        this.y= y;
        this.x= x;
        if ( y.rank()!=1 ) {
            throw new IllegalArgumentException("source is not rank 1: "+y );
        }
        if ( x.rank()!=1 ) {
            throw new IllegalArgumentException("source x is not rank 1: "+x );
        }        
    }
    
    /**
     * create the SimpleXYDataImpl with the dataset, using known schemes.
     * @param ds the dataset
     * @see SemanticOps#xtagsDataSet(org.virbo.dataset.QDataSet) 
     */
    public SimpleXYDataImpl( QDataSet ds ) {
        this( SemanticOps.xtagsDataSet(ds), ds );
    }
    
    @Override
    public int size() {
        return x.length();
    }

    @Override
    public double getX(int i) {
        return x.value(i);
    }

    @Override
    public double getY(int i) {
        return y.value(i);
    }

}
