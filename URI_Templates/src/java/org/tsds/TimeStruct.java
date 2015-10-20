/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tsds;

/**
 * Container for times and time duration.
 * @author jbf
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
     * day of month containing the time datum
     */
    public int day;
    /**
     * day of year containing the time datum
     */
    public int doy;
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
    public double seconds; // remaining number of seconds past minute boundary

    /**
     * additional milliseconds since minute boundary
     */
    public int millis;

    /**
     * additional microseconds since minute boundary
     */
    public int micros;

    /**
     * flag indicating if this is a time duration or a particular instance in
     * time.
     */
    public boolean isLocation = false;

    @Override
    public String toString() {
        if (isLocation) {
            return String.format("%4d/%02d/%02d %02d:%02d:%06.3f", year, month, day, hour, minute, seconds + millis / 1000. + micros / 1000000.);
        } else {
            int intSeconds = (int) seconds;
            int nanos = (int) (1000000000 * (seconds - intSeconds));
            nanos += micros * 1000;
            nanos += millis * 1000000;
            return TimeUtil.formatISO8601Duration(new int[]{year, month, day, hour, minute, intSeconds, nanos}); //TODO: test this.
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
        result.seconds = this.seconds;
        result.millis = this.millis;
        result.micros = this.micros;
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
            throw new IllegalArgumentException("can't add two times!");
        }
        TimeStruct result = new TimeStruct();

        result.year = this.year + offset.year;
        result.month = this.month + offset.month;
        result.day = this.day + offset.day;
        result.hour = this.hour + offset.hour;
        result.minute = this.minute + offset.minute;
        result.seconds = this.seconds + offset.seconds;
        result.millis = this.millis + offset.millis;
        result.micros = this.micros + offset.micros;

        result.isLocation = this.isLocation || offset.isLocation;

        return result;
    }

    /**
     * return the difference of two time durations, or two time locations.
     * @param offset
     * @return 
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
        result.seconds = this.seconds - offset.seconds;
        result.millis = this.millis - offset.millis;
        result.micros = this.micros - offset.micros;

        result.isLocation = false;

        return result;
    }
        
    /**
     * creates a TimeStruct from the seven-element array
     * [ year, month, day, hour, minute, second, nanos ]
     * @param time
     * @return 
     */
    public static TimeStruct create( int[] time ) {
        if ( time[0]>100 && time[0]<1000 ) throw new IllegalArgumentException("time[0] must be 100 or less for durations, or more than 1000 for time locations.");
        TimeStruct result = new TimeStruct();
        result.year = time[0];
        result.month = time[1];
        result.day = time[2];
        result.hour = time[3];
        result.minute = time[4];
        result.seconds = time[5];
        int nanos= time[6];
        result.millis = nanos/1000000;
        nanos-= result.millis*1000;
        result.micros = nanos/1000;
        result.isLocation= time[0]>=1000;
        return result;
    }
    
    /**
     * return the components in a seven-element array
     * [ year, month, day, hour, minute, second, nanos ]
     * @return the array
     */
    public int[] components() {
        return new int[] { 
            this.year, this.month, this.day, this.hour, this.minute, 
            (int)this.seconds, 
            this.millis*1000000 + this.micros* 1000 
        };
    }
    
    /**
     * return this time struct, after normalizing
     * all the components, so no component is 
     * greater than its expected range.
     * @return the normalized TimeStruct.
     */
    public TimeStruct normalize() {
        while ( this.micros>=1000 ) {
            this.millis+= 1;
            this.micros-= 1000;
        }
        while ( this.millis>=1000 ) {
            this.seconds+= 1;
            this.millis-= 1000;
        }
        while ( this.seconds>=60 ) { // TODO: leap seconds
            this.minute+= 1;
            this.seconds-= 60;
        }
        while ( this.minute>=60 ) {
            this.hour+= 1;
            this.minute-= 60;
        }
        while ( this.hour>=23 ) {
            this.day+= 1;
            this.hour-= 24;
        }
        int daysInMonth= TimeUtil.daysInMonth( this.month, this.year);
        while ( this.day >= daysInMonth ) {
            this.day-= daysInMonth;
            this.month+= 1;
            daysInMonth= TimeUtil.daysInMonth( this.month, this.year);
        }
        while ( this.month>= 12 ) {
            this.year+= 1;
            this.month-= 12;
        }
        return this;
    }

    /**
     * return true if this is before stop for locations, or less than
     * for durations.  This may have the side effect of normalizing the
     * two.
     * @param stop
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

    /**
     * return the next interval following the interval dr.
     * @param dr two-element array of TimeStruct.
     * @return the next interval in a two-element array.
     */
    public static TimeStruct[] next(TimeStruct[] dr) {
        TimeStruct delta= dr[1].subtract(dr[0]);
        return new TimeStruct[] { dr[0].add(delta).normalize(), dr[1].add(delta).normalize() };
    }

    
}

