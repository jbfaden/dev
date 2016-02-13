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
 * 
 * @param {String} spec
 * @param {String} file
 * @returns {String} timerange
 */
function parse( spec, file ) {
    spec= setup(spec);
    n= spec.numberOfFields;
    buf= new Int32Array( new ArrayBuffer(53*4) );
    
    for ( i=0; i<n; i++ ) {
        if ( spec.parseOffsets[i]!==-1 ) {
            buf[spec.formatOffsets[i]]= file.substring( spec.parseOffsets[i], spec.parseOffsets[i] + spec.lengths[i] );
        } else {
            throw "variable length and odd fields not yet implemented";
        }
    }
    
    return sprintf( "%04d-%02d-%02dT%02d:%02d:%02d.%06d/%04d-%02d-%02dT%02d:%02d:%02d.%06d", buf[0], buf[4], buf[7], buf[10], buf[11], buf[14], buf[17], buf[0], buf[4], buf[7], buf[10], buf[11], buf[14], buf[17] );
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
    var fc= {};              // field codes
    var delims={};          // constant characters between fields.
    var beginEndOffset={};  // 26 for end fields, 0 for start fields
    var lengths={};         // number of characters in field, -1 for unknown.
    var parseOffsets={};     // offset into filename we are parsing.
    var formatOffsets={};    // offset into 26-character canonical format.
    ss= spec.split( '$' );
    position= ss[0].length;
    for ( i=0; i<ss.length-1; i++ ) { 
        s= ss[i+1];
        if ( s.charAt(0)==='(' ) {
            throw "parenthesis not yet implemented";
        }
        fc[i]= s.charAt(0);
        delims[i]= s.substring(1);
        parseOffsets[i]= position;
        beginEndOffset[i]= 0;
        lengths[i]= fc[i]==="Y" ? 4 : 2 ;
        switch ( fc[i] ) {
            case "Y":
                formatOffsets[i]= 0;
                break;
            case "m":
                formatOffsets[i]= 5;
                break;
            case "d":
                formatOffsets[i]= 8;
                break;
            case "H":
                formatOffsets[i]= 11;
                break;
            case "M":
                formatOffsets[i]= 14;
                break;
            case "S":
                formatOffsets[i]= 17;
                break;
            default:
                throw "unsupported field code: "+fc[i];
        }
        position += lengths[i];
        position += delims[i];
    }
    return {
        prefix:ss[0],
        numberOfFields:i,
        fieldcodes:fc,
        parseOffsets:parseOffsets,
        formatOffsets:formatOffsets,
        lengths:lengths,
        delims:delims,
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