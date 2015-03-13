/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdi.data;

/**
 * This is the closest to the old Das2 TableDataSet model, where it's a 
 * set of 2-index tables as the mode changes.
 * @author jbf
 */
public interface SimpleMultiTable {
  
    int tableCount();
    
    SimpleBinnedData2D getTable(int i);
  
}
