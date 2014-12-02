package ca.uwaterloo.asw;

import java.lang.reflect.Type;

public interface InstructionResolver {

	public void register(
			String[] requireDataNames,
			Type[] requireDataTypes, 
			String produceDataName,
			Class<? extends Instruction<?, ?>> instructionClass);

	public void register(Class<? extends Instruction<?, ?>> instructionClass);

	public int numberOfRegisteredInstruction();

	public Instruction<?, ?> resolveInstruction();

	public void beforInstructionExecution(Instruction<?, ?> instruction);

	public void afterInstructionExecution(Instruction<?, ?> instruction);
}
