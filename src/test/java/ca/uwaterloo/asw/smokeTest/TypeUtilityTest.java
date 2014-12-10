package ca.uwaterloo.asw.smokeTest;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import ca.uwaterloo.asw.testObjects.TestInstruction0;
import ca.uwaterloo.asw.testObjects.TestInstruction1;
import ca.uwaterloo.asw.testObjects.TestInstructionWithoutDependency.InstructionWithGenericType;
import ca.uwaterloo.asw.testObjects.TestInstructionWithoutDependency.InstructionWithoutParameterizedTypes;
import ca.uwaterloo.asw4j.DataNode;
import ca.uwaterloo.asw4j.Instruction;
import ca.uwaterloo.asw4j.ToolResolver;
import ca.uwaterloo.asw4j.internal.InstructionNode;
import ca.uwaterloo.asw4j.reflection.TypeToken;
import ca.uwaterloo.asw4j.reflection.TypeUtility;

public class TypeUtilityTest {

	@Ignore
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

	
}
