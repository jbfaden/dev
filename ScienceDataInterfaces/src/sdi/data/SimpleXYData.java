/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdi.data;

/**
 * Simplest, least abstract 
 * @author jbf
 */
public interface SimpleXYData {
    int size();
    double getX(int i);
    double getY(int i);
}
