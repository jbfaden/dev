/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdi.data;

/**
 * Metadata for X, Y, and Z values.
 * @author faden@cottagesystems.com
 */
public interface XYZMetadata extends XYMetadata {

    /**
     * the Z units
     * @return the Z units
     */
    Units getZUnits();

    /**
     * Java-identifier that is useful for identifying the data.
     * @return the name 
     */
    String getZName();

    /**
     * Human-consumable label for the data.
     * @return the label
     */
    String getZLabel();
}
