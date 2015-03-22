
package sdi.data;

/**
 * Metadata for X and Y values, including:<ul>
 * <li>Units for identifying SI components and time locations.
 * <li>Name providing a Java identifier.
 * <li>Label for plot labels.
 * </ul>
 * @author faden@cottagesystems.com
 */
public interface XYMetadata extends Named {

    /**
     * the X units.
     * @return the X units
     */
    Units getXUnits();

    /**
     * the Y units
     * @return the Y units
     */
    Units getYUnits();

    /**
     * Java-identifier that is useful for identifying the data.
     * @return the name 
     */
    String getXName();

    /**
     * Java-identifier that is useful for identifying the data.
     * @return the name 
     */
    String getYName();

    /**
     * Human-consumable label for the data.  This is a string 
     * representation of the X dimension. In general, the this method returns 
     * a string that "textually represents" this object. The result should 
     * be a concise but informative representation that is easy for a person 
     * to read.
     * @return the label
     */
    String getXLabel();

    /**
     * Human-consumable label for the data.  This is a string 
     * representation of the Y dimension. In general, the this method returns 
     * a string that "textually represents" this object. The result should 
     * be a concise but informative representation that is easy for a person 
     * to read.
     * @return the label
     */
    String getYLabel();
}
