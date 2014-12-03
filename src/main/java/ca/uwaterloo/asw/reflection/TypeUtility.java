package ca.uwaterloo.asw.reflection;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;

/**
 * It is currently not supporting {@link WildcardType}
 */
public class TypeUtility {

	public static Type canonicalize(Type type) {

		if (type instanceof Class) {
			Class<?> c = (Class<?>) type;
			return c.isArray() ? new GenericArrayTypeImpl(
					canonicalize(c.getComponentType())) : c;

		} else if (type instanceof ParameterizedType) {
			ParameterizedType p = (ParameterizedType) type;
			return new ParameterizedTypeImpl(p.getOwnerType(), p.getRawType(),
					p.getActualTypeArguments());

		} else if (type instanceof GenericArrayType) {
			GenericArrayType g = (GenericArrayType) type;
			return new GenericArrayTypeImpl(g.getGenericComponentType());

		} else {
			return type;
		}
	}

	public static String typeToString(Type type) {
		return type instanceof Class ? ((Class<?>) type).getName() : type
				.toString();
	}

	public static boolean equals(Type a, Type b) {
		if (a == b) {
			return true;

		} else if (a instanceof Class) {
			return a.equals(b);

		} else if (a instanceof ParameterizedType) {
			if (!(b instanceof ParameterizedType)) {
				return false;
			}

			ParameterizedType pa = (ParameterizedType) a;
			ParameterizedType pb = (ParameterizedType) b;

			if (pa == pb || (pa != null && pa.equals(pb))) {
				return pa.getRawType().equals(pb.getRawType())
						&& Arrays.equals(pa.getActualTypeArguments(),
								pb.getActualTypeArguments());
			} else {
				return false;
			}

		} else if (a instanceof GenericArrayType) {
			if (!(b instanceof GenericArrayType)) {
				return false;
			}

			GenericArrayType ga = (GenericArrayType) a;
			GenericArrayType gb = (GenericArrayType) b;
			return equals(ga.getGenericComponentType(),
					gb.getGenericComponentType());

		} else if (a instanceof WildcardType) {
			if (!(b instanceof WildcardType)) {
				return false;
			}

			WildcardType wa = (WildcardType) a;
			WildcardType wb = (WildcardType) b;
			return Arrays.equals(wa.getUpperBounds(), wb.getUpperBounds())
					&& Arrays.equals(wa.getLowerBounds(), wb.getLowerBounds());

		} else if (a instanceof TypeVariable) {
			if (!(b instanceof TypeVariable)) {
				return false;
			}
			TypeVariable<?> va = (TypeVariable<?>) a;
			TypeVariable<?> vb = (TypeVariable<?>) b;
			return va.getGenericDeclaration() == vb.getGenericDeclaration()
					&& va.getName().equals(vb.getName());

		} else {
			return false;
		}
	}

