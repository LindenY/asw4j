package ca.uwaterloo.asw.smokeTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import ca.uwaterloo.asw.DataNode;

public class DataNodeTest {

	@Test
	public void testPutAndGet() {
		
		Date date = new Date();
		String string = "String";
		List<Long> list = new ArrayList<Long>();
		
		DataNode dataNode = new DataNode();
		dataNode.put(date);
		dataNode.put(string);
		dataNode.put(list);
		
		Date rDate = dataNode.get(Date.class);
		assertTrue(rDate == date);
		
		String rString = dataNode.get(String.class);
		assertTrue(rString == string);
		
		List<Long> rList = dataNode.get(ArrayList.class);
		assertTrue(rList == list);
	}
	
	@Test
	public void testPutAndGetWithName() {
		
		Date d0 = new Date();
		Date d1 = new Date();
		Date d2 = new Date();
		String s0 = "String";
		String s1 = "String";
		String s2 = "String";
		List<Long> l0 = new ArrayList<Long>();
		List<Long> l1 = new ArrayList<Long>();
		List<Long> l2 = new ArrayList<Long>();
		
		DataNode dataNode = new DataNode();
		dataNode.put(d0);
		dataNode.put(d1, "d1");
		dataNode.put(d2, "d2");
		dataNode.put(s0);
		dataNode.put(s1, "s1");
		dataNode.put(s2, "s2");
		dataNode.put(l0);
		dataNode.put(l1, "l1");
		dataNode.put(l2, "l2");
		
		Date rD0 = dataNode.get(Date.class);
		assertTrue(rD0 == d0);
		Date rD1 = dataNode.get(Date.class, "d1");
		assertTrue(rD1 == d1);
		Date rD2 = dataNode.get(Date.class, "d2");
		assertTrue(rD2 == d2);
		
		String rS0 = dataNode.get(String.class);
		assertTrue(rS0 == s0);
		String rS1 = dataNode.get(String.class, "s1");
		assertTrue(rS1 == s1);
		String rS2 = dataNode.get(String.class, "s2");
		assertTrue(rS2 == s2);
		
		List<Long> rL0 = dataNode.get(ArrayList.class);
		assertTrue(rL0 == l0);
		List<Long> rL1 = dataNode.get(ArrayList.class, "l1");
		assertTrue(rL1 == l1);
		List<Long> rL2 = dataNode.get(ArrayList.class, "l2");
		assertTrue(rL2 == l2);
	}
	
	@Test
	public void testPutAndGetWithDuplication() {
		Date d0 = new Date();
		Date d1 = new Date();
		String s0 = "String0";
		String s1 = "String1";
		List<Long> l0 = new ArrayList<Long>();
		List<Long> l1 = new ArrayList<Long>();
		
		DataNode dataNode = new DataNode();
		dataNode.put(d0);
		dataNode.put(d1);
		dataNode.put(s0);
		dataNode.put(s1);
		dataNode.put(l0);
		dataNode.put(l1);
		
		assertTrue(dataNode.size() == 6);
		
		Date rDate = dataNode.get(Date.class);
		String rString = dataNode.get(String.class);
		List<Long> rList = dataNode.get(ArrayList.class);
		
		assertTrue(rDate == d0);
		assertTrue(rString == s0);
		assertTrue(rList == l0);
		
		List<Date> rDateList = dataNode.getAll(Date.class);
		List<String> rStringList = dataNode.getAll(String.class);
		List<ArrayList> rListList = dataNode.getAll(ArrayList.class);
		
		assertFalse(rDateList == null);
		assertFalse(rStringList == null);
		assertFalse(rListList == null);
		
		assertTrue(rDateList.size() == 2);
		assertTrue(rStringList.size() == 2);
		assertTrue(rListList.size() == 2);
	}
	
	@Ignore
	@Test
	public void testPutAndGetWithGenericType() {
		List<String> strList = new ArrayList<String>();
		List<Date> dateList = new ArrayList<Date>();
		
		DataNode dataNode = new DataNode();
		dataNode.put(strList);
		dataNode.put(dateList);
		
		List rStrList = dataNode.get(strList.getClass());
		List rDateList = dataNode.get(dateList.getClass());
		
		assertTrue(rStrList == strList);
		assertTrue(rDateList == dateList);
		assertFalse(rStrList == dateList);
		assertFalse(rDateList == strList);
		
		System.out.println(strList.getClass().getName());
		System.out.println(dateList.getClass().getName());
		
	}
}
