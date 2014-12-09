package ca.uwaterloo.asw.internal;

import java.lang.reflect.InvocationTargetException;

import ca.uwaterloo.asw.Instruction;
import ca.uwaterloo.asw.ToolResolver;

public interface InstructionClassOmitter {

	public Instruction<?, ?> getInstanceOfInstruction(ToolResolver toolResolver)
			throws NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException;

	public int numberOfInstructionIssued();

	public void returnInstanceOfInstruction(Instruction<?, ?> instruction);

}
