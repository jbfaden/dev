
function create( spec ) {
    var x=0;
    x.parse= function(  ) {
        return "2016-01-17T00:00:00Z/2016-01-18T00:00:00Z";
    };
    x.format= function( ) {
        return "/tmp/20160117.dat";
    };
    return x;
}

