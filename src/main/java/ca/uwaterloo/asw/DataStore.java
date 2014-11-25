package ca.uwaterloo.asw;

import java.util.Collection;
import java.util.List;

public interface DataStore {

	public boolean add(Object obj);
	
	public boolean add(Object obj, String name);

	public boolean addAll(Collection<Object> objs);

	public boolean contain(Object obj);

	public <T> boolean contain(Class<T> clazz);

	public <T> boolean contain(Class<T> clazz, String name);
	
	public boolean contain(String name);
	
	public <T> T get(Class<T> clazz);
	
	public <T> T get(Class<T> clazz, String name);
	
	public DataNode get(String name);
	
	public <T> List<T> getAll(Class<T> clazz);
	
	public <T> List<T> getAll(Class<T> clazz, String name);
	
	public List<DataNode> getAll(String name);

	public boolean remove(Object obj);

	public boolean mark(Object obj);
}
