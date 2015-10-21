/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tsds;

import java.text.ParseException;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author jbf
 */
public class TimeUtil {
    
    private static final Pattern time1, time2, time3, time4, time5;
    
    static {
        String d= "[-:]"; // delim
        String i4= "(\\d\\d\\d\\d)";
        String i3= "(\\d+)";
        String i2= "(\\d\\d)";

        String iso8601time= i4 + d + i2 + d + i2 + "T" + i2 + d + i2 + "((" + d + i2 + "(\\." + i3 + ")?)?)Z?" ;  // "2012-03-27T12:22:36.786Z"
        String iso8601time2= i4 + i2 + i2 + "T" + i2 + i2 + "(" + i2 + ")?Z?" ;
        String iso8601time3= i4 + d + i3 + "T" + i2 + d + i2 + "(" + i2 + ")?Z?" ;
        String iso8601time4= i4 + d + i2 + d + i2 + "Z?" ;
        String iso8601time5= i4 + d + i3 + "Z?" ;
        time1= Pattern.compile(iso8601time);
        time2= Pattern.compile(iso8601time2);
        time3= Pattern.compile(iso8601time3);
        time4= Pattern.compile(iso8601time4);
        time5= Pattern.compile(iso8601time5);
    }
    
    private static int getInt( String val, int deft ) {
        if ( val==null ) {
            if ( deft!=-99 ) return deft; else throw new IllegalArgumentException("bad digit");
        }
        int n= val.length()-1;
        if ( Character.isLetter( val.charAt(n) ) ) {
            return Integer.parseInt(val.substring(0,n));
        } else {
            return Integer.parseInt(val);
        }
    }
    
    private static double getDouble( String val, double deft ) {
        if ( val==null ) {
            if ( deft!=-99 ) return deft; else throw new IllegalArgumentException("bad digit");
        }
        int n= val.length()-1;
        if ( Character.isLetter( val.charAt(n) ) ) {
            return Double.parseDouble(val.substring(0,n));
        } else {
            return Double.parseDouble(val);
        }
    }
    
    /**
     * Parser for ISO8601 formatted times.
     * returns null or int[7]: [ Y, m, d, H, M, S, nano ]
     * The code cannot parse any iso8601 string, but this code should.  Right now it parses:
     * "2012-03-27T12:22:36.786Z"
     * "2012-03-27T12:22:36"
     * (and some others) TODO: enumerate and test.
     * TODO: this should use parseISO8601Datum.
     * @param str iso8601 string.
     * @return null or int[7]: [ Y, m, d, H, M, S, nano ]
     */
    public static int[] parseISO8601 ( String str ) {

        Matcher m;

        m= time1.matcher(str);
        if ( m.matches() ) {
            String sf= m.group(10);
            if ( sf!=null && sf.length()>9 ) throw new IllegalArgumentException("too many digits in nanoseconds part");
            int nanos= sf==null ? 0 : ( Integer.parseInt(sf) * (int)Math.pow( 10, ( 9 - sf.length() ) ) );
            return new int[] { Integer.parseInt( m.group(1) ), Integer.parseInt( m.group(2) ), Integer.parseInt( m.group(3) ), getInt( m.group(4), 0 ), getInt( m.group(5), 0 ), getInt( m.group(8), 0), nanos };
        } else {
            m= time2.matcher(str);
            if ( m.matches() ) {
                return new int[] { Integer.parseInt( m.group(1) ), Integer.parseInt( m.group(2) ), Integer.parseInt( m.group(3) ), getInt( m.group(4), 0 ), getInt( m.group(5), 0 ), getInt( m.group(7), 0), 0 };
            } else {
                m= time3.matcher(str);
                if ( m.matches() ) {
                    return new int[] { Integer.parseInt( m.group(1) ), 1, Integer.parseInt( m.group(2) ), getInt( m.group(3), 0 ), getInt( m.group(4), 0 ), getInt( m.group(5), 0), 0 };
                } else {
                    m= time4.matcher(str);
                    if ( m.matches() ) {
                        return new int[] { Integer.parseInt( m.group(1) ), Integer.parseInt( m.group(2) ), getInt( m.group(3), 0 ), 0, 0, 0, 0 };
                    } else {
                        m= time5.matcher(str);
                        if ( m.matches() ) {
                            return new int[] { Integer.parseInt( m.group(1) ), 1, Integer.parseInt( m.group(2) ), 0, 0, 0, 0 };
                        }
                    }
                }
            }
        }
        return null;
    }
    
