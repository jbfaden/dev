package sdi.data;

import com.google.common.base.Optional;

/**
 * the bins are enforced to be contiguous;
 * it is also expected that they are increasing
 */
public interface SimpleContiguousBinnedData1D {

  /**
   * the number of bins.
   * @return 
   */
  int size();
  
  /**
   * Return the low boundary of the bin.  Note the high for each bin is the low 
   * of the next bin.
   * @param i the bin index.
   * @return the low boundary of the bin. 
   */
  double getXBinLo(int i);
  
  /**
   * The reference value for each bin, which will be within the bounds
   * of the bin.  
   * @param i the bin index.
   * @return the reference value for the bin.
   */
  double getXBinReference(int i);
  
  /**
   * return the high bin edge of the last bin.
   * @return the high bin edge of the last bin.
   */
  double getLastXBinHi();
  
  /**
   * get the Y value for the data.
   * @param i
   * @return the Y value for the data.
   */
  double getY(int i);
}
