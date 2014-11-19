package ca.uwaterloo.asw;

public abstract class WorkerManager<T> {

	protected int coreNumWorkers;
	protected int maxNumWorkers;
	
	protected ToolResolver toolResolver;
	protected Combiner<T> combiner;
	protected DataNodeStore dataNodeStore;
	protected InstructionResolver instructionResolver;
	
	public WorkerManager(int coreNumWorkers,
						 int maxNumWorkers,
						 ToolResolver toolResolver,
						 Combiner<T> combiner,
						 DataNodeStore dataNodeStore,
						 InstructionResolver instructionResolver) {
		
		this.coreNumWorkers = coreNumWorkers;
		this.maxNumWorkers = maxNumWorkers;
		
		this.toolResolver = toolResolver;
		this.combiner = combiner;
		this.dataNodeStore = dataNodeStore;
		this.instructionResolver = instructionResolver;
	}

	public int getCoreNumWorkers() {
		return coreNumWorkers;
	}

	public void setCoreNumWorkers(int coreNumWorkers) {
		this.coreNumWorkers = coreNumWorkers;
	}

	public int getMaxNumWorkers() {
		return maxNumWorkers;
	}

	public void setMaxNumWorkers(int maxNumWorkers) {
		this.maxNumWorkers = maxNumWorkers;
	}

	public ToolResolver getToolResolver() {
		return toolResolver;
	}

	public void setToolResolver(ToolResolver toolResolver) {
		this.toolResolver = toolResolver;
	}

	public Combiner<T> getCombiner() {
		return combiner;
	}

	public void setCombiner(Combiner<T> combiner) {
		this.combiner = combiner;
	}

	public DataNodeStore getDataNodeStore() {
		return dataNodeStore;
	}

	public void setDataNodeStore(DataNodeStore dataNodeStore) {
		this.dataNodeStore = dataNodeStore;
	}

	public InstructionResolver getInstructionResolver() {
		return instructionResolver;
	}

	public void setInstructionResolver(InstructionResolver instructionResolver) {
		this.instructionResolver = instructionResolver;
	}
	
	abstract public T start();
	abstract public void shutDown();
	abstract public void shutDownNow();
	abstract public void awaitShutDown(int timeOut) throws InterruptedException;
	
}
