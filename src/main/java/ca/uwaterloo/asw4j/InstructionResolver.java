package ca.uwaterloo.asw4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

public interface InstructionResolver {
	
	public void setDataStore(DataStore dataStore);
	
	public DataStore getDataStore();

	public void afterInstructionExecution(Instruction<?, ?> instruction,
			Throwable throwed);

	public void beforeInstructionExecution(Instruction<?, ?> instruction);

	public Instruction<?, ?> resolveInstruction() throws SecurityException,
			IllegalArgumentException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException;

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
