
package org.tsds;

/**
 * Container for times and time duration.
 * @author faden@cottagesystems.com
 */
public class TimeStruct {

    /**
     * year containing the time datum
     */
    public int year;
    
    /**
     * month containing the time datum
     */
    public int month;
    
    /**
     * day of month containing the time datum.  Note, to support 
     * day of year (doy), month can be set to 1 and this to the day of year.
     */
    public int day;

    /**
     * hour containing the time datum
     */
    public int hour;
    
    /**
     * minute containing the time datum
     */
    public int minute;
    
    /**
     * seconds since the last minute boundary of the time datum
     */
    public int second; 

    /**
     * additional milliseconds
     */
    public int millis;

    /**
     * additional nanoseconds
     */
    public int nanos;

    /**
     * flag indicating if this is a time duration or a particular instance in
     * time.
     */
    public boolean isLocation = false;

    @Override
    public String toString() {
        if (isLocation) {
            return String.format("%4d/%02d/%02dT%02d:%02d:%06.3f", year, month, day, hour, minute, second + millis / 1000. + nanos / 1000000000.);
        } else {
            int nanosl;
            nanosl = this.nanos;
            nanosl += millis * 1000000;
            return TimeUtil.formatISO8601Duration(new int[]{year, month, day, hour, minute, second, nanosl }); //TODO: test this.
        }
    }

    /**
     * return a copy of this TimeStruct.
     * @return a copy of this TimeStruct.
     */
    public TimeStruct copy() {
        TimeStruct result = new TimeStruct();
        result.year = this.year;
        result.month = this.month;
        result.day = this.day;
        result.hour = this.hour;
        result.minute = this.minute;
        result.second = this.second;
        result.millis = this.millis;
        result.nanos = this.nanos;
        result.isLocation = this.isLocation;
        return result;
    }

    /**
     * return the sum of a time location and a time duration, or
     * two time durations.
     * @param offset the offset.
     * @return the sum.
     */
    public TimeStruct add(TimeStruct offset) {
        if (offset.isLocation && this.isLocation) {
            throw new IllegalArgumentException("can't add two time locations!");
        }
        TimeStruct result = new TimeStruct();

        result.year = this.year + offset.year;
        result.month = this.month + offset.month;
        result.day = this.day + offset.day;
        result.hour = this.hour + offset.hour;
        result.minute = this.minute + offset.minute;
        result.second = this.second + offset.second;
        result.millis = this.millis + offset.millis;
        result.nanos = this.nanos + offset.nanos;

        result.isLocation = this.isLocation || offset.isLocation;

        return result;
    }

    /**
     * return the difference of two time durations, 
     * or two time locations, or a time location and duration.
     * @param offset the difference for each component.
     * @return duration or time location.
     */
    public TimeStruct subtract( TimeStruct offset ) {
        if ( !this.isLocation && offset.isLocation) {
            throw new IllegalArgumentException("can't subtract a time location from an duration!");
        }
        TimeStruct result = new TimeStruct();

        result.year = this.year - offset.year;
        result.month = this.month - offset.month;
        result.day = this.day - offset.day;
        result.hour = this.hour - offset.hour;
        result.minute = this.minute - offset.minute;
        result.second = this.second - offset.second;
        result.millis = this.millis - offset.millis;
        result.nanos = this.nanos - offset.nanos;

        result.isLocation = this.isLocation && !offset.isLocation;

        return result;
    }
        
    /**
     * creates a TimeStruct from the seven-element array
     * [ year, month, day, hour, minute, second, nanoseconds ].
     * When time[0] is less than 100, this will be flagged as a time duration.
     * @param time the time decomposed in a seven element array
     * @return the TimeStruct.
     */
    public static TimeStruct create( int[] time ) {
        if ( time[0]>100 && time[0]<1000 ) throw new IllegalArgumentException("time[0] must be 100 or less for durations, or more than 1000 for time locations.");
        TimeStruct result = new TimeStruct();
        result.year = time[0];
        result.month = time[1];
        result.day = time[2];
        result.hour = time[3];
        result.minute = time[4];
        result.second = time[5];
        int nanos= time[6];
        result.millis = nanos/1000000;
        nanos-= result.millis*1000000; 
        result.nanos = nanos;
        result.isLocation= time[0]>=1000;
        return result;
    }
    
    /**
     * return the components in a seven-element array
     * [ year, month, day, hour, minute, second, nanoseconds ].
     * @return the array [ year, month, day, hour, minute, second, nanoseconds ].
     */
    public int[] components() {
        normalize();
        return new int[] { 
            this.year, this.month, this.day, this.hour, this.minute, 
            this.second,
            this.millis*1000000 + this.nanos
        };
    }
    
