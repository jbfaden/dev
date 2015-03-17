package sdi.data;

/**
 * two-index fill detector
 *
 * @author jbf
 */
public interface FillDetector2D {

    /**
     * returns true if the data is fill and should not be used.
     *
     * @param i the first index
     * @param j the second index
     * @return true if the data is fill and should not be used.
     */
    boolean isFill(int i, int j);
}
