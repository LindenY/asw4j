package ca.uwaterloo.asw;

import java.util.HashMap;
import java.util.Map;

import ca.uwaterloo.asw.reflection.TypeToken;

public class DataNode {

	private Map<TypeToken<?>, Object> dataMap;

	public DataNode() {
		dataMap = new HashMap<TypeToken<?>, Object>();
	}

	public Object put(Object obj) {
		return put(obj, null);
	}

	public Object put(Object obj, String name) {
		if (obj == null) {
			return null;
		}

		TypeToken<?> typeToken = TypeToken.get(obj.getClass(), name);
		return dataMap.put(typeToken, obj);
	}

	public <T> T get(Class<? extends T> clazz) {
		return get(clazz, null);
	}

	public <T> T get(Class<? extends T> clazz, String name) {
		TypeToken<?> typeToken = TypeToken.get(clazz, name);
		Object obj = dataMap.get(typeToken);
		if (obj == null) {
			return null;
		}
		return (T) obj;
	}

}
