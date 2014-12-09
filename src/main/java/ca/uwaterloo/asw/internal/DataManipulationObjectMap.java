package ca.uwaterloo.asw.internal;

import java.util.concurrent.ConcurrentHashMap;

import ca.uwaterloo.asw.Balancer;
import ca.uwaterloo.asw.Combiner;
import ca.uwaterloo.asw.DataManipulationRegister;
import ca.uwaterloo.asw.reflection.TypeToken;

public class DataManipulationObjectMap implements DataManipulationRegister {
	
	private ConcurrentHashMap<TypeToken<?>, Combiner<?>> combiners;
	private ConcurrentHashMap<TypeToken<?>, Balancer<?>> balancers;

	public DataManipulationObjectMap() {
		combiners = new ConcurrentHashMap<TypeToken<?>, Combiner<?>>();
		balancers = new ConcurrentHashMap<TypeToken<?>, Balancer<?>>();
	}
	
	public <T> void registerBalancer(TypeToken<T> typeToken,
			Balancer<T> balancer) {
		balancers.put(typeToken, balancer);
	}

	public <T> void registerCombiner(TypeToken<T> typeToken,
			Combiner<T> combiner) {
		combiners.put(typeToken, combiner);
	}

	@SuppressWarnings("unchecked")
	public <T> Combiner<T> getCombiner(TypeToken<T> typeToken) {
		return (Combiner<T>) combiners.get(typeToken);
	}
	
	@SuppressWarnings("unchecked")
	public <T> Balancer<T> getBalancer(TypeToken<T> typeToken) {
		return (Balancer<T>) balancers.get(typeToken);
	}
}
