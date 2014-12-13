package ca.uwaterloo.asw4j;

import java.lang.reflect.Type;
import java.util.concurrent.Future;

public interface WorkerManager {
	
	public STATE getState();
	
	public <T> Future<T> asyncStart(Class<T> type, String name);
	
	public <T> T start(Class<T> type, String name);
	
	
	public int numberOfRegisteredInstruction();

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass);

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String produceDataName);

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes);

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes,
			String produceDataName);

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes,
			String produceDataName,
			boolean supportSingleton);
	
	public static enum STATE {
		Ready,
		Running,
		Cancelled,
		TerminatedByException,
		Finished
	}
}
