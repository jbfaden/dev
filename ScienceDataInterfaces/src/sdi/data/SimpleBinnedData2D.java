package sdi.data;

/**
 * two-index table of numbers, each tagged with an X and a Y bin.
 *
 * @author faden@cottagesystems.com
 */
public interface SimpleBinnedData2D {

    int sizeX();

    Bin getXBin(int i);

    int sizeY();

    Bin getYBin(int j);

    double getZ(int i, int j);
}
