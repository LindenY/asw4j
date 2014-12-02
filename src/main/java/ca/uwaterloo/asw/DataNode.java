package ca.uwaterloo.asw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.uwaterloo.asw.reflection.TypeToken;

public class DataNode {

	private Map<TypeToken<?>, List<Object>> dataMap;
	private int numObjs = 0;

	public DataNode() {
		dataMap = new HashMap<TypeToken<?>, List<Object>>();
	}

	public void put(Object obj) {
		put(obj, null);
	}

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
		numObjs ++;
	}

	public <T> T get(Class<T> clazz) {
		return get(clazz, null);
	}

	public <T> T get(Class<T> clazz, String name) {
		return get(TypeToken.get(clazz, name));
	}
	
	public <T> T get(TypeToken<T> typeToken) {
		
		List<T> objs = getAll(typeToken);
		if (objs == null) {
			return null;
		}
		return objs.get(0);
	}
	
	public <T> List<T> getAll(Class<T> clazz) {
		return getAll(clazz, null);
	}
	
	public <T> List<T> getAll(Class<T> clazz, String name) {
		return getAll(TypeToken.get(clazz, name));
	}
	
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
	
	public int size() {
		return numObjs;
	}
	
	public List<Object> values() {
		List<Object> values = null;
		for (List<Object> objs : dataMap.values()) {
			if (values == null) {
				values = objs;
			} else {
				values.addAll(values);
			}
		}
		return values;
	}
}
