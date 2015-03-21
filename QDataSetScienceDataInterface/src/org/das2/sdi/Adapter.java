
package org.das2.sdi;

import com.google.common.base.Optional;
import org.das2.datum.Units;
import org.virbo.dataset.AbstractRank1DataSet;
import org.virbo.dataset.MutablePropertyDataSet;
import org.virbo.dataset.QDataSet;
import org.virbo.dsops.Ops;
import sdi.data.FillDetector;
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
     * @param ds
     * @param clazz e.g. XYData.class
     * @return the implementation, e.g. XYDataImpl.
     */
    public static <T> T adapt( QDataSet ds, Class<T> clazz  ) {
        if ( XYData.class.isAssignableFrom(clazz) ) {
            return (T)new XYDataImpl( ds );
        } else if ( SimpleXYData.class.isAssignableFrom(clazz) ) {
            return (T)new SimpleXYDataImpl( ds );
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
        return result;
    }
    
    private static MutablePropertyDataSet getY( XYData xydata ) {
        MutablePropertyDataSet result= getY( (SimpleXYData)xydata );
        XYMetadata meta= xydata.getMetadata();
        result.putProperty( QDataSet.UNITS, Units.lookupUnits( meta.getYUnits().getName() ) );
        result.putProperty( QDataSet.LABEL, meta.getYLabel() );
        result.putProperty( QDataSet.NAME, meta.getYName() );
        return result;
    }
     
    /**
     * return a QDataSet for the xydata
     * @param xydata
     * @return 
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
        Number fill= (Number) ds.property(QDataSet.FILL_VALUE);
        Number vmin= (Number) ds.property(QDataSet.VALID_MIN);
        Number vmax= (Number) ds.property(QDataSet.VALID_MAX);
        if ( fill==null ) {
            if ( vmin==null && vmax==null ) {
                return Optional.absent(); // optimization
            } else {
                return Optional.fromNullable( new FillDetectorImpl(ds) );
            }
        } else {
            return Optional.fromNullable( new FillDetectorImpl(ds) );
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
        if ( dxp!=null && dxm!=null ) {
            return Optional.fromNullable( new UncertaintyProviderImpl( dxp, dxm ) );
        } else {
            return Optional.absent();
        }
    }
}
