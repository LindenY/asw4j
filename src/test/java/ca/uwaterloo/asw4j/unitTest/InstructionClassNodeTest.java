package ca.uwaterloo.asw4j.unitTest;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import ca.uwaterloo.asw4j.Instruction;
import ca.uwaterloo.asw4j.ToolResolver;
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
	
	public static class TR implements ToolResolver {

		private String ji = "jichengcheng";
		
		public void registerTool(String name, Object tool) {
			
		}

		public Object getTool(String name) {
			return ji;
		}
		
	}
	
	public static class InstructionWithToolResolver extends Instruction<ArrayList<Integer>, String> {

		private String ji;
		
		public InstructionWithToolResolver(ToolResolver toolResolver) {
			super(toolResolver);
			ji = (String) toolResolver.getTool(null);
		}
		
		@Override
		public String execute(ArrayList<Integer> requireData) {
			
			int sum = 0;
			for(int n: requireData) {
				sum += n;
				
				System.out.println(ji);
			}
			return String.valueOf(sum);
		}
	}
	
	public static class InstructionWithTwoConstructors extends Instruction<ArrayList<Integer>, String> {

		private String ji;
		
		public InstructionWithTwoConstructors() {
			
			ji = "jichengcheng";
		}
		
		public InstructionWithTwoConstructors(ToolResolver toolResolver) {
			
			super(toolResolver);
			ji = (String) toolResolver.getTool(null);
		}
		
		@Override
		public String execute(ArrayList<Integer> requireData) {
			
			return null;
		}
	}
	
	InstructionClassNode node0 = new InstructionClassNode(
			Instruction0.class, null, new Class<?>[] {String.class}, 
			null, Date.class, false, false);
	
	String[] requireDateNames1 = {"Hello", " ", "World"};
	Class<?>[] requireDataTypes1 = new Class<?>[] {String.class, String.class, String.class};
	InstructionClassNode node1 = new InstructionClassNode(
			Instruction1.class, requireDateNames1, requireDataTypes1, 
			"Hello World", String.class, true, false);
	
	String[] requireDataNames2 = {"one", "two", "three"};
	Class<?>[] requireDataTypes2 = new Class<?>[] {Integer.class, Integer.class, Integer.class};
	InstructionClassNode node2 = new InstructionClassNode(InstructionWithToolResolver.class, 
			requireDataNames2, requireDataTypes2, "sum", String.class, true, true);
	
	InstructionClassNode node3 = new InstructionClassNode(InstructionWithTwoConstructors.class, 
			requireDataNames2, requireDataTypes2, "sum", String.class, true, true);
	
	@Test
	public void testConstructorWithLegalParameters() {
		
		assertTrue(node0.getInstructionClass().equals(Instruction0.class));
		assertFalse(node0.isSupportSingleton());
		
		assertTrue(node1.getInstructionClass().equals(Instruction1.class));
		assertTrue(node1.isSupportSingleton());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorWithNullRequireDataTypes() {
		
		new InstructionClassNode(Instruction0.class, null, 
				null, null, Date.class, false, false);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorWithEmptyRequireDataTypes() {
		
		new InstructionClassNode(Instruction0.class, null, 
				new Class<?>[] {}, null, Date.class, false, false);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorWithNullRequireDataNamesAndTypesGreaterThanOne() {
		
		new InstructionClassNode(Instruction0.class, null, 
				new Class<?>[] {String.class, Date.class}, null, Date.class, true, true);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorWithDifferentLengthInRequireDataNamesAndTypes() {
		
		new InstructionClassNode(Instruction1.class, requireDateNames1, 
				new Class<?>[] {String.class, String.class}, 
				"Hello World", String.class, true, false);
	}
	
	@Ignore
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorWithNullProduceDataType() {
		
		new InstructionClassNode(Instruction0.class, null, 
				new Class<?>[] {String.class}, null, null, false, false);
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
	public void testGetInstanceOfInstructionWithToolResolver() throws 
	NoSuchMethodException, SecurityException, InstantiationException, 
	IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		ToolResolver toolResolver = new TR();
		Instruction<?,?> instanceOfInstruction = node2.getInstanceOfInstruction(toolResolver);
		assertTrue(instanceOfInstruction.getClass().equals(InstructionWithToolResolver.class));
	}
	
	@Test
	public void testGetInstanceOfInstructionWithToolResolverOfTwoConstructors() throws 
	NoSuchMethodException, SecurityException, InstantiationException, 
	IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		ToolResolver toolResolver = new TR();
		Instruction<?,?> instanceOfInstruction = node3.getInstanceOfInstruction(toolResolver);
		assertTrue(instanceOfInstruction.getClass().equals(InstructionWithTwoConstructors.class));
	}
	
	@Test (expected = NoSuchMethodException.class)
	public void testGetInstanceOfInstructionWithToolResolverOfConstructorWithoutTF() throws 
	NoSuchMethodException, SecurityException, InstantiationException, 
	IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		ToolResolver toolResolver = new TR();
		Instruction<?,?> instanceOfInstruction = node0.getInstanceOfInstruction(toolResolver);
		assertTrue(instanceOfInstruction.getClass().equals(Instruction0.class));
	}
	
	@Test
	public void testGetInstanceOfInstructionWithoutToolResolver() throws 
	NoSuchMethodException, SecurityException, InstantiationException, 
	IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		Instruction<?,?> instanceOfInstruction = node0.getInstanceOfInstruction(null);
		assertTrue(instanceOfInstruction.getClass().equals(Instruction0.class));
	}
	
	@Test
	public void testGetInstanceOfInstructionWithoutToolResolverOfTwoConstructors() throws 
	NoSuchMethodException, SecurityException, InstantiationException, 
	IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		Instruction<?,?> instanceOfInstruction = node3.getInstanceOfInstruction(null);
		assertTrue(instanceOfInstruction.getClass().equals(InstructionWithTwoConstructors.class));
	}
	
	@Test (expected = NoSuchMethodException.class)
	public void testGetInstanceOfInstructionWithoutToolResolverOfConstructorWithTR() throws 
	NoSuchMethodException, SecurityException, InstantiationException, 
	IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		Instruction<?,?> instanceOfInstruction = node2.getInstanceOfInstruction(null);
		assertTrue(instanceOfInstruction.getClass().equals(InstructionWithToolResolver.class));
	}
	
	@Test
	public void testNumOfInstructionIssued() throws 
	NoSuchMethodException, SecurityException, InstantiationException, 
	IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		Instruction<?,?> instanceOfInstruction0 = node3.getInstanceOfInstruction(null);
		assertEquals(node3.numberOfInstructionIssued(), 1);
		
		Instruction<?,?> instanceOfInstruction1 = node3.getInstanceOfInstruction(null);
		Instruction<?,?> instanceOfInstruction2 = node3.getInstanceOfInstruction(null);
		assertEquals(node3.numberOfInstructionIssued(), 3);
		
		node3.returnInstanceOfInstruction(instanceOfInstruction1);
		assertEquals(node3.numberOfInstructionIssued(), 2);
	}
 }
