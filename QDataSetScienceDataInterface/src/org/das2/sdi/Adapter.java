
package org.das2.sdi;

import java.util.Optional;
import org.virbo.dataset.AbstractRank1DataSet;
import org.virbo.dataset.MutablePropertyDataSet;
import org.virbo.dataset.QDataSet;
import org.virbo.dataset.SemanticOps;
import org.virbo.dataset.WeightsDataSet;
import org.virbo.dsops.Ops;
import sdi.data.BinnedData1D;
import sdi.data.BinnedData2D;
import sdi.data.ContiguousBinnedData1D;
import sdi.data.FillDetector;
import sdi.data.FillDetector2D;
import sdi.data.SimpleBinnedData1D;
import sdi.data.SimpleBinnedData2D;
import sdi.data.SimpleContiguousBinnedData1D;
import sdi.data.SimpleXYData;
import sdi.data.SimpleXYZData;
import sdi.data.UncertaintyProvider;
import sdi.data.UncertaintyProvider2D;
import sdi.data.XYData;
import sdi.data.XYZData;

/**
 * Adapters to and from QDataSet and ScienceDataInterface 
 * @author faden@cottagesystems.com
 */
public class Adapter {
         
    private Adapter() {
        // this class is not to be instantiated.
    }
    
    /**
     * Adapt the QDataSets for X and Y to SimpleXYData.  
     * @param x a rank 1 QDataSet
     * @param y a rank 1 QDataSet
     * @return the XYData.
     */
    public static SimpleXYData adaptSimpleXYData( QDataSet x, QDataSet y ) {
        return new SimpleXYDataImpl(x, y);
    }

    /**
     * Adapt the QDataSet to SimpleXYData.  
     * @param ds a rank 1 QDataSet with a rank 1 DEPEND_0.
     * @return the SimpleXYData.
     */
    public static SimpleXYData adaptSimpleXYData( QDataSet ds ) {
        return new SimpleXYDataImpl( ds );
    }
        
    /**
     * Adapt the QDataSets for X and Y to XYData.  
     * @param x a rank 1 QDataSet
     * @param y a rank 1 QDataSet
     * @return the XYData.
     */
    public static XYData adaptXYData( QDataSet x, QDataSet y ) {
        return new XYDataImpl(x, y);
    }

    /**
     * Adapt the QDataSet to XYData.  
     * @param ds a rank 1 QDataSet with a rank 1 DEPEND_0.
     * @return the XYData.
     */
    public static XYData adaptXYData( QDataSet ds ) {
        return new XYDataImpl( ds );
    }
    
    /**
     * Adapt the QDataSets for X, Y, and Z to SimpleXYZData.  
     * @param x a rank 1 QDataSet
     * @param y a rank 1 QDataSet
     * @param z a rank 1 QDataSet
     * @return the SimpleXYZData.
     */
    public static SimpleXYZData adaptSimpleXYZData( QDataSet x, QDataSet y, QDataSet z ) {
        return new SimpleXYZDataImpl(x,y,z);
    }

    /**
     * Adapt the QDataSet to SimpleXYZData.  
     * @param ds a rank 1 QDataSet with a rank 1 DEPEND_0 and rank 1 PLANE_0, or a rank 2 table of X,Y,Z.
     * @return the SimpleXYZData.
     */
    public static SimpleXYZData adaptSimpleXYZData( QDataSet ds ) {
        return new SimpleXYZDataImpl( ds );
    }
     
    /**
     * Adapt the QDataSets for X and Y to XYZData.  
     * @param x a rank 1 QDataSet
     * @param y a rank 1 QDataSet
     * @param z a rank 1 QDataSet
     * @return the XYZData.
     */
    public static XYZData adaptXYZData( QDataSet x, QDataSet y, QDataSet z ) {
        return new XYZDataImpl(x,y,z);
    }

    /**
     * Adapt the QDataSet to XYZData.  
     * @param ds a rank 1 QDataSet with a rank 1 DEPEND_0 and rank 1 PLANE_0, or a rank 2 table of X,Y,Z.
     * @return the XYZData.
     */
    public static XYZData adaptXYZData( QDataSet ds ) {
        return new XYZDataImpl( ds );
    }
            
