package ca.uwaterloo.asw4j.internal;

import java.util.List;

import ca.uwaterloo.asw4j.Instruction;
import ca.uwaterloo.asw4j.reflection.TypeToken;

public interface InstructionClassInfo {

	public TypeToken<?> getProduceData();

	public List<TypeToken<?>> getRequireDatas();

	public boolean isSupportSingleton();
	
	public STATE getState();
	
	public static enum STATE {
		Ready,
		Running,
		Blocking,
		Terminated
	}
}
