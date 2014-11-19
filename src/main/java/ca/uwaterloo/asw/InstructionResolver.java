package ca.uwaterloo.asw;

public interface InstructionResolver {

	public void registerInstruction(String[] requiredNames, Class<Instruction> instructionClass);
	public Instruction resolveInstruction();
	public Instruction getInstruction(String[] dataNodeNames);
	
	public void beforInstructionExecution(Instruction instruction);
	public void afterInstructionExecution(Instruction instruction);
}