    private static final String simpleFloat= "\\d?\\.?\\d+";
    public static final String iso8601duration= "P(\\d+Y)?(\\d+M)?(\\d+D)?(T(\\d+H)?(\\d+M)?("+simpleFloat+"S)?)?";
    public static final Pattern iso8601DurationPattern= Pattern.compile(iso8601duration);

    /**
     * returns a 7 element array with [year,mon,day,hour,min,sec,nanos].
     * @param stringIn
     * @return 7-element array with [year,mon,day,hour,min,sec,nanos]
     * @throws ParseException if the string does not appear to be valid.
     */
    public static int[] parseISO8601Duration( String stringIn ) throws ParseException {
        Matcher m= iso8601DurationPattern.matcher(stringIn);
        if ( m.matches() ) {
            double dsec=getDouble( m.group(7),0 );
            int sec= (int)dsec;
            int nanosec= (int)( ( dsec - sec ) * 1e9 );
            return new int[] { getInt( m.group(1), 0 ), getInt( m.group(2), 0 ), getInt( m.group(3), 0 ), getInt( m.group(5), 0 ), getInt( m.group(6), 0 ), sec, nanosec };
        } else {
            if ( stringIn.contains("P") && stringIn.contains("S") && !stringIn.contains("T") ) {
                throw new ParseException("ISO8601 duration expected but not found.  Was the T missing before S?",0);
            } else {
                throw new ParseException("ISO8601 duration expected but not found.",0);
            }
        }
    }
    
    /**
     * returns the time found in an iso8601 string, or null.  This supports
     * periods (durations) as in: 2007-03-01T13:00:00Z/P1Y2M10DT2H30M
     * Other examples:<ul>
     *   <li>2007-03-01T13:00:00Z/2008-05-11T15:30:00Z
     *   <li>2007-03-01T13:00:00Z/P1Y2M10DT2H30M
     *   <li>P1Y2M10DT2H30M/2008-05-11T15:30:00Z
     *   <li>2007-03-01T00:00Z/P1D
     *   <li>2012-100T02:00/03:45
     * </ul>
     * http://en.wikipedia.org/wiki/ISO_8601#Time_intervals
     * @param stringIn the iso8601 time.
     * @return null or a DatumRange
     * @throws java.text.ParseException
     */
    public static TimeStruct[] parseISO8601Range( String stringIn ) throws ParseException {

        String[] parts= stringIn.split("/",-2);
        if ( parts.length!=2 ) return null;

        boolean d1= parts[0].charAt(0)=='P'; // true if it is a duration
        boolean d2= parts[1].charAt(0)=='P';

        int[] digits0;
        int[] digits1;
        int lsd= -1;

        if ( d1 ) {
            digits0= parseISO8601Duration( parts[0] );
        } else {
            digits0= new int[7];
            lsd= parseISO8601Datum( parts[0], digits0, lsd );
            for ( int j=lsd+1; j<3; j++ ) digits0[j]=1; // month 1 is first month, not 0. day 1 
        }

        if ( d2 ) {
            digits1= parseISO8601Duration(parts[1]);
        } else {
            if ( d1 ) {
                digits1= new int[7];
            } else {
                digits1= Arrays.copyOf( digits0, digits0.length );
            }
            lsd= parseISO8601Datum( parts[1], digits1, lsd );
            for ( int j=lsd+1; j<3; j++ ) digits1[j]=1; // month 1 is first month, not 0. day 1 
        }

        if ( digits0==null || digits1==null ) return null;
        
        if ( d1 ) {
            for ( int i=0; i<7; i++ ) digits0[i] = digits1[i] - digits0[i];
        }

        if ( d2 ) {
            for ( int i=0; i<7; i++ ) digits1[i] = digits0[i] + digits1[i];
        }

        TimeStruct t0= TimeStruct.create(digits0);
        TimeStruct t1= TimeStruct.create(digits1);
                
        return new TimeStruct[] { t0, t1 };

    }
    
