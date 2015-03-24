
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
        String name= (String) x.property(QDataSet.NAME);
        if ( name==null ) name="x";
        return name;        
    }

    @Override
    public String getYName() {
        String name= (String) y.property(QDataSet.NAME);
        if ( name==null ) name="y";
        return name;
    }

    @Override
    public String getXLabel() {
        String label= (String) x.property(QDataSet.LABEL);
        if ( label==null ) label= getXName();
        return label;
    }

    @Override
    public String getYLabel() {
        String label= (String) y.property(QDataSet.LABEL);
        if ( label==null ) label= getYName();
        return label;
    }

    @Override
    public String getName() {
        return getYName();
    }
    
}
