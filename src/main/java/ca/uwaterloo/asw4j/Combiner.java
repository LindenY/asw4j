package ca.uwaterloo.asw4j;

import java.util.Collection;

public interface Combiner<T> {

	public T combine(Collection<T> collection);
	
}
