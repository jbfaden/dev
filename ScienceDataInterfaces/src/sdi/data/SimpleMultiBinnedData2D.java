package sdi.data;

/**
 * This is closest to the old Das2 TableDataSet model, where it's a 
 * set of 2-index tables as the mode changes.
 * @author jbf
 */
public interface SimpleMultiBinnedData2D {
  
    int dataCount();
    
    SimpleBinnedData2D getData(int i);
  
}
