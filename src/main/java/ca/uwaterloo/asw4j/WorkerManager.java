package ca.uwaterloo.asw4j;

import java.lang.reflect.Type;
import java.util.concurrent.Future;

public interface WorkerManager {
	
	public STATE getState();
	
	public void awaitShutDown(int timeOut) throws InterruptedException;
	
	public void shutDown();
	
	public void shutDownNow();
	
	public <T> Future<T> asyncStart(Class<T> type, String name);
	
	public <T> T start(Class<T> type, String name);
	
	public void setExceptionHandlePolicy(ExceptionHandlePolicy policy);
	
	public ExceptionHandlePolicy getExceptionHandlePolicy();
	
	
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

	
	public static enum ExceptionHandlePolicy {
		BlockInstructionAndRun,
		BlockInstructionPrintTraceAndRun,
		Terminate
	}
	
	public static enum STATE {
		Ready,
		Canceled,
		TerminatedByException,
		Finished
	}
}
