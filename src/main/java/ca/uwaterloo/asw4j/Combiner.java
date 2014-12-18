package ca.uwaterloo.asw4j;

import java.util.Collection;

/**
 * <p>
 * An interface defined to be used by {@link DataStore} for the purpose of
 * combining {@link Object}s stored in {@link DataStore}.
 * </p>
 * <p>
 * It should be registered with a specific {@link TypeToken}, which specifies
 * the {@link Type} of {@link Object}s this {@link Combiner} can combine, in
 * {@link DataStore}. All {@link Object}s stored in {@link DataStore} matching
 * the given {@link TypeToken} are passed in a {@link Collection} as parameter
 * upon invoking {@link #combine(Collection)}. And the result are stored back
 * into the {@link DataStore}.
 * </p>
 * 
 * @author Desmond Lin
 * @since 1.0.0
 * @param <T>
 *            The {@link Type} of {@link Object}s it can combine.
 */
public interface Combiner<T> {

	/**
	 * Combine the given {@link Collection} of {@link T}s.
	 * 
	 * @param collection
	 *            A {@link Collection} of {@link T}s to be combined.
	 * @return A {@link Collection} of combined {@link T}s.
	 */
	public T combine(Collection<T> collection);

}
