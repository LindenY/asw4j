package ca.uwaterloo.asw.smokeTest;

import java.lang.reflect.Type;
import java.util.Date;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uwaterloo.asw.DataNode;
import ca.uwaterloo.asw.reflection.TypeUtility;
import ca.uwaterloo.asw.testObjects.TestInstruction0;
import ca.uwaterloo.asw.testObjects.TestInstruction1;

public class TypeUtilityTest {

	@Test
	public void testGetSuperclassTypeParameter() {

		Type strType = (Type) String.class;
		Type dateType = (Type) Date.class;
		Type longType = (Type) Long.class;
		Type dataNodeType = (Type) DataNode.class;
		
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
