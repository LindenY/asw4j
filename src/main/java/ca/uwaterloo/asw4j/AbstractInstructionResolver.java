package ca.uwaterloo.asw4j;

import java.util.concurrent.ConcurrentHashMap;

import ca.uwaterloo.asw4j.internal.InstructionClassNode;

/**
 * <p>
 * An abstract class of {@link InstructionResolver} and {@link InstructionClassRegister}.
 * </p>
 * <p>
 * {@link AbstractInstructionResolver} defines the basic constructors and fields
 * of {@link InstructionResolver}.
 * </p>
 * 
 * @author Desmond Lin
 * @since 1.0.0
 */
public abstract class AbstractInstructionResolver implements
		InstructionResolver {

	protected final boolean enablePooling;

	protected ConcurrentHashMap<Class<? extends Instruction<?, ?>>, InstructionClassNode> instructionClassMap;

	protected ToolResolver toolResolver;
	protected DataStore dataStore;
	
	protected boolean prepared;

	public AbstractInstructionResolver(DataStore dataStore,
			ToolResolver toolResolver, boolean enablePooling) {
		this.toolResolver = toolResolver;
		this.dataStore = dataStore;
		this.enablePooling = enablePooling;

		instructionClassMap = new ConcurrentHashMap<Class<? extends Instruction<?, ?>>, InstructionClassNode>();
	}

	public void afterInstructionExecution(Instruction<?, ?> instruction,
			Throwable throwed) {
		
		InstructionClassNode node = getInstructionClassNode(instruction);
		if (node != null) {
			dataStore.add(instruction.getResult(), node.getProduceData().getName());
			node.returnInstanceOfInstruction(instruction);
		}
	}

	public void beforeInstructionExecution(Instruction<?, ?> instruction) {
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

	public int numberOfRegisteredInstruction() {
		return instructionClassMap.size();
	}
	

	// TODO : Add comments
	/**
	 * 
	 * @return
	 */
	public boolean isPoolingEnabled() {
		return enablePooling;
	}
	
	/**
	 * 
	 */
	protected void prepare() {
		
	}
	
	/**
	 * 
	 * @param instruction
	 * @return
	 */
	protected InstructionClassNode getInstructionClassNode(
			Instruction<?, ?> instruction) {
		return instructionClassMap.get(instruction.getClass());
	}

	/**
	 * 
	 * @param instructionNode
	 */
	protected void putIntructionClassNode(InstructionClassNode instructionNode) {

		InstructionClassNode node = instructionClassMap.put(
				instructionNode.getInstructionClass(), instructionNode);

		if (node != null) {
			throw new IllegalArgumentException(instructionNode
					.getInstructionClass().getName()
					+ " is already registered.");
		}
	}
}
