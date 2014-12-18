package ca.uwaterloo.asw4j;

import java.lang.reflect.Type;

/**
 * <p>
 * An abstract class that implements {@link WorkerManager}.
 * </p>
 * <p>
 * {@link AbstractWorkerManager} defines the basic constructors and fields for
 * the actual implementation of {@link WorkerManager}.
 * </p>
 * 
 * @author Desmond Lin
 * @since 1.0.0
 *
 */
public abstract class AbstractWorkerManager implements WorkerManager {

	protected InstructionResolver instructionResolver;
	protected volatile STATE state;

	public AbstractWorkerManager(InstructionResolver instructionResolver) {
		this.instructionResolver = instructionResolver;
	}

	public InstructionResolver getInstructionResolver() {
		return instructionResolver;
	}

	public void setInstructionResolver(
			AbstractInstructionResolver instructionResolver) {
		this.instructionResolver = instructionResolver;
	}
	
	public STATE getState() {
		return state;
	}
	
	

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass) {
		instructionResolver.registerInstructionClass(instructionClass);
	}

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String produceDataName) {
		instructionResolver.registerInstructionClass(instructionClass,
				produceDataName);
	}

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes) {
		instructionResolver.registerInstructionClass(instructionClass,
				requireDataNames, requireDataTypes);
	}

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes,
			String produceDataName) {
		instructionResolver.registerInstructionClass(instructionClass,
				requireDataNames, requireDataTypes, produceDataName);
	}

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes,
			String produceDataName,
			Class<? extends Instruction<?, ?>>[] dependencies,
			boolean supportSingleton, boolean supportAsync) {
		instructionResolver.registerInstructionClass(instructionClass,
				requireDataNames, requireDataTypes, produceDataName);
	}

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes,
			String produceDataName, boolean supportSingleton) {
		instructionResolver.registerInstructionClass(instructionClass,
				requireDataNames, requireDataTypes, produceDataName,
				supportSingleton);
	}

	public int numberOfRegisteredInstruction() {
		return instructionResolver.numberOfRegisteredInstruction();
	}
	
}
