package ca.uwaterloo.asw;

import java.lang.reflect.Type;

import ca.uwaterloo.asw.reflection.TypeToken;

public abstract class AbstractWorkerManager<T> implements WorkerManager<T>,
		InstructionClassRegister, DataManipulationRegister {

	protected AbstractDataStore dataStore;
	protected AbstractInstructionResolver instructionResolver;

	public AbstractWorkerManager(AbstractDataStore dataStore,
			AbstractInstructionResolver instructionResolver) {
		this.dataStore = dataStore;
		this.instructionResolver = instructionResolver;
	}

	public DataStore getDataStore() {
		return dataStore;
	}

	public InstructionResolver getInstructionResolver() {
		return instructionResolver;
	}

	public void setDataStore(AbstractDataStore dataStore) {
		this.dataStore = dataStore;
	}

	public void setInstructionResolver(
			AbstractInstructionResolver instructionResolver) {
		this.instructionResolver = instructionResolver;
	}

	public <T> void registerBalancer(TypeToken<T> typeToken,
			Balancer<T> balancer) {
		dataStore.registerBalancer(typeToken, balancer);
	}

	public <T> void registerCombiner(TypeToken<T> typeToken,
			Combiner<T> combiner) {
		dataStore.registerCombiner(typeToken, combiner);
	}

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass) {
		instructionResolver.registerInstructionClass(instructionClass);
	}

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String produceDataName) {
		instructionResolver.registerInstructionClass(instructionClass,
				produceDataName);
	}

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes) {
		instructionResolver.registerInstructionClass(instructionClass,
				requireDataNames, requireDataTypes);
	}

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes,
			String produceDataName) {
		instructionResolver.registerInstructionClass(instructionClass,
				requireDataNames, requireDataTypes, produceDataName);
	}

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes,
			String produceDataName,
			Class<? extends Instruction<?, ?>>[] dependencies,
			boolean supportSingleton, boolean supportAsync) {
		instructionResolver.registerInstructionClass(instructionClass,
				requireDataNames, requireDataTypes, produceDataName);
	}

}
