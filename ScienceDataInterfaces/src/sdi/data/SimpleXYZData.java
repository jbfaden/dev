package sdi.data;

/**
 * Simplest, least abstract data set has Z as a function of X and Y.
 * <br>
 * X(i),Y(i) &rarr; Z(i) 
 * @author faden@cottagesystems.com
 */
public interface SimpleXYZData {

    /**
     * get the size of the dataset.
     * @return the size of the dataset.
     */
    int size();

    /**
     * the X value
     * @param i the index, 0 &le; i &lt; size()
     * @return the X value
     */
    double getX(int i);

    /**
     * the Y value 
     * @param i the index, 0 &le; i &lt; size()
     * @return the Y value 
     */
    double getY(int i);

    /**
     * the Z value
     * @param i the index, 0 &le; i &lt; size()
     * @return the Z value
     */
    double getZ(int i);
}
