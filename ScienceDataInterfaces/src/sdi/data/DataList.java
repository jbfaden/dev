
package sdi.data;

/**
 * When interfaces need a list of datasets, this interface should
 * be used to provide a read-only container for them.
 * 
 * @author faden@cottagesystems.com
 * @param <T> the element type, such as BinnedData2D
 */
public interface DataList<T> extends Iterable<T> {
    
    /**
     * return the number of elements
     * @return the number of elements
     */
    public int size();
    
    /**
     * return the element at the index.
     * @param i the index
     * @return the element.
     */
    public T get( int i );
    
}
