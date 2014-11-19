package ca.uwaterloo.asw;

public interface InstructionResolver {

	public <T extends Instruction> void registerInstruction(String[] requiredNames,  Class<T> instructionClass);
	public int numberOfRegisteredInstruction();
	public Instruction resolveInstruction();
	public Instruction getInstruction(String[] dataNodeNames);
	
	public void beforInstructionExecution(Instruction instruction);
	public void afterInstructionExecution(Instruction instruction);
}
