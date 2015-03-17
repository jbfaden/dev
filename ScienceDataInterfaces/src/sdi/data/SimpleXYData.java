package sdi.data;

/**
 * <p>
 * Simplest, least abstract data set has Y as a function of X. Represented here
 * as a figure:</p>
 * <img src="http://jfaden.net/~jbf/autoplot/renderings/SimpleXYData.png" alt="SimpleXYData.png">
 *
 * @author faden@cottagesystems.com
 */
public interface SimpleXYData {

    /**
     * get the size of the dataset.
     *
     * @return the size of the dataset.
     */
    int size();

    /**
     * get the X-value, the independent variable.
     *
     * @param i the index, 0 &le; i &lt; <code>size()</code>
     * @return the value at the index.
     */
    double getX(int i);

    /**
     * get the Y-value, the dependent variable.
     *
     * @param i the index, 0 &le; i &lt; <code>size()</code>
     * @return the value at the index.
     */
    double getY(int i);
}
