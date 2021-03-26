
package org.das2.sdi;

import org.das2.qds.QDataSet;
import org.das2.qds.SemanticOps;

import sdi.data.Units;
import sdi.data.XYZMetadata;

/**
 *
 * @author faden@cottagesystems.com
 */
class XYZMetadataImpl extends XYMetadataImpl implements XYZMetadata  {

    QDataSet z;
    
    public XYZMetadataImpl( QDataSet x, QDataSet y, QDataSet z ) {
        super( x, y );
        this.z= z;
    }
    
    @Override
    public Units getZUnits() {
        return new Units(SemanticOps.getUnits(z).getId());
    }

    @Override
    public String getZName() {
        String name= (String) z.property(QDataSet.NAME);
        if ( name==null ) name="z";
        return name;
    }

    @Override
    public String getZLabel() {
        String label= (String) z.property(QDataSet.LABEL);
        if ( label==null ) label= getZName();
        return label;
    }

    @Override
    public String getName() {
        return getZName();
    }
    
}
