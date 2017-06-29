
package org.das2.sdi;

import org.das2.qds.QDataSet;
import org.das2.qds.SemanticOps;

import sdi.data.FillDetector;

/**
 * Fill detector uses SemanticOps.weightsDataSet
 * @author faden@cottagesystems.com
 */
class FillDetectorImpl implements FillDetector {
    
    QDataSet wds;
    
    public FillDetectorImpl( QDataSet y ) {
        this.wds= SemanticOps.weightsDataSet(y);
    }
    
    @Override
    public boolean isFill(int i) {
        return wds.value(i)==0;
    }
}
