/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdi.data;

/**
 * 
 * @author jbf
 */
public interface MultiTable extends SimpleMultiTable {
    
    @Override
    BinnedData2D getTable(int i);
    
    XYZMetadata getMetadata();      
}
