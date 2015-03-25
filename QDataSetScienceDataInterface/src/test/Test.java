
package test;

import java.util.Optional;
import org.das2.datum.Units;
import org.das2.sdi.Adapter;
import org.das2.sdi.BinnedData2DAdapter;
import org.das2.sdi.XYDataAdapter;
import org.das2.sdi.XYZDataAdapter;
import org.virbo.dataset.DataSetOps;
import org.virbo.dataset.QDataSet;
import org.virbo.dataset.SemanticOps;
import org.virbo.dataset.examples.Schemes;
import org.virbo.dsops.Ops;
import sdi.data.BinnedData2D;
import sdi.data.FillDetector;
import sdi.data.SimpleXYData;
import sdi.data.UncertaintyProvider;
import sdi.data.UncertaintyProvider2D;
import sdi.data.XYData;
import sdi.data.XYMetadata;
import sdi.data.XYZData;
import sdi.data.XYZMetadata;

/**
 * Tests to exercise the code.
 * @author faden@cottagesystems.com
 */
public class Test {
    
    private static void test1() {
        System.err.println("==test1==");
        QDataSet ds= Schemes.simpleSpectrogram().slice(0).trim(0,10);
        SimpleXYData xyds= Adapter.adapt( ds, SimpleXYData.class );
        for ( int i=0; i<xyds.size(); i++ ) {
            System.err.printf("%f %f\n", xyds.getX(i), xyds.getY(i) );
        }   
    }
    
    private static void test2() {
        System.err.println("==test2==");
        QDataSet ds= Schemes.scalarTimeSeries().trim(0,10);
        XYData xyds= Adapter.adapt( ds, XYData.class );
        XYMetadata m= xyds.getMetadata();
        Units u= Units.lookupUnits(m.getXUnits().getName());
        for ( int i=0; i<xyds.size(); i++ ) {
            System.err.printf("%s %f\n", u.createDatum( xyds.getX(i) ), xyds.getY(i) );
        }   
    }
    
    
    private static XYData demoXY() {
        XYData data= new XYData() {
            double[] xx= { 1,2,3,4,5,6,7,8,9 };
            double[] yy= { 1.5,3,4.5,3.6,99,3,4,3.5,3.7 };
            double[] dyyPlus= { 0.9,1,1,1,1,2,2,1,1 };
            double[] dyyMinus= { 1.1,1,1,2,2,2,1,1,1 };
            
            @Override
            public Optional<FillDetector> getFillDetector() {
                return Optional.of( (FillDetector) (int index) -> ( yy[index]>98 ) ); // Neat Lambda, thanks Netbeans!
            }
            
            @Override
            public Optional<UncertaintyProvider> getXUncertProvider() {
                return Optional.empty();
            }
            
            @Override
            public Optional<UncertaintyProvider> getYUncertProvider() {
                return Optional.of( new UncertaintyProvider() {
                    @Override
                    public double getUncertPlus(int i) {
                        return yy[i]+dyyPlus[i];
                    }

                    @Override
                    public double getUncertMinus(int i) {
                        return yy[i]-dyyMinus[i];
                    }
                } );
            }
            
            @Override
            public XYMetadata getMetadata() {
                return new XYMetadata() {
                    @Override
                    public sdi.data.Units getXUnits() {
                        return new sdi.data.Units("hours since 2015-03-21");
                    }
                    @Override
                    public sdi.data.Units getYUnits() {
                        return new sdi.data.Units("# * cm^-3");
                    }
                    @Override
                    public String getXName() {
                        return "exampleX";
                    }
                    @Override
                    public String getYName() {
                        return "exampleY";
                    }
                    @Override
                    public String getXLabel() {
                        return "Example X";
                    }
                    @Override
                    public String getYLabel() {
                        return "Example Y";
                    }
                    @Override
                    public String getName() {
                        return "ExampleData";
                    }
                };
            }
            @Override
            public int size() {
                return xx.length;
            }

            @Override
            public double getX(int i) {
                return xx[i];
            }

            @Override
            public double getY(int i) {
                return yy[i];
            }
        };
        return data;
    }
    
    private static void printMetadataXY( XYMetadata xy ) {
        System.err.println( "xname:  "+xy.getXName() );
        System.err.println( "xlabel: "+xy.getXLabel() );
        System.err.println( "xunits: "+xy.getXUnits() );
        System.err.println( "yname:  "+xy.getYName() );
        System.err.println( "ylabel: "+xy.getYLabel() );
        System.err.println( "yunits: "+xy.getYUnits() );
    }
    
