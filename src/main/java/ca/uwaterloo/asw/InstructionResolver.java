package ca.uwaterloo.asw;

public interface InstructionResolver {

	public void register(String[] requireDataNames,
			Class<?>[] requireDataTypes, String produceDataName,
			Class<?> produceDataType,
			Class<? extends Instruction<?, ?>> instructionClass);

	public void register(Class<?>[] requireDataTypes,
			Class<?> produceDataType,
			Class<? extends Instruction<?, ?>> instructionClass);

	public void register(String[] requireDataNames, String produceDataName,
			Class<? extends Instruction<?, ?>> instructionClass);

	public void register(String[] requireDataNames,
			Class<? extends Instruction<?, ?>> instructionClass);

	public void register(String produceDataName,
			Class<? extends Instruction<?, ?>> instructionClass);

	public void register(Class<? extends Instruction<?, ?>> instructionClass);

	public int numberOfRegisteredInstruction();

	public Instruction<?, ?> resolveInstruction();

	public void beforInstructionExecution(Instruction<?, ?> instruction);

	public void afterInstructionExecution(Instruction<?, ?> instruction);
}
