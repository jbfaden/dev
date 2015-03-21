
package test;

import com.google.common.base.Optional;
import org.das2.datum.Units;
import org.das2.sdi.Adapter;
import org.virbo.dataset.QDataSet;
import org.virbo.dataset.examples.Schemes;
import sdi.data.FillDetector;
import sdi.data.SimpleXYData;
import sdi.data.UncertaintyProvider;
import sdi.data.XYData;
import sdi.data.XYMetadata;

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
            double[] dyyPlus= { 1,1,1,1,1,2,2,1,1 };
            double[] dyyMinus= { 1,1,1,2,2,2,1,1,1 };
            
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
    
    private static void printXYData( XYData xyds ) {  
        XYMetadata m= xyds.getMetadata();
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
    
    private static void test4() {
        System.err.println("== test4 ==");
        XYData data= demoXY();
        printXYData( data );
        QDataSet ds= Adapter.adapt(data);
        System.err.println("==");
        XYData xyds= Adapter.adapt(ds, XYData.class );
        printXYData( xyds );
    }
    
    public static void main( String[] args ) {
        test1();
        test2();
        test4();
    }
}
