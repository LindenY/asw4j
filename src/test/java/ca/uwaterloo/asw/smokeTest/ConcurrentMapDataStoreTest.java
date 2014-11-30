package ca.uwaterloo.asw.smokeTest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;
import ca.uwaterloo.asw.ConcurrentMapDataStore;
import ca.uwaterloo.asw.DataNode;
import ca.uwaterloo.asw.DataStore;
import ca.uwaterloo.asw.reflection.TypeToken;

public class ConcurrentMapDataStoreTest {

	@Test
	public void testAddAndGet() {

		DataStore concurrentMapStore = new ConcurrentMapDataStore();

		String str = "String0";
		Long ln = 0l;
		Date date = new Date();

		concurrentMapStore.add(str);
		concurrentMapStore.add(ln);
		concurrentMapStore.add(date);

		String rStr = concurrentMapStore.getAndRemove(String.class);
		Long rLn = concurrentMapStore.getAndRemove(Long.class);
		Date rDate = concurrentMapStore.getAndRemove(Date.class);

		assertFalse(rStr == null);
		assertFalse(rLn == null);
		assertFalse(rDate == null);

		assertTrue(rStr == str);
		assertTrue(rLn == ln);
		assertTrue(rDate == date);
	}

	@Test
	public void testAddAndGetAndRemove() {

		DataStore concurrentMapStore = new ConcurrentMapDataStore();

		String str0 = "String0";
		String str1 = "String1";
		String str2 = "String2";
		String str3 = "String3";
		String str4 = "String4";

		Long long0 = 0l;
		Long long1 = 1l;
		Long long2 = 2l;
		Long long3 = 3l;
		Long long4 = 4l;

		Date date0 = new Date();
		Date date1 = new Date();
		Date date2 = new Date();
		Date date3 = new Date();
		Date date4 = new Date();

		concurrentMapStore.add(str0);
		concurrentMapStore.add(str1);
		concurrentMapStore.add(str2);
		concurrentMapStore.add(str3);
		concurrentMapStore.add(str4);

		concurrentMapStore.add(long0);
		concurrentMapStore.add(long1);
		concurrentMapStore.add(long2);
		concurrentMapStore.add(long3);
		concurrentMapStore.add(long4);

		concurrentMapStore.add(date0);
		concurrentMapStore.add(date1);
		concurrentMapStore.add(date2);
		concurrentMapStore.add(date3);
		concurrentMapStore.add(date4);

		String rStr0 = concurrentMapStore.getAndRemove(String.class);
		String rStr1 = concurrentMapStore.getAndRemove(String.class);
		String rStr2 = concurrentMapStore.getAndRemove(String.class);
		String rStr3 = concurrentMapStore.getAndRemove(String.class);
		String rStr4 = concurrentMapStore.getAndRemove(String.class);
		String rStr5 = concurrentMapStore.getAndRemove(String.class);

		assertTrue(rStr0 == str0);
		assertTrue(rStr1 == str1);
		assertTrue(rStr2 == str2);
		assertTrue(rStr3 == str3);
		assertTrue(rStr4 == str4);
		assertTrue(rStr5 == null);

		Long rLong0 = concurrentMapStore.getAndRemove(Long.class);
		Long rLong1 = concurrentMapStore.getAndRemove(Long.class);
		Long rLong2 = concurrentMapStore.getAndRemove(Long.class);
		Long rLong3 = concurrentMapStore.getAndRemove(Long.class);
		Long rLong4 = concurrentMapStore.getAndRemove(Long.class);
		Long rLong5 = concurrentMapStore.getAndRemove(Long.class);

		assertTrue(rLong0 == long0);
		assertTrue(rLong1 == long1);
		assertTrue(rLong2 == long2);
		assertTrue(rLong3 == long3);
		assertTrue(rLong4 == long4);
		assertTrue(rLong5 == null);

		Date rDate0 = concurrentMapStore.getAndRemove(Date.class);
		Date rDate1 = concurrentMapStore.getAndRemove(Date.class);
		Date rDate2 = concurrentMapStore.getAndRemove(Date.class);
		Date rDate3 = concurrentMapStore.getAndRemove(Date.class);
		Date rDate4 = concurrentMapStore.getAndRemove(Date.class);
		Date rDate5 = concurrentMapStore.getAndRemove(Date.class);

		assertTrue(rDate0 == date0);
		assertTrue(rDate1 == date1);
		assertTrue(rDate2 == date2);
		assertTrue(rDate3 == date3);
		assertTrue(rDate4 == date4);
		assertTrue(rDate5 == null);
	}

