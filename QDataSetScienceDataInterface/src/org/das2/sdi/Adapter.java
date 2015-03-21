
package org.das2.sdi;

import com.google.common.base.Optional;
import org.virbo.dataset.QDataSet;
import sdi.data.FillDetector;
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
