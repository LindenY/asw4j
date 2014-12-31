package ca.uwaterloo.asw4j.reflection;

import java.lang.reflect.Type;

import static ca.uwaterloo.asw4j.internal.Assert.*;

/**
 * <p>
 * This class is served as an identity for data {@link Type}, which is used to
 * distinguish and track the difference between same data type.
 * </p>
 * <p>
 * It will be confusing when identifying and resolving the desire data, if only
 * {@link Type} is used. Therefore, {@link TypeToken} is introduced to prevent
 * the data {@link Type} confusion.
 * </p>
 * <p>
 * To have different {@link TypeToken}s for the exact same {@link Type}, a name
 * {@link String} can be used as an id.
 * </p>
 * <p>
 * Use {@link #get(Class)}, {@link #get(Type)}, {@link #get(Class, String)}, and
 * {@link #get(Type, String)} to obtain a {@link TypeToken}.
 * </p>
 * 
 * @author Desmond Lin
 * @since 1.0.0
 *
 * @param <T> The {@link Type} that this {@link TypeToken} represent.
 */
public class TypeToken<T> {

	private final String name;
	private final Class<? super T> rawType;
	private final Type type;
	private final int hashCode;

	@SuppressWarnings("unchecked")
	private TypeToken(Type type, String name) {
		
		assertNull(type);
		
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
		return TypeUtility.typeToString(type) + " " + name;
	}

	public static TypeToken<?> get(Type type, String name) {
		return new TypeToken<Object>(type, name);
	}

	public static <T> TypeToken<T> get(Class<T> type, String name) {
		return new TypeToken<T>(type, name);
	}

	public static TypeToken<?> get(Type type) {
		return new TypeToken<Object>(type, null);
	}

	public static <T> TypeToken<T> get(Class<T> type) {
		return new TypeToken<T>(type, null);
	}
}
