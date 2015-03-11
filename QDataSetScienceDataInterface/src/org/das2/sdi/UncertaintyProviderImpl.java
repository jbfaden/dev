/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.das2.sdi;

import org.virbo.dataset.QDataSet;
import sdi.data.UncertaintyProvider;

/**
 *
 * @author jbf
 */
public class UncertaintyProviderImpl implements UncertaintyProvider {

    QDataSet uncertPlus;
    QDataSet uncertMinus;
    
    public UncertaintyProviderImpl( QDataSet uncertPlus, QDataSet uncertMinus ) {
        this.uncertPlus= uncertPlus;
        this.uncertMinus= uncertMinus;
    }
    
    @Override
    public double getUncertPlus(int i) {
        return uncertPlus.value(i);
    }

    @Override
    public double getUncertMinus(int i) {
        return uncertMinus.value(i);
    }
    
}
