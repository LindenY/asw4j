package ca.uwaterloo.asw4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import ca.uwaterloo.asw4j.reflection.TypeToken;

public class DCGInstructionResolver extends AbstractDependencyInstructionResolver {

	public DCGInstructionResolver(DataStore dataStore) {
		this(dataStore, null, false);
	}
	
	public DCGInstructionResolver(DataStore dataStore, ToolResolver toolResolver) {
		this(dataStore, toolResolver, false);
	}
	
	public DCGInstructionResolver(DataStore dataStore, ToolResolver toolResolver, boolean enablePooling) {
		super(dataStore, toolResolver, enablePooling);
	}

	@Override
	public void afterInstructionExecution(Instruction<?, ?> instruction,
			Throwable throwed) {
		Object result = instruction.getResult();
		if (result instanceof DataNode) {
			DataNode rdn = (DataNode) result;
			
			for (TypeToken<?> tk : rdn.keySet()) {
				dataStore.addAll(rdn.getAll(tk), tk.getName());
			}
		} else {
			super.afterInstructionExecution(instruction, throwed);
		}
	}

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes,
			String produceDataName, boolean supportSingleton,
			Class<? extends Instruction<?, ?>>[] dependencies,
			boolean supportAsync) {
		
		
		
	}

	public Instruction<?, ?> resolveInstruction() throws SecurityException,
			IllegalArgumentException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
