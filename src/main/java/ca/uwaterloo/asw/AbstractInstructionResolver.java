package ca.uwaterloo.asw;

public abstract class AbstractInstructionResolver implements
		InstructionResolver, InstructionClassRegister {

	protected ToolResolver toolResolver;
	protected DataStore dataStore;

	public AbstractInstructionResolver(DataStore dataStore) {
		this(null, dataStore);
	}
	
	public AbstractInstructionResolver(ToolResolver toolResolver,
			DataStore dataStore) {
		this.toolResolver = toolResolver;
		this.dataStore = dataStore;
	}

	public void afterInstructionExecution(Instruction<?, ?> instruction, Throwable throwed) {
	}

	public void beforInstructionExecution(Instruction<?, ?> instruction) {
	}

	public DataStore getDataStore() {
		return dataStore;
	}

	public ToolResolver getToolResolver() {
		return toolResolver;
	}

	public void setDataStore(DataStore dataStore) {
		this.dataStore = dataStore;
	}

	public void setToolResolver(ToolResolver toolResolver) {
		this.toolResolver = toolResolver;
	}
}

