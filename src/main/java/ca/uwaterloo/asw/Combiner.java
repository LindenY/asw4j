package ca.uwaterloo.asw;

import java.util.Collection;

public interface Combiner<T> {

	public T combine(Collection<T> collection);
	
}
