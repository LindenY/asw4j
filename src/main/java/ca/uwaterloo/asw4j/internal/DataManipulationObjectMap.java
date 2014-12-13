package ca.uwaterloo.asw4j.internal;

import java.util.concurrent.ConcurrentHashMap;

import ca.uwaterloo.asw4j.Balancer;
import ca.uwaterloo.asw4j.Combiner;
import ca.uwaterloo.asw4j.reflection.TypeToken;

public class DataManipulationObjectMap {
	
	private ConcurrentHashMap<TypeToken<?>, Combiner<?>> combiners;
	private ConcurrentHashMap<TypeToken<?>, Balancer<?>> balancers;

	public DataManipulationObjectMap() {
		combiners = new ConcurrentHashMap<TypeToken<?>, Combiner<?>>();
		balancers = new ConcurrentHashMap<TypeToken<?>, Balancer<?>>();
	}
	
	public void registerBalancer(TypeToken<?> typeToken,
			Balancer<?> balancer) {
		balancers.put(typeToken, balancer);
	}

	public void registerCombiner(TypeToken<?> typeToken,
			Combiner<?> combiner) {
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
