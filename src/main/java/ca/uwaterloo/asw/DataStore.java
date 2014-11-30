package ca.uwaterloo.asw;

import java.util.List;

import ca.uwaterloo.asw.DataNode;
import ca.uwaterloo.asw.reflection.TypeToken;

public interface DataStore {

	public void add(Object obj);
	
	public void add(Object obj, String name);

	public void addAll(List<Object> objs);
	
	//public void addAll(List<Object> objs, List<TypeToken<?>> typeTokens);

	public boolean contain(Object obj);

	public boolean contain(Class<?> type);

	public boolean contain(TypeToken<?> typeToken);
	
	public boolean containAll(List<TypeToken<?>> typeTokens);
	
	public <T> T getAndRemove(Class<T> type);
	
	public <T> T getAndRemove(Class<T> type, String name);
	
	public <T> T getAndRemove(TypeToken<T> typeToken);
	
	public DataNode getAndRemoveAll(List<TypeToken<?>> typeTokens);

}
