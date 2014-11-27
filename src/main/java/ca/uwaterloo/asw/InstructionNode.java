package ca.uwaterloo.asw;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.uwaterloo.asw.meta.DependOn;
import ca.uwaterloo.asw.meta.ProduceData;
import ca.uwaterloo.asw.meta.RequireData;
import ca.uwaterloo.asw.reflection.TypeToken;
import ca.uwaterloo.asw.reflection.TypeUtility;

public class InstructionNode {

	private List<Class<? extends Instruction<?, ?>>> dependencies;
	private List<TypeToken<?>> requireDatas;
	private TypeToken<?> produceData;

	// TODO: This part of code is ugly, fix it later;
	public InstructionNode(Class<? extends Instruction<?, ?>> instructionClass) {
		
		dependencies = getInstructionDependencies(instructionClass);
		requireDatas = getInstructionRequireDatas(instructionClass);
		produceData = getInstructionProduceData(instructionClass);
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
		dependencies = dependencies == null 
				? new ArrayList<Class<? extends Instruction<?, ?>>>()
				: dependencies;

		return dependencies;
	}

	public static List<TypeToken<?>> getInstructionRequireDatas(Class<? extends Instruction<?, ?>> instructionClass) {
		
		Type[] parameteriedTypes = TypeUtility
				.getSuperclassTypeParameter(instructionClass);
		Type parameteriedType = parameteriedTypes == null ? null
				: parameteriedTypes[0];

		List<TypeToken<?>> requireDatas = new ArrayList<TypeToken<?>>();
		RequireData requireDataAnnotation = instructionClass
				.getAnnotation(RequireData.class);
		if (requireDataAnnotation != null) {
			String[] requireNames = requireDataAnnotation.names();
			Class<?>[] requireTypes = requireDataAnnotation.types();

			if (requireNames == null && requireTypes == null) {
				requireDatas.add(TypeToken.get(parameteriedType));
			} else if (requireNames != null && requireTypes == null) {
				if (requireNames.length == 1) {
					requireDatas.add(TypeToken.get(parameteriedType,
							requireNames[0]));
				} else {
					throw new IllegalArgumentException();
				}
			} else if (requireNames == null && requireTypes != null) {
				if (requireTypes.length <= 1) {
					requireDatas.add(TypeToken.get(parameteriedType));
				} else {
					if (parameteriedType == DataNode.class) {
						requireDatas = new ArrayList<TypeToken<?>>();
						for (int i = 0; i < requireTypes.length; i++) {
							requireDatas.add(TypeToken.get(requireTypes[i]));
						}
					} else {
						throw new IllegalArgumentException();
					}
				}
			} else {
				if (requireNames.length == 1) {
					requireDatas.add(TypeToken.get(parameteriedType,
							requireNames[0]));
				} else {

					if (requireNames.length != requireTypes.length
							|| parameteriedType != DataNode.class) {
						throw new IllegalArgumentException();
					}

					for (int i = 0; i < requireNames.length; i++) {
						requireDatas.add(TypeToken.get(requireTypes[i],
								requireNames[i]));
					}
				}
			}
		} else {
			requireDatas.add(TypeToken.get(parameteriedType));
		}
		
		return requireDatas;
	}
	
	public static TypeToken<?> getInstructionProduceData(Class<? extends Instruction<?, ?>> instructionClass) {
		
		TypeToken<?> produceData = null;
		
		Type[] parameteriedTypes = TypeUtility
				.getSuperclassTypeParameter(instructionClass);
		Type parameteriedType = parameteriedTypes == null ? null
				: parameteriedTypes[1];
		ProduceData produceDataAnnotation = instructionClass
				.getAnnotation(ProduceData.class);
		if (produceDataAnnotation != null) {
			if (produceDataAnnotation.name() != null
					&& produceDataAnnotation.name().length() > 0) {
				produceData = TypeToken.get(
						parameteriedType == null ? produceDataAnnotation.type()
								: parameteriedType, produceDataAnnotation
								.name());
			}
		} else {
			produceData = TypeToken.get(parameteriedType);
		}
		
		return produceData;
	}
}
