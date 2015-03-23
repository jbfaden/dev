
package org.das2.sdi;

import com.google.common.base.Optional;
import org.das2.datum.Units;
import org.virbo.dataset.AbstractRank1DataSet;
import org.virbo.dataset.ArrayDataSet;
import org.virbo.dataset.DDataSet;
import org.virbo.dataset.MutablePropertyDataSet;
import org.virbo.dataset.QDataSet;
import org.virbo.dataset.SemanticOps;
import org.virbo.dataset.WeightsDataSet;
import org.virbo.dsops.Ops;
import sdi.data.FillDetector;
import sdi.data.FillDetector2D;
import sdi.data.SimpleBinnedData1D;
import sdi.data.SimpleBinnedData2D;
import sdi.data.SimpleXYData;
import sdi.data.UncertaintyProvider;
import sdi.data.XYData;

/**
 * Utility adapters to ScienceDataInterface.
 * @author faden@cottagesystems.com
 */
public class Adapter {
    
    /**
     * returns null or the implementation of the class.
     * @param <T>
     * @param ds the QDataSet
     * @param clazz e.g. XYData.class
     * @return the implementation, e.g. XYDataImpl.
     */
    public static <T> T adapt( QDataSet ds, Class<T> clazz  ) {
        if ( XYData.class.isAssignableFrom(clazz) ) {
            return (T)new XYDataImpl( ds );
        } else if ( SimpleXYData.class.isAssignableFrom(clazz) ) {
            return (T)new SimpleXYDataImpl( ds );
        } else if ( SimpleBinnedData1D.class.isAssignableFrom(clazz) ) {
            return (T)new SimpleBinnedData1DImpl( ds );
        } else if ( SimpleBinnedData2D.class.isAssignableFrom(clazz) ) {
            return (T)new SimpleBinnedData2DImpl( ds );
        } else {
            throw new IllegalArgumentException("Unsupported interface: "+clazz);
        }
    }
    
    
    /**
     * returns the QDataSet implementing the UncertaintyProvider or null.
     * @param ds the dataset
     * @param oup the UncertainProvider Optional, which can be absent.
     * @param minus if true, then implement DELTA_MINUS, or if false then implement DELTA_PLUS.
     * @return the QDataSet implementing the UncertaintyProvider or null.
     */
    protected static QDataSet getUPAdapter( QDataSet ds, Optional<UncertaintyProvider> oup, boolean minus ) {
        if ( oup.isPresent() ) {
            UncertaintyProvider up= oup.get();
            MutablePropertyDataSet result= new AbstractRank1DataSet(ds.length()) {
                @Override
                public double value(int i) {
                    return minus ? ( ds.value(i) - up.getUncertMinus(i) ) : ( up.getUncertPlus(i) - ds.value(i) );
                }
            };
            result.putProperty(QDataSet.UNITS,SemanticOps.getUnits(ds).getOffsetUnits());
            return result;
        } else {
            return null;
        }
    }
    
    /**
     * return the weights dataset, or null.
     * @param ofd the FillDetector Optional.
     * @return the weights dataset, or null.
     */
    protected static QDataSet getWeights( QDataSet ds, Optional<FillDetector> ofd ) {
        if ( ofd.isPresent() ) {
            FillDetector fd= ofd.get();
            return new AbstractRank1DataSet( ds.length()) {
                @Override
                public double value(int i) {
                    return fd.isFill(i) ? 0.0 : 1.0;
                }
            };
        } else {
            return null;
        }
    }
   
    
    /**
     * provide the fill detector, if one is needed.
     * @param ds the dataset.
     * @return the FillDetector Optional.
     */
    public static Optional<FillDetector> getFillDetector(QDataSet ds) {
        QDataSet wds= (QDataSet) SemanticOps.weightsDataSet(ds);
        if ( wds instanceof WeightsDataSet.Finite || wds instanceof WeightsDataSet.AllValid ) {
            return  Optional.absent();
        } else {
            return  Optional.fromNullable( new FillDetectorImpl(ds) );
        }
    }

    
    /**
     * provide the fill detector, if one is needed.
     * @param ds the dataset.
     * @return the FillDetector Optional.
     */
    public static Optional<FillDetector2D> getFillDetector2D(QDataSet ds) {
        QDataSet wds= (QDataSet) SemanticOps.weightsDataSet(ds);
        if ( wds instanceof WeightsDataSet.Finite || wds instanceof WeightsDataSet.AllValid ) {
            return  Optional.absent();
        } else {
            return  Optional.fromNullable( new FillDetector2DImpl(ds) );
        }
    }
    
    /**
     * provide the uncertainty provider, if one is available.
     * @param ds the dataset
     * @return the UncertaintyProvider Optional.
     */
    public static Optional<UncertaintyProvider> getUncertaintyProvider( QDataSet ds ) {
        QDataSet dxp= (QDataSet) ds.property(QDataSet.DELTA_PLUS);
        QDataSet dxm= (QDataSet) ds.property(QDataSet.DELTA_MINUS);
        dxp= DDataSet.copy(dxp);
        dxm= DDataSet.copy(dxm);
        if ( dxp!=null && dxm!=null ) {
            return Optional.fromNullable( new UncertaintyProviderImpl( Ops.add( ds, dxp ), Ops.subtract( ds, dxm ) ) );
        } else {
            return Optional.absent();
        }
    }
}
