package ca.uwaterloo.asw.smokeTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
		System.out.println("Compare class[Date] : " + (rDate == date));
		
		String rString = dataNode.get(String.class);
		System.out.println("Compare class[String] : " + (rString == string));
		
		List<Long> rList = dataNode.get(ArrayList.class);
		System.out.println("Compare class[List] : " + (rList == list));
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
		System.out.println("Compare class[Date] with name[null] : " + (rD0 == d0));
		Date rD1 = dataNode.get(Date.class, "d1");
		System.out.println("Compare class[Date] with name[d1] : " + (rD1 == d1));
		Date rD2 = dataNode.get(Date.class, "d2");
		System.out.println("Compare class[Date] with name[d2] : " + (rD2 == d2));
		
		String rS0 = dataNode.get(String.class);
		System.out.println("Compare class[String] with name[null] : " + (rS0 == s0));
		String rS1 = dataNode.get(String.class, "s1");
		System.out.println("Compare class[String] with name[s1] : " + (rS1 == s1));
		String rS2 = dataNode.get(String.class, "s2");
		System.out.println("Compare class[String] with name[s1] : " + (rS2 == s2));
		
		List<Long> rL0 = dataNode.get(ArrayList.class);
		System.out.println("Compare class[List] with name[null] : " + (rL0 == l0));
		List<Long> rL1 = dataNode.get(ArrayList.class, "l1");
		System.out.println("Compare class[List] with name[l1] : " + (rL1 == l1));
		List<Long> rL2 = dataNode.get(ArrayList.class, "l2");
		System.out.println("Compare class[List] with name[l2] : " + (rL2 == l2));
	}
}
