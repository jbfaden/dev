
package org.das2.sdi;

import java.util.Optional;

import sdi.data.Bin;
import sdi.data.BinnedData1D;
import sdi.data.BinnedData2D;
import sdi.data.FillDetector;
import sdi.data.FillDetector2D;
import sdi.data.UncertaintyProvider;
import sdi.data.UncertaintyProvider2D;
import sdi.data.Units;
import sdi.data.XYMetadata;
import sdi.data.XYZMetadata;

/**
 * Operations for testing the interface
 * @author faden@cottagesystems.com
 */
public class Operations {
    
    /**
     * return the metadata for use when the X component is removed.
     * @param meta the XYZ metadata.
     * @return the XY metadata.
     */
    public static XYMetadata sliceMetadataOnX( final XYZMetadata meta ) {
        return new XYMetadata() {

            @Override
            public Units getXUnits() {
                return meta.getYUnits();
            }

            @Override
            public Units getYUnits() {
                return meta.getZUnits();
            }

            @Override
            public String getXName() {
                return meta.getYName();
            }

            @Override
            public String getYName() {
                return meta.getZName();
            }

            @Override
            public String getXLabel() {
                return meta.getYLabel();
            }

            @Override
            public String getYLabel() {
                return meta.getZLabel();
            }

            @Override
            public String getName() {
                return meta.getName();
            }
        
        };
    }
    
    /**
     * return the metadata for use when the Y component is removed.
     * @param meta the XYZ metadata.
     * @return the XY metadata.
     */
    public static XYMetadata sliceMetadataOnY( final XYZMetadata meta ) {
        return new XYMetadata() {

            @Override
            public Units getXUnits() {
                return meta.getXUnits();
            }

            @Override
            public Units getYUnits() {
                return meta.getZUnits();
            }

            @Override
            public String getXName() {
                return meta.getXName();
            }

            @Override
            public String getYName() {
                return meta.getZName();
            }

            @Override
            public String getXLabel() {
                return meta.getXLabel();
            }

            @Override
            public String getYLabel() {
                return meta.getZLabel();
            }

            @Override
            public String getName() {
                return meta.getName();
            }
        
        };
    }    

    public static Optional<FillDetector> sliceFillDetectorAtX( final Optional<FillDetector2D> ofill, final int iSlice ) {
        if ( ofill.isPresent() ) {
            final FillDetector2D fillDetector1= ofill.get();
            return Optional.of( (FillDetector) (int i) -> fillDetector1.isFill(iSlice,i) );
        } else {
            return Optional.empty();
        }
    }
            
        
    public static Optional<UncertaintyProvider> sliceUncertAtX( final Optional<UncertaintyProvider2D> uncert, final int iSlice ) {
        if ( uncert.isPresent() ) {
            final UncertaintyProvider2D uncert1= uncert.get();
            return Optional.of( new UncertaintyProvider() {
                @Override
                public double getUncertPlus(int i) {
                    return uncert1.getUncertPlus( iSlice, i );
                }
                @Override
                public double getUncertMinus(int i) {
                    return uncert1.getUncertMinus( iSlice, i );
                }
            });
        } else {
            return Optional.empty();
        }
    }
            
    /**
     * Slice the 2-D data at the index.
     * @param ds the dataset.
     * @param iSlice the index.
     * @return the slice in a BinnedData1D
     */
    public static BinnedData1D sliceAtX( BinnedData2D ds, final int iSlice ) {
        
        return new BinnedData1D() {

            @Override
            public Optional<FillDetector> getFillDetector() {
                return sliceFillDetectorAtX( ds.getFillDetector(), iSlice );
            }

            @Override
            public Optional<UncertaintyProvider> getYUncertProvider() {
                return sliceUncertAtX( ds.getZUncertProvider(), iSlice );
            }

            @Override
            public XYMetadata getMetadata() {
                return sliceMetadataOnX( ds.getMetadata() );
            }

            @Override
            public int size() {
                return ds.sizeY();
            }

            @Override
            public Bin getXBin(int i) {
                return ds.getYBin(i);
            }

            @Override
            public double getY(int i) {
                return ds.getZ( iSlice, i );
            }
            
        };
    }
}
