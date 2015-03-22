package sdi.data;

/**
 * Provides the uncertainty of a measurement, providing the boundaries of the
 * one-sigma error bar in the data units.  Note that the index will correspond 
 * to an index of the dataset, so that if getYUncertaintyProvider is used to get
 * the uncertainties, then the Y index (j) should be used.
 *
 * @author faden@cottagesystems.com
 */
public interface UncertaintyProvider {

    /**
     * returns the upper bound of the error bar in the data units.
     * Note that the index will correspond to an index of the dataset.
     *
     * @param i the index corresponding to the data set source.
     * @return the upper bound of the error bar.
     */
    double getUncertPlus(int i);

    /**
     * returns the lower bound of the error bar in the data units.
     * Note that the index will correspond to an index of the dataset.
     *
     * @param i the index, 0 &le; i &lt; size()
     * @return the lower bound of the error bar.
     */
    double getUncertMinus(int i);
}
