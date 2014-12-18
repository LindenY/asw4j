package ca.uwaterloo.asw4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import ca.uwaterloo.asw4j.internal.DataManipulationObjectMap;
import ca.uwaterloo.asw4j.reflection.TypeToken;

/**
 * <p>
 * An implementation of {@link DataStore}. Thread Safe.
 * </p>
 * <p>
 * This implementation is built around a {@link ConcurrentHashMap} to support
 * concurrency programming. {@link TypeToken} is used as the key, and
 * {@link ArrayList} as the value.
 * </p>
 * 
 * @author Desmond Lin
 * @since 1.0.0
 */
public class ConcurrentMapDataStore implements DataStore {

	private ConcurrentHashMap<TypeToken<?>, List<Object>> concurrentMap;
	private DataManipulationObjectMap manipulationObjectMap;

	private volatile int size;

	public ConcurrentMapDataStore() {
		concurrentMap = new ConcurrentHashMap<TypeToken<?>, List<Object>>();
		manipulationObjectMap = new DataManipulationObjectMap();
		size = 0;
	}

	public void add(Object obj) {
		add(obj, null);
	}

	public void add(Object obj, String name) {
		TypeToken<?> typeToken = TypeToken.get(obj.getClass(), name);
		List<Object> objs = concurrentMap.get(typeToken);

		if (objs == null) {
			objs = new ArrayList<Object>();
			concurrentMap.put(typeToken, objs);
		}

		synchronized (objs) {
			objs.add(obj);
			size++;
		}

		combine(typeToken);
		balance(typeToken);
	}

	public void addAll(Collection<?> objs) {
		addAll(objs, null);
	}

	public void addAll(Collection<?> objs, String name) {
		for (Object obj : objs) {
			add(obj, name);
		}
	}

	public boolean contain(Class<?> type) {
		return contain(TypeToken.get(type));
	}

	public boolean contain(Class<?> type, String name) {
		return contain(TypeToken.get(type, name));
	}

	public boolean contain(TypeToken<?> typeToken) {

		List<Object> objs = concurrentMap.get(typeToken);

		if (objs == null) {
			return false;
		}

		return objs.size() > 0 ? true : false;
	}

	public boolean containAll(List<TypeToken<?>> typeTokens) {

		Map<TypeToken<?>, Integer> tokenCountMap = new HashMap<TypeToken<?>, Integer>();

		for (TypeToken<?> typeToken : typeTokens) {

			tokenCountMap.put(
					typeToken,
					(tokenCountMap.get(typeToken) == null) ? 1 : tokenCountMap
							.get(typeToken) + 1);

			if (!contain(typeToken)) {
				return false;
			} else {
				if (concurrentMap.get(typeToken).size() < tokenCountMap
						.get(typeToken)) {
					return false;
				}
			}
		}
		return true;
	}

	public <T> List<T> getAllValues(Class<T> type) {
		return getAllValues(type, null);
	}

