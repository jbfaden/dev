
package sdi.data;

/**
 * A dataset that contains metadata is MetadataSrc.
 *
 * @author faden@cottagesystems.com
 * @param <T> the metadata type
 */
public interface MetadataSrc<T> {

    T getMetadata();
}
