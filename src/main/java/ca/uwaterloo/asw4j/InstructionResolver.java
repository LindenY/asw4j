package ca.uwaterloo.asw4j;

import java.lang.reflect.InvocationTargetException;

public interface InstructionResolver {

	public void afterInstructionExecution(Instruction<?, ?> instruction,
			Throwable throwed);

	public void beforInstructionExecution(Instruction<?, ?> instruction);

	public Instruction<?, ?> resolveInstruction() throws SecurityException,
			IllegalArgumentException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException;

}