	public static Class<?> getRawType(Type type) {
		if (type instanceof Class<?>) {
			return (Class<?>) type;
		} else if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			Type rawType = parameterizedType.getRawType();

			if (rawType instanceof Class) {
				return (Class<?>) rawType;
			} else {
				throw new IllegalArgumentException();
			}

		} else if (type instanceof GenericArrayType) {
			Type componentType = ((GenericArrayType) type)
					.getGenericComponentType();
			return Array.newInstance(getRawType(componentType), 0).getClass();

		} else if (type instanceof TypeVariable) {
			return Object.class;
		} else if (type instanceof WildcardType) {
			return getRawType(((WildcardType) type).getUpperBounds()[0]);
		} else {
			String className = type == null ? "null" : type.getClass()
					.getName();
			throw new IllegalArgumentException(
					"Expected a Class, ParameterizedType, or "
							+ "GenericArrayType, but <" + type
							+ "> is of type " + className);
		}
	}

	@SuppressWarnings("unchecked")
	public static Type[] getSuperclassTypeParameter(Class<?> subclass) {
		Type superclass = subclass.getGenericSuperclass();
		if (superclass instanceof Class) {
			throw new RuntimeException("Missing type parameter.");
		}
		ParameterizedType parameterizedType = (ParameterizedType) superclass;
		return parameterizedType.getActualTypeArguments();
	}

	public static Type resolveGenericType(Class<?> type, Type subType) {
		Class<?> rawType;
		if (subType instanceof ParameterizedType)
			rawType = (Class<?>) ((ParameterizedType) subType).getRawType();
		else
			rawType = (Class<?>) subType;

		if (type.equals(rawType))
			return subType;

		Type result;
		if (type.isInterface()) {
			for (Type superInterface : rawType.getGenericInterfaces())
				if (superInterface != null
						&& !superInterface.equals(Object.class))
					if ((result = resolveGenericType(type, superInterface)) != null)
						return result;
		}

		Type superClass = rawType.getGenericSuperclass();
		if (superClass != null && !superClass.equals(Object.class))
			if ((result = resolveGenericType(type, superClass)) != null)
				return result;

		return null;
	}

	public static Class<?> resolveRawClass(Type genericType, Class<?> subType) {
		if (genericType instanceof Class) {
			return (Class<?>) genericType;
		} else if (genericType instanceof ParameterizedType) {
			return resolveRawClass(
					((ParameterizedType) genericType).getRawType(), subType);
		} else if (genericType instanceof GenericArrayType) {
			GenericArrayType arrayType = (GenericArrayType) genericType;
			Class<?> compoment = resolveRawClass(
					arrayType.getGenericComponentType(), subType);
			return Array.newInstance(compoment, 0).getClass();
		} else if (genericType instanceof TypeVariable) {
			/*TypeVariable<?> variable = (TypeVariable<?>) genericType;
			genericType = getTypeVariableMap(subType).get(variable);
			genericType = genericType == null ? resolveBound(variable)
					: resolveRawClass(genericType, subType);*/
		}

		return genericType instanceof Class ? (Class<?>) genericType
				: null;
	}

	private static final class GenericArrayTypeImpl implements GenericArrayType {

		private final Type componentType;

		public GenericArrayTypeImpl(Type componentType) {
			this.componentType = canonicalize(componentType);
		}

		public Type getGenericComponentType() {
			return componentType;
		}

		@Override
		public boolean equals(Object o) {
			return o instanceof GenericArrayType
					&& TypeUtility.equals(this, (GenericArrayType) o);
		}

		@Override
		public int hashCode() {
			return componentType.hashCode();
		}

		@Override
		public String toString() {
			return typeToString(componentType) + "[]";
		}
	}

	private static final class ParameterizedTypeImpl implements
			ParameterizedType {

		private final Type ownerType;
		private final Type rawType;
		private final Type[] typeArguments;

		public ParameterizedTypeImpl(Type ownerType, Type rawType,
				Type... typeArguments) {
			// require an owner type if the raw type needs it
			if (rawType instanceof Class<?>) {
				Class rawTypeAsClass = (Class) rawType;
			}

			this.ownerType = ownerType == null ? null : canonicalize(ownerType);
			this.rawType = canonicalize(rawType);
			this.typeArguments = typeArguments.clone();
			for (int t = 0; t < this.typeArguments.length; t++) {
				this.typeArguments[t] = canonicalize(this.typeArguments[t]);
			}
		}

		public Type[] getActualTypeArguments() {
			return typeArguments.clone();
		}

		public Type getRawType() {
			return rawType;
		}

		public Type getOwnerType() {
			return ownerType;
		}

		@Override
		public boolean equals(Object other) {
			return other instanceof ParameterizedType
					&& TypeUtility.equals(this, (ParameterizedType) other);
		}

		@Override
		public int hashCode() {
			return Arrays.hashCode(typeArguments) ^ rawType.hashCode()
					^ (ownerType == null ? 0 : ownerType.hashCode());
		}

		@Override
		public String toString() {
			StringBuilder stringBuilder = new StringBuilder(
					30 * (typeArguments.length + 1));
			stringBuilder.append(typeToString(rawType));

			if (typeArguments.length == 0) {
				return stringBuilder.toString();
			}

			stringBuilder.append("<").append(typeToString(typeArguments[0]));
			for (int i = 1; i < typeArguments.length; i++) {
				stringBuilder.append(", ").append(
						typeToString(typeArguments[i]));
			}
			return stringBuilder.append(">").toString();
		}
	}

}
