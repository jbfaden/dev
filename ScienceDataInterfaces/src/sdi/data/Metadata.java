package sdi.data;

public interface Metadata {
	/**
     * the units.
     * @return the units
     */
    Units getUnits();

    /**
     * Java-identifier that is useful for identifying the data.
     * @return the name 
     */
    String getName();

    /**
     * Human-consumable label for the data.  This is a string 
     * representation of this dimension. In general, the this method returns 
     * a string that "textually represents" this object. The result should 
     * be a concise but informative representation that is easy for a person 
     * to read.
     * @return the label
     */
    String getLabel();
}
