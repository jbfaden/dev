
package org.das2.sdi;

import org.das2.qds.QDataSet;
import org.das2.qds.SemanticOps;

import sdi.data.FillDetector2D;

/**
 *
 * @author faden@cottagesystems.com
 */
class FillDetector2DImpl implements FillDetector2D {

    QDataSet wds;
    
    public FillDetector2DImpl( QDataSet y ) {
        this.wds= SemanticOps.weightsDataSet(y);
    }

    @Override
    public boolean isFill(int i, int j) {
        return wds.value(i,j)==0;
    }

}