    /**
     * format the two time structs that are the start and end of a range.
     * @param timeRange two-element array of time structs.
     * @return the formatted time range.
     * TODO: this can be more efficient.
     */
    public static String formatISO8601Range(TimeStruct[] timeRange) {
        
        int[] t1= timeRange[0].components();
        int[] t2= timeRange[1].components();
        
        int lsd=0;
        for ( int i=0; i<t1.length; i++ ) {
            if ( t1[i]!=0 || t2[i]!=0 ) {
                lsd= i;
            }
        }
        StringBuilder st1= new StringBuilder( String.format( "%4d-%02d-%02dT%02d:%02d", 
                        t1[0], t1[1], t1[2],
                        t1[3], t1[4] ) );
        StringBuilder st2= new StringBuilder( String.format( "%4d-%02d-%02dT%02d:%02d", 
                        t2[0], t2[1], t2[2],
                        t2[3], t2[4] ) );
        if ( 5<=lsd ) {
            st1.append( String.format( ":%02d", t1[5] ) );
            st2.append( String.format( ":%02d", t2[5] ) );
        }
        if ( 6<=lsd ) {
            if ( t1[6] % 1000000 == 0 && t2[6] % 1000000 == 0 ) {
                st1.append( String.format( ".%03d", t1[6]/1000000 ) );
                st2.append( String.format( ".%03d", t2[6]/1000000 ) );
            } else if ( t1[6] % 1000 == 0 && t2[6] % 1000 == 0 ) {
                st1.append( String.format( ".%06d", t1[6]/1000 ) );
                st2.append( String.format( ".%06d", t2[6]/1000 ) );
            } else {
                st1.append( String.format( ".%09d", t1[6] ) );
                st2.append( String.format( ".%09d", t2[6] ) );
            }
        }
        
        return st1.toString() + "/" + st2.toString();
        
    }
    
    /**
     * format ISO8601 duration string, for example [1,0,0,1,0,0,0] -> P1YT1H
     * @param t 6 or 7-element array with [year,mon,day,hour,min,sec,nanos]
     * @return the formatted ISO8601 duration
     */
    public static String formatISO8601Duration( int[] t ) {
        StringBuilder result= new StringBuilder(24);
        result.append('P');
        if ( t[0]!=0 ) result.append(t[0]).append('Y');
        if ( t[1]!=0 ) result.append(t[1]).append('M');
        if ( t[2]!=0 ) result.append(t[2]).append('D');
        if ( t[3]!=0 || t[4]!=0 || t[5]!=0 || ( t.length==7 && t[6]!=0 ) ) {
            result.append('T');
            if ( t[3]!=0 ) result.append(t[3]).append('H');
            if ( t[4]!=0 ) result.append(t[4]).append('M');
            if ( t[5]!=0 || ( t.length==7 && t[6]!=0 ) ) {
                if ( t.length<7 || t[6]==0 ) {
                    result.append(t[5]).append('S');
                } else {
                    double sec= t[5] + t[6]/1000000000.;
                    result.append( sec ).append('S');
                }
            }
        }
        return result.toString();
    }
    
