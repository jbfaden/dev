package sdi.data;

/**
 * interval containing a reference location. The reference must be somewhere
 * within the interval. When a bin cannot be used, for example if data located
 * by the Bin is to be interpolated, then the reference value should be used.
 *
 * @see Validator#checkValid(sdi.data.Bin) 
 * @author faden@cottagesystems.com
 */
public interface Bin {

    /**
     * return the minimum value for the bin
     *
     * @return the minimum value for the bin
     */
    double getMin();

    /**
     * return the maximum value for the bin
     *
     * @return the maximum value for the bin
     */
    double getMax();

    /**
     * return a single reference value for the bin, which must be within the
     * bin: getMin()&le;getReference()&le;getMax()
     *
     * @return a single reference value for the bin
     */
    double getReference();
}
