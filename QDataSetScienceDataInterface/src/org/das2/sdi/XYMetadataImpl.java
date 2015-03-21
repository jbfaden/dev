
package org.das2.sdi;

import org.virbo.dataset.QDataSet;
import org.virbo.dataset.SemanticOps;
import sdi.data.Units;
import sdi.data.XYMetadata;

/**
 *
 * @author faden@cottagesystems.com
 */
public class XYMetadataImpl implements XYMetadata {

    QDataSet x;
    QDataSet y;
    
    public XYMetadataImpl( QDataSet x, QDataSet y ) {
        this.x= x;
        this.y= y;
    }
    
    @Override
    public Units getXUnits() {
        return new Units(SemanticOps.getUnits(x).getId());
    }

    @Override
    public Units getYUnits() {
        return new Units(SemanticOps.getUnits(y).getId());
    }

    @Override
    public String getXName() {
        return (String) x.property(QDataSet.NAME);
    }

    @Override
    public String getYName() {
        return (String) y.property(QDataSet.NAME);
    }

    @Override
    public String getXLabel() {
        return (String) x.property(QDataSet.LABEL);
    }

    @Override
    public String getYLabel() {
        return (String) y.property(QDataSet.LABEL);
    }

    @Override
    public String getName() {
        return (String) y.property(QDataSet.NAME); //TODO: verify this, QDataSet doesn't have a separate name.
    }
    
}
