package sdi.data;

/**
 * This is the closest to the old Das2 TableDataSet model, where it's a 
 * set of 2-index tables as the mode changes.  
 * 
 * Semantic rules:<ul>
 * <li>Each table must have the same metadata.  
 * </ul>
 * @author jbf
 */
public interface MultiBinnedData2D extends SimpleMultiBinnedData2D {
    
    @Override
    BinnedData2D getData(int i);
    
    XYZMetadata getMetadata();      
}
