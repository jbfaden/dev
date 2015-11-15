
import org.tsds.TimeStruct;
import org.tsds.TimeUtil;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jbf
 */
public class TimeUtilTest {

    /**
     * in
     * http://www.pelagodesign.com/blog/2009/05/20/iso-8601-date-validation-that-doesnt-suck/
     * , there is a list of times. I don't handle all of these, but this will
     * account for what is handled.
     */
    private static void pelagoDesignTests() {
        String[] ss = new String[]{
            "2009-12T12:34", "2009/01/12T12:34:00.000Z",
            "2009", "x",
            "2009-05-19", "2009/05/19T00:00:00.000Z",
            "2009-05-19", "2009/05/19T00:00:00.000Z",
            "20090519", "x",
            "2009123", "x",
            "2009-05", "2009/01/05T00:00:00.000Z",
            "2009-123", "2009/01/123T00:00:00.000Z",
            "2009-222", "2009/01/222T00:00:00.000Z",
            "2009-001", "2009/01/01T00:00:00.000Z",
            "2009-W01-1", "x",
            "2009-W51-1", "x",
            "2009-W511", "x",
            "2009-W33", "x",
            "2009W511", "x",
            "2009-05-19", "2009/05/19T00:00:00.000Z",
            "2009-05-19 00:00", "x",
            "2009-05-19 14", "x",
            "2009-05-19 14:31", "x",
            "2009-05-19 14:39:22", "x",
            "2009-05-19T14:39Z", "2009/05/19T14:39:00.000Z",
            "2009-W21-2", "x",
            "2009-W21-2T01:22", "x",
            "2009-139", "2009/01/139T00:00:00.000Z",
            "2009-05-19T14:39:22-06:00", "x",
            "2009-05-19T14:39:22+0600", "x",
            "2009-05-19T14:39:22-01", "x",
            "20090621T0545Z", "2009/06/21T05:45:00.000Z",
            "2007-04-06T00:00", "2007/04/06T00:00:00.000Z",
            "2007-04-05T24:00", "2007/04/05T24:00:00.000Z",
            "2010-02-18T16:23:48.5", "2010/02/18T16:23:48.500Z",
            "2010-02-18T16:23:48,444", "x",
            "2010-02-18T16:23:48,3-06:00", "x",
            "2010-02-18T16:23.4", "x",
            "2010-02-18T16:23,25", "x",
            "2010-02-18T16:23.33+0600", "x",
            "2010-02-18T16.23334444", "x",
            "2010-02-18T16,2283", "x",
            "2009-05-19 143922.500", "x",
            "2009-05-19 1439,55", "x",};
        for ( int i=0; i<ss.length; i+=2 ) {
            String s= ss[i];
            String n= ss[i+1];
            if ( !n.startsWith("x") ) {
                int[] ii = TimeUtil.parseISO8601(s);
                String result = TimeStruct.create(ii).toString() + "Z";
                if ( !result.equals(n) ) {
                    throw new IllegalArgumentException("doesn't match correctly normalized result: "+s);
                } else {
                    System.err.println("\"" + s + "\", \"" + result + "\"");
                }
            } else {
                try {
                    int[] ii = TimeUtil.parseISO8601(s);
                    String result = TimeStruct.create(ii).toString() + "Z";
                    System.err.println("\"" + s + "\", \"x" + result + "\"");
                } catch ( Exception e ) {
                    System.err.println("\"" + s + "\", \"x" + e.getMessage() + "\"");
                }
            }
        }
    }

    private static void testParseISO8601() {
        String s;
        int[] ii;

        s = "2014-09-02T10:55:10-05:00";
        ii = TimeUtil.parseISO8601(s);
        System.err.println(TimeStruct.create(ii).toString() + "Z");

        // test TZ change to next day
        s = "2014-09-02T23:13:32-05:00";
        ii = TimeUtil.parseISO8601(s);
        System.err.println(TimeStruct.create(ii).toString() + "Z");

        // test TZ change to next day
        s = "2014-09-02T23:13:32-05";
        ii = TimeUtil.parseISO8601(s);
        System.err.println(TimeStruct.create(ii).toString() + "Z");

    }

    public static void main(String[] args) {
        pelagoDesignTests();
        testParseISO8601();
    }
}
