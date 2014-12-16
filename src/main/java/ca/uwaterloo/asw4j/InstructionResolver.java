package ca.uwaterloo.asw4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

/**
 * <p>
 * A interface defined to register {@link Instruction} classes, manage
 * ready-to-run {@link Instruction} classes and resolve the require data and the
 * produce data of {@link Instruction} classes.
 * </p>
 * <p>
 * To resolve and inject the require data of {@link Instruction} classes, and
 * manage the produce data of {@link Instruction} classes, a {@link DataStore}
 * is used to achieve such goal.
 * </p>
 * 
 * @author Desmond Lin
 * @since 1.0.0
 */
public interface InstructionResolver {

	/**
	 * Set the {@link DataStore}.
	 * 
	 * @param dataStore
	 *            The {@link DataStore} of this {@link InstructionResolver}.
	 */
	public void setDataStore(DataStore dataStore);

	/**
	 * Get the {@link DataStore}.
	 * 
	 * @return The {@link DataStore} of this {@link InstructionResolver}.
	 */
	public DataStore getDataStore();

	/**
	 * Invoked after resolved {@link Instruction} execution.
	 * 
	 * @param instruction
	 *            The resolved {@link Instruction} finished execution.
	 * @param throwed
	 *            The {@link Throwable} thrown during {@link Instruction}
	 *            execution.
	 */
	public void afterInstructionExecution(Instruction<?, ?> instruction,
			Throwable throwed);

	/**
	 * Invoked before resolved {@link Instruction} execution.
	 * 
	 * @param instruction
	 *            The resolved {@link Instruction} about to be executed.
	 */
	public void beforeInstructionExecution(Instruction<?, ?> instruction);

	/**
	 * Resolve the ready-to-run {@link Instruction} and inject the require data
	 * of such {@link Instruction}.
	 * 
	 * @return If there is a ready-to-run {@link Instruction} class found, a
	 *         {@link Instruction} with everything ready is returned; otherwise,
	 *         return {@code null}.
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Instruction<?, ?> resolveInstruction() throws SecurityException,
			IllegalArgumentException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException;

	/**
	 * Get the number of registered {@link Instruction}.
	 * 
	 * @return The number of registered {@link Instruction}.
	 */
	public int numberOfRegisteredInstruction();

	/**
	 * Register an {@link Instruction} class.
	 * 
	 * @param instructionClass
	 *            The {@link Instruction} class to be registered.
	 */
	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass);

	/**
	 * Register an {@link Instruction} class.
	 * 
	 * @param instructionClass
	 *            The {@link Instruction} class to be registered.
	 * @param produceDataName
	 *            The produce data name of the {@link Instruction} class.
	 */
	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String produceDataName);

	/**
	 * Register an {@link Instruction} class.
	 * 
	 * @param instructionClass
	 *            The {@link Instruction} class to be registered.
	 * @param requireDataNames
	 *            The require data types of the {@link Instruction} class.
	 * @param requireDataTypes
	 *            The require data names of the {@link Instruction} class.
	 */
	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes);

	/**
	 * Register an {@link Instruction} class.
	 * 
	 * @param instructionClass
	 *            The {@link Instruction} class to be registered.
	 * @param requireDataNames
	 *            The require data types of the {@link Instruction} class.
	 * @param requireDataTypes
	 *            The require data names of the {@link Instruction} class.
	 * @param produceDataName
	 *            The produce data name of the {@link Instruction} class.
	 */
	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes,
			String produceDataName);

	/**
	 * Register an {@link Instruction} class.
	 * 
	 * @param instructionClass
	 *            The {@link Instruction} class to be registered.
	 * @param requireDataNames
	 *            The require data types of the {@link Instruction} class.
	 * @param requireDataTypes
	 *            The require data names of the {@link Instruction} class.
	 * @param produceDataName
	 *            The produce data name of the {@link Instruction} class.
	 * @param supportSingleton
	 *            If the {@link Instruction} is singleton. Default is {@code false}.
	 */
	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes,
			String produceDataName, boolean supportSingleton);
}
