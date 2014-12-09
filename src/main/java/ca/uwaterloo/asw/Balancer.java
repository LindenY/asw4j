package ca.uwaterloo.asw;

import java.util.Collection;

public interface Balancer<T> {

	public Collection<T> balance(Collection<T> collection);
	
}
