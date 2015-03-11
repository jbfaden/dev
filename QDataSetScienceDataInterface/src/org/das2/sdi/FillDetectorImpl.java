/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.das2.sdi;

import org.virbo.dataset.QDataSet;
import org.virbo.dataset.SemanticOps;
import sdi.data.FillDetector;

/**
 *
 * @author jbf
 */
public class FillDetectorImpl implements FillDetector {
    
    QDataSet wds;
    
    public FillDetectorImpl( QDataSet y ) {
        this.wds= SemanticOps.weightsDataSet(y);
    }
    
    @Override
    public boolean isFill(int i) {
        return wds.value(i)==0;
    }
}
