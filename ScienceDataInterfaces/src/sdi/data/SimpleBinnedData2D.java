package sdi.data;

import com.google.common.base.Optional;

public interface SimpleBinnedData2D {
  int sizeX();
  Bin getXBin(int i);

  int sizeY();
  Bin getYBin(int j);

  double getZ(int i, int j);
}




