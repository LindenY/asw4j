package ca.uwaterloo.asw4j.internal;

import java.lang.reflect.InvocationTargetException;

import ca.uwaterloo.asw4j.Instruction;
import ca.uwaterloo.asw4j.ToolResolver;

public interface InstructionClassOmitter {

	public Instruction<?, ?> getInstanceOfInstruction(ToolResolver toolResolver)
			throws NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException;

	public int numberOfInstructionIssued();

	public void returnInstanceOfInstruction(Instruction<?, ?> instruction);

}
