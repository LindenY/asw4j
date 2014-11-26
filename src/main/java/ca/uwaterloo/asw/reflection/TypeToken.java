package ca.uwaterloo.asw.reflection;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class TypeToken<T> {

	private final String name;
	private final Class<? super T> rawType;
	private final Type type;
	private final int hashCode;

	@SuppressWarnings("unchecked")
	private TypeToken(Type type, String name) {
		this.name = name;
		this.type = TypeUtility.canonicalize(type);
		this.rawType = (Class<? super T>) TypeUtility.getRawType(this.type);
		this.hashCode = this.type.hashCode()
				^ ((name == null || name.equals("") ? 0 : name.hashCode()));
	}

	public String getName() {
		return name;
	}

	public Class<? super T> getRawType() {
		return rawType;
	}

	public Type getType() {
		return type;
	}

	public int getHashCode() {
		return hashCode;
	}

	@SuppressWarnings("unchecked")
	static Type[] getSuperclassTypeParameter(Class<?> subclass) {
		Type superclass = subclass.getGenericSuperclass();
		if (superclass instanceof Class) {
			throw new RuntimeException("Missing type parameter.");
		}
		ParameterizedType parameterizedType = (ParameterizedType) superclass;
		return parameterizedType.getActualTypeArguments();
	}

	@Override
	public final int hashCode() {
		return this.hashCode;
	}

	@Override
	public final boolean equals(Object o) {
		if (o instanceof TypeToken<?>) {
			TypeToken<?> t = (TypeToken<?>) o;
			return TypeUtility.equals(type, t.type)
					&& (name == t.name || (name != null && name.equals(t.name)));
		}
		return false;
	}

	@Override
	public final String toString() {
		return TypeUtility.typeToString(type);
	}

	public static TypeToken<?> get(Type type, String name) {
		return new TypeToken<Object>(type, name);
	}

	public static <T> TypeToken<T> get(Class<T> type, String name) {
		return new TypeToken<T>(type, name);
	}
}
