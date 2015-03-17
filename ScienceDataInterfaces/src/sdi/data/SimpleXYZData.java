package sdi.data;

public interface SimpleXYZData {

    /**
     * get the size of the dataset.
     * @return the size of the dataset.
     */
    int size();

    /**
     * the X value
     * @param i the index, 0 &le; i &lt size()
     * @return 
     */
    double getX(int i);

    /**
     * the Y value 
     * @param i the index, 0 &le; i &lt size()
     * @return 
     */
    double getY(int i);

    /**
     * the Z value
     * @param i the index, 0 &le; i &lt size()
     * @return 
     */
    double getZ(int i);
}
