package ca.uwaterloo.asw4j;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.uwaterloo.asw4j.DataNode;
import ca.uwaterloo.asw4j.reflection.TypeToken;

public interface DataStore {

	public void add(Object obj);
	
	public void add(Object obj, String name);

	public void addAll(List<?> objs);
	
	public void addAll(List<?> objs, String name);

	public boolean contain(Class<?> type);

	public boolean contain(Object obj);

	public boolean contain(TypeToken<?> typeToken);
	
	public boolean containAll(List<TypeToken<?>> typeTokens);
	
	public Map<TypeToken<?>, List<Object>> getAllValues();
	
	public <T> T getAndRemove(Class<T> type);
	
	public <T> T getAndRemove(Class<T> type, String name);
	
	public <T> T getAndRemove(TypeToken<T> typeToken);
	
	public DataNode getAndRemoveAll(List<TypeToken<?>> typeTokens);
	
	public Set<TypeToken<?>> keySet();
	
	public int size();
	
	public Collection<List<Object>> values();
	
	public <T> T combineAndGet(TypeToken<T> typeToken);
	
	public void registerBalancer(TypeToken<?> typeToken, Balancer<?> balancer);
	
	public void registerCombiner(TypeToken<?> typeToken, Combiner<?> combiner);
}
