package ca.uwaterloo.asw4j;

import java.lang.reflect.Type;

import ca.uwaterloo.asw4j.meta.DependOn;
import ca.uwaterloo.asw4j.meta.Singleton;

/**
 * The sub-interface defined to provide more dependency related
 * {@link Instruction} class register methods.
 * 
 * @author Desmond Lin
 * @since 1.1.0
 */
public interface DependencyInstructionClassRegister extends
		InstructionClassRegister {

	/**
	 * <p>
	 * Register an {@link Instruction} class.
	 * </p>
	 * <p>
	 * The depending {@link Instruction} classes of the registered
	 * {@link Instruction} are recursively registered.
	 * </p>
	 * 
	 * @param instructionClass
	 *            The {@link Instruction} class to be registered.
	 * @param dependencies
	 *            The depending {@link Instruction} classes of the
	 *            {@link Instruction} class.
	 * 
	 * @see {@link Singleton} and {@link DependOn}
	 */
	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			Class<? extends Instruction<?, ?>>[] dependencies);
	
	/**
	 * <p>
	 * Register an {@link Instruction} class.
	 * </p>
	 * <p>
	 * The depending {@link Instruction} classes of the registered
	 * {@link Instruction} are recursively registered.
	 * </p>
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
	 *            If the {@link Instruction} class is singleton. Default is
	 *            {@code false}.
	 * @param dependencies
	 *            The depending {@link Instruction} classes of the
	 *            {@link Instruction} class.
	 * 
	 * @see {@link Singleton} and {@link DependOn}
	 */
	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes,
			String produceDataName, boolean supportSingleton,
			Class<? extends Instruction<?, ?>>[] dependencies);
	
	/**
	 * <p>
	 * Register an {@link Instruction} class.
	 * </p>
	 * <p>
	 * The depending {@link Instruction} classes of the registered
	 * {@link Instruction} are recursively registered.
	 * </p>
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
	 *            If the {@link Instruction} class is singleton. Default is
	 *            {@code false}.
	 * @param dependencies
	 *            The depending {@link Instruction} classes of the
	 *            {@link Instruction} class.
	 * @param supportAsync
	 *            If the {@link Instruction} class supports async property.
	 * 
	 * @see {@link Singleton} and {@link DependOn}
	 */
	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes,
			String produceDataName, boolean supportSingleton,
			Class<? extends Instruction<?, ?>>[] dependencies,
			boolean supportAsync);
}
