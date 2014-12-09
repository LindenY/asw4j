package ca.uwaterloo.asw.internal;

import java.util.List;

import ca.uwaterloo.asw.Instruction;
import ca.uwaterloo.asw.reflection.TypeToken;

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
