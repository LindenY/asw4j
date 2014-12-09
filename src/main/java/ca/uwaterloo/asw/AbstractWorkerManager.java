package ca.uwaterloo.asw;

public abstract class AbstractWorkerManager<T> implements WorkerManager<T> {

	protected DataStore dataStore;
	protected InstructionResolver instructionResolver;
	
	public AbstractWorkerManager(DataStore dataStore, InstructionResolver instructionResolver) {
		this.dataStore = dataStore;
		this.instructionResolver = instructionResolver;
	}

	public DataStore getDataStore() {
		return dataStore;
	}

	public void setDataStore(DataStore dataStore) {
		this.dataStore = dataStore;
	}

	public InstructionResolver getInstructionResolver() {
		return instructionResolver;
	}

	public void setInstructionResolver(InstructionResolver instructionResolver) {
		this.instructionResolver = instructionResolver;
	}
	
	public void registerInstruction(Class<? extends Instruction<?, ?>> instructionClass) {
		instructionResolver.register(instructionClass);
	}
	
}
