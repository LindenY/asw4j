package ca.uwaterloo.asw.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ca.uwaterloo.asw.Instruction;
import ca.uwaterloo.asw.ToolResolver;
import ca.uwaterloo.asw.reflection.TypeToken;

public class InstructionNode {

	protected final Class<? extends Instruction<?, ?>> instructionClass;
	protected final List<Class<? extends Instruction<?, ?>>> dependencies;
	protected final List<TypeToken<?>> requireDatas;
	protected final TypeToken<?> produceData;
	protected final boolean supportAsync;
	protected final boolean supportSingleton;
	
	public InstructionNode(Class<? extends Instruction<?, ?>> instructionClass) {
		
		this(
				instructionClass,
				InstructionClassUtility.getInstructionRequireDataNames(instructionClass),
				InstructionClassUtility.getInstructionRequireDataTypes(instructionClass),
				InstructionClassUtility.getInstructionProduceDataName(instructionClass)
			);
	}
	
	public InstructionNode(Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, 
			Type[] requireDataTypes
			) {
		
		this(
				instructionClass,
				requireDataNames,
				requireDataTypes,
				InstructionClassUtility.getInstructionProduceDataName(instructionClass)
			);
	}
	
	public InstructionNode(Class<? extends Instruction<?, ?>> instructionClass,
			String produceDataName
			) {
		
		this(
				instructionClass,
				InstructionClassUtility.getInstructionRequireDataNames(instructionClass),
				InstructionClassUtility.getInstructionRequireDataTypes(instructionClass),
				produceDataName
			);
	}

	public InstructionNode(Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, 
			Type[] requireDataTypes,
			String produceDataName
			) {
		
		this(
				instructionClass,
				requireDataNames,
				requireDataTypes,
				produceDataName,
				InstructionClassUtility.getInstructionDependencies(instructionClass),
				InstructionClassUtility.getInstructionSingletonSupport(instructionClass),
				InstructionClassUtility.getInstructionAsyncSupport(instructionClass)
			);
	}
	
	public InstructionNode(Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, 
			Type[] requireDataTypes,
			String produceDataName,
			List<Class<? extends Instruction<?, ?>>> dependencies,
			boolean supportSingleton,
			boolean supportAsync
			) {
		
		this.instructionClass = instructionClass;
		
		InstructionClassUtility.checkPreconditionOfRequireDatas(requireDataNames, requireDataTypes);
		this.requireDatas = new ArrayList<TypeToken<?>>();
		this.produceData = null;
		
		this.dependencies = (
				dependencies == null 
					? new ArrayList<Class<? extends Instruction<?, ?>>>()
					: dependencies);
		
		
		this.supportSingleton = supportSingleton;
		this.supportAsync = supportAsync;
	}

	public Instruction<?, ?> getInstructionInstance(ToolResolver toolResolver) {

		try {
			Constructor<? extends Instruction<?, ?>> constructor = instructionClass
					.getDeclaredConstructor(ToolResolver.class);
			Instruction<?, ?> instruction = constructor
					.newInstance(toolResolver);
			return instruction;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		try {
			Constructor<? extends Instruction<?, ?>> constructor = instructionClass
					.getDeclaredConstructor();
			Instruction<?, ?> instruction = constructor.newInstance();
			return instruction;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return null;
	}

}
