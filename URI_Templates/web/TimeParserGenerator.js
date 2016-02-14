/**
 * make the timerange into a canonical timerange for
 * processing.  The result will have 26*2+1 characters:
 * YYYY-mm-ddTHH:MM:SS.ssssss/YYYY-mm-ddTHH:MM:SS.ssssss
 * Note no Z's!
 * @param {String} timerange
 * @returns {String} 53-character ASCII string.
 */
function makeCanonical( timerange ) {
    l= Math.floor( timerange.length / 2 );
    hasZs= timerange.charAt(l-1)==='Z';
    if ( hasZs ) {
        throw "timerange should not contain Zs indicating timezone.";
    }
    if ( timerange.charAt(l)!=='/' ) {
        throw "timerange must have / for middle character";
    }
    pad= "0000-01-01T00:00:00.000000";
    t1= timerange.substring(0,l) + pad.substr(l,26);
    t2= timerange.substring(l) + pad.substr(l,26);
    return t1 + t2;
}

/**
 * private method for copying time object.
 * @param {TimeStruct} src
 * @param {TimeStruct} dst
 */
function copyTime( src, dst ) {
    dst.year = src.year;
    dst.month = src.month;
    dst.day = src.day;
    dst.hour = src.hour;
    dst.minute = src.minute;
    dst.second = src.second;
    dst.millis = src.millis;
    dst.nanos = src.nanos;
    dst.isLocation= src.isLocation;
}

/**
 * format the time.
 * @param {TimeStruct} ts
 * @returns {String} "0000-01-01T00:00:00.000000"
 */
function formatTime( ts ) {   
    return sprintf( "%04d-%02d-%02dT%02d:%02d:%02d.%06d", ts.year, ts.month, ts.day, ts.hour, ts.minute, ts.second, ts.millis*1000 + ts.nanos/1000 );
}


/**
 * private method also creates the object
 * @param {TimeStruct} src
 * @return {TimeStruct} new time that is a copy of src.
 */
function copyTime( src ) {
    dst= {};
    copyTime( src, dst );
    return dst;
}

/**
 * 
 * @param {TimeStruct} location
 * @param {TimeStruct} duration
 * @returns {TimeStruct}
 */
function add( location, duration ) {
    dst= {};   
    dst.year = location.year + duration.year;
    dst.month = location.month + duration.month;
    dst.day = location.day + duration.day;
    dst.hour = location.hour + duration.hour;
    dst.minute = location.minute + duration.minute;
    dst.second = location.second + duration.second;
    dst.millis = location.millis + duration.millis;
    dst.nanos = location.nanos + duration.nanos;
    dst.isLocation= location.isLocation || duration.isLocation;
    return dst;
}

/**
 * create a decomposed time, with separate fields for years, months, etc.
 * @param {boolean} isLocation true if it is a location, false if it is a duration.
 * @returns {TimeStruct}
 */
function createTimeStruct( isLocation ) {
    ts= {};
    /**
     * year containing the time datum
     */
    ts.year= isLocation ? 9000 : 0 ;
    
    /**
     * month containing the time datum
     */
    ts.month= isLocation ? 1 : 0;
    
    /**
     * day of month containing the time datum.  Note, to support 
     * day of year (doy), month can be set to 1 and this to the day of year.
     */
    ts.day= isLocation ? 1 : 0;

    /**
     * hour containing the time datum
     */
    ts.hour= 0;
    
    /**
     * minute containing the time datum
     */
    ts.minute= 0;
    
    /**
     * seconds since the last minute boundary of the time datum
     */
    ts.second= 0;

    /**
     * additional milliseconds
     */
    ts.millis=0;

    /**
     * additional nanoseconds
     */
    ts.nanos= 0;
    
    /**
     * flag indicating if this is a time duration or a particular instance in
     * time.
     */
    ts.isLocation = isLocation;
    
    return ts;
}

/**
 * 
 * @param {String} spec
 * @param {String} file
 * @returns {String} timerange
 */
