
package sdi.data;

/**
 * Object that has a name, which is a valid Java identifier.
 * @author faden@cottagesystems.com
 */
public interface Named {

    /**
     * return the name, which is a valid Java identifier identifying the
     * Named object.  Valid names start with a letter or underscore, and contain
     * letters, underscores, and numbers.  
     *
     * @return the name
     */
    String getName();
}
