
package org.das2.sdi;

import org.virbo.dataset.QDataSet;
import org.virbo.dataset.SemanticOps;
import sdi.data.SimpleXYData;

/**
 *
 * @author faden@cottagesystems.com
 */
public class SimpleXYDataImpl implements SimpleXYData {

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
     * @param source 
     * @see SemanticOps#xtagsDataSet(org.virbo.dataset.QDataSet) 
     */
    public SimpleXYDataImpl( QDataSet source ) {
        this( SemanticOps.xtagsDataSet(source), source );
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
