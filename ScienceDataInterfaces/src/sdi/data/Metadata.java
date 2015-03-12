/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdi.data;

/**
 *
 * @author jbf
 * @param <T> the type of metadata, such as XYMetadata
 */
public interface Metadata<T> {
    T getMetadata();
}