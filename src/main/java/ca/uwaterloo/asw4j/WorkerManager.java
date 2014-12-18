package ca.uwaterloo.asw4j;

import java.lang.reflect.Type;
import java.util.concurrent.Future;

/**
 * <p>
 * An interface defined to run {@link Instruction} resolved by
 * {@link InstructionResolver} on different "workers"({@link Thread}s).
 * </p>
 * 
 * @author Desmond Lin
 * @since 1.0.0
 */
public interface WorkerManager {

	/**
	 * Get the current {@link STATE} of this {@link WorkerManager}.
	 * 
	 * @return Return the current {@link STATE} of this {@link WorkerManager}.
	 */
	public STATE getState();

	/**
	 * Create and return a {@link Future} that can be used to cancel execution
	 * and/or wait for completion.
	 * 
	 * @param type
	 *            The data type of execution's result.
	 * @param name
	 *            The data name of execution's result.
	 * @return A {@link Future} that can be used to cancel execution and/or wait
	 *         for completion.
	 */
	public <T> Future<T> asyncStart(Class<T> type, String name);

	/**
	 * Wait and return the result of execution.
	 * 
	 * @param type
	 *            The data type of execution's result.
	 * @param name
	 *            The data name of execution's result.
	 * @return The result of execution.
	 */
	public <T> T start(Class<T> type, String name);

	/**
	 * @see InstructionResolver#numberOfRegisteredInstruction()
	 */
	public int numberOfRegisteredInstruction();

	/**
	 * @see InstructionResolver#registerInstructionClass(Class)
	 */
	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass);

	/**
	 * @see InstructionResolver#registerInstructionClass(Class, String)
	 */
	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String produceDataName);

	/**
	 * @see InstructionResolver#registerInstructionClass(Class, String[],
	 *      Type[])
	 */
	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes);

	/**
	 * @see InstructionResolver#registerInstructionClass(Class, String[],
	 *      Type[], String)
	 */
	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes,
			String produceDataName);

	/**
	 * @see InstructionResolver#registerInstructionClass(Class, String[],
	 *      Type[], String, boolean)
	 */
	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes,
			String produceDataName, boolean supportSingleton);

	/**
	 * <p>
	 * The {@code enum} representation of the states of {@link WorkerManager}.
	 * </p>
	 * <ul>
	 * <li>{@link STATE#Ready} : The {@link WorkerManager} is ready.</li>
	 * <li>{@link STATE#Running} : The {@link WorkerManager} is running.</li>
	 * <li>{@link STATE#Canceled} : The {@link WorkerManager} is canceled</li>
	 * <li>{@link STATE#TerminatedByException} : The {@link WorkerManager} is
	 * terminated by {@link Exception}</li>
	 * <li>{@link STATE#Finished} : The {@link WorkerManager} is finished.</li>
	 * </ul>
	 * 
	 * @author Desmond Lin
	 * @since 1.0.0
	 */
	public static enum STATE {
		/**
		 * The {@link WorkerManager} is ready.
		 */
		Ready,
		/**
		 * {@link STATE#Running} : The {@link WorkerManager} is running.
		 */
		Running,
		/**
		 * The {@link WorkerManager} is canceled.
		 */
		Canceled,
		/**
		 * The {@link WorkerManager} is terminated by {@link Exception}
		 */
		TerminatedByException, 
		/**
		 * The {@link WorkerManager} is finished.
		 */
		Finished
	}
}
