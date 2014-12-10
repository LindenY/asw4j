package ca.uwaterloo.asw4j.internal;

import java.lang.reflect.InvocationTargetException;

import ca.uwaterloo.asw4j.Instruction;
import ca.uwaterloo.asw4j.ToolResolver;

/**
 * <p>
 * 	Manage {@link Instruction}'s instantiation and recycling. 
 * </p>
 * 
 * @author Desmond Lin
 * @since 1.0.0
 */
public interface InstructionClassOmitter {

	/**
	 * <p>
	 * Get an instance of {@link Instruction} class.
	 * </p>
	 * 
	 * @param toolResolver
	 *            if a new instance of {@link Instruction} needs to be created, this {@link ToolResolver} would be passed
	 *            to create the new instance. if {@code null} is passed, then
	 *            constructor {@link Instruction#Instruction()} would be called
	 *            instead of {@link Instruction#Instruction(ToolResolver)}
	 * @return {@link Instruction}
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public Instruction<?, ?> getInstanceOfInstruction(ToolResolver toolResolver)
			throws NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException;

	/**
	 * <p>
	 * 	Get the number of {@link Instruction} has been issued
	 * </p>
	 * 
	 * @return {@code int}
	 */
	public int numberOfInstructionIssued();

	/**
	 *<p>
	 *	Return the {@link Instruction} instance issued by this {@link InstructionClassOmitter}
	 *</p>
	 * 
	 * @param instruction The {@link Instruction} instance to be returned.
	 */
	public void returnInstanceOfInstruction(Instruction<?, ?> instruction);

}
