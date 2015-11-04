
package org.tsds;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Command line version.
 * @author jbf
 */
public class URITemplates {
    
    private static final Logger logger= Logger.getLogger("org.tsds.serv");
    
    /**
     * Usage: java -jar org.tsds.URITemplates template timerange
     * @param args 
     */
    public static void main( String[] args ) {
        if ( args.length==0 ) {
            System.err.println("Usage: java -jar org.tsds.URITemplates <template> <timerange>");
            System.exit(1);
        }
        generateParse(args[0], args[1], System.out );
        System.out.close();
        System.exit(0);
        
    }
    
    private static boolean supportsGenerate( String s ) {
        return !(s.contains("$v") || s.contains("$(v") ||
                s.contains("$x") || s.contains("$(x") ||
                s.contains(";sparse") || s.contains(",sparse"));  // TODO: make canonical!!!
    } 
        
    /**
     * parse method.
     * @param context
     * @param root
     * @param template
     * @param out
     * @return
     * @throws IOException 
     */
    private static int doParse( TimeStruct[] context, String root, String template, PrintStream out) throws IOException {
        logger.entering( "org.tsds.URITemplatesServlet", "doParse {0} {1}", new Object[] { root, template } );
        
        int count= 0;

        if ( !root.endsWith("/") ) root= root+"/";
        
        String childTemplate= "";
        
        int iparent= template.indexOf("/");
        if ( iparent>-1 ) {
            childTemplate= template.substring(iparent+1);
            template= template.substring(0,iparent);
        }
        
        URL rootUrl= new URL(root);
        URL[] result;
        
        try (InputStream in = rootUrl.openStream()) {
            result= HtmlUtil.getDirectoryListing( rootUrl, in, true );
        }
        
        String[] names= new String[result.length];
        int len= root.length();
        for ( int i=0; i< result.length; i++ ) names[i]= result[i].toString().substring(len);

        TimeParserGenerator tpg= TimeParserGenerator.create( template );
        tpg.setContext( context[0] );
        
        for ( String n : names ) {
            
            Map<String,String> ffs= new HashMap();
            
            try {
                TimeStruct[] ts= tpg.parse(n,ffs).getTimeRange();
                
                String v;
                v= ffs.get( tpg.getFieldHandlerByCode("v").getId() );
            
                if ( childTemplate.length()>0 ) {
                    count= count+ doParse( ts, root + "/" + n, childTemplate, out );
                    
                } else {
                    if ( v==null ) v= "N/A";
                    
                    out.println( root + n );
                            
                    count= count+1;
                }
                
            } catch (ParseException ex) {
                logger.log(Level.FINEST, "not part of templated collection: {0}", n);
                
            }
            
        }

        logger.exiting( "org.tsds.URITemplatesServlet", "doParse {0} {1}", new Object[] { root, template } );
        
        return count;

    }
    
    /**
     * format the result to html, sent to the PrintWriter.
     * @param uri the template, such as http://emfisis.physics.uiowa.edu/Flight/RBSP-A/L3/$Y/$m/$d/rbsp-a_magnetometer_1sec-gse_emfisis-L3_$Y$m$d_v$(v,sep).cdf
     * @param timerange the range constraint, such as 2012-10-01/P2D
     * @param out the output stream, which should not be closed.
     */
    protected static void generateParse( String uri, String timerange, PrintStream out ) {
            
        if ( (timerange.split(" ") ).length==10 ) {  // make it easier to use Jon V.'s document "uri_template_test_cases.txt"
            String[] trs= timerange.split(" ");
            timerange= String.format( "%s-%s-%sT%s:%s/%s-%s-%sT%s:%s", (Object[]) trs );
        }

        long t0= System.currentTimeMillis();

        TimeStruct[] drtr;
        try {
            drtr= TimeUtil.parseISO8601Range(timerange);
        } catch ( ParseException ex ) {
            throw new RuntimeException( "unable to parse ISO8601 timerange",ex );
        }
        if ( drtr==null ) throw new IllegalArgumentException("unable to interpret ISO8601 timerange: "+timerange);

        String[] ss= uri.split("/");

        StringBuilder generateUriBuilder= new StringBuilder(ss[0]);

        int i=1;
        while ( i<ss.length && supportsGenerate(ss[i]) ) {
            generateUriBuilder.append("/").append(ss[i]);
            i++;
        }

        String generateUri= generateUriBuilder.toString();

        String parseUri;
        if ( generateUri.length()==uri.length() ) {
            parseUri= "";
        } else {
            parseUri= uri.substring(generateUri.length()+1);
        }

        TimeParserGenerator tp= TimeParserGenerator.create(generateUri);

        int i1= generateUri.indexOf("$(enum;");

        String[] enums;
        String id;

        if ( i1>-1 ) {
            int ix= generateUri.indexOf("$(enum",i1+6);
            if (ix>-1 ) {
                throw new IllegalArgumentException( "Template can only contain one $(enum)." );
            } else {
                TimeParserGenerator.EnumFieldHandler fh= (TimeParserGenerator.EnumFieldHandler) tp.getFieldHandlerByCode("enum");
                enums= fh.getValues();
                id= fh.getId();
            }
        } else {
            enums= new String[] { "" };
            id= "";
        }

        String st= tp.format( drtr[0], null, Collections.singletonMap( id, enums[0] ) );
        TimeStruct[] dr;
        try {
            dr = tp.parse( st,null ).getTimeRange();
        } catch (ParseException ex) {
            throw new RuntimeException("unable to parse timerange",ex);
        }

        int count= 0;

        TimeStruct stop= drtr[1];
        while ( count<=10000 && dr[0].lt( stop ) ) {
            for ( String enum1 : enums ) {
                st= tp.format( dr[0], dr[1], Collections.singletonMap( id, enum1 ) ); 

                if ( parseUri.length()==0 ) {
                    out.println(TimeUtil.formatISO8601Range(dr) );
                    count++;
                } else {
                    try {
                        count+= doParse(dr, st, parseUri, out );
                    } catch ( IOException ex ) {
                        logger.fine("exception thrown, presumably because the folder does not exist.");
                    }
                }

                if ( count>10000 ) {
                    logger.warning( "<tr><td></td><td>Search limited to 10000 results.</td></tr>" );
                    break;
                }
                dr= TimeStruct.next( dr );

            }
        }

        long dt= System.currentTimeMillis() - t0;

        logger.log( Level.FINE, "{0} results calculated in {1} milliseconds.", new Object[] { count,dt } );
    
    }

}