    /**
     * Adapt the QDataSets for X and Y to SimpleBinnedData1D.  
     * @param x a rank 1 QDataSet with BIN_MINUS and BIN_PLUS
     * @param y a rank 1 QDataSet
     * @return the SimpleBinnedData1D.
     */
    public static SimpleBinnedData1D adaptSimpleBinnedData1D( QDataSet x, QDataSet y ) {
        return new SimpleBinnedData1DImpl(x,y);
    }

    /**
     * Adapt the QDataSet to SimpleBinnedData1D.  
     * @param ds a rank 1 QDataSet with a rank 1 DEPEND_0 with BIN_MINUS and BIN_PLUS
     * @return the SimpleBinnedData1D.
     */
    public static SimpleBinnedData1D adaptSimpleBinnedData1D( QDataSet ds ) {
        return new SimpleBinnedData1DImpl( ds );
    }
         
       
    /**
     * Adapt the QDataSets for X and Y to BinnedData1D.  
     * @param x a rank 1 QDataSet with BIN_MINUS and BIN_PLUS
     * @param y a rank 1 QDataSet
     * @return the BinnedData1D.
     */
    public static BinnedData1D adaptBinnedData1D( QDataSet x, QDataSet y ) {
        return new BinnedData1DImpl(x,y);
    }

    /**
     * Adapt the QDataSet to BinnedData1D.  
     * @param ds a rank 1 QDataSet with a rank 1 DEPEND_0 with BIN_MINUS and BIN_PLUS
     * @return the BinnedData1D.
     */
    public static BinnedData1D adaptBinnedData1D( QDataSet ds ) {
        return new BinnedData1DImpl( ds );
    }
         
    /**
     * Adapt the QDataSets for X, Y, and Z to SimpleBinnedData2D.  
     * @param x a rank 1 QDataSet with BIN_MINUS and BIN_PLUS
     * @param y a rank 1 QDataSet with BIN_MINUS and BIN_PLUS
     * @param z a rank 2 QDataSet
     * @return the SimpleBinnedData1D.
     */
    public static SimpleBinnedData2D adaptSimpleBinnedData2D( QDataSet x, QDataSet y, QDataSet z ) {
        return new SimpleBinnedData2DImpl(x,y,z);
    }

    /**
     * Adapt the QDataSet to SimpleBinnedData2D.  
     * @param ds a rank 1 QDataSet with a rank 1 DEPEND_0 with BIN_MINUS and BIN_PLUS and a rank 1 DEPEND_1 with BIN_MINUS and BIN_PLUS.
     * @return the SimpleBinnedData2D.
     */
    public static SimpleBinnedData2D adaptSimpleBinnedData2D( QDataSet ds ) {
        return new SimpleBinnedData2DImpl( ds );
    }
    
    /**
     * Adapt the QDataSets for X, Y, and Z to BinnedData2D.  
     * @param x a rank 1 QDataSet with BIN_MINUS and BIN_PLUS
     * @param y a rank 1 QDataSet with BIN_MINUS and BIN_PLUS
     * @param z a rank 2 QDataSet
     * @return the BinnedData1D.
     */
    public static BinnedData2D adaptBinnedData2D( QDataSet x, QDataSet y, QDataSet z ) {
        return new BinnedData2DImpl(x,y,z);
    }

    /**
     * Adapt the QDataSet to BinnedData2D.  
     * @param ds a rank 1 QDataSet with a rank 1 DEPEND_0 with BIN_MINUS and BIN_PLUS and a rank 1 DEPEND_1 with BIN_MINUS and BIN_PLUS.
     * @return the BinnedData2D.
     */
    public static BinnedData2D adaptBinnedData2D( QDataSet ds ) {
        return new BinnedData2DImpl( ds );
    }


    /**
     * Adapt the QDataSets for the X boundaries and Y to SimpleContiguousBinnedData1D.  
     * @param xlow a rank 1 QDataSet
     * @param xhigh a rank 1 QDataSet, or rank 0 QDataSet, of which the last datum is used.
     * @param y a rank 1 QDataSet
     * @return the SimpleContiguousBinnedData1D.
     */
    public static SimpleContiguousBinnedData1D adaptSimpleContiguousBinnedData1D( QDataSet xlow, QDataSet xhigh, QDataSet y ) {
        return new SimpleContiguousBinnedData1DImpl( xlow, xhigh, y);
    }

