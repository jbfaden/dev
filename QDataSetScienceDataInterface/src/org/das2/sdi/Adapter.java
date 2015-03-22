
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
import sdi.data.XYMetadata;

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
    
    
    private static MutablePropertyDataSet getX( XYData xydata ) {
        MutablePropertyDataSet result= getX( (SimpleXYData)xydata );
        XYMetadata meta= xydata.getMetadata();
        result.putProperty( QDataSet.UNITS, Units.lookupUnits( meta.getXUnits().getName() ) );
        result.putProperty( QDataSet.LABEL, meta.getXLabel() );
        result.putProperty( QDataSet.NAME, meta.getXName() );
        result.putProperty( QDataSet.DELTA_MINUS, getUPAdapter( result, xydata.getXUncertProvider(), true ) );
        result.putProperty( QDataSet.DELTA_MINUS, getUPAdapter( result, xydata.getXUncertProvider(), false ) );        
        return result;
    }
    
    /**
     * returns the QDataSet implementing the UncertaintyProvider or null.
     * @param ds the dataset
     * @param up the UncertainProvider Optional, which can be absent.
     * @param minus if true, then implement DELTA_MINUS, or if false then implement DELTA_PLUS.
     * @return the QDataSet implementing the UncertaintyProvider or null.
     */
    private static QDataSet getUPAdapter( QDataSet ds, Optional<UncertaintyProvider> oup, boolean minus ) {
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
    private static QDataSet getWeights( QDataSet ds, Optional<FillDetector> ofd ) {
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
    
    private static MutablePropertyDataSet getY( XYData xydata ) {
        MutablePropertyDataSet result= getY( (SimpleXYData)xydata );
        XYMetadata meta= xydata.getMetadata();
        result.putProperty( QDataSet.UNITS, Units.lookupUnits( meta.getYUnits().getName() ) );
        result.putProperty( QDataSet.LABEL, meta.getYLabel() );
        result.putProperty( QDataSet.NAME, meta.getYName() );
        result.putProperty( QDataSet.DELTA_MINUS, getUPAdapter( result, xydata.getYUncertProvider(), true ) );
        result.putProperty( QDataSet.DELTA_PLUS, getUPAdapter( result, xydata.getYUncertProvider(), false ) );
        result.putProperty( QDataSet.WEIGHTS, getWeights( result, xydata.getFillDetector() ) );
        return result;
    }
     
    /**
     * return a QDataSet for the xydata
     * @param xydata the xydata
     * @return a QDataSet
     */
    public static QDataSet adapt( XYData xydata ) {
        MutablePropertyDataSet dep0= getX(xydata);
        MutablePropertyDataSet ds= getY(xydata);
        ds.putProperty( QDataSet.DEPEND_0, dep0 );
        return ds;
    }

    private static MutablePropertyDataSet getX( SimpleXYData xydata ) {
        AbstractRank1DataSet result= new AbstractRank1DataSet( xydata.size() ) {
            @Override
            public double value(int i) {
                return xydata.getX(i);
            }
        };
        return result;
    }
    
    private static MutablePropertyDataSet getY( SimpleXYData xydata ) {
        AbstractRank1DataSet result= new AbstractRank1DataSet( xydata.size() ) {
            @Override
            public double value(int i) {
                return xydata.getY(i);
            }
        };
        return result;
    }
         
    /**
     * adapt the simple XY data.
     * @param simpleXYData the data
     * @return a QDataSet
     */
    public static QDataSet adapt( SimpleXYData simpleXYData ) {
        MutablePropertyDataSet dep0= getX(simpleXYData);
        MutablePropertyDataSet ds= getY(simpleXYData);
        ds.putProperty( QDataSet.DEPEND_0, dep0 );
        return ds;       
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
