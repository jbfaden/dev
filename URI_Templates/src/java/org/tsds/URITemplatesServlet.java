/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tsds;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jbf
 */
public class URITemplatesServlet extends HttpServlet {

    private static final Logger logger= Logger.getLogger("org.tsds.serv");
    
    private boolean supportsGenerate( String s ) {
        return !(s.contains("$v") || s.contains("$(v") ||
                s.contains("$x") || s.contains("$(x") ||
                s.contains(";sparse") || s.contains(",sparse"));  // TODO: make canonical!!!
    } 

    /**
     * The root is listed and files matching the template are returned.
     * @param root the root URI, for example http://cdaweb.gsfc.nasa.gov/sp_phys/data/omni/hourly/2000/
     * @param template the template for files, for example omni2_h0_mrg1hr_$Y$(m;delta=6)01_v$v.cdf
     * @param out the stream to which HTML output is added.
     * @return the number of items added to the list.
     * @throws IOException 
     */
    private int doParse( TimeStruct[] context, String root, String template, PrintWriter out) throws IOException {
        
        logger.entering( "org.tsds.URITemplatesServlet", "doParse {0} {1}", new Object[] { root, template } );
        
        int count= 0;

        if ( !root.endsWith("/") ) root= root+"/";
        
        URL rootUrl= new URL(root);
        InputStream in = rootUrl.openStream();
                
        URL[] result;
        try {
            result= HtmlUtil.getDirectoryListing( rootUrl, in, true );
        } finally {
            in.close();
        }
        
        String[] names= new String[result.length];
        int len= root.length();
        for ( int i=0; i< result.length; i++ ) names[i]= result[i].toString().substring(len);

        TimeParserGenerator tpg= TimeParserGenerator.create( template );
        tpg.setContext( context[0] );
        
        for ( String n : names ) {
            out.printf(  "<tr>" );
            
            Map<String,String> ffs= new HashMap();
            
            try {
                TimeStruct[] ts= tpg.parse(n,ffs).getTimeRange();
                String v;
                v= ffs.get( tpg.getFieldHandlerByCode("v").getId() );
            
                if ( v==null ) v= "N/A";
            
                out.printf("<td>"+root + "/" + n + "</td><td>"+ TimeUtil.formatISO8601Range(ts) + "</td><td>" + v +"</td>\n" );
                out.printf(  "</tr>" );
                count= count+1;
                
            } catch (ParseException ex) {
                logger.log(Level.FINEST, "not part of templated collection: {0}", n);
                
            }
            
        }

        logger.exiting( "org.tsds.URITemplatesServlet", "doParse {0} {1}", new Object[] { root, template } );
        
        return count;
        
    }
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            String uri= request.getParameter("resourceURI");
            String timerange= request.getParameter("timerange");
            
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

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>URI Templates</title>");            
            out.println("</head>");
            out.println("<body>");
                        
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
            
            if ( parseUri.contains("/") ) {
                throw new IllegalArgumentException("parse portion of URI cannot contain /");
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

            out.printf( "<h1>Hybrid generate/parse result</h1>");
            out.printf( "<h3>%s</h3>\n", uri );
            out.printf( "<p>search limited to %s</p>\n", timerange );

            if ( parseUri.length()==0 ) {
                out.printf( "<p>generation used for entire URI</p>\n" );
            } else {
                out.printf( "<p>generation used for " + generateUri + ", parsing for " +parseUri + " </p>\n" );
            }
            out.printf( "<table border=1>\n" );
            out.printf( "<tr><td>Filename</td><td>Time Range</td><td>Version</td></tr>" );

            int count= 0;
            
            TimeStruct stop= drtr[1];
            while ( count<=10000 && dr[0].lt( stop ) ) {
                for ( String enum1 : enums ) {
                    st= tp.format( dr[0], dr[1], Collections.singletonMap( id, enum1 ) ); 
                    
                    if ( parseUri.length()==0 ) {
                        out.printf(  "<tr><td>"+st + "</td><td>"+ TimeUtil.formatISO8601Range(dr) + "</td><td>N/A</td><tr>\n" );
                        count++;
                    } else {
                        try {
                            count+= doParse(dr, st, parseUri, out );
                        } catch ( IOException ex ) {
                            logger.fine("exception thrown, presumably because the folder does not exist.");
                        }
                    }

                    if ( count>10000 ) {
                        out.printf( "<tr><td></td><td>Search limited to 10000 results.</td></tr>" );
                        break;
                    }
                    dr= TimeStruct.next( dr );
                    
                }
            }
            
            out.println( "</table>");
            
            long dt= System.currentTimeMillis() - t0;
            
            out.printf("%d results calculated in %d milliseconds.\n",count,dt);
            
            out.println("</body>");
            out.println("</html>");
        }
    }


    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
