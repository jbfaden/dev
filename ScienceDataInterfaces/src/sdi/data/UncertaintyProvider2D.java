package sdi.data;

/**
 *
 * Provides the uncertainty of a 2-dimensional measurement, providing the
 * boundaries of the one-sigma error bar in the data units.  Note that the
 * indices of the uncertainty value correspond to the indices of the dataset.
 *
 * @author vandejd1
 */
public interface UncertaintyProvider2D {

    /**
     * returns the upper bound of the error bar in the data units.
     *
     * @param i the first dimension index from the data source
     * @param j the second dimension index from the data source
     * 
     * @return the upper bound of the error bar.
     */
    double getUncertPlus(int i, int j);

    /**
     * returns the lower bound of the error bar in the data units.
     *
     * @param i the first index, 0 &le; i &lt; size()
     * @param j the second index, 0 &le; i &lt; size()
     * 
     * @return the lower bound of the error bar.
     */
    double getUncertMinus(int i, int j);
}
