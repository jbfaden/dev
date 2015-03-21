
package sdi.data;

/**
 * A dataset that contains metadata is a metadata source, or MetadataSrc.
 *
 * @author faden@cottagesystems.com
 * @param <T> the metadata type
 * @see XYMetadata
 * @see XYZMetadata
 */
public interface MetadataSrc<T> {
    /**
     * return the metadata for the object.
     * @return the metadata 
     */
    T getMetadata();
}
