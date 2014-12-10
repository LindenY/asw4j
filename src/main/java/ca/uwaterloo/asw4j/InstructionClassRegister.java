package ca.uwaterloo.asw4j;

import java.lang.reflect.Type;

public interface InstructionClassRegister {
	
	public int numberOfRegisteredInstruction();

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass);

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String produceDataName);

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes);

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes,
			String produceDataName);

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes,
			String produceDataName,
			boolean supportSingleton);
	
}
