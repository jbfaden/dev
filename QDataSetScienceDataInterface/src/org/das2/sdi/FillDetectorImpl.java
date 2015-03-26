
package org.das2.sdi;

import org.virbo.dataset.QDataSet;
import org.virbo.dataset.SemanticOps;
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