	@Test
	public void testGetAndRemoveWithMultipleTypes() {

		ConcurrentMapDataStore concurrentMapStore = new ConcurrentMapDataStore();

		String str0 = "String0";
		String str1 = "String1";
		String str2 = "String2";
		String str3 = "String3";
		String str4 = "String4";

		Long long0 = 0l;
		Long long1 = 1l;
		Long long2 = 2l;
		Long long3 = 3l;
		Long long4 = 4l;

		Date date0 = new Date();
		Date date1 = new Date();
		Date date2 = new Date();
		Date date3 = new Date();
		Date date4 = new Date();

		concurrentMapStore.add(str0);
		concurrentMapStore.add(str1);
		concurrentMapStore.add(str2);
		concurrentMapStore.add(str3);
		concurrentMapStore.add(str4);

		concurrentMapStore.add(long0);
		concurrentMapStore.add(long1);
		concurrentMapStore.add(long2);
		concurrentMapStore.add(long3);
		concurrentMapStore.add(long4);

		concurrentMapStore.add(date0);
		concurrentMapStore.add(date1);
		concurrentMapStore.add(date2);
		concurrentMapStore.add(date3);
		concurrentMapStore.add(date4);

		List<TypeToken<?>> typeTokens = new ArrayList<TypeToken<?>>();
		typeTokens.add(TypeToken.get(String.class));
		typeTokens.add(TypeToken.get(Long.class));
		typeTokens.add(TypeToken.get(Date.class));

		DataNode rDN0 = concurrentMapStore.getAndRemoveAll(typeTokens);
		assertFalse(rDN0 == null);
		assertTrue(testDataNode(rDN0, new Object[] { str0, long0, date0 }));
		DataNode rDN1 = concurrentMapStore.getAndRemoveAll(typeTokens);
		assertFalse(rDN1 == null);
		assertTrue(testDataNode(rDN1, new Object[] { str1, long1, date1 }));
		DataNode rDN2 = concurrentMapStore.getAndRemoveAll(typeTokens);
		assertFalse(rDN2 == null);
		assertTrue(testDataNode(rDN2, new Object[] { str2, long2, date2 }));
		DataNode rDN3 = concurrentMapStore.getAndRemoveAll(typeTokens);
		assertFalse(rDN3 == null);
		assertTrue(testDataNode(rDN3, new Object[] { str3, long3, date3 }));
		DataNode rDN4 = concurrentMapStore.getAndRemoveAll(typeTokens);
		assertFalse(rDN4 == null);
		assertTrue(testDataNode(rDN4, new Object[] { str4, long4, date4 }));
	}

	private boolean testDataNode(DataNode dataNode, Object[] objs) {

		for (Object obj : objs) {

			Object rObj = dataNode.get(obj.getClass());
			if (rObj != obj) {
				return false;
			}
		}
		return true;
	}

	@Test
	public void testContain() {

		ConcurrentMapDataStore concurrentMapStore = new ConcurrentMapDataStore();

		String str0 = "String0";
		Long long0 = 0l;
		Date date0 = new Date();

		List<TypeToken<?>> typeTokens = new ArrayList<TypeToken<?>>();
		typeTokens.add(TypeToken.get(Long.class));
		typeTokens.add(TypeToken.get(Date.class));
		typeTokens.add(TypeToken.get(String.class));

		concurrentMapStore.add(str0);
		concurrentMapStore.add(long0);
		concurrentMapStore.add(date0);

		assertTrue(concurrentMapStore.contain(String.class));
		assertTrue(concurrentMapStore.contain(Long.class));
		assertTrue(concurrentMapStore.contain(Date.class));
		assertFalse(concurrentMapStore.contain(Integer.class));

		assertTrue(concurrentMapStore.containAll(typeTokens));
		typeTokens.add(TypeToken.get(Integer.class));
		assertFalse(concurrentMapStore.containAll(typeTokens));
	}

	@Test
	public void testConcurrentModification() {
		
		DataStore concurrentMapStore = new ConcurrentMapDataStore();
		
		Thread thread0 = new Thread(
				new AddAndGetAndRemoveOperation(
						concurrentMapStore, 
						50, 
						AddAndGetAndRemoveOperation.TestType.string, 
						AddAndGetAndRemoveOperation.TestType.date));
		Thread thread1 = new Thread(
				new AddAndGetAndRemoveOperation(
						concurrentMapStore, 
						50, 
						AddAndGetAndRemoveOperation.TestType.date, 
						AddAndGetAndRemoveOperation.TestType.string));
		
		thread0.start();
		
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
		}
		thread1.start();
		
		
	}
	
	private static class AddAndGetAndRemoveOperation implements Runnable {

		public enum TestType {
			string,
			ln,
			date
		}
		
		private DataStore dataStore;
		private int times;
		private TestType add;
		private TestType get;
		
		public AddAndGetAndRemoveOperation(DataStore dataStore, int times, TestType add, TestType get) {
			this.dataStore = dataStore;
			this.times = times;
			this.add = add;
			this.get = get;
		}
		
		public void run() {
			
			int count = 0;
			int getCount = 0;
			while (count < times) {
				
				Object addObj = null;
				if (add == TestType.string) {
					addObj = (Object) ("string" + count);
				} else if (add == TestType.date) {
					addObj = (Object) new Date();
				} else {
					addObj = (Object) count;
				}
				System.out.println("Add : " + addObj.toString());
				
				dataStore.add(addObj);
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
				/*Class<?> getType = null;
				if (get == TestType.string) {
					getType = String.class;
				} else if (get == TestType.date) {
					getType = Date.class;
				} else {
					getType = Long.class;
				}
				Object getObject = dataStore.getAndRemove(getType);
				
				if (getObject != null) {
					getCount ++;
					System.out.println("Get data : " + getObject.toString() );
				} else { 
					System.out.println("Get null for " + getType.getName());
				}*/
				
				count ++;
			}
			
			System.out.println("GetCount: " + getCount);
		}
		
	}
}
