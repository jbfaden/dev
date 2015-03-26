
package org.das2.sdi;

import org.virbo.dataset.QDataSet;
import sdi.data.UncertaintyProvider2D;

/**
 *
 * @author faden@cottagesystems.com
 */
class UncertaintyProvider2DImpl implements UncertaintyProvider2D {

    QDataSet uncertPlus;
    QDataSet uncertMinus;
    
    public UncertaintyProvider2DImpl( QDataSet uncertPlus, QDataSet uncertMinus ) {
        this.uncertPlus= uncertPlus;
        this.uncertMinus= uncertMinus;
    }
    
    @Override
    public double getUncertPlus(int i, int j) {
        return uncertPlus.value(i,j);
    }

    @Override
    public double getUncertMinus(int i, int j) {
        return uncertMinus.value(i,j);
    }
    
}
