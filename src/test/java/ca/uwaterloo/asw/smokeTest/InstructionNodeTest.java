package ca.uwaterloo.asw.smokeTest;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import ca.uwaterloo.asw.Instruction;
import ca.uwaterloo.asw.internal.InstructionNode;
import ca.uwaterloo.asw.reflection.TypeToken;
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
		
		Class<?>[] produceDataTypes = InstructionNode.getInstructionRequireDataTypes(TestInstruction2.class);
		
		assertFalse(produceDataTypes == null);
		assertTrue(produceDataTypes.length == 3);
		assertTrue(produceDataTypes[0] == String.class);
		assertTrue(produceDataTypes[1] == Long.class);
		assertTrue(produceDataTypes[2] == Date.class);
	}

	@Test
	public void testProduceDataName() {
		
		String produceDataName = InstructionNode.getInstructionProduceDataName(TestInstruction2.class);

	}
	
	@Test
	public void testProduceDataType() {
		
		Class<?> produceDataType = InstructionNode.getInstructionProduceDataType(TestInstruction2.class);
		
	}

}
