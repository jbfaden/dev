package sdi.data;

/**
 * Provides the uncertainty of a measurement, providing the boundaries of the
 * one-sigma error bar in the data units.
 *
 * @author faden@cottagesystems.com
 */
public interface UncertaintyProvider {

    /**
     * returns the upper bound of the error bar in the data units.
     *
     * @param i the index, 0 &le; i &lt; size()
     * @return the upper bound of the error bar.
     */
    double getUncertPlus(int i);

    /**
     * returns the lower bound of the error bar in the data units.
     *
     * @param i the index, 0 &le; i &lt; size()
     * @return the lower bound of the error bar.
     */
    double getUncertMinus(int i);
}
