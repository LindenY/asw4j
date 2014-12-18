package ca.uwaterloo.asw4j;

import java.util.Collection;
import java.util.List;
import java.lang.reflect.Type;

import ca.uwaterloo.asw4j.reflection.TypeToken;

/**
 * A store class serves to store, balance/combine, and resolve require data and
 * produce data for {@link Instruction} class.
 * 
 * @author Desmond Lin
 * @since 1.0.0
 */
public interface DataStore {

	/**
	 * Adding a {@link Object} into the {@link DataStore}.
	 * 
	 * @param obj
	 *            The {@link Object} to be added
	 */
	public void add(Object obj);

	/**
	 * Adding a {@link Object} into the {@link DataStore} along with a name
	 * {@link String}.
	 * 
	 * @param obj
	 *            The {@link Object} to be added
	 * @param name
	 *            The name of the {@link Object} used to distinguish the
	 *            difference between the same {@link Type} of {@link Object}s
	 */
	public void add(Object obj, String name);

	/**
	 * Adding all {@link Object}s in the {@link Collection} into the
	 * {@link DataStore}
	 * 
	 * @param objs
	 *            The {@link Object}s to be added.
	 */
	public void addAll(Collection<?> objs);

	/**
	 * <p>
	 * Adding all {@link Object}s in the {@link Collection} into the
	 * {@link DataStore} along with a name {@link String}.
	 * </p>
	 * <p>
	 * All the {@link Object}'s type will be attached with the name
	 * {@link String} to distinguish the {@link Type} difference the difference
	 * between the same {@link Type} of {@link Object}s.
	 * </p>
	 * 
	 * @param objs
	 *            The {@link Object}s to be added
	 * @param name
	 *            The name of the {@link Object} used to distinguish the same
	 *            {@link Type} of {@link Object}
	 */
	public void addAll(Collection<?> objs, String name);

	/**
	 * Check if the {@link DataStore} contains a {@link Object} matching the
	 * given {@link Type}.
	 * 
	 * @param type
	 *            The {@link Type} to be checked against.
	 * 
	 * @return return {@code true}, if there is a {@link Object} matching the
	 *         given {@link Type}; otherwise, return {@code false}.
	 */
	public boolean contain(Class<?> type);

	/**
	 * Check if the {@link DataStore} contains a {@link Object} matching the
	 * given {@link Type} object and the name {@link String}.
	 * 
	 * @param type
	 *            The {@link Type} to be checked against.
	 * @param name
	 *            The name {@link String} to be checked against.
	 * @return return {@code true}, if there is a {@link Object} matching the
	 *         given {@link Type} and the name {@link String}; otherwise, return
	 *         {@code false}.
	 */
	public boolean contain(Class<?> type, String name);

	/**
	 * Check if the {@link DataStore} contains a {@link Object} matching the
	 * given {@link TypeToken}.
	 * 
	 * @param typeToken
	 *            The {@link TypeToken} to be checked against.
	 * @return
	 */
	public boolean contain(TypeToken<?> typeToken);

	/**
	 * Check if the {@link DataStore} contains all the {@link TypeToken} in the
	 * {@link Collection}.
	 * 
	 * @param typeTokens
	 *            The {@link TypeToken}s to be checked against.
	 * @return return {@code true}, if {@link DataStore} contains all the
	 *         {@link TypeToken} in the {@link Collection}.
	 */
	public boolean containAll(List<TypeToken<?>> typeTokens);
	
	/**
	 * Get all {@link Object}s matching the given {@link Type}.
	 * 
	 * @param type
	 *            The {@link Type} to be checked against.
	 * @return All {@link Object}s matching the given {@link Type}.
	 */
	public <T> List<T> getAllValues(Class<T> type);

	/**
	 * Get all {@link Object}s matching the given {@link Type} and the name
	 * {@link String}.
	 * 
	 * @param type
	 *            The {@link Type} to be checked against.
	 * @param name
	 *            The name {@link String} to be checked against.
	 * @return All {@link Object}s matching the given {@link Type} and the name
	 *         {@link String}.
	 */
	public <T> List<T> getAllValues(Class<T> type, String name);

	/**
	 * Get all {@link Object}s matching the given {@link TypeToken}.
	 * 
	 * @param typeToken
	 *            The {@link TypeToken} to be checked against.
	 * @return All {@link Object}s matching the given {@link TypeToken}.
	 */
	public <T> List<T> getAllValues(TypeToken<T> typeToken);

	/**
	 * Get and remove a {@link Object} in the {@link DataStore} matching the
	 * given {@link Type}.
	 * 
	 * @param type
	 *            The {@link Type} to be checked against.
	 * @return A {@link Object} the with given {@link Type} will be returned; if
	 *         no such {@link Object} is found, return {@code null}
	 */
	public <T> T getAndRemove(Class<T> type);

