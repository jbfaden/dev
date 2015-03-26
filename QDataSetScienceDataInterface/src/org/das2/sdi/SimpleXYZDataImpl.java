
package org.das2.sdi;

import org.virbo.dataset.QDataSet;
import org.virbo.dataset.SemanticOps;
import sdi.data.SimpleXYZData;

/**
 *
 * @author faden@cottagesystems.com
 */
class SimpleXYZDataImpl implements SimpleXYZData {

    QDataSet y;
    QDataSet x;
    QDataSet z;
    
    /**
     * create the SimpleXYZDataImpl with x, y, and z rank 1 datasets.
     * @param x rank 1 dataset independent parameter
     * @param y rank 1 dataset independent parameter
     * @param z rank 1 dataset dependent parameter
     * @see #SimpleXYZDataImpl(org.virbo.dataset.QDataSet, org.virbo.dataset.QDataSet, org.virbo.dataset.QDataSet) which must contain repeat code.
     */
    public SimpleXYZDataImpl( QDataSet x, QDataSet y, QDataSet z ) {
        this.y= y;
        this.x= x;
        this.z= z;
        if ( z.rank()!=1 ) {
            throw new IllegalArgumentException("source z is not rank 1: "+z );
        }
        if ( y.rank()!=1 ) {
            throw new IllegalArgumentException("source y is not rank 1: "+y );
        }
        if ( x.rank()!=1 ) {
            throw new IllegalArgumentException("source x is not rank 1: "+x );
        }        
    }
    
    /**
     * create the SimpleXYDataImpl with the dataset, using known schemes.
     * @param ds the dataset
     * @see SemanticOps#xtagsDataSet(org.virbo.dataset.QDataSet) 
     * @see SemanticOps#ytagsDataSet(org.virbo.dataset.QDataSet)
     * @see SemanticOps#getDependentDataSet(org.virbo.dataset.QDataSet) 
     * @see #SimpleXYZDataImpl(org.virbo.dataset.QDataSet) which must contain repeat code.
     */
    public SimpleXYZDataImpl( QDataSet ds ) {
        x= SemanticOps.xtagsDataSet(ds);
        y= SemanticOps.ytagsDataSet(ds);
        z= SemanticOps.getDependentDataSet(ds);
        
        if ( z.rank()!=1 ) {
            throw new IllegalArgumentException("source z is not rank 1: "+z );
        }
        if ( y.rank()!=1 ) {
            throw new IllegalArgumentException("source y is not rank 1: "+y );
        }
        if ( x.rank()!=1 ) {
            throw new IllegalArgumentException("source x is not rank 1: "+x );
        }             
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

    @Override
    public double getZ(int i) {
        return z.value(i);
    }

}