    /**
     * Adapt the QDataSet to SimpleXYData.  
     * @param ds a rank 1 QDataSet with a rank 2 DEPEND_0 with BINS_1="low,high", or a rank 1 DEPEND_0 with BIN_PLUS and BIN_MINUS.
     * @return the SimpleContiguousBinnedData1D.
     */
    public static SimpleContiguousBinnedData1D adaptSimpleContiguousBinnedData1D( QDataSet ds ) {
        return new SimpleContiguousBinnedData1DImpl( ds );
    }
    
    /**
     * Adapt the QDataSets for the X boundaries and Y to ContiguousBinnedData1D.  
     * @param xlow a rank 1 QDataSet
     * @param xhigh a rank 1 QDataSet, or rank 0 QDataSet, of which the last datum is used.
     * @param y a rank 1 QDataSet
     * @return the ContiguousBinnedData1D.
     */
    public static ContiguousBinnedData1D adaptContiguousBinnedData1D( QDataSet xlow, QDataSet xhigh, QDataSet y ) {
        return new ContiguousBinnedData1DImpl( xlow, xhigh, y);
    }

    /**
     * Adapt the QDataSet to SimpleXYData.  
     * @param ds a rank 1 QDataSet with a rank 2 DEPEND_0 with BINS_1="low,high", or a rank 1 DEPEND_0 with BIN_PLUS and BIN_MINUS.
     * @return the ContiguousBinnedData1D.
     */
    public static ContiguousBinnedData1D adaptContiguousBinnedData1D( QDataSet ds ) {
        return new ContiguousBinnedData1DImpl( ds );
    }
        
        
    /**
     * returns the QDataSet implementing the UncertaintyProvider or null.
     * @param ds the dataset
     * @param oup the UncertainProvider Optional, which can be absent.
     * @param plus if true, then implement DELTA_PLUS, or if false then implement DELTA_MINUS.
     * @return the QDataSet implementing the UncertaintyProvider or null.
     */
    protected static QDataSet getUPAdapter( QDataSet ds, Optional<UncertaintyProvider> oup, boolean plus ) {
        if ( oup.isPresent() ) {
            UncertaintyProvider up= oup.get();
            MutablePropertyDataSet result= new AbstractRank1DataSet(ds.length()) {
                @Override
                public double value(int i) {
                    return plus ? ( up.getUncertPlus(i) - ds.value(i) ): ( ds.value(i) - up.getUncertMinus(i) );
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
     * @param ds the dataset described by this result 
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
            return Optional.empty();
        } else {
            return Optional.ofNullable( new FillDetectorImpl(ds) );
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
            return Optional.empty();
        } else {
            return Optional.ofNullable( new FillDetector2DImpl(ds) );
        }
    }
    
    /**
     * create the uncertainty provider, if one is available.  This
     * looks for the DELTA_PLUS and DELTA_MINUS properties.
     * @param ds the dataset
     * @return the UncertaintyProvider Optional.
     */
    public static Optional<UncertaintyProvider> getUncertaintyProvider( QDataSet ds ) {
        QDataSet dxp= (QDataSet) ds.property(QDataSet.DELTA_PLUS);
        QDataSet dxm= (QDataSet) ds.property(QDataSet.DELTA_MINUS);
        if ( dxp!=null && dxm!=null ) {
            return Optional.ofNullable( new UncertaintyProviderImpl( Ops.add( ds, dxp ), Ops.subtract( ds, dxm ) ) );
        } else {
            return Optional.empty();
        }
    }

    /**
     * create the 2-D uncertainty provider, if one is available.  This
     * looks for the DELTA_PLUS and DELTA_MINUS properties.  Note this Scheme
     * is never used in Autoplot.
     * @param ds the rank 2 dataset.
     * @return the UncertaintyProvider2D Optional.
     */
    public static Optional<UncertaintyProvider2D> getUncertaintyProvider2D( QDataSet ds ) {
        QDataSet dxp= (QDataSet) ds.property(QDataSet.DELTA_PLUS);
        QDataSet dxm= (QDataSet) ds.property(QDataSet.DELTA_MINUS);
        if ( dxp!=null && dxm!=null ) {
            return Optional.ofNullable( new UncertaintyProvider2DImpl( Ops.add( ds, dxp ), Ops.subtract( ds, dxm ) ) );
        } else {
            return Optional.empty();
        }
    }
}