	public <T> List<T> getAllValues(Class<T> type, String name) {
		return getAllValues(TypeToken.get(type, name));
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getAllValues(TypeToken<T> typeToken) {
		return (List<T>) concurrentMap.get(typeToken);
	}

	public <T> T getAndRemove(Class<T> type) {
		return getAndRemove(TypeToken.get(type));
	}

	public <T> T getAndRemove(Class<T> type, String name) {
		return getAndRemove(TypeToken.get(type, name));
	}

	@SuppressWarnings("unchecked")
	public <T> T getAndRemove(TypeToken<T> typeToken) {
		List<Object> objs = concurrentMap.get(typeToken);

		if (objs == null || objs.size() <= 0) {
			return null;
		}

		Object obj = null;
		synchronized (objs) {
			obj = objs.get(0);
			objs.remove(0);
			size--;
		}

		return (T) obj;
	}

	public DataNode getAndRemoveAll(Collection<TypeToken<?>> typeTokens) {

		DataNode dataNode = new DataNode();

		for (TypeToken<?> typeToken : typeTokens) {
			dataNode.put(getAndRemove(typeToken), typeToken.getName());
		}

		return dataNode;
	}

	public <T> T combineAndGet(Class<?> type) {
		return combineAndGet(type, null);
	}

	@SuppressWarnings("unchecked")
	public <T> T combineAndGet(Class<?> type, String name) {
		return (T) combineAndGet(TypeToken.get(type, name));
	}

	@SuppressWarnings("unchecked")
	public <T> T combineAndGet(TypeToken<T> typeToken) {
		List<T> objs = (List<T>) concurrentMap.get(typeToken);

		if (objs == null || objs.size() <= 0) {
			return null;
		}

		combine(typeToken);

		return objs.get(0);
	}

	/**
	 * <p>
	 * {@inheritDoc}
	 * </p>
	 * <p>
	 * NOTE: This method is not encouraged to be used, because right now it uses
	 * normal {@link ArrayList} to store the objects and {@link ArrayList} does
	 * not support concurrent operation. To prevent
	 * {@link ConcurrentModificationException} from happening,it uses
	 * 'synchronized' block to lock the entire {@link ArrayList} from accessing.
	 * By using this method, it surely prevent the happening of the
	 * {@link ConcurrentModificationException}, but with the cost of
	 * performance.
	 * </p>
	 */
	@Deprecated
	public boolean containObject(Object obj) {

		TypeToken<?> typeToken = TypeToken.get(obj.getClass());

		if (!contain(typeToken)) {
			return false;
		}

		List<Object> objs = concurrentMap.get(typeToken);
		synchronized (objs) {
			return objs.contains(obj);
		}
	}

	public int size() {
		return size;
	}

	public void registerBalancer(TypeToken<?> typeToken, Balancer<?> balancer) {
		manipulationObjectMap.registerBalancer(typeToken, balancer);
	}

	public void registerCombiner(TypeToken<?> typeToken, Combiner<?> combiner) {
		manipulationObjectMap.registerCombiner(typeToken, combiner);
	}
	
	/**
	 * Returns a {@link Set} view of the keys contained in this
	 * {@link ConcurrentMapDataStore}.
	 * 
	 * @return The {@link Set} of {@link TypeToken} which are served as key in
	 *         this {@link ConcurrentMapDataStore}.
	 */
	public Set<TypeToken<?>> keySet() {
		return concurrentMap.keySet();
	}

	/**
	 * Returns a {@link Collection} view of the values contained in this
	 * {@link ConcurrentMapDataStore}.
	 * 
	 * @return The {@link ArrayList} of {@link Object}s stored in this
	 *         {@link ConcurrentMapDataStore}.
	 */
	public Collection<List<Object>> values() {
		return concurrentMap.values();
	}
	
	/**
	 * Return a {@link Map} view of {@link TypeToken} and {@link ArrayList} in
	 * this {@link ConcurrentMapDataStore}.
	 * 
	 * @return The {@link Map} of {@link TypeToken}s and {@link ArrayList} of
	 *         {@link Object}s.
	 */
	public Map<TypeToken<?>, List<Object>> getDataMap() {
		Map<TypeToken<?>, List<Object>> resultMap = new HashMap<TypeToken<?>, List<Object>>();
		for (TypeToken<?> tk : concurrentMap.keySet()) {
			List<Object> objs = concurrentMap.get(tk);
			if (objs.size() > 0) {
				resultMap.put(tk, objs);
			}
		}
		return resultMap;
	}

	/**
	 * <pre>
	 * Helper function.
	 * </pre>
	 * <p>
	 * Invoke {@link Combiner#combine(Collection)} for the given
	 * {@link TypeToken}.
	 * </p>
	 * 
	 * @param typeToken
	 *            The {@link TypeToken} to be checked against.
	 * @return Return {@code true}, if the {@link Combiner#combine(Collection)}
	 *         is performed for the given {@link TypeToken}; otherwise, return
	 *         {@code false}.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean combine(TypeToken<?> typeToken) {
		List<Object> objs = concurrentMap.get(typeToken);

		if (objs == null || objs.size() <= 0) {
			return false;
		}

		Combiner<?> combiner = manipulationObjectMap.getCombiner(typeToken);
		if (combiner == null) {
			return false;
		}

		synchronized (objs) {
			Object obj = combiner.combine((Collection) objs);
			size = size - objs.size() + 1;
			objs.clear();
			objs.add(obj);
		}

		return true;
	}

	/**
	 * <pre>
	 * Helper function.
	 * </pre>
	 * <p>
	 * Invoke {@link Balancer#balance(Collection)} for the given
	 * {@link TypeToken}
	 * </p>
	 * 
	 * @param typeToken
	 *            The {@link TypeToken} to be checked against.
	 * @return Return {@code true}, if the {@link Balancer#balance(Collection)}
	 *         is performed for the given {@link TypeToken}; otherwise, return
	 *         {@code false}.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean balance(TypeToken<?> typeToken) {
		List<Object> objs = concurrentMap.get(typeToken);

		if (objs == null || objs.size() <= 0) {
			return false;
		}

		Balancer<?> balancer = manipulationObjectMap.getBalancer(typeToken);
		if (balancer == null) {
			return false;
		}

		synchronized (objs) {
			Collection balanced = balancer.balance((Collection) objs);
			size = size - objs.size() + balanced.size();
			objs.clear();
			objs.addAll(balanced);
		}

		return true;
	}
}