    private static void printXYData( XYData xyds ) {  
        XYMetadata m= xyds.getMetadata();
        printMetadataXY( m );
        Optional<UncertaintyProvider> oup= xyds.getYUncertProvider();
        Optional<FillDetector> ofd= xyds.getFillDetector();
        FillDetector fd;
        if ( ofd.isPresent() ) {
            fd= ofd.get();
        } else {
            fd= (int index) -> false; // JAVA 8
        }
        Units u= Units.lookupUnits(m.getXUnits().getName());
        if ( oup.isPresent() ) {
            UncertaintyProvider up= oup.get();
            for ( int i=0; i<xyds.size(); i++ ) {
                if ( fd.isFill(i) ) {
                    System.err.printf("%s *** (***-***)\n", u.createDatum( xyds.getX(i) ),xyds.getY(i), up.getUncertMinus(i), up.getUncertPlus(i) );
                } else {
                    System.err.printf("%s %s (%f-%f)\n", u.createDatum( xyds.getX(i) ), xyds.getY(i), up.getUncertMinus(i), up.getUncertPlus(i) );                    
                }
            }            
        } else {
            for ( int i=0; i<xyds.size(); i++ ) {
                System.err.printf("%s %s\n", u.createDatum( xyds.getX(i) ), fd.isFill(i) ? "***" : xyds.getY(i) );
            }
        }
    }
    
    private static void printQDataSet( QDataSet y ) {  
        QDataSet x= (QDataSet) y.property(QDataSet.DEPEND_0);
        QDataSet dymn= (QDataSet) y.property( QDataSet.DELTA_MINUS );
        QDataSet dyup= (QDataSet) y.property( QDataSet.DELTA_PLUS );
        QDataSet wds= (QDataSet) SemanticOps.weightsDataSet(y);

        Units u= SemanticOps.getUnits(x);
        
        if ( dymn!=null && dyup!=null ) {
            for ( int i=0; i<y.length(); i++ ) {
                if ( wds.value(i)==0 ) {
                    System.err.printf("%s *** (***-***)\n", u.createDatum( x.value(i) ) );
                } else {
                    System.err.printf("%s %s (%f-%f)\n", u.createDatum( x.value(i) ), y.value(i), y.value(i)-dymn.value(i), y.value(i)+dyup.value(i));
                }
            }
        } else {
            for ( int i=0; i<y.length(); i++ ) {
                System.err.printf("%s %s\n",  u.createDatum( x.value(i) ), wds.value(i)==0 ? "***" : y.value(i) );
            }
        }
    }    
    
    private static void test4() {
        System.err.println("== test4 ==");
        XYData data= demoXY();
        printXYData( data );
        QDataSet ds= XYDataAdapter.adapt(data);
        System.err.println("== xydata -> qdataset ==");
        printQDataSet( ds );
        System.err.println("== qdataset -> xydata ==");
        XYData xyds= Adapter.adapt(ds, XYData.class );
        printXYData( xyds );
    }
    
    private static void printMetadataXYZ( XYZMetadata xyz ) {
        System.err.println( "xname:  "+xyz.getXName() );
        System.err.println( "xlabel: "+xyz.getXLabel() );
        System.err.println( "xunits: "+xyz.getXUnits() );
        System.err.println( "yname:  "+xyz.getYName() );
        System.err.println( "ylabel: "+xyz.getYLabel() );
        System.err.println( "yunits: "+xyz.getYUnits() );
        System.err.println( "zname:  "+xyz.getZName() );
        System.err.println( "zlabel: "+xyz.getZLabel() );
        System.err.println( "zunits: "+xyz.getZUnits() );
    }
    
    private static void printBinnedData2D( BinnedData2D d ) {
        printMetadataXYZ( d.getMetadata() );
        System.err.printf("%30s ","");
        for ( int j=0; j<d.sizeY(); j++ ) {
            System.err.printf( "%9.2f ",d.getYBin(j).getReference() );
        }
        Units x= Units.lookupUnits( d.getMetadata().getXUnits().getName() );
        System.err.println("");
        for ( int i=0; i<d.sizeX(); i++ ) {
            System.err.printf("%30s ", x.createDatum(d.getXBin(i).getReference()) );
            for ( int j=0; j<d.sizeY(); j++ ) {
                System.err.printf( "%9.2e ",d.getZ(i,j) );
            }
            System.err.println("");
        }
    }
    
