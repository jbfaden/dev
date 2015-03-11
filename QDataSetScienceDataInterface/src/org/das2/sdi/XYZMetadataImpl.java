/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.das2.sdi;

import org.virbo.dataset.QDataSet;
import org.virbo.dataset.SemanticOps;
import sdi.data.Units;
import sdi.data.XYZMetadata;

/**
 *
 * @author jbf
 */
public class XYZMetadataImpl extends XYMetadataImpl implements XYZMetadata  {

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
        return (String) z.property(QDataSet.NAME);
    }

    @Override
    public String getZLabel() {
        return (String) z.property(QDataSet.LABEL);
    }
    
}
