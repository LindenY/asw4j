package ca.uwaterloo.asw;

import ca.uwaterloo.asw.reflection.TypeToken;

public interface DataManipulationRegister {

	public <T> void registerBalancer(TypeToken<T> typeToken, Balancer<T> balancer);
	
	public <T> void registerCombiner(TypeToken<T> typeToken, Combiner<T> combiner);
	
}
