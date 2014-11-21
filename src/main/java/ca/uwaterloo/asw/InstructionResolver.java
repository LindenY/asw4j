package ca.uwaterloo.asw;

public interface InstructionResolver {

	public void registerInstruction(String[] requiredNames,  Class instructionClass);
	public int numberOfRegisteredInstruction();
	public Instruction resolveInstruction();
	public Instruction getInstruction(String[] dataNodeNames);
	
	public void beforInstructionExecution(Instruction instruction);
	public void afterInstructionExecution(Instruction instruction);
}
