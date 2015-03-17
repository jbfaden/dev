package sdi.data;

/**
 * Data where the X bins are enforced to be contiguous and increasing.
 */
public interface SimpleContiguousBinnedData1D {

    /**
     * the number of bins.
     *
     * @return the number of bins.
     */
    int size();

    /**
     * Return the low boundary of the bin. Note the high for each bin is the low
     * of the next bin.
     *
     * @param i the index, 0 &le; i &lt; <code>size()</code>
     * @return the low boundary of the bin.
     */
    double getXBinLo(int i);

    /**
     * The reference value for each bin, which will be within the bounds of the
     * bin.
     *
     * @param i the index, 0 &le; i &lt; <code>size()</code>
     * @return the reference value for the bin.
     */
    double getXBinReference(int i);

    /**
     * return the high bin edge of the last bin.
     *
     * @return the high bin edge of the last bin.
     */
    double getLastXBinHi();

    /**
     * get the Y value for the data.
     *
     * @param i the index, 0 &le; i &lt; <code>size()</code>
     * @return the Y value for the data.
     */
    double getY(int i);
}
