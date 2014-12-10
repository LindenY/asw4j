package ca.uwaterloo.asw4j.internal;

import java.lang.reflect.Type;

import ca.uwaterloo.asw4j.Instruction;
import ca.uwaterloo.asw4j.meta.DependOn;
import ca.uwaterloo.asw4j.meta.ProduceData;
import ca.uwaterloo.asw4j.meta.RequireData;
import ca.uwaterloo.asw4j.meta.Singleton;
import ca.uwaterloo.asw4j.reflection.TypeUtility;

public class InstructionClassUtility {

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

	public static boolean getInstructionAsyncSupport(
			Class<? extends Instruction<?, ?>> instructionClass) {

		DependOn dependOnAnnotation = instructionClass
				.getAnnotation(DependOn.class);

		if (dependOnAnnotation != null) {
			return dependOnAnnotation.async();
		}

		return true;
	}

	public static Class<? extends Instruction<?, ?>>[] getInstructionDependencies(
			Class<? extends Instruction<?, ?>> instructionClass) {

		DependOn dependOnAnnotation = instructionClass
				.getAnnotation(DependOn.class);

		if (dependOnAnnotation != null) {
			Class<? extends Instruction<?, ?>>[] depends = dependOnAnnotation
					.instructions();
			if (depends != null) {
				return depends;
			}
		}
		
		return null;
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

	public static boolean getInstructionSingletonSupport(
			Class<? extends Instruction<?, ?>> instructionClass) {

		Singleton singletonAnnotation = instructionClass
				.getAnnotation(Singleton.class);
		if (singletonAnnotation != null) {
			return true;
		}

		return false;
	}
}
