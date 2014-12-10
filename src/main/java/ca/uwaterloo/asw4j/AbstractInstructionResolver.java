package ca.uwaterloo.asw4j;

import java.util.concurrent.ConcurrentHashMap;

import ca.uwaterloo.asw4j.internal.InstructionClassNode;

public abstract class AbstractInstructionResolver implements
		InstructionResolver, InstructionClassRegister {

	protected final boolean enablePooling;
	
	protected ConcurrentHashMap<Class<? extends Instruction<?, ?>>, InstructionClassNode>
		instructionClassMap;
	
	protected ToolResolver toolResolver;
	protected DataStore dataStore;
	
	public AbstractInstructionResolver(DataStore dataStore,
			ToolResolver toolResolver, boolean enablePooling) {
		this.toolResolver = toolResolver;
		this.dataStore = dataStore;
		this.enablePooling = enablePooling;
		
		instructionClassMap = 
				new ConcurrentHashMap<Class<? extends Instruction<?, ?>>, InstructionClassNode>();
	}

	public void afterInstructionExecution(Instruction<?, ?> instruction, Throwable throwed) {
		InstructionClassNode node = getInstructionClassNode(instruction);
		if (node != null) {
			node.returnInstanceOfInstruction(instruction);
		}
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
	
	public boolean isPoolingEnabled() {
		return enablePooling;
	}
	
	protected InstructionClassNode getInstructionClassNode(Instruction<?, ?> instruction) {
		return instructionClassMap.get(instruction.getClass());
	}
	
	protected void putIntructionClassNode(InstructionClassNode instructionNode) {
		instructionClassMap.put(instructionNode.getInstructionClass(), instructionNode);
	}
}

