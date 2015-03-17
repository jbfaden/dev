
package sdi.data;

/**
 * XY data where the X dimension has bins associated with it and Y
 * is a dependent variable.
 *
 * @author vandejd1
 *
 */
public interface SimpleBinnedData1D {

    /**
     * get the size of the dataset.
     * @return the size of the dataset.
     */
    int size();

    /**
     * get the bin for the X-value.
     * @param i the index, 0 &le; i &lt; <code>size()</code>
     * @return the bin for the X-value.
     */
    Bin getXBin(int i);

    /**
     * get the Y-value, the dependent variable.
     *
     * @param i the index, 0 &le; i &lt; <code>size()</code>
     * @return the value at the index.
     */
    double getY(int i);
}

//interface Data<R, M extends Named> {
//R getData();
//M getMD();
//}
