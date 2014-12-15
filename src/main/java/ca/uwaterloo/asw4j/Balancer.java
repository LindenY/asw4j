package ca.uwaterloo.asw4j;

import java.util.Collection;

/**
 * <p>
 * An interface defined to be used by {@link DataStore} for the purpose of
 * balancing workload by redistributing the {@link Object}s stored in
 * {@link DataStore}.
 * </p>
 * <p>
 * It should be registered with a specific {@link TypeToken}, which specifies
 * the {@link Type} of {@link Object}s this {@link Balancer} can balance, in
 * {@link DataStore}. All {@link Object}s stored in {@link DataStore} matching
 * the given {@link TypeToken} are passed in {@link Collection} as a parameter
 * upon invoking {@link #balance(Collection)}. And the result are stored back
 * into the {@link DataStore}.
 * </p>
 * 
 * @author Desmond Lin
 * @since 1.0.0
 * @param <T>
 *            The {@link Type} of {@link Object}s it can balance.
 */
public interface Balancer<T> {

	/**
	 * Balance the given {@link Collection} of {@link T}s.
	 * 
	 * @param collection
	 *            A {@link Collection} of {@link T}s to be balanced.
	 * @return A {@link Collection} of balanced {@link T}s.
	 */
	public Collection<T> balance(Collection<T> collection);

}