    private static void printBinnedData2DQDataSet( QDataSet ds ) {
        System.err.printf("%30s ","");
        QDataSet y= (QDataSet) ds.property(QDataSet.DEPEND_1);
        QDataSet x= (QDataSet) ds.property(QDataSet.DEPEND_0);
        for ( int j=0; j<y.length(); j++ ) {
            System.err.printf( "%9.2f ",y.value(j) );
        }
        System.err.println("");
        for ( int i=0; i<x.length(); i++ ) {
            System.err.printf("%30s ",x.slice(i) );
            for ( int j=0; j<y.length(); j++ ) {
                System.err.printf( "%9.2e ",ds.slice(i).slice(j).value() );
            }
            System.err.println("");
        }
    }
    
    private static void test5() {
        System.err.println("== test5 ==");
        QDataSet rank2ds= Schemes.simpleSpectrogramTimeSeries();
        rank2ds= DataSetOps.leafTrim( rank2ds, 0, 8 );
        rank2ds= rank2ds.trim(0,12);
        BinnedData2D bd2d= Adapter.adapt( rank2ds, BinnedData2D.class );
        System.err.println("== qdataset -> BinnedData2D ==");
        printBinnedData2D( bd2d );
        QDataSet ds= BinnedData2DAdapter.adapt( bd2d );
        System.err.println("== BinnedData2D -> QDataSet ==");
        printBinnedData2DQDataSet( ds );
    }
    

    private static void printXYZData(XYZData xyzdata) {
        Units xu= Units.lookupUnits( xyzdata.getMetadata().getXUnits().getName() );
        Units yu= Units.lookupUnits( xyzdata.getMetadata().getYUnits().getName() );
        Units zu= Units.lookupUnits( xyzdata.getMetadata().getZUnits().getName() );
        printMetadataXYZ(xyzdata.getMetadata());
        for ( int i=0; i<xyzdata.size(); i++ ) {
            System.err.println( String.format( "%s %s %s", 
                    xu.createDatum( xyzdata.getX(i) ),
                    yu.createDatum( xyzdata.getY(i) ), 
                    zu.createDatum( xyzdata.getZ(i) ) ) );
        }
    }

    private static void printXYZDataQDataSet(QDataSet ds) {
        QDataSet x= SemanticOps.xtagsDataSet(ds);
        QDataSet y= SemanticOps.ytagsDataSet(ds);
        QDataSet z= SemanticOps.getDependentDataSet(ds);
        Units xu= SemanticOps.getUnits(x);
        Units yu= SemanticOps.getUnits(y);
        Units zu= SemanticOps.getUnits(z);
        for ( int i=0; i<x.length(); i++ ) {
            System.err.println( String.format( "%s %s %s", 
                    xu.createDatum( x.value(i) ),
                    yu.createDatum( y.value(i) ), 
                    zu.createDatum( z.value(i) ) ) );
        }
    }

    private static void test6() {
        System.err.println("== test6 ==");
        QDataSet rank2ds= Ops.ripplesVectorTimeSeries(10);
        XYZData bd2d= Adapter.adapt( rank2ds, XYZData.class );
        System.err.println("== QDataSet -> XYZData ==");
        printXYZData( bd2d );
        QDataSet ds= XYZDataAdapter.adapt( bd2d );
        System.err.println("== XYZData -> QDataSet ==");
        printXYZDataQDataSet( ds );
    }
    
    /**
     * test UncertaintyProvider2D 
     */
    public static void test7() throws Exception {
        QDataSet z= Ops.findgen(4,5);
        z= Ops.putProperty( z, QDataSet.DELTA_MINUS, Ops.dataset(0.3) );
        z= Ops.putProperty( z, QDataSet.DELTA_PLUS, Ops.dataset(0.4) );
        QDataSet x= Ops.timegen("2014-03-25T06:48", "1s", 4 );
        QDataSet y= Ops.findgen(5);
        BinnedData2D bd2d= Adapter.adapt( x, y, z, BinnedData2D.class );
        
        UncertaintyProvider2D dbd2d= bd2d.getZUncertProvider().get();
        
        for ( int i=0; i<bd2d.sizeX(); i++ ) {
            for ( int j=0; j<bd2d.sizeY(); j++ ) {
                System.err.println( String.format( "%d %d: ", i, j ) 
                        + bd2d.getZ(i,j) 
                        + " ("+dbd2d.getUncertMinus(i, j)
                        + " - "+dbd2d.getUncertPlus(i, j) + ")");
            }
        }

    }
    
    
    public static void main( String[] args ) throws Exception {
        test1();
        test2();
        test4();
        test5();
        test6();
        test7();
    }
}
