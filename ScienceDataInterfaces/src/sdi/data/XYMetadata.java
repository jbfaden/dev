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
public interface XYMetadata extends Named {

    Units getXUnits();

    Units getYUnits();

    String getXName();

    String getYName();

    String getXLabel();

    String getYLabel();
}
