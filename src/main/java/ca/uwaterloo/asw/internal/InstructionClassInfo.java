package ca.uwaterloo.asw.internal;

import java.util.List;

import ca.uwaterloo.asw.Instruction;
import ca.uwaterloo.asw.reflection.TypeToken;

public interface InstructionClassInfo {

	public List<Class<? extends Instruction<?, ?>>> getDependencies();

	public List<TypeToken<?>> getRequireDatas();

	public TypeToken<?> getProduceData();

	public boolean isSupportAsync();

	public boolean isSupportSingleton();
	
}
