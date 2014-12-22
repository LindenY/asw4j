package ca.uwaterloo.asw4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

public class DCGInstructionResolver extends AbstractInstructionResolver {

	public DCGInstructionResolver(DataStore dataStore,
			ToolResolver toolResolver, boolean enablePooling) {
		super(dataStore, toolResolver, enablePooling);
		// TODO Auto-generated constructor stub
	}

	public Instruction<?, ?> resolveInstruction() throws SecurityException,
			IllegalArgumentException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {
		// TODO Auto-generated method stub
		return null;
	}

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass) {
		// TODO Auto-generated method stub

	}

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String produceDataName) {
		// TODO Auto-generated method stub

	}

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes) {
		// TODO Auto-generated method stub

	}

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes,
			String produceDataName) {
		// TODO Auto-generated method stub

	}

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes,
			String produceDataName, boolean supportSingleton) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterInstructionExecution(Instruction<?, ?> instruction,
			Throwable throwed) {
		// TODO Auto-generated method stub
		super.afterInstructionExecution(instruction, throwed);
	}
	
	

}
