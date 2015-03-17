package sdi.data;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

/**
 * XY data where the X dimension has bins associated with it
 *
 * @author vandejd1
 *
 */
public interface SimpleBinnedData1D {

    int size();

    Bin getXBin(int i);

    double getY(int i);
}

//interface Data<R, M extends Named> {
//R getData();
//M getMD();
//}
