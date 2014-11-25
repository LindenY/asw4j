package ca.uwaterloo.asw;

public abstract class AbstractInstructionResolver implements InstructionResolver {

	protected ToolResolver toolResolver;
	protected DataStore dataNodeStore;
	
	public AbstractInstructionResolver(ToolResolver toolResolver, DataStore dataNodeStore) {
		this.toolResolver = toolResolver;
		this.dataNodeStore = dataNodeStore;
	}

	public DataStore getDataNodeStore() {
		return dataNodeStore;
	}

	public void setDataNodeStore( DataStore dataNodeStore) {
		this.dataNodeStore = dataNodeStore;
	}
	
}
