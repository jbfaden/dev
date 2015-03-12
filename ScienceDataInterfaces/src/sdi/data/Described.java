/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdi.data;

/**
 * A dataset that contains metadata is Described.
 * @author jbf
 * @param <T> the metadata type
 */
public interface Described<T> {
    T getMetadata();
}