    /**
     * new attempt to write a clean ISO8601 parser.  This should also parse 02:00
     * in the context of 2010-002T00:00/02:00.  This does not support 2-digit years, which
     * were removed in ISO 8601:2004.
     * 
     * @param str the ISO8601 string
     * @param result the datum, decomposed into [year,month,day,hour,minute,second,nano]
     * @param lsd -1 or the current position ???
     * @return the lsd least significant digit
     */
    public static int parseISO8601Datum( String str, int[] result, int lsd ) {
        StringTokenizer st= new StringTokenizer( str, "-T:.Z", true );
        Object dir= null;
        final Object DIR_FORWARD = "f";
        final Object DIR_REVERSE = "r";
        int want= 0;
        boolean haveDelim= false;
        while ( st.hasMoreTokens() ) {
            char delim= ' ';
            if ( haveDelim ) {
                delim= st.nextToken().charAt(0);
                if ( st.hasMoreElements()==false ) { // "Z"
                    break;
                }
            } else {
                haveDelim= true;
            }
            String tok= st.nextToken();
            if ( dir==null ) {
                if ( tok.length()==4 ) { // typical route
                    int iyear= Integer.parseInt( tok ); 
                    result[0]= iyear;
                    want= 1;
                    dir=DIR_FORWARD;
                } else if ( tok.length()==6 ) {
                    want= lsd;
                    if ( want!=6 ) throw new IllegalArgumentException("lsd must be 6");
                    result[want]= Integer.parseInt( tok.substring(0,2) );
                    want--;
                    result[want]= Integer.parseInt( tok.substring(2,4) );
                    want--;
                    result[want]= Integer.parseInt( tok.substring(4,6) );
                    want--;
                    dir=DIR_REVERSE; 
                } else if ( tok.length()==7 ) {
                    result[0]= Integer.parseInt( tok.substring(0,4) );
                    result[1]= 1;
                    result[2]= Integer.parseInt( tok.substring(4,7) );
                    want= 3;                    
                    dir=DIR_FORWARD; 
                } else if ( tok.length()==8 ) {
                    result[0]= Integer.parseInt( tok.substring(0,4) );
                    result[1]= Integer.parseInt( tok.substring(4,6) );
                    result[2]= Integer.parseInt( tok.substring(6,8) );
                    want= 3;                    
                    dir=DIR_FORWARD;
                } else {
                    dir= DIR_REVERSE;
                    want= lsd;  // we are going to have to reverse these when we're done.
                    int i= Integer.parseInt( tok );
                    result[want]= i;
                    want--;
                }
            } else if ( dir==DIR_FORWARD) {
                if ( want==1 && tok.length()==3 ) { // $j
                    result[1]= 1;
                    result[2]= Integer.parseInt( tok ); 
                    want= 3;
                } else if ( want==3 && tok.length()==6 ) {
                    result[want]= Integer.parseInt( tok.substring(0,2) );
                    want++;
                    result[want]= Integer.parseInt( tok.substring(2,4) );
                    want++;
                    result[want]= Integer.parseInt( tok.substring(4,6) );
                    want++;
                } else if ( want==3 && tok.length()==4 ) {
                    result[want]= Integer.parseInt( tok.substring(0,2) );
                    want++;
                    result[want]= Integer.parseInt( tok.substring(2,4) );
                    want++;
                } else {
                    int i= Integer.parseInt( tok );
                    if ( delim=='.' && want==6 ) {
                        int n= 9-tok.length();
                        result[want]= i * ((int)Math.pow(10,n));
                    } else {
                        result[want]= i;
                    }
                    want++;
                }
            } else if ( dir==DIR_REVERSE ) { // what about 1200 in reverse?
                int i= Integer.parseInt( tok ); 
                if ( delim=='.' ) {
                    int n= 9-tok.length();
                    result[want]= i * ((int)Math.pow(10,n));
                } else {
                    result[want]= i;
                }
                want--;
            }
        }
        
        if ( dir==DIR_REVERSE ) {
            int iu= want+1;
            int id= lsd;
            while( iu<id ) {
                int t= result[iu];
                result[iu]= result[id];
                result[id]= t;
                iu= iu+1;
                id= id-1;
            }
        } else {
            lsd= want-1;
        }
        
        return lsd;
    }
    
    /**
     * return the leap year for years 1581-8999.
     * @param year the four-digit year.
     * @return true if the year is a leap year.
     */
    public static boolean isLeapYear( int year ) {
        if ( year<1000 ) throw new IllegalArgumentException("year must be four-digits");
        return ( year % 4 ) == 0 && ( year % 100 != 0 || year % 400 == 0 );
    }
        
    private final static String[] mons= {
        "jan", "feb", "mar", "apr", "may", "jun",
        "jul", "aug", "sep", "oct", "nov", "dec"
    };
    
