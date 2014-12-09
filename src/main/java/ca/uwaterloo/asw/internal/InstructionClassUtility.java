package ca.uwaterloo.asw.internal;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.uwaterloo.asw.Instruction;
import ca.uwaterloo.asw.meta.DependOn;
import ca.uwaterloo.asw.meta.ProduceData;
import ca.uwaterloo.asw.meta.RequireData;
import ca.uwaterloo.asw.meta.Singleton;
import ca.uwaterloo.asw.reflection.TypeUtility;

public class InstructionClassUtility {

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
			return requireDataAnnotation.names();
		}

		return null;
	}

	public static final Type[] getInstructionRequireDataTypes(
			Class<? extends Instruction<?, ?>> instructionClass) {

		RequireData requireDataAnnotation = instructionClass
				.getAnnotation(RequireData.class);

		if (requireDataAnnotation != null) {
			return requireDataAnnotation.types();
		}

		return new Type[] { TypeUtility
				.getSuperclassTypeParameter(instructionClass)[0] };
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

	public static final Type getInstructionProduceDataType(
			Class<? extends Instruction<?, ?>> instructionClass) {
		return TypeUtility.getSuperclassTypeParameter(instructionClass)[1];
	}

	public static final void checkPreconditionOfRequireDatas(String[] names,
			Type[] types) {

		if (types == null || types.length == 0) {
			throw new IllegalArgumentException();
		} else if (names == null || names.length == 0) {
			if (types.length != 1) {
				throw new IllegalArgumentException();
			}
		} else if (names.length != types.length) {
			throw new IllegalArgumentException();
		}
	}
}
