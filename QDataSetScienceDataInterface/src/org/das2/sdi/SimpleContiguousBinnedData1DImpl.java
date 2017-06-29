
package org.das2.sdi;

import org.das2.qds.DataSetUtil;
import org.das2.qds.QDataSet;
import org.das2.qds.SemanticOps;
import org.das2.qds.ops.Ops;

import sdi.data.SimpleContiguousBinnedData1D;

/**
 *
 * @author faden@cottagesystems.com
 */
class SimpleContiguousBinnedData1DImpl implements SimpleContiguousBinnedData1D {

    QDataSet xlow;
    QDataSet xhigh;
    QDataSet x;
    QDataSet y;
    
        
    /**
     * create the SimpleContiguousBinnedData1DImpl with x and y rank 1 datasets.
     * The x dataset property BIN_PLUS and BIN_MINUS will be used to derive the
     * bins, or when they are not available, then DataSetUtil.guessCadence
     * will be used to calculate the boundaries.
     * @param xlow the independent parameter in a rank 1 QDataSet
     * @param xhigh if rank 0, then the high bin, or if rank 1, then only the last element is used.
     * @param y the dependent parameter in a rank 1 QDataSet
     */
    public SimpleContiguousBinnedData1DImpl( QDataSet xlow, QDataSet xhigh, QDataSet y ) {
        this.y= y;
        if ( y.rank()!=1 ) {
            throw new IllegalArgumentException("source y is not rank 1: "+y );
        }
        if ( xlow.rank()!=1 ) {
            throw new IllegalArgumentException("source xlow is not rank 1: "+xlow );
        }
        if ( xhigh.rank()==1 ) {
            this.xhigh= xhigh.slice(xhigh.length()-1);
        } else if ( xhigh.rank()==0 ) {
            this.xhigh= xhigh;
        } else {
            throw new IllegalArgumentException("source xhigh must be rank 0 or rank 1");
        }
        this.xlow= xlow;
    }
    
    /**
     * create the SimpleBinnedData1DImpl, using known schemes.
     * @param ds the dataset
     * @see SemanticOps#xtagsDataSet(org.virbo.dataset.QDataSet) 
     */
    public SimpleContiguousBinnedData1DImpl( QDataSet ds ) {
        this.y= ds;
        QDataSet bins= SemanticOps.xtagsDataSet( ds );
        if ( bins.rank()==2 && bins.property(QDataSet.BINS_1).equals(QDataSet.VALUE_BINS_MIN_MAX) ) {
            this.xlow= Ops.slice1(bins,0);
            this.xhigh= bins.slice( bins.length()-1 ).slice(1);
            this.x= Ops.reduceMean(bins,1);
        } else if ( bins.rank()==1 ) {
            this.x= bins;
            if ( (QDataSet) bins.property(QDataSet.BIN_MINUS)==null 
                || (QDataSet) bins.property(QDataSet.BIN_PLUS)==null ) {
                QDataSet cadence= DataSetUtil.guessCadence(bins,null);
                if ( cadence!=null ) {
                    cadence= Ops.divide(cadence,2);
                    xlow= Ops.subtract( this.x, cadence );
                    xhigh= Ops.add( this.x, cadence );
                } else {
                    throw new IllegalArgumentException("source x must have BIN_PLUS and BIN_MINUS");
                }
            } else {
                xlow= Ops.subtract( this.x, bins.property(QDataSet.BIN_MINUS) );
                xhigh= Ops.add( this.x, bins.property(QDataSet.BIN_PLUS) );
            }
        }
    }
    
    @Override
    public int size() {
        return this.x.length();
    }

    @Override
    public double getXBinLo(int i) {
        return this.xlow.value(i);
    }

    @Override
    public double getXBinReference(int i) {
        return this.x.value(i);
    }

    @Override
    public double getLastXBinHi() {
        return this.xhigh.value();
    }

    @Override
    public double getY(int i) {
        return this.y.value(i);
    }

}
