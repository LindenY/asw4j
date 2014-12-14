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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ca.uwaterloo.asw4j.ConcurrentMapDataStore;
import ca.uwaterloo.asw4j.DataStore;
import ca.uwaterloo.asw4j.reflection.TypeToken;

@RunWith(Parameterized.class)
public class DataStoreTest {

	@Parameterized.Parameters
	public static Collection<Object[]> testImplementations() throws SecurityException, NoSuchMethodException {
		
		Constructor<? extends DataStore> concurrentMapDataStoreConstructor = ConcurrentMapDataStore.class.getDeclaredConstructor();
		Constructor[] constructors = new Constructor[] {concurrentMapDataStoreConstructor};
		
		List<Constructor<DataStore>[]> testImplements = new ArrayList<Constructor<DataStore>[]>();
		testImplements.add(constructors);
		return testImplementations();
	}
	
	private Constructor<DataStore> dataStoreConstructor;
	
	@Before
	public void testSetup(Constructor dataStoreConstructor) {
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
		
		
	}
	
	public void testContainAll() {
		
	}
	
	public void testGetAndRemoveAll() {
		
	}
	
	public void testSize() {
		
	}
	
	public void testCombineAndGet() {
		
	}
}