    /**
     * returns 1..12 for the English month name.  (Sorry, rest of world...)
     *
     * @param s the three-letter month name, jan,feb,...,nov,dec
     * @return 1,2,...,11,12 for the English month name
     * @throws ParseException if the name isn't recognized
     */
    public static int monthNumber( String s ) throws ParseException {
        if ( s.length()<3 ) throw new ParseException("need at least three letters",0);
        s= s.substring(0,3);
        for ( int i=0; i<12; i++ ) {
            if ( s.equalsIgnoreCase( mons[i] ) ) return i+1;
        }
        throw new ParseException("Unable to parse month", 0 );
    }
    
    /**
     * returns "jan", "jeb", ... for month number (1..12).
     * @param mon integer from 1 to 12.
     * @return three character English month name.
     */
    public static String monthNameAbbrev( int mon ) {
        if ( mon<1 || mon>12 ) throw new IllegalArgumentException("invalid month number: "+mon);
        return mons[mon-1];
    }    
    
    private final static int[][] daysInMonth = {
        {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31},
        {0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}
    };
        
    /**
     * return the number of days in the month of the year.  Note the 
     * result is not valid for years less than 1600 or so.  
     * @param month the month (1..12)
     * @param year the year (1000..8999)
     * @return the number of days in the month.
     */
    public static int daysInMonth(int month, int year) {
        return daysInMonth[isLeapYear(year)?1:0][month];
    }

    private final static int[][] dayOffset = {
        {0, 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 365},
        {0, 0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335, 366}
    };
    
    /**
     * return the day of year for the month and day, for the given year
     * @param month the month, january=1, february=2, etc.
     * @param day day of month
     * @param year four-digit year
     * @return the day of year
     */
    public static int dayOfYear( int month, int day, int year ) {
        return day + dayOffset[isLeapYear(year)?1:0][month];
    }
        
    /**
     * calculation of julianDay based on http://www.imcce.fr/en/grandpublic/temps/jour_julien.php
     * This is slightly slower because of a cusp at 1582, but is accurate
     * before these times.
     * @param YY  Gregorian year
     * @param MM  Gregorian month
     * @param DD Gregorian day
     * @return 
     */
    public static int julianDayIMCCE( int YY, int MM, int DD ) {
        int GGG = 1;
        if( YY < 1582 ) GGG = 0;
        if( YY <= 1582 && MM < 10 ) GGG = 0;
        if( YY <= 1582 && MM == 10 && DD < 5 ) GGG = 0;
        int JD = -1 * (7 * ( ((MM + 9) / 12) + YY) / 4);
        int S = 1;
        if ((MM - 9) < 0) S = -1;
        int A = Math.abs(MM - 9);
        int J1 = (YY + S * (A / 7));
        J1 = -1 * (((J1 / 100) + 1) * 3 / 4);
        JD = JD + (275 * MM / 9) + DD + (GGG * J1);
        JD = JD + 1721027 + 2 * GGG + 367 * YY;
        return JD;
    }
    
    /**
     *Break the Julian day apart into month, day year.  This is based on
     *http://en.wikipedia.org/wiki/Julian_day (GNU Public License), and 
     *was introduced when toTimeStruct failed when the year was 1886.
     *@see julianDay( int year, int mon, int day )
     *@param julian the (integer) number of days that have elapsed since the initial epoch at noon Universal Time (UT) Monday, January 1, 4713 BC
     *@return a TimeStruct with the month, day and year fields set.
     */
    public static TimeStruct julianToGregorian( int julian ) {
        int j = julian + 32044;
        int g = j / 146097;
        int dg = j % 146097;
        int c = (dg / 36524 + 1) * 3 / 4;
        int dc = dg - c * 36524;
        int b = dc / 1461;
        int db = dc % 1461;
        int a = (db / 365 + 1) * 3 / 4;
        int da = db - a * 365;
        int y = g * 400 + c * 100 + b * 4 + a;
        int m = (da * 5 + 308) / 153 - 2;
        int d = da - (m + 4) * 153 / 5 + 122;
        int Y = y - 4800 + (m + 2) / 12;
        int M = (m + 2) % 12 + 1;
        int D = d + 1;
        TimeStruct result= new TimeStruct();
        result.year= Y;
        result.month= M;
        result.day= D;
        result.isLocation= true;
        return result;
    }

}
