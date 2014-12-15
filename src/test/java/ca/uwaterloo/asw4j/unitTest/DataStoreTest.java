package ca.uwaterloo.asw4j.unitTest;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ca.uwaterloo.asw4j.ConcurrentMapDataStore;
import ca.uwaterloo.asw4j.DataStore;
import ca.uwaterloo.asw4j.reflection.TypeToken;

@RunWith(Parameterized.class)
public class DataStoreTest {

	@SuppressWarnings("unchecked")
	@Parameterized.Parameters
	public static Collection<Constructor<? extends DataStore>[]> testImplementations() throws SecurityException, NoSuchMethodException {
		
		Constructor<? extends DataStore> concurrentMapDataStoreConstructor = ConcurrentMapDataStore.class.getDeclaredConstructor();
		Constructor<? extends DataStore>[] constructors = new Constructor[] {concurrentMapDataStoreConstructor};
		
		List<Constructor<? extends DataStore>[]> testImplements = new ArrayList<Constructor<? extends DataStore>[]>();
		testImplements.add(constructors);
		return testImplements;
	}
	
	private Constructor<? extends DataStore> dataStoreConstructor;

	public DataStoreTest(Constructor<? extends DataStore> dataStoreConstructor) {
		this.dataStoreConstructor = dataStoreConstructor;
	}
	
