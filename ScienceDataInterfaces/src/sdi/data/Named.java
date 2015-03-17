
package sdi.data;

/**
 * Object that has a name, which is a valid Java identifier.
 * @author faden@cottagesystems.com
 */
public interface Named {

    /**
     * return the name, which is a valid Java identifier identifying the
     * Named object.
     *
     * @return the name
     */
    String getName();
}
