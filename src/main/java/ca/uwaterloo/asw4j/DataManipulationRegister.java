package ca.uwaterloo.asw4j;

import ca.uwaterloo.asw4j.reflection.TypeToken;

public interface DataManipulationRegister {

	public void registerBalancer(TypeToken<?> typeToken, Balancer<?> balancer);
	
	public void registerCombiner(TypeToken<?> typeToken, Combiner<?> combiner);
	
}
