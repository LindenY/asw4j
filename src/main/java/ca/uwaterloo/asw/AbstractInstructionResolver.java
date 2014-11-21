package ca.uwaterloo.asw;

public abstract class AbstractInstructionResolver implements InstructionResolver {

	protected ToolResolver toolResolver;
	protected DataNodeStore dataNodeStore;
	
	public AbstractInstructionResolver(ToolResolver toolResolver, DataNodeStore dataNodeStore) {
		this.toolResolver = toolResolver;
		this.dataNodeStore = dataNodeStore;
	}

	public DataNodeStore getDataNodeStore() {
		return dataNodeStore;
	}

	public void setDataNodeStore( DataNodeStore dataNodeStore) {
		this.dataNodeStore = dataNodeStore;
	}
	
}
