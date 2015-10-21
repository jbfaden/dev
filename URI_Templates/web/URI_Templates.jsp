<%-- 
    Document   : URI_Templates
    Created on : Sep 28, 2015, 10:35:44 AM
    Author     : jbf
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>URI Templates Demo</title>
    </head>
    <body>    
        
        <h1>Hybrid Generating/Parsing URIs</h1>
        <p>This will use the generation code for all parts of the URI which can be handled
            with generation, and then will switch to parsing for parts that need
            parsing.  For example, the $Y/$m/$d/ components can be resolved, and then 
            listings are used to resolve $H$M$(S,sparse).png.
        </p>
        
        <script lang="javascript">
            function example( uri, tr ) {
                document.getElementById('resourceURI3').value= uri;
                document.getElementById('timerange3').value= tr;
            }
        </script>
        
    <form action="URITemplatesServlet" method="GET">
        Enter URI (<a href="http://tsds.org//uri_templates#Time_Range_Rules">help</a>):
        Examples:
        <a href="#" onclick="example('http://emfisis.physics.uiowa.edu/Flight/RBSP-A/L3/$Y/$m/$d/rbsp-a_magnetometer_1sec-gse_emfisis-L3_$Y$m$d_v$(v,sep).cdf','2012-10-01/04');">A</a>
        <a href="#" onclick="example('http://cdaweb.gsfc.nasa.gov/sp_phys/data/omni/hourly/$Y/omni2_h0_mrg1hr_$Y$(m;delta=6)01_v$v.cdf','2000/P3Y');">B</a>
        <a href="#" onclick="example('http://autoplot.org/data/agg/hyd/$Y/po_h0_hyd_$Y$m$d_v$v.cdf','1999/2001');">C</a>
        <a href="#" onclick="example('http://sarahandjeremy.net/~jbf/powerMeter/$Y/$m/$d/$H$M$(S,sparse).jpg','2015-05-01/P1D');">D</a>
        <br>
        <textarea rows="1" cols="120" id="resourceURI3" name="resourceURI" >http://autoplot.org/data/versioning/data_$Y_$m_$d_v$v.qds</textarea><br>
        <br>Enter ISO8601 Time Range limiting the results printed: <br>
        <textarea rows="1" cols="50" id="timerange3" name="timerange" >2010-03-01/2010-03-10</textarea><br>
        <input type="checkbox" hidden="true" name="generate"/>
        <input type="hidden" name="mode" value="hybrid" />
        <br>
        <input type="submit" value="Try it out" />
    </form>        
        <br><small>version 20151020T0748</small>
    </body>
</html>
