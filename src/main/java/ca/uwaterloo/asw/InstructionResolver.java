package ca.uwaterloo.asw;

public interface InstructionResolver {

	public void register(String[] requiredDataNames,
			Class<?>[] requiredDataTypes, String producedDataName,
			Class<?> producedDataType,
			Class<? extends Instruction<?, ?>> instructionClass);

	public void register(Class<?>[] requiredDataTypes,
			Class<?> producedDataType,
			Class<? extends Instruction<?, ?>> instructionClass);

	public void register(String[] requiredDataNames, String producedDataName,
			Class<? extends Instruction<?, ?>> instructionClass);

	public void register(String[] requiredDataNames,
			Class<? extends Instruction<?, ?>> instructionClass);

	public void register(String producedDataName,
			Class<? extends Instruction<?, ?>> instructionClass);

	public void register(Class<? extends Instruction<?, ?>> instructionClass);

	public int numberOfRegisteredInstruction();

	public Instruction<?, ?> resolveInstruction();

	public void beforInstructionExecution(Instruction<?, ?> instruction);

	public void afterInstructionExecution(Instruction<?, ?> instruction);
}