	/**
	 * Get and remove a {@link Object} in the {@link DataStore} matching the
	 * given {@link Type} and the name {@link String}.
	 * 
	 * @param type
	 *            The {@link Type} to be checked against.
	 * @param name
	 *            The name {@link String} to be checked against.
	 * @return A {@link Object} with the given {@link Type} and the name
	 *         {@link String} will be returned; if no such {@link Object} is
	 *         found, return {@code null}
	 */
	public <T> T getAndRemove(Class<T> type, String name);

	/**
	 * Get and remove a {@link Object} in the {@link DataStore} matching the
	 * given {@link TypeToken}.
	 * 
	 * @param typeToken
	 *            The {@link TypeToken} to be checked against.
	 * @return A {@link Object} with the given {@link TypeToken} will be
	 *         returned; if no such {@link Object} is found, return {@code null}
	 */
	public <T> T getAndRemove(TypeToken<T> typeToken);

	/**
	 * <p>
	 * For each {@link TypeToken} in {@link Collection}, get and remove a
	 * {@link Object} matching the {@link TypeToken}.
	 * </p>
	 * <p>
	 * All the {@link Objects}s are then put into a {@link DataNode}. This is a
	 * workaround for a Java method to return more than one variable.
	 * </p>
	 * 
	 * @param typeTokens
	 *            The {@link TypeToken}s to be checked against.
	 * @return A {@link DataNode} contains all the {@link Object}s which are
	 *         gotten and removed from {@link DataStore}
	 */
	public DataNode getAndRemoveAll(Collection<TypeToken<?>> typeTokens);

	/**
	 * Use the registered {@link Combiner} for the given {@link Type} to
	 * {@link Combiner#combine(Collection)} all the {@link Object}s in the
	 * {@link DataStore} matching the given {@link Type}
	 * 
	 * @param type
	 *            The {@link Type} to be checked against
	 * @return A combined {@link Object} will be returned, if there is a
	 *         {@link Combiner} registered for the given {@link Type}.
	 *         Otherwise, the first found {@link Object} will be returned, if
	 *         there is no {@link Combiner} registered for the given
	 *         {@link Type}.
	 */
	public <T> T combineAndGet(Class<?> type);

	/**
	 * Use the registered {@link Combiner} for the given {@link Type} and the
	 * name {@link String} to {@link Combiner#combine(Collection)} all the
	 * {@link Object}s in the {@link DataStore} matching the given {@link Type}
	 * and the name {@link String}
	 * 
	 * @param type
	 *            The {@link Type} to be checked against.
	 * @param name
	 *            The name {@link String} to be checked against.
	 * @return A combined {@link Object} will be returned, if there is a
	 *         {@link Combiner} registered for the given {@link Type} and the
	 *         name {@link String}. Otherwise, the first found {@link Object}
	 *         will be returned, if there is no {@link Combiner} registered for
	 *         the given {@link Type} and the name {@link String}.
	 */
	public <T> T combineAndGet(Class<?> type, String name);

	/**
	 * Use the registered {@link Combiner} for the given {@link Type} to
	 * {@link Combiner#combine(Collection)} all the {@link Object}s in the
	 * {@link DataStore} matching the given {@link Type}
	 * 
	 * @param type
	 *            The {@link Type} to be checked against
	 * @return A combined {@link Object} will be returned, if there is a
	 *         {@link Combiner} registered for the given {@link Type}.
	 *         Otherwise, the first found {@link Object} will be returned, if
	 *         there is no {@link Combiner} registered for the given
	 *         {@link Type}.
	 */
	public <T> T combineAndGet(TypeToken<T> typeToken);

	/**
	 * Check if the specified {@link Object} existed in the {@link DataStore}.
	 * 
	 * @param obj
	 *            The {@link Object} to be checked against.
	 * @return Return {@code true}, if such {@link Object} is found; Otherwise,
	 *         return {@code false}.
	 */
	public boolean containObject(Object obj);

	/**
	 * Number of {@link Object}s in the {@link DataStore}.
	 * 
	 * @return The number of {@link Object}s in the {@link DataStore}.
	 */
	public int size();

	/**
	 * Register a {@link Balancer} for the given {@link TypeToken}
	 * 
	 * @param typeToken
	 *            The {@link TypeToken} to be registered as.
	 * @param balancer
	 *            The {@link Balancer} to be registered.
	 */
	public void registerBalancer(TypeToken<?> typeToken, Balancer<?> balancer);

	/**
	 * Register a {@link Combiner} for the given {@link TypeToken}
	 * 
	 * @param typeToken
	 *            The {@link TypeToken} to be registered as.
	 * @param balancer
	 *            The {@link Combiner} to be registered.
	 */
	public void registerCombiner(TypeToken<?> typeToken, Combiner<?> combiner);
}
