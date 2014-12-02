package ca.uwaterloo.asw.smokeTest;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import ca.uwaterloo.asw.DataNode;
import ca.uwaterloo.asw.ToolResolver;
import ca.uwaterloo.asw.reflection.TypeToken;
import ca.uwaterloo.asw.reflection.TypeUtility;
import ca.uwaterloo.asw.testObjects.TestInstruction0;
import ca.uwaterloo.asw.testObjects.TestInstruction1;
import ca.uwaterloo.asw.testObjects.TestInstructionWithoutDependency.InstructionWithGenericType;
import ca.uwaterloo.asw.testObjects.TestInstructionWithoutDependency.InstructionWithoutParameterizedTypes;

public class TypeUtilityTest {

	@Test
	public void testGetSuperclassTypeParameter() {

		Type strType = String.class;
		Type dateType = Date.class;
		Type longType = Long.class;
		Type dataNodeType = DataNode.class;
		
		Type[] rInstruction0 = TypeUtility
				.getSuperclassTypeParameter(TestInstruction0.class);
		Type[] rInstruction1 = TypeUtility
				.getSuperclassTypeParameter(TestInstruction1.class);
		
		assertFalse(rInstruction0[0] == null);
		assertFalse(rInstruction0[1] == null);
		assertFalse(rInstruction1[0] == null);
		assertFalse(rInstruction1[1] == null);
		
		assertTrue(dateType == rInstruction0[0]);
		assertTrue(strType == rInstruction0[1]);
		assertTrue(dataNodeType == rInstruction1[0]);
		assertTrue(longType == rInstruction1[1]);
	}
	
	@Ignore
	@Test
	public void testCanonicalize() {
		
		TypeToken<?> typeToken = TypeToken.get(InstructionWithGenericType.class);
		
		System.out.println(typeToken.toString());
		
		InstructionWithGenericType instruction = new InstructionWithGenericType(new ToolResolver() {
			public void registerTool(String name, Object tool) {
			}
			public Object getTool(String name) {
				return null;
			}
		});
		
		printSuperclassTypeParameter(instruction.getClass());
		
		Map<String, List<Date>> gl = new HashMap<String, List<Date>>();
		printSuperclassTypeParameter(gl.getClass());
		
		
		InstructionWithoutParameterizedTypes iwpt = new InstructionWithoutParameterizedTypes(getToolResolver());
		printSuperclassTypeParameter(iwpt.getClass());
	}
	
	private void printSuperclassTypeParameter(Class<?> type) {
		System.out.println();
		System.out.println("-------------Start PrintSuperclassTypeParameter----------------");
		System.out.println("Class<?> is " + type.getName());
		Type[] types = TypeUtility.getSuperclassTypeParameter(type);
		if (types == null) {
			System.out.println("null");
		} else {
			for (Type t : types) {
				System.out.println("TypeArgument: " + t.toString());
			}
		}
		System.out.println("-------------End PrintSuperclassTypeParameter----------------");
		System.out.println();
	}
	
	private ToolResolver getToolResolver() {
		return new ToolResolver() {
			
			public void registerTool(String name, Object tool) {
			}
			
			public Object getTool(String name) {
				return null;
			}
		};
	}
}
