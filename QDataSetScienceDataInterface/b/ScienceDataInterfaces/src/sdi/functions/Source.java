package sdi.functions;
/**
 * General interface for a source of any other abstraction.
 * 
 * @author peachjm1
 *
 * @param <T> The type of abstraction this source can provide.
 */
public interface Source<T> {
	/**
	 * Get the object this source can provide.
	 * @return the object
	 */
	T get();
}
