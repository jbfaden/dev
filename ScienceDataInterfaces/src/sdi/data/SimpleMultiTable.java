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
