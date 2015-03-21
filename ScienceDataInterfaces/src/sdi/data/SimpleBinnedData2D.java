package sdi.data;

/**
 * two-index table of numbers, each tagged with an X and a Y bin.  
 * <br>
 * X(i),Y(j) &rarr; Z(i,j)
 *
 * @author faden@cottagesystems.com
 */
public interface SimpleBinnedData2D {

    /**
     * return the number of X bins
     * @return the number of X bins
     */
    int sizeX();

    /**
     * return the bin for the X-value
     * @param i the index, 0 &le; i &lt; sizeX()
     * @return the bin
     */
    Bin getXBin(int i);

    /**
     * the number of Y bins
     * @return the number of Y bins
     */
    int sizeY();

    /**
     * return the bin for the Y-value
     * @param j the index, 0 &le; j &lt; sizeY()
     * @return the bin
     */
    Bin getYBin(int j);

    /**
     * get the Z value at i,j
     * @param i the index, 0 &le; i &lt; sizeX()
     * @param j the index, 0 &le; j &lt; sizeY()
     * @return the Z value
     */
    double getZ(int i, int j);
}
