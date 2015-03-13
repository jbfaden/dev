package sdi.data;

/**
 * two-index table of numbers.
 * @author jbf
 */
public interface SimpleBinnedData2D {
  int sizeX();
  Bin getXBin(int i);

  int sizeY();
  Bin getYBin(int j);

  double getZ(int i, int j);
}




