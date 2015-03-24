
package test;

import com.google.common.base.Optional;
import org.das2.datum.Units;
import org.das2.sdi.Adapter;
import org.das2.sdi.BinnedData2DAdapter;
import org.das2.sdi.XYDataAdapter;
import org.virbo.dataset.DataSetOps;
import org.virbo.dataset.QDataSet;
import org.virbo.dataset.SemanticOps;
import org.virbo.dataset.examples.Schemes;
import sdi.data.BinnedData2D;
import sdi.data.FillDetector;
import sdi.data.SimpleXYData;
import sdi.data.UncertaintyProvider;
import sdi.data.XYData;
import sdi.data.XYMetadata;
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
                return Optional.absent();
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
    
    public static void main( String[] args ) {
        test1();
        test2();
        test4();
        test5();
    }
}
