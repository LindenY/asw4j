package ca.uwaterloo.asw4j;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.uwaterloo.asw4j.reflection.TypeToken;

/**
 * <p>
 * A class used to store different {@link Type} of {@link Object}s.
 * </p>
 * <p>
 * {@link DataNode} is used in many occasions as the workaround for Java
 * limitations. For {@link DataStore}, {@link DataNode} is used for methods to
 * have more than one return variables. Also, for {@link Instruction},
 * {@link DataStore} is used to resolve the require data.
 * </p>
 * <p>
 * In order to reduce the multi-thread overhead and improve the performance,
 * {@link DataNode} is implemented in a way that is not thread safe. With that
 * being said, {@link DataNode} is ought to be used within a single thread to
 * prevent unexpected error from happening.
 * </p>
 * 
 * @author Desmond Lin
 * @since 1.0.0
 */
public class DataNode {

	private Map<TypeToken<?>, List<Object>> dataMap;
	private int numObjs = 0;

	/**
	 * Constructor
	 */
	public DataNode() {
		dataMap = new HashMap<TypeToken<?>, List<Object>>();
	}

	/**
	 * Put an {@link Object} into {@link DataNode}.
	 * 
	 * @param obj
	 *            The {@link Object} to be put.
	 */
	public void put(Object obj) {
		put(obj, null);
	}

	/**
	 * Put an {@link Object} into {@link DataNode} along with a name
	 * {@link String}.
	 * 
	 * @param obj
	 *            The {@link Object} to be put.
	 * @param name
	 *            The name of the {@link Object} used to distinguish the
	 *            difference between the same {@link Type} of {@link Object}s
	 */
	public void put(Object obj, String name) {
		if (obj == null) {
			return;
		}

		TypeToken<?> typeToken = TypeToken.get(obj.getClass(), name);
		List<Object> objs = dataMap.get(typeToken);
		if (objs == null) {
			objs = new ArrayList<Object>();
			dataMap.put(typeToken, objs);
		}
		objs.add(obj);
		numObjs++;
	}

	/**
	 * Get the {@link Object} matching the given {@link Type} in
	 * {@link DataStore}
	 * 
	 * @param type
	 *            The {@link Type} to be checked against.
	 * @return Return the {@link Object} matching the given {@link Type}.
	 */
	public <T> T get(Class<T> type) {
		return get(type, null);
	}

	/**
	 * Get the {@link Object} matching the given {@link Type} and the name
	 * {@link String} in {@link DataStore}
	 * 
	 * @param type
	 *            The {@link Type} to be checked against.
	 * @param name
	 *            The name {@link String} to be checked against.
	 * @return Return the {@link Object} matching the given {@link Type} and the
	 *         name {@link String}.
	 */
	public <T> T get(Class<T> type, String name) {
		return get(TypeToken.get(type, name));
	}

	/**
	 * Get the {@link Object} matching the given {@link TypeToken} in the
	 * {@link DataStore}.
	 * 
	 * @param typeToken
	 *            The {@link TypeToken} to be checked against.
	 * @return Return the {@link Object} matching the given {@link TypeToken}.
	 */
	public <T> T get(TypeToken<T> typeToken) {

		List<T> objs = getAll(typeToken);
		if (objs == null) {
			return null;
		}
		return objs.get(0);
	}

	/**
	 * Get all the {@link Objects} matching the given {@link Type}in
	 * {@link DataStore}
	 * 
	 * @param type
	 *            The {@link Type} to be checked against.
	 * @return Return all the {@link Object}s matching the given {@link Type}.
	 */
	public <T> List<T> getAll(Class<T> type) {
		return getAll(type, null);
	}

	/**
	 * Get all the {@link Objects} matching the given {@link Type} and the name
	 * {@link String} in {@link DataStore}
	 * 
	 * @param type
	 *            The {@link Type} to be checked against.
	 * @param name
	 *            The name {@link String} to be checked against.
	 * @return Return all the {@link Object}s matching the given {@link Type}
	 *         and the name {@link String}.
	 */
	public <T> List<T> getAll(Class<T> type, String name) {
		return getAll(TypeToken.get(type, name));
	}

	/**
	 * Get all {@link Object}s matching the given {@link TypeToken} in the
	 * {@link DataNode}.
	 * 
	 * @param typeToken
	 *            The {@link TypeToken} to be checked against.
	 * @return Return all the {@link Object}s matching the given
	 *         {@link TypeToken}.
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getAll(TypeToken<T> typeToken) {

		if (typeToken == null) {
			return null;
		}

		List<T> objs = (List<T>) dataMap.get(typeToken);
		if (objs == null || objs.size() <= 0) {
			return null;
		}
		return objs;
	}

	/**
	 * Returns a {@link Set} view of the keys contained in this
	 * {@link DataNode}.
	 * 
	 * @return The {@link Set} of {@link TypeToken} which are served as key in
	 *         this {@link DataNode}.
	 */
	public Set<TypeToken<?>> keySet() {
		return dataMap.keySet();
	}

	/**
	 * Number of {@link Object}s in the {@link DataNode}.
	 * 
	 * @return The number of {@link Object}s in the {@link DataNode}.
	 */
	public int size() {
		return numObjs;
	}

	/**
	 * Returns a {@link Collection} view of the values contained in this
	 * {@link DataNode}.
	 * 
	 * @return The {@link ArrayList} of {@link Object}s stored in this
	 *         {@link DataNode}.
	 */
	public List<Object> values() {
		List<Object> values = new ArrayList<Object>();
		for (List<Object> objs : dataMap.values()) {
			values.addAll(objs);
		}
		return values;
	}
}
