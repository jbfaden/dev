package sdi.data;

/**
 * Provides the uncertainty of a measurement, providing the boundaries of the
 * one-sigma error bar in the data units.
 *
 * @author jbf
 */
public interface UncertaintyProvider {

    /**
     * returns the upper bound of the error bar in the data units.
     *
     * @param i
     * @return the upper bound of the error bar.
     */
    double getUncertPlus(int i);

    /**
     * returns the lower bound of the error bar in the data units.
     *
     * @param i
     * @return the lower bound of the error bar.
     */
    double getUncertMinus(int i);
}
