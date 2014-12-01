package ca.uwaterloo.asw.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.uwaterloo.asw.DataNode;
import ca.uwaterloo.asw.Instruction;
import ca.uwaterloo.asw.ToolResolver;
import ca.uwaterloo.asw.meta.DependOn;
import ca.uwaterloo.asw.meta.ProduceData;
import ca.uwaterloo.asw.meta.RequireData;
import ca.uwaterloo.asw.meta.Singleton;
import ca.uwaterloo.asw.reflection.TypeToken;
import ca.uwaterloo.asw.reflection.TypeUtility;

public class InstructionNode {

	protected Class<? extends Instruction<?, ?>> instructionClass;
	protected List<Class<? extends Instruction<?, ?>>> dependencies;
	protected List<TypeToken<?>> requireDatas;
	protected TypeToken<?> produceData;

	protected final boolean supportAsync;
	protected final boolean supportSingleton;

	public InstructionNode(String[] requireDataNames,
			Class<?>[] requireDataTypes, String produceDataName,
			Class<?> produceDataType,
			Class<? extends Instruction<?, ?>> instructionClass) {

		this.instructionClass = instructionClass;

		dependencies = getInstructionDependencies(instructionClass);

		supportAsync = getInstructionAsyncSupport(instructionClass);

		supportSingleton = getInstructionSingletonSupport(instructionClass);

		requireDatas = new ArrayList<TypeToken<?>>();
		for (int i = 0; i < requireDataTypes.length; i++) {
			Type type = requireDataTypes[i];
			String name = (requireDataNames == null || requireDataNames.length <= i) 
					? null
					: requireDataNames[i];
			requireDatas.add(TypeToken.get(type, name));
		}

		produceData = TypeToken.get(produceDataType, produceDataName);
	}

	public List<Class<? extends Instruction<?, ?>>> getDependencies() {
		return dependencies;
	}

	public List<TypeToken<?>> getRequireDatas() {
		return requireDatas;
	}

	public TypeToken<?> getProduceData() {
		return produceData;
	}

	public Class<? extends Instruction<?, ?>> getInstruction() {
		return instructionClass;
	}

	public boolean isSupportAsync() {
		return supportAsync;
	}

	public boolean isSupportSingleton() {
		return supportSingleton;
	}

	public Instruction<?, ?> getInstructionInstance(ToolResolver toolResolver) {

		try {
			Constructor<? extends Instruction<?, ?>> constructor = instructionClass
					.getDeclaredConstructor(ToolResolver.class);
			Instruction<?, ?> instruction = constructor
					.newInstance(toolResolver);
			return instruction;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static List<Class<? extends Instruction<?, ?>>> getInstructionDependencies(
			Class<? extends Instruction<?, ?>> instructionClass) {

		List<Class<? extends Instruction<?, ?>>> dependencies = null;
		DependOn dependOnAnnotation = instructionClass
				.getAnnotation(DependOn.class);

		if (dependOnAnnotation != null) {
			Class<? extends Instruction<?, ?>>[] depends = dependOnAnnotation
					.instructions();
			if (depends != null) {
				dependencies = Arrays.asList(depends);
			}
		}
		dependencies = dependencies == null ? new ArrayList<Class<? extends Instruction<?, ?>>>()
				: dependencies;

		return dependencies;
	}

	public static boolean getInstructionAsyncSupport(
			Class<? extends Instruction<?, ?>> instructionClass) {

		DependOn dependOnAnnotation = instructionClass
				.getAnnotation(DependOn.class);

		if (dependOnAnnotation != null) {
			return dependOnAnnotation.async();
		}

		return true;
	}

	public static boolean getInstructionSingletonSupport(
			Class<? extends Instruction<?, ?>> instructionClass) {

		Singleton singletonAnnotation = instructionClass
				.getAnnotation(Singleton.class);
		if (singletonAnnotation != null) {
			return true;
		}

		return false;
	}

	public static final String[] getInstructionRequireDataNames(
			Class<? extends Instruction<?, ?>> instructionClass) {

		RequireData requireDataAnnotation = instructionClass
				.getAnnotation(RequireData.class);

		if (requireDataAnnotation != null) {
			checkRequireDataArgumentsCondition(requireDataAnnotation,
					instructionClass);
			return requireDataAnnotation.names();
		}

		return null;
	}

	public static final Class<?>[] getInstructionRequireDataTypes(
			Class<? extends Instruction<?, ?>> instructionClass) {

		RequireData requireDataAnnotation = instructionClass
				.getAnnotation(RequireData.class);

		if (requireDataAnnotation != null) {
			checkRequireDataArgumentsCondition(requireDataAnnotation,
					instructionClass);
			return requireDataAnnotation.types();
		}

		return new Class<?>[] { (Class<?>) getInstructionActualParameterizedArgumentAtIndex(
				instructionClass, 0) };
	}

	public static final String getInstructionProduceDataName(
			Class<? extends Instruction<?, ?>> instructionClass) {

		ProduceData produceDataAnnotation = instructionClass
				.getAnnotation(ProduceData.class);
		if (produceDataAnnotation != null) {
			return produceDataAnnotation.name();
		}
		return null;
	}

	public static final Class<?> getInstructionProduceDataType(
			Class<? extends Instruction<?, ?>> instructionClass) {

		Type type = getInstructionActualParameterizedArgumentAtIndex(
				instructionClass, 1);
		if (type != null) {
			return (Class<?>) type;
		}

		ProduceData produceDataAnnotation = instructionClass
				.getAnnotation(ProduceData.class);
		if (produceDataAnnotation != null) {
			return produceDataAnnotation.type();
		}
		return null;
	}

	private static final Type getInstructionActualParameterizedArgumentAtIndex(
			Class<? extends Instruction<?, ?>> instructionClass, int index) {

		Type[] parameterizedTypes = TypeUtility
				.getSuperclassTypeParameter(instructionClass);

		if (parameterizedTypes != null) {
			return parameterizedTypes[index];
		} else {
			return null;
		}
	}

	// TODO: make it more specific what is the error.
	private static final void checkRequireDataArgumentsCondition(
			RequireData requireDataAnnotation,
			Class<? extends Instruction<?, ?>> instructionClass) {

		String[] names = requireDataAnnotation.names();
		Class<?>[] types = requireDataAnnotation.types();
		Type parameterizedType = getInstructionActualParameterizedArgumentAtIndex(
				instructionClass, 0);

		if (names.length > 1 && parameterizedType != DataNode.class) {
			throw new IllegalArgumentException();
		}

		if (names.length > 1 && types.length == 1 && types[0] != Object.class) {
			throw new IllegalArgumentException();
		}

		if (names.length > 1 && types.length > 1
				&& names.length != types.length) {
			throw new IllegalArgumentException();
		}

		if (names.length < types.length) {
			throw new IllegalArgumentException();
		}
	}
}