function parse( spec, file ) {
    spec= setup(spec);
    n= spec.numberOfFields;
    
    startTime= createTimeStruct(true);
    time= startTime;
    
    for ( i=0; i<n; i++ ) {
        if ( spec.parseOffsets[i]!==-1 ) {
            field= parseInt( file.substring( spec.parseOffsets[i], spec.parseOffsets[i] + spec.lengths[i] ) );
            switch ( spec.fieldCodes[i] ) {
                case "Y":
                    time.year= field;
                    break;
                case "m":
                    time.month= field;
                    break;
                case "d":
                    time.day= field;
                    break;
                case "H":
                    time.hour= field;
                    break;
                case "M":
                    time.minute= field;
                    break;
                case "S":
                    time.second= field;
                    break;
                case "subsec":
                    mult= Math.pow( 10, 9-spec.fieldModifiers[i].places );
                    time.nanos= mult * field;
                default:
                    throw "unsupported field code: "+spec.fieldCodes[i];
            }            
        } else {
            throw "variable length and odd fields not yet implemented";
        }
    }
    
    stopTime= add( startTime, spec.timeWidth );
    
    return formatTime(startTime) + "/" + formatTime(stopTime);
    
}

/**
 * Format the timeranges into filenames based on the template.  For example,
 * If spec is $Y$m$d.dat and timerange is 2015-01-01T00:00/2015-01-04T00:00, 
 * then the three strings are returned 20150101.dat, 20150102.dat, 20150103.dat.
 * @param {String} spec e.g. $Y$m$d
 * @param {String} timerange e.g. 2015-01-01T00:00/2015-01-10T00:00
 * @returns {String array} files
 */
function format( spec, timerange ) {
    spec= setup(spec);
    timerange= makeCanonical(timerange);
    buf= spec.prefix;
    n= spec.numberOfFields;
    for ( i=0; i<n; i++ ) {
        if ( spec.formatOffsets[0]!==-1 ) {
            field= timerange.substring( spec.beginEndOffsets[i] + spec.formatOffsets[i],  spec.beginEndOffsets[i] + spec.formatOffsets[i]+spec.lengths[i] );
        } else {
            throw "variable length and odd fields not yet implemented";
        }
        buf= buf + field + spec.delims[i];
    }
    return buf;
}

/**
 * returns fieldcode, len, delta, beginend
 * Only $Y, $m, $d, $H, $M, $S are supported presently.
 * No modifiers are supported.
 * @param {type} spec
 * @returns {undefined}
 */
function setup( spec ) {
    var fieldCodes= {};      // field codes
    var fieldModifiers= {};  // modifiers for any field.
    var delims={};           // constant characters between fields.
    var beginEndOffset={};   // 26 for end fields, 0 for start fields
    var lengths={};          // number of characters in field, -1 for unknown.
    var parseOffsets={};     // offset into filename we are parsing.
    var formatOffsets={};    // offset into 26-character canonical format.
    var deltas={};           // step, usually one, of this unit.
    ss= spec.split( '$' );
    position= ss[0].length;
    
    timeWidth= createTimeStruct()
    smallestField= 0;
            
    for ( i=0; i<ss.length-1; i++ ) { 
        s= ss[i+1];
        if ( s.charAt(0)==='(' ) {
            index= s.indexOf(')');
            fcparams= s.substring(1,index);
            delims[i]= s.substring(index+1);
            fcparamsSplit= fcparams.split(";");
            fieldCodes[i]= fcparamsSplit[0];
            deltas[i]= 1; // see below where this might be updated
            fieldModifiers[i]= {};
            for ( k=1; k<fcparamsSplit.length; k++ ) {
                paramVal= fcparamsSplit[k].split("=");
                modifier= paramVal[0];
                value= paramVal[1];
                fieldModifiers[i][modifier]= value;
                if ( modifier==="delta" ) deltas[i]= parseInt(value);
            }
            //throw "parenthesis not yet implemented";
        } else {
            fieldCodes[i]= s.charAt(0);
            delims[i]= s.substring(1);
            deltas[i]= 1;
        }
        parseOffsets[i]= position;
        beginEndOffset[i]= 0;
        lengths[i]= fieldCodes[i]==="Y" ? 4 : 2 ;
        switch ( fieldCodes[i] ) {
            case "Y":
                formatOffsets[i]= 0;
                break;
            case "m":
                formatOffsets[i]= 5;
                if ( smallestField<=5 ) {
                    smallestField= 5;
                    smallestFieldDelta= deltas[i];
                }
                break;
            case "d":
                formatOffsets[i]= 8;
                if ( smallestField<=8 ) {
                    smallestField= 8;
                    smallestFieldDelta= deltas[i];
                }
                break;
            case "H":
                formatOffsets[i]= 11;
                if ( smallestField<=11 ) {
                    smallestField= 11;
                    smallestFieldDelta= deltas[i];
                }
                break;
            case "M":
                formatOffsets[i]= 14;
                if ( smallestField<=14 ) {
                    smallestField= 14;
                    smallestFieldDelta= deltas[i];
                }
                break;
            case "S":
                formatOffsets[i]= 17;
                if ( smallestField<=17 ) {
                    smallestField= 17;
                    smallestFieldDelta= deltas[i];
                }
                break;
            default:
                throw "unsupported field code: "+fieldCodes[i];
        }
        position += lengths[i];
        position += delims[i].length;
    }
    
    timeWidth= createTimeStruct(false);
    switch ( smallestField ) {
        case 0:
            timeWidth.year= smallestFieldDelta;
            break;
        case 5:
            timeWidth.month= smallestFieldDelta;
            break;
        case 8:
            timeWidth.day= smallestFieldDelta;
            break;
        case 11:
            timeWidth.hour= smallestFieldDelta;
            break;
        case 14:
            timeWidth.minute= smallestFieldDelta;
            break;
        case 17:
            timeWidth.second= smallestFieldDelta;
            break;
    }
    
    return {
        prefix:ss[0],
        numberOfFields:i,
        fieldCodes:fieldCodes,
        fieldModifiers:fieldModifiers,
        parseOffsets:parseOffsets,
        formatOffsets:formatOffsets,
        lengths:lengths,
        delims:delims,
        deltas:deltas,
        context:null, 
        timeWidth:timeWidth,
        beginEndOffsets:beginEndOffset
    };
}
/**
 * Create a Parser/Formatter object based on the specification provided.
 * @param {String} spec
 * @returns {String} the configured parser generator
 */
