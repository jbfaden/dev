
package org.das2.sdi;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * Example implementation of DataList wraps an unmodifiableList.
 * @author faden@cottagesystems.com
 * @param <T> the element type, such as BinnedData2D
 */
public class DataListImpl<T> implements DataList {
    
    private final List<T> back;
    
    /**
     * create the DataList from a copy of the list provided.
     * @param back the list.
     */
    public DataListImpl( List<T> back ) {
        this.back= new ArrayList( back );
    }
    
    @Override
    public int size() {
        return back.size();
    }

    @Override
    public T get(int i) {
        return back.get(i);
    }

    @Override
    public Iterator<T> iterator() {
        return back.iterator();
    }

}
