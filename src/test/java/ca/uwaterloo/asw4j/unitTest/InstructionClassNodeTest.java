package ca.uwaterloo.asw4j.unitTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.junit.Test;

import ca.uwaterloo.asw4j.Instruction;
import ca.uwaterloo.asw4j.internal.InstructionClassNode;
import ca.uwaterloo.asw4j.reflection.TypeToken;

public class InstructionClassNodeTest {
	
	public static class Instruction0 extends Instruction<String, Date> {
		
		@Override
		public Date execute(String requireData) {
			Date date = new Date();
			return date;
		}
	}
	
	public static class Instruction1 extends Instruction<ArrayList<String>, String> {

		@Override
		public String execute(ArrayList<String> requireData) {
			String returnStr = "";
			for(String str: requireData) {
				returnStr += str;
			}
			return returnStr;
		}
	}
	
	InstructionClassNode node0 = new InstructionClassNode(
			Instruction0.class, null, new Class<?>[] {String.class}, 
			null, Date.class, false, false);
	
	
	String[] requireDateNames1 = {"Hello", " ", "World"};
	InstructionClassNode node1 = new InstructionClassNode(
			Instruction1.class, requireDateNames1, new Class<?>[] {String.class, String.class, String.class}, 
			"Hello World", String.class, true, false);
	
	@Test
	public void testConstructorWithLegalParameters() {
		
		assertFalse(node0.isSupportSingleton());
		assertTrue(node1.isSupportSingleton());
		
	}
	
	@Test
	public void testConstructorWithIllegalParameters() {
		
	}

	@Test
	public void testGetRequireDatas() {
		
		List<TypeToken<?>> requireDatas0 = new ArrayList<TypeToken<?>>();
		requireDatas0.add(TypeToken.get(String.class));
		assertTrue(node0.getRequireDatas().equals(requireDatas0));
		
		List<TypeToken<?>> requireDatas1 = new ArrayList<TypeToken<?>>();
		requireDatas1.add(TypeToken.get(String.class, "Hello"));
		requireDatas1.add(TypeToken.get(String.class, " "));
		requireDatas1.add(TypeToken.get(String.class, "World"));
		assertTrue(node1.getRequireDatas().equals(requireDatas1));
	}
	
	@Test
	public void testGetProduceData() {
	
		assertTrue(node0.getProduceData().equals(TypeToken.get(Date.class)));
		assertTrue(node1.getProduceData().equals(TypeToken.get(String.class, "Hello World")));
	}
	
	@Test
	public void testGetInstanceOfInstructionWithToolResolver() {
		
	}
	
	@Test
	public void testGetInstanceOfInstructionWithoutToolResolver() {
		
	}
	
	@Test
	public void testNumOfInstructionIssued() {
		
	}
 }
