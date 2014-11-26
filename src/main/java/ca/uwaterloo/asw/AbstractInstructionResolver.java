package ca.uwaterloo.asw;

public abstract class AbstractInstructionResolver implements InstructionResolver {

	protected ToolResolver toolResolver;
	protected DataStore dataStore;
	
	public AbstractInstructionResolver(ToolResolver toolResolver, DataStore dataStore) {
		this.toolResolver = toolResolver;
		this.dataStore = dataStore;
	}

	public DataStore getDataStore() {
		return dataStore;
	}

	public void setDataStore(DataStore dataStore) {
		this.dataStore = dataStore;
	}
	
	protected class InstructionNode {
		
		// TODO : finish this
	}
}
