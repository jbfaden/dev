package sdi.data;

/**
 * <p>
 * Simplest, least abstract data set has Y as a function of X, or
 * X(i) &rarr; Y(i) where 0 &le; i &lt; <code>size()</code>
 * </p>
 * 
 * <p>The figure below represents a SimpleXYData where size() is 5, getX(1) is 2.0, and getY(1) is 3.0:</p>
 * <br>
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
