/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdi.data;

/**
 * Interface detects if the value at an index is fill.
 * @author jbf
 * @see XYData#getFillDetector() 
 */

public interface FillDetector {
    /**
     * return true if the value at the index is fill and cannot be used.
     * @param index the index within the dataset.
     * @return true if the value at the index is fill and cannot be used.
     */
    boolean isFill(int index);
}