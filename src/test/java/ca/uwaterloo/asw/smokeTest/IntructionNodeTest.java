package ca.uwaterloo.asw.smokeTest;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import ca.uwaterloo.asw.Instruction;
import ca.uwaterloo.asw.InstructionNode;
import ca.uwaterloo.asw.reflection.TypeToken;
import ca.uwaterloo.asw.testObjects.TestInstruction0;
import ca.uwaterloo.asw.testObjects.TestInstruction1;
import ca.uwaterloo.asw.testObjects.TestInstruction2;

public class InstructionNodeTest {

	@Test
	public void testDependencies() {

		InstructionNode instructionNode = new InstructionNode(
				TestInstruction2.class);

		List<Class<? extends Instruction<?, ?>>> dependencies = instructionNode
				.getDependencies();

		assertFalse(dependencies == null);
		assertTrue(dependencies.size() == 2);
		assertTrue(dependencies.get(0) == TestInstruction0.class);
		assertTrue(dependencies.get(1) == TestInstruction1.class);

	}

	@Test
	public void testRequireDatas() {

		InstructionNode instructionNode = new InstructionNode(
				TestInstruction2.class);

		List<TypeToken<?>> requireDatas = instructionNode.getRequireDatas();

		assertFalse(requireDatas == null);
		assertTrue(requireDatas.get(0).equals(
				TypeToken.get(String.class, "data0")));
		assertTrue(requireDatas.get(1).equals(
				TypeToken.get(Long.class, "data1")));
		assertTrue(requireDatas.get(2).equals(
				TypeToken.get(Date.class, "data2")));
	}

	@Test
	public void testProduceData() {
		InstructionNode instructionNode = new InstructionNode(
				TestInstruction2.class);

		TypeToken<?> produceData = instructionNode.getProduceData();
		
		assertFalse(produceData == null);
		assertTrue(produceData.equals(TypeToken.get(String.class, "produced")));
		assertFalse(produceData.equals(TypeToken.get(Long.class, "produced")));
		assertFalse(produceData.equals(TypeToken.get(String.class, "bahlah")));
	}

}
