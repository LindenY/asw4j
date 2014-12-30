package ca.uwaterloo.asw4j.unitTest;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Test;

import static org.junit.Assert.*;
import ca.uwaterloo.asw4j.internal.NonBlockingLinkedQueue;

public class NonBlockingLinkedQueueTest {

	@Test
	public void testSize() {
		NonBlockingLinkedQueue<String> strQueue = new NonBlockingLinkedQueue<String>();
		
		assertEquals(0, strQueue.size());
		for (int i=1; i<=10; i++) {
			strQueue.add("str");
			assertEquals(i, strQueue.size());
		}
		
		for (int i=9; i>=0; i--) {
			strQueue.peek();
			assertEquals(10, strQueue.size());
		}
		
		for (int i=9; i>=0; i--) {
			strQueue.poll();
			assertEquals(i, strQueue.size());
		}
	}
	
	@Test
	public void testClear() {
		NonBlockingLinkedQueue<String> strQueue = new NonBlockingLinkedQueue<String>();
		
		for (int i=1; i<=10; i++) {
			strQueue.add("str");
		}
		
		strQueue.clear();
		assertEquals(0, strQueue.size());
		assertEquals(null, strQueue.peek());
		assertEquals(null, strQueue.poll());
		
		strQueue.add("str");
		assertEquals(1, strQueue.size());
		assertEquals("str", strQueue.peek());
		assertEquals("str", strQueue.poll());
	}
	
	@Test
	public void testIsEmpty() {
		NonBlockingLinkedQueue<String> strQueue = new NonBlockingLinkedQueue<String>();
		assertTrue(strQueue.isEmpty());
		
		strQueue.add("str");
		assertFalse(strQueue.isEmpty());
		
		strQueue.poll();
		assertTrue(strQueue.isEmpty());
	}
	
	@Test(expected=NullPointerException.class)
	public void testAddAndPool() {
		NonBlockingLinkedQueue<String> strQueue = new NonBlockingLinkedQueue<String>();
		
		String str0 = "str0";
		String str1 = "str1";
		String str2 = "str2";
		String str3 = "str3";
		String str4 = "str4";
		
		assertTrue(null == strQueue.poll());
		
		strQueue.add(str0);
		strQueue.add(str1);
		strQueue.add(str2);
		strQueue.add(str3);
		strQueue.add(str4);
		
		assertTrue(str0 == strQueue.poll());
		assertTrue(str1 == strQueue.poll());
		assertTrue(str2 == strQueue.poll());
		assertTrue(str3 == strQueue.poll());
		assertTrue(str4 == strQueue.poll());
		assertTrue(null == strQueue.poll());
		
		strQueue.add(null);
	}
	
	@Test
	public void testPeek() {
		NonBlockingLinkedQueue<String> strQueue = new NonBlockingLinkedQueue<String>();
		
		String str0 = "str0";
		String str1 = "str1";
		
		strQueue.add(str0);
		strQueue.add(str1);
		
		assertTrue(str0 == strQueue.peek());
		assertTrue(str0 == strQueue.peek());
		assertTrue(str0 == strQueue.poll());
		assertTrue(str1 == strQueue.peek());
		assertTrue(str1 == strQueue.peek());
		assertTrue(str1 == strQueue.poll());
		assertTrue(null == strQueue.peek());
		assertTrue(null == strQueue.peek());
	}
	
	@Test
	public void testToArray() {
		NonBlockingLinkedQueue<String> strQueue = new NonBlockingLinkedQueue<String>();
		
		for (int i=0; i<5; i++) {
			strQueue.add(String.valueOf(i));
		}
		
		Object[] objs = strQueue.toArray();
		assertTrue(objs.length == 5);
		for (int i=0; i<5; i++) {
			assertEquals(String.valueOf(i), objs[i]);
		}
	}
	
	@Test(expected=ArrayStoreException.class)
	public void testToArrayWithArgument() {
		NonBlockingLinkedQueue<String> strQueue = new NonBlockingLinkedQueue<String>();
		
		for (int i=0; i<5; i++) {
			strQueue.add(String.valueOf(i));
		}
		
		String[] strs = strQueue.toArray(new String[5]);
		assertTrue(strs.length == 5);
		for (int i=0; i<5; i++) {
			assertEquals(String.valueOf(i), strs[i]);
		}
		
		strs = new String[6];
		for (int i=0; i<=5; i++) {
			strs[i] = "str" + i;
		}
		strs = strQueue.toArray(strs);
		assertTrue(strs.length == 6);
		for (int i=0; i<5; i++) {
			assertEquals(String.valueOf(i), strs[i]);
		}
		assertTrue(strs[5] == null);
		
		strQueue.toArray(new Long[10]);
	}
	
	@Test(expected=NullPointerException.class)
	public void testContain() {
		NonBlockingLinkedQueue<String> strQueue = new NonBlockingLinkedQueue<String>();
		String str0 = "str0";
		String str1 = "str1";
		String str2 = "str2";
		strQueue.add(str0);
		strQueue.add(str1);
		strQueue.add(str2);
		
		assertTrue(strQueue.contains(str2));
		assertTrue(strQueue.contains(str1));
		assertTrue(strQueue.contains(str2));
		assertFalse(strQueue.contains("str3"));
		assertFalse(strQueue.contains("str4"));
		strQueue.contains(null);
	}
	
	@Test(expected=NullPointerException.class)
	public void testContainAll() {
		NonBlockingLinkedQueue<String> strQueue = new NonBlockingLinkedQueue<String>();
		String str0 = "str0";
		String str1 = "str1";
		String str2 = "str2";
		strQueue.add(str0);
		strQueue.add(str1);
		strQueue.add(str2);
		
		String[] strArr = new String[]{ str0, str1 };
		assertTrue(strQueue.containsAll(Arrays.asList(strArr)));
		strArr = new String[]{ str0, str1, str2 };
		assertTrue(strQueue.containsAll(Arrays.asList(strArr)));
		strArr = new String[]{ str0, str1, "str3" };
		assertFalse(strQueue.containsAll(Arrays.asList(strArr)));
		strArr = new String[]{ str0, str1, str2, "str3" };
		assertFalse(strQueue.containsAll(Arrays.asList(strArr)));
		strArr = new String[3];
		strQueue.containsAll(Arrays.asList(strArr));
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testIterator() {
		NonBlockingLinkedQueue<String> strQueue = new NonBlockingLinkedQueue<String>();
		String str0 = "str0";
		String str1 = "str1";
		String str2 = "str2";
		strQueue.add(str0);
		strQueue.add(str1);
		strQueue.add(str2);
		
		Iterator<String> iterator = strQueue.iterator();
		assertTrue(iterator.hasNext());
		assertEquals(str0, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(str1, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(str2, iterator.next());
		assertFalse(iterator.hasNext());
		iterator.next();
	}
}
