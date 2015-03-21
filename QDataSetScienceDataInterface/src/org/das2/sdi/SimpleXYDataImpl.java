
package org.das2.sdi;

import org.virbo.dataset.QDataSet;
import org.virbo.dataset.SemanticOps;
import sdi.data.SimpleXYData;

/**
 *
 * @author faden@cottagesystems.com
 */
public class SimpleXYDataImpl implements SimpleXYData {

    QDataSet y;
    QDataSet x;
    
    public SimpleXYDataImpl( QDataSet source ) {
        this.y= source;
        this.x= SemanticOps.xtagsDataSet(source);
    }
    
    @Override
    public int size() {
        return x.length();
    }

    @Override
    public double getX(int i) {
        return x.value(i);
    }

    @Override
    public double getY(int i) {
        return y.value(i);
    }

}
