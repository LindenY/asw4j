package ca.uwaterloo.asw4j;

import java.lang.reflect.Type;

import ca.uwaterloo.asw4j.meta.Singleton;

/**
 * The basic interface defined to register {@link Instruction} classes.
 * 
 * @author Desmond Lin
 * @since 1.1.0
 */
public interface InstructionClassRegister {

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
	 *            If the {@link Instruction} class is singleton. Default is {@code false}.
	 *            
	 * @see {@link Singleton}
	 */
	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes,
			String produceDataName, boolean supportSingleton);
}
