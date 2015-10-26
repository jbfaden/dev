
package org.tsds;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilities for working with websites, such as getting directory listings.
 * @author jbf
 */
public class HtmlUtil {
    
    private static final Logger logger= Logger.getLogger("org.tsds.htmlutil");
    
    /**
     * Get the listing of the web directory, returning links that are "under" the given URL.
     * Note this does not handle off-line modes where we need to log into
     * a website first, as is often the case for a hotel.
     *
     * This was refactored to support caching of listings by simply writing the content to disk.
     *
     * Taken from dasCoreUtil package at 
     * https://saturn.physics.uiowa.edu/svn/das2/dasCore/community/autoplot2011/trunk/dasCoreUtil/src/org/das2/util/filesystem/HtmlUtil.java
     * 
     * @param url the address.
     * @param urlStream stream containing the URL content.
     * @param childCheck only return links to URLs "under" the url.
     * @return list of URIs referred to in the page.
     * @throws IOException
     */
    public static URL[] getDirectoryListing( URL url, InputStream urlStream, boolean childCheck ) throws IOException {
        // search the input stream for links
        // first, read in the entire URL

        long t0= System.currentTimeMillis();
        byte b[] = new byte[10000];
        int numRead = urlStream.read(b);
        StringBuilder contentBuffer = new StringBuilder( 10000 );

        if ( numRead!=-1 ) contentBuffer.append( new String( b, 0, numRead ) );
        while (numRead != -1) {
            logger.finest("download listing");
            numRead = urlStream.read(b);
            if (numRead != -1) {
                String newContent = new String(b, 0, numRead);
                contentBuffer.append( newContent );
            }
        }
        urlStream.close();

        logger.log(Level.FINER, "read listing data in {0} millis", (System.currentTimeMillis() - t0));
        String content= contentBuffer.toString();

        String hrefRegex= "(?i)href\\s*=\\s*([\"'])(.+?)\\1";
        Pattern hrefPattern= Pattern.compile( hrefRegex );

        Matcher matcher= hrefPattern.matcher( content );

        ArrayList urlList= new ArrayList();

        String surl= url.toString();

        while ( matcher.find() ) {
            logger.finest("parse listing");
            String strLink= matcher.group(2);
            URL urlLink;

            try {
                urlLink = new URL(url, strLink);
                strLink = urlLink.toString();
            } catch (MalformedURLException e) {
                logger.log(Level.SEVERE, "bad URL: {0} {1}", new Object[]{url, strLink});
                continue;
            }

            if ( childCheck ) {
                if ( strLink.startsWith(surl) && strLink.length() > surl.length() && null==urlLink.getQuery() ) {
                    String file= strLink.substring( surl.length() );
                    if ( !file.startsWith("../") ) {
                        urlList.add( urlLink );
                    }
                }
            } else {
                urlList.add( urlLink );
            }
        }

        return (URL[]) urlList.toArray( new URL[urlList.size()] );

    }
}