    /**
     * return this time struct, after normalizing
     * all the components, so no component is 
     * greater than its expected range.
     * Note that leap seconds are not accounted for.  TODO: account for them.
     * @return the normalized TimeStruct.
     */
    public TimeStruct normalize() {
        while ( this.nanos>=1000000 ) {
            this.millis+= 1;
            this.nanos-= 1000000;
        }
        while ( this.nanos<0 ) {
            this.millis-= 1;
            this.nanos+= 1000000;
        }
        while ( this.millis>=1000 ) {
            this.second+= 1;
            this.millis-= 1000;
        }
        while ( this.millis<0 ) {
            this.second-= 1;
            this.millis+= 1000;
        }
        while ( this.second>=60 ) { // TODO: leap seconds
            this.minute+= 1;
            this.second-= 60;
        }
        while ( this.second<0 ) { // TODO: leap seconds
            this.minute-= 1;
            this.second+= 60;
        }
        while ( this.minute>=60 ) {
            this.hour+= 1;
            this.minute-= 60;
        }
        while ( this.minute<0 ) {
            this.hour-= 1;
            this.minute+= 60;
        }
        while ( this.hour>=23 ) {
            this.day+= 1;
            this.hour-= 24;
        }
        while ( this.hour<0 ) {
            this.day-= 1;
            this.hour+= 24;
        }
        // Irregular month lengths make it impossible to do this nicely.  Either 
        // months should be incremented or days should be incremented, but not
        // both.  Note Day-of-Year will be normalized to Year,Month,Day here
        // as well.  e.g. 2000/13/01 because we incremented the month.
        if ( this.day>28 ) {  
            int daysInMonth= TimeUtil.daysInMonth( this.month, this.year );
            while ( this.day > daysInMonth ) {
                this.day-= daysInMonth;
                this.month+= 1;
                if ( this.month>12 ) break;
                daysInMonth= TimeUtil.daysInMonth( this.month, this.year );
            }
        }
        if ( this.day==0 ) { // handle borrow when it is no more than one day.
            this.month=- 1;
            if ( this.month==0 ) {
                this.month= 12;
                this.year-= 1;
            }
            int daysInMonth= TimeUtil.daysInMonth( this.month, this.year );
            this.day= daysInMonth;
        }
        while ( this.month>12 ) {
            this.year+= 1;
            this.month-= 12;
        }
        if ( this.month<0 ) { // handle borrow when it is no more than one year.
            this.year+= 1;
            this.month+= 12;
        }
        return this;
    }

    /**
     * return true if this is before stop for locations, 
     * or less than for durations.  This may have the side effect of 
     * normalizing the two.
     * @param stop another time location or duration.
     * @return return true if this is before stop for locations
     */
    public boolean lt(TimeStruct stop) {
        if ( this.isLocation!=stop.isLocation ) throw new IllegalArgumentException("both must be locations or durations");
        normalize();
        stop.normalize();
        int[] cthis= components();
        int[] cstop= stop.components();
        for ( int i=0; i<cthis.length; i++ ) {
            if ( cthis[i]<cstop[i] ) return true;
            if ( cthis[i]>cstop[i] ) return false;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.year;
        hash = 97 * hash + this.month;
        hash = 97 * hash + this.day;
        hash = 97 * hash + this.hour;
        hash = 97 * hash + this.minute;
        hash = 97 * hash + this.second;
        hash = 97 * hash + this.millis;
        hash = 97 * hash + this.nanos;
        hash = 97 * hash + (this.isLocation ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals( Object o ) {
        if ( !( o instanceof TimeStruct ) ) {  
            return false;
        }
        TimeStruct stop= (TimeStruct)o;
        int[] cthis= components();
        int[] cstop= stop.components();
        for ( int i=0; i<cthis.length; i++ ) {
            if ( cthis[i]!=cstop[i] ) return false;
            if ( cthis[i]!=cstop[i] ) return false;
        }
        return true;
    }
    
    
    /**
     * return the next interval following the interval dr.
     * @param dr two-element array of TimeStruct.
     * @return the next interval in a two-element array.
     */
    public static TimeStruct[] next(TimeStruct[] dr) {
        TimeStruct delta= dr[1].subtract(dr[0]);
        return new TimeStruct[] { dr[0].add(delta).normalize(), dr[1].add(delta).normalize() };
    }
    
    
    public static void main( String[] args ) {
        TimeStruct ts1, ts2;
        ts1= TimeStruct.create(new int[] { 2000,4,5, 0,0, 0, 0 } );
        ts2= TimeStruct.create(new int[] { 2000,4,4, 24,0, 0, 0 } );
        System.err.println( ts1.equals(ts2) );
    }

    
}