	public DataStore getDataStore() {
		DataStore dataStore = null;
		
		try {
			dataStore = dataStoreConstructor.newInstance();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return dataStore;
	}
	
	@Test
	public void testAddContainGetAndRemove() {
		DataStore dataStore = getDataStore();
		
		//Test Objects
		String str0 = "String0";
		String str1 = "String1";
		String str2 = "String2";
		String str3 = "String3";
		Long long0 = 0l;
		Long long1 = 1l;
		Long long2 = 2l;
		Long long3 = 3l;
		Date date0 = new Date();
		Date date1 = new Date();
		Date date2 = new Date();
		Date date3 = new Date();
		List<String> list = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();
		Set<String> set = new HashSet<String>();
		
		//Test Add
		dataStore.add(str0);
		dataStore.add(str1);
		dataStore.add(str2, "String");
		dataStore.add(str3, "String");
		dataStore.add(long0);
		dataStore.add(long1);
		dataStore.add(long2, "Long");
		dataStore.add(long3, "Long");
		dataStore.add(date0);
		dataStore.add(date1);
		dataStore.add(date2, "Date");
		dataStore.add(date3, "Date");
		dataStore.add(list);
		dataStore.add(map);
		dataStore.add(set);
		
		// Test Contain
		assertTrue(dataStore.contain(String.class));
		assertTrue(dataStore.contain(TypeToken.get(String.class, "String")));
		assertTrue(dataStore.contain(String.class));
		assertTrue(dataStore.contain(TypeToken.get(String.class, "String")));
		assertTrue(dataStore.contain(String.class));
		assertTrue(dataStore.contain(TypeToken.get(String.class, "String")));
		assertTrue(dataStore.contain(ArrayList.class));
		assertTrue(dataStore.contain(HashMap.class));
		assertTrue(dataStore.contain(HashSet.class));
		
		//Test GetAndRemove
		assertTrue(testEquality(dataStore.getAndRemove(String.class), str0, str1));
		assertTrue(testEquality(dataStore.getAndRemove(String.class), str0, str1));
		assertTrue(testEquality(dataStore.getAndRemove(String.class), null));
		assertTrue(testEquality(dataStore.getAndRemove(String.class, "String"), str2, str3));
		assertTrue(testEquality(dataStore.getAndRemove(String.class, "String"), str2, str3));
		assertTrue(testEquality(dataStore.getAndRemove(String.class, "String"), null));
		
		assertTrue(testEquality(dataStore.getAndRemove(Long.class), long0, long1));
		assertTrue(testEquality(dataStore.getAndRemove(Long.class), long0, long1));
		assertTrue(testEquality(dataStore.getAndRemove(Long.class), null));
		assertTrue(testEquality(dataStore.getAndRemove(Long.class, "Long"), long2, long3));
		assertTrue(testEquality(dataStore.getAndRemove(Long.class, "Long"), long2, long3));
		assertTrue(testEquality(dataStore.getAndRemove(Long.class), null));
		
		assertTrue(testEquality(dataStore.getAndRemove(Date.class), date0, date1));
		assertTrue(testEquality(dataStore.getAndRemove(Date.class), date0, date1));
		assertTrue(testEquality(dataStore.getAndRemove(Date.class), null));
		assertTrue(testEquality(dataStore.getAndRemove(Date.class, "Date"), date2, date3));
		assertTrue(testEquality(dataStore.getAndRemove(Date.class, "Date"), date2, date3));
		assertTrue(testEquality(dataStore.getAndRemove(Date.class), null));
		
		assertTrue(testEquality(dataStore.getAndRemove(ArrayList.class), list));
		assertTrue(testEquality(dataStore.getAndRemove(ArrayList.class), null));
		assertTrue(testEquality(dataStore.getAndRemove(HashMap.class), map));
		assertTrue(testEquality(dataStore.getAndRemove(HashMap.class), null));
		assertTrue(testEquality(dataStore.getAndRemove(HashSet.class), set));
		assertTrue(testEquality(dataStore.getAndRemove(HashSet.class), null));
	}
	
	private boolean testEquality(Object... objs) {
		for (int i=1; i<objs.length; i++) {
			if (objs[0] == objs[i]) {
				return true;
			}
		}
		return false;
	}
	
	@Test
	public void testContainAll() {
		
		DataStore dataStore = getDataStore();
		
		//Test Objects
		String str0 = "String0";
		String str1 = "String1";
		Long long0 = 0l;
		Long long1 = 1l;
		Date date0 = new Date();
		Date date1 = new Date();
		List<String> list = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();
		Set<String> set = new HashSet<String>();
		
		// Add Test Objects
		dataStore.add(str0);
		dataStore.add(str1, "String");
		dataStore.add(long0);
		dataStore.add(long1, "Long");
		dataStore.add(date0);
		dataStore.add(date1, "Date");
		dataStore.add(list);
		dataStore.add(map);
		dataStore.add(set);
		
		//Test ContainAll
		List<TypeToken<?>> testTokens0 = new ArrayList<TypeToken<?>>();
		testTokens0.add(TypeToken.get(String.class));
		testTokens0.add(TypeToken.get(String.class, "String"));
		testTokens0.add(TypeToken.get(Long.class));
		testTokens0.add(TypeToken.get(Long.class, "Long"));
		testTokens0.add(TypeToken.get(Date.class));
		testTokens0.add(TypeToken.get(Date.class, "Date"));
		testTokens0.add(TypeToken.get(ArrayList.class));
		testTokens0.add(TypeToken.get(HashMap.class));
		testTokens0.add(TypeToken.get(HashSet.class));
		assertTrue(dataStore.containAll(testTokens0));
		
		List<TypeToken<?>> testTokens1 = new ArrayList<TypeToken<?>>();
		testTokens1.add(TypeToken.get(String.class));
		testTokens1.add(TypeToken.get(Long.class));
		testTokens1.add(TypeToken.get(Date.class));
		testTokens1.add(TypeToken.get(ArrayList.class));
		testTokens1.add(TypeToken.get(HashMap.class));
		testTokens1.add(TypeToken.get(HashSet.class));
		assertTrue(dataStore.containAll(testTokens1));
		
		List<TypeToken<?>> testTokens2 = new ArrayList<TypeToken<?>>();
		testTokens2.add(TypeToken.get(String.class, "String"));
		testTokens2.add(TypeToken.get(Long.class, "Long"));
		testTokens2.add(TypeToken.get(Date.class, "Date"));
		testTokens2.add(TypeToken.get(ArrayList.class));
		testTokens2.add(TypeToken.get(HashMap.class));
		testTokens2.add(TypeToken.get(HashSet.class));
		assertTrue(dataStore.containAll(testTokens2));
		
		dataStore.getAndRemove(String.class);
		dataStore.getAndRemove(Long.class);
		dataStore.getAndRemove(Date.class);
		assertFalse(dataStore.containAll(testTokens0));
		assertFalse(dataStore.containAll(testTokens1));
		
		dataStore.getAndRemove(String.class, "String");
		dataStore.getAndRemove(Long.class,"Long");
		dataStore.getAndRemove(Date.class, "Date");
		assertFalse(dataStore.containAll(testTokens0));
		assertFalse(dataStore.containAll(testTokens1));
		
		dataStore.getAndRemove(ArrayList.class);
		dataStore.getAndRemove(HashMap.class);
		dataStore.getAndRemove(HashSet.class);
		assertFalse(dataStore.containAll(testTokens0));
		assertFalse(dataStore.containAll(testTokens2));
	}
	
	public void testGetAndRemoveAll() {
		
	}
	
	public void testSize() {
		
	}
	
	public void testCombineAndGet() {
		
	}
}
