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

public class ConcurrentMapDataStore extends AbstractDataStore {

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
			size ++;
		}
	}

	public void addAll(List<?> objs) {
		addAll(objs, null);
	}
	
	public void addAll(List<?> objs, String name) {
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

	/**
	 * NOTE: This method is not encouraged to be used, because right now it uses
	 * normal {@link ArrayList} to store the objects and {@link ArrayList} does
	 * not support concurrent operation. To prevent
	 * {@link ConcurrentModificationException} from happening,it uses
	 * 'synchronized' block to lock the entire {@link ArrayList} from accessing.
	 * By using this method, it surely prevent the happening of the
	 * {@link ConcurrentModificationException}, but with the cost of
	 * performance.
	 */
	@Deprecated
	public boolean contain(Object obj) {

		TypeToken<?> typeToken = TypeToken.get(obj.getClass());

		if (!contain(typeToken)) {
			return false;
		}

		List<Object> objs = concurrentMap.get(typeToken);
		synchronized (objs) {
			return objs.contains(obj);
		}
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

	public Map<TypeToken<?>, List<Object>> getAllValues() {
		Map<TypeToken<?>, List<Object>> resultMap = new HashMap<TypeToken<?>, List<Object>>();
		for (TypeToken<?> tk : concurrentMap.keySet()) {
			List<Object> objs = concurrentMap.get(tk);
			if (objs.size() > 0) {
				resultMap.put(tk, objs);
			}
		}
		return resultMap;
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
		
		combine(typeToken);
		balance(typeToken);

		List<Object> objs = concurrentMap.get(typeToken);

		if (objs == null || objs.size() <= 0) {
			return null;
		}

		Object obj = null;
		synchronized (objs) {
			obj = objs.get(0);
			objs.remove(0);
			size --;
		}

		return (T) obj;
	}

	public DataNode getAndRemoveAll(List<TypeToken<?>> typeTokens) {

		DataNode dataNode = new DataNode();

		for (TypeToken<?> typeToken : typeTokens) {
			dataNode.put(getAndRemove(typeToken), typeToken.getName());
		}

		return dataNode;
	}

	public Set<TypeToken<?>> keySet() {
		return concurrentMap.keySet();
	}

	public int size() {
		return size;
	}

	public Collection<List<Object>> values() {
		return concurrentMap.values();
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

	public void registerBalancer(TypeToken<?> typeToken,
			Balancer<?> balancer) {
		manipulationObjectMap.registerBalancer(typeToken, balancer);
	}

	public void registerCombiner(TypeToken<?> typeToken,
			Combiner<?> combiner) {
		manipulationObjectMap.registerCombiner(typeToken, combiner);
	}
	
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
			objs.clear();
			objs.add(obj);
		}
		
		return true;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
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
			objs.clear();
			objs.addAll(balanced);
		}
		
		return true;
	}
}
