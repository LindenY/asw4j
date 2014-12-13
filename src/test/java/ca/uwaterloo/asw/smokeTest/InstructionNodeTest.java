package ca.uwaterloo.asw.smokeTest;

import static org.junit.Assert.*;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import ca.uwaterloo.asw.Instruction;
import ca.uwaterloo.asw.internal.InstructionNode;
import ca.uwaterloo.asw.testObjects.TestInstruction0;
import ca.uwaterloo.asw.testObjects.TestInstruction1;
import ca.uwaterloo.asw.testObjects.TestInstruction2;

public class InstructionNodeTest {

	@Test
	public void testDependencies() {

		List<Class<? extends Instruction<?, ?>>> dependencies = InstructionNode
				.getInstructionDependencies(TestInstruction2.class);

		assertFalse(dependencies == null);
		assertTrue(dependencies.size() == 2);
		assertTrue(dependencies.get(0) == TestInstruction0.class);
		assertTrue(dependencies.get(1) == TestInstruction1.class);

	}

	@Test
	public void testRequireDataNames() {

		String[] requireDataNames = InstructionNode.getInstructionRequireDataNames(TestInstruction2.class);

		assertFalse(requireDataNames == null);
		assertTrue(requireDataNames.length == 3);
		assertTrue(requireDataNames[0].equals("data0"));
		assertTrue(requireDataNames[1].equals("data1"));
		assertTrue(requireDataNames[2].equals("data2"));
	}
	
	@Test
	public void testRequireDataTypes() {
		
		Type[] requireDataTypes = InstructionNode.getInstructionRequireDataTypes(TestInstruction2.class);
		
		assertFalse(requireDataTypes == null);
		assertTrue(requireDataTypes.length == 3);
		assertTrue(requireDataTypes[0] == String.class);
		assertTrue(requireDataTypes[1] == Long.class);
		assertTrue(requireDataTypes[2] == Date.class);
	}

	@Ignore
	@Test
	public void testProduceDataName() {
		
		String produceDataName = InstructionNode.getInstructionProduceDataName(TestInstruction2.class);

	}
	
	@Ignore
	@Test
	public void testProduceDataType() {
		
		Type produceDataType = InstructionNode.getInstructionProduceDataType(TestInstruction2.class);
		
	}

	@Test 
	public void testInstructionInstantiation() {
		
		InstructionNode instructionNode = 
				new InstructionNode(null, new Class<?>[]{String.class}, null, MyInstruction.class);
		
		Instruction<?, ?> rInstruction = instructionNode.getInstructionInstance(null);
	}
	
	public static class MyInstruction extends Instruction<String, Date> {

		@Override
		public Date execute(String requireData) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
