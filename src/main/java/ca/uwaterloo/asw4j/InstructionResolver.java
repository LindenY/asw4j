package ca.uwaterloo.asw4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import ca.uwaterloo.asw4j.meta.Singleton;

/**
 * <p>
 * An interface defined to register {@link Instruction} classes, manage
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
	 * Set the {@link ToolResolver}.
	 * 
	 * @param toolResolver
	 */
	public void setToolResolver(ToolResolver toolResolver);
	
	/**
	 * Get the {@link ToolResolver}.
	 * 
	 * @return
	 */
	public ToolResolver getToolResolver();

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
	
}
