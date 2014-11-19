package ca.uwaterloo.asw;

public interface InstructionResolver {

	public void registerInstruction(String[] requiredNames, Class instructionClass);
	public Instruction resolveInstruction();
	public Instruction getInstruction(String[] dataNodeNames);
	
}
