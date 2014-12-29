package ca.uwaterloo.asw4j.unitTest;

import org.junit.Test;
import static org.junit.Assert.*;

import ca.uwaterloo.asw4j.internal.NonBlockingLinkedQueue;

public class NonBlockingLinkedQueueTest {

	@Test
	public void testSize() {
		NonBlockingLinkedQueue<String> strQueue = new NonBlockingLinkedQueue<String>();
		
		assertEquals(0, strQueue.size());
		for (int i=1; i<=10; i++) {
			strQueue.push("str");
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
			strQueue.push("str");
		}
		
		strQueue.clear();
		assertEquals(0, strQueue.size());
		assertEquals(null, strQueue.peek());
		assertEquals(null, strQueue.poll());
		
		strQueue.push("str");
		assertEquals(1, strQueue.size());
		assertEquals("str", strQueue.peek());
		assertEquals("str", strQueue.poll());
	}
	
	@Test
	public void testIsEmpty() {
		NonBlockingLinkedQueue<String> strQueue = new NonBlockingLinkedQueue<String>();
		assertTrue(strQueue.isEmpty());
		
		strQueue.push("str");
		assertFalse(strQueue.isEmpty());
		
		strQueue.poll();
		assertTrue(strQueue.isEmpty());
	}
	
	@Test
	public void testPushAndPool() {
		NonBlockingLinkedQueue<String> strQueue = new NonBlockingLinkedQueue<String>();
		
		String str0 = "str0";
		String str1 = "str1";
		String str2 = "str2";
		String str3 = "str3";
		String str4 = "str4";
		
		assertTrue(null == strQueue.poll());
		
		strQueue.push(str0);
		strQueue.push(str1);
		strQueue.push(str2);
		strQueue.push(str3);
		strQueue.push(str4);
		
		assertTrue(str0 == strQueue.poll());
		assertTrue(str1 == strQueue.poll());
		assertTrue(str2 == strQueue.poll());
		assertTrue(str3 == strQueue.poll());
		assertTrue(str4 == strQueue.poll());
		assertTrue(null == strQueue.poll());
	}
	
	@Test
	public void testPeek() {
		NonBlockingLinkedQueue<String> strQueue = new NonBlockingLinkedQueue<String>();
		
		String str0 = "str0";
		String str1 = "str1";
		
		strQueue.push(str0);
		strQueue.push(str1);
		
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
			strQueue.push(String.valueOf(i));
		}
		
		Object[] objs = strQueue.toArray();
		assertTrue(objs.length == 5);
		for (int i=0; i<5; i++) {
			assertEquals(String.valueOf(i), objs[i]);
		}
	}
}