function create( spec ) {
    var x={};
    x.parse= function(  ) {
        return "2016-01-17T00:00:00/2016-01-18T00:00:00";
    };
    x.format= function( ) {
        return "/tmp/20160117.dat";
    };
    return x;
}

// From https://github.com/jakobwesthoff/sprintf.js/blob/master/src/sprintf.js
/**
 * Copyright (c) 2010 Jakob Westhoff
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
(function(root, factory) {
    // CommonJS module
    if (typeof module != 'undefined') {
        module.exports = factory();
    }
    // AMD
    else if (typeof define == 'function' && typeof define.amd == 'object') {
        define(factory);
    }
    // Global
    else {
        this.sprintf = factory();
    }
}(this, function() {
    var sprintf = function( format ) {
        // Check for format definition
        if ( typeof format != 'string' ) {
            throw "sprintf: The first arguments need to be a valid format string.";
        }

        /**
         * Define the regex to match a formating string
         * The regex consists of the following parts:
         * percent sign to indicate the start
         * (optional) sign specifier
         * (optional) padding specifier
         * (optional) alignment specifier
         * (optional) width specifier
         * (optional) precision specifier
         * type specifier:
         *  % - literal percent sign
         *  b - binary number
         *  c - ASCII character represented by the given value
         *  d - signed decimal number
         *  f - floating point value
         *  o - octal number
         *  s - string
         *  x - hexadecimal number (lowercase characters)
         *  X - hexadecimal number (uppercase characters)
         */
        var r = new RegExp( /%(\+)?([0 ]|'(.))?(-)?([0-9]+)?(\.([0-9]+))?([%bcdfosxX])/g );

        /**
         * Each format string is splitted into the following parts:
         * 0: Full format string
         * 1: sign specifier (+)
         * 2: padding specifier (0/<space>/'<any char>)
         * 3: if the padding character starts with a ' this will be the real
         *    padding character
         * 4: alignment specifier
         * 5: width specifier
         * 6: precision specifier including the dot
         * 7: precision specifier without the dot
         * 8: type specifier
         */
        var parts      = [];
        var paramIndex = 1;
        while ( part = r.exec( format ) ) {
            // Check if an input value has been provided, for the current
            // format string (no argument needed for %%)
            if ( ( paramIndex >= arguments.length ) && ( part[8] != '%' ) ) {
                throw "sprintf: At least one argument was missing.";
            }

            parts[parts.length] = {
                /* beginning of the part in the string */
                begin: part.index,
                /* end of the part in the string */
                end: part.index + part[0].length,
                /* force sign */
                sign: ( part[1] == '+' ),
                /* is the given data negative */
                negative: ( parseFloat( arguments[paramIndex] ) < 0 ) ? true : false,
                /* padding character (default: <space>) */
                padding: ( part[2] == undefined )
                    ? ( ' ' ) /* default */
                    : ( ( part[2].substring( 0, 1 ) == "'" )
                    ? ( part[3] ) /* use special char */
                    : ( part[2] ) /* use normal <space> or zero */
                ),
                /* should the output be aligned left?*/
                alignLeft: ( part[4] == '-' ),
                /* width specifier (number or false) */
                width: ( part[5] != undefined ) ? part[5] : false,
                /* precision specifier (number or false) */
                precision: ( part[7] != undefined ) ? part[7] : false,
                /* type specifier */
                type: part[8],
                /* the given data associated with this part converted to a string */
                data: ( part[8] != '%' ) ? String ( arguments[paramIndex++] ) : false
            };
        }

        var newString = "";
        var start = 0;
        // Generate our new formated string
        for( var i=0; i<parts.length; ++i ) {
            // Add first unformated string part
            newString += format.substring( start, parts[i].begin );

            // Mark the new string start
            start = parts[i].end;

            // Create the appropriate preformat substitution
            // This substitution is only the correct type conversion. All the
            // different options and flags haven't been applied to it at this
            // point
            var preSubstitution = "";
            switch ( parts[i].type ) {
                case '%':
                    preSubstitution = "%";
                    break;
                case 'b':
                    preSubstitution = Math.abs( parseInt( parts[i].data ) ).toString( 2 );
                    break;
                case 'c':
                    preSubstitution = String.fromCharCode( Math.abs( parseInt( parts[i].data ) ) );
                    break;
                case 'd':
                    preSubstitution = String( Math.abs( parseInt( parts[i].data ) ) );
                    break;
                case 'f':
                    preSubstitution = ( parts[i].precision === false )
                        ? ( String( ( Math.abs( parseFloat( parts[i].data ) ) ) ) )
                        : ( Math.abs( parseFloat( parts[i].data ) ).toFixed( parts[i].precision ) );
                    break;
                case 'o':
                    preSubstitution = Math.abs( parseInt( parts[i].data ) ).toString( 8 );
                    break;
                case 's':
                    preSubstitution = parts[i].data.substring( 0, parts[i].precision ? parts[i].precision : parts[i].data.length ); /* Cut if precision is defined */
                    break;
                case 'x':
                    preSubstitution = Math.abs( parseInt( parts[i].data ) ).toString( 16 ).toLowerCase();
                    break;
                case 'X':
                    preSubstitution = Math.abs( parseInt( parts[i].data ) ).toString( 16 ).toUpperCase();
                    break;
                default:
                    throw 'sprintf: Unknown type "' + parts[i].type + '" detected. This should never happen. Maybe the regex is wrong.';
            }

            // The % character is a special type and does not need further processing
            if ( parts[i].type ==  "%" ) {
                newString += preSubstitution;
                continue;
            }

            // Modify the preSubstitution by taking sign, padding and width
            // into account

            // Pad the string based on the given width
            if ( parts[i].width != false ) {
                // Padding needed?
                if ( parts[i].width > preSubstitution.length )
                {
                    var origLength = preSubstitution.length;
                    for( var j = 0; j < parts[i].width - origLength; ++j )
                    {
                        preSubstitution = ( parts[i].alignLeft == true )
                            ? ( preSubstitution + parts[i].padding )
                            : ( parts[i].padding + preSubstitution );
                    }
                }
            }

            // Add a sign symbol if neccessary or enforced, but only if we are
            // not handling a string
            if ( parts[i].type == 'b'
                || parts[i].type == 'd'
                || parts[i].type == 'o'
                || parts[i].type == 'f'
                || parts[i].type == 'x'
                || parts[i].type == 'X' ) {
                if ( parts[i].negative == true ) {
                    preSubstitution = "-" + preSubstitution;
                }
                else if ( parts[i].sign == true ) {
                    preSubstitution = "+" + preSubstitution;
                }
            }

            // Add the substitution to the new string
            newString += preSubstitution;
        }

        // Add the last part of the given format string, which may still be there
        newString += format.substring( start, format.length );

        return newString;
    };

    // Allow the sprintf function to be attached to any string or the string prototype
    sprintf.attach = function(target) {
        target.printf = function() {
            var newArguments = Array.prototype.slice.call( arguments );
            newArguments.unshift( String( this ) );
            return sprintf.apply( undefined, newArguments );
        };
    };

    // Export the sprintf function to the outside world
    return sprintf;
}));