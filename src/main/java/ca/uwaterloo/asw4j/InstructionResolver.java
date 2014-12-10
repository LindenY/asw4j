package ca.uwaterloo.asw4j;

public interface InstructionResolver {

	public void afterInstructionExecution(Instruction<?, ?> instruction, Throwable throwed);

	public void beforInstructionExecution(Instruction<?, ?> instruction);

	public Instruction<?, ?> resolveInstruction();
	
}
