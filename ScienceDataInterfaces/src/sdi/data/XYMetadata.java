
package sdi.data;

/**
 * Metadata for X and Y values.
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
     * Human-consumable label for the data.
     * @return the label
     */
    String getXLabel();

    /**
     * Human-consumable label for the data.
     * @return the label
     */
    String getYLabel();
}
