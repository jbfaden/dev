/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdi.data;

/**
 *
 * @author jbf
 * @param <T>
 */
public interface Data<T> {
    T getData();
    MetadataSrc getMetadata();
}
