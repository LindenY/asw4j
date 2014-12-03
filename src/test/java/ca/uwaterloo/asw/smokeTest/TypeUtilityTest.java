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

import ca.uwaterloo.asw.DataNode;
import ca.uwaterloo.asw.Instruction;
import ca.uwaterloo.asw.ToolResolver;
import ca.uwaterloo.asw.internal.InstructionNode;
import ca.uwaterloo.asw.reflection.TypeToken;
import ca.uwaterloo.asw.reflection.TypeUtility;
import ca.uwaterloo.asw.testObjects.TestInstruction0;
import ca.uwaterloo.asw.testObjects.TestInstruction1;
import ca.uwaterloo.asw.testObjects.TestInstructionWithoutDependency.InstructionWithGenericType;
import ca.uwaterloo.asw.testObjects.TestInstructionWithoutDependency.InstructionWithoutParameterizedTypes;

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

	@Test
	public void testCanonicalize() {

		/*
		 * TypeToken<?> typeToken0 = TypeToken
		 * .get(InstructionWithGenericType.class); TypeToken<?> typeToken1 =
		 * TypeToken .get(InstructionWithoutParameterizedTypes.class);
		 * TypeToken<?> typeToken2 = TypeToken.get(TestInstruction0.class);
		 * 
		 * printTypeToken(typeToken0); printTypeToken(typeToken1);
		 * printTypeToken(typeToken2);
		 */

		for (Type t : InstructionNode
				.getInstructionRequireDataTypes(InstructionWithGenericType.class)) {
			printTypeToken(TypeToken.get(t));
		}

		Instruction<HashMap<String, Date>, String> instruction = new Instruction<HashMap<String, Date>, String>() {
			@Override
			public String execute(HashMap<String, Date> requireData) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		
		for (Type t : InstructionNode
				.getInstructionRequireDataTypes((Class<? extends Instruction<?, ?>>) instruction.getClass())) {
			printTypeToken(TypeToken.get(t));
		}

	}

	@Ignore
	@Test
	public void testTypeTool() {

		List<String> strList = new ArrayList<String>();
		List<Date> dateList = new ArrayList<Date>();

		TypeToken<?> typeToken0 = TypeToken.get(strList.getClass());
		TypeToken<?> typeToken1 = TypeToken.get(dateList.getClass());

		assertTrue(typeToken0.equals(typeToken1));

		printTypeToken(typeToken0);
		printTypeToken(typeToken1);

		checkType(TypeUtility
				.resolveGenericType(List.class, strList.getClass()));
		checkType(TypeUtility.resolveRawClass(ArrayList.class,
				strList.getClass()));

	}

	private void checkType(Type type) {
		System.out.println();
		System.out
				.println("----------------- Start CheckType -----------------");
		if (type instanceof Class<?>) {
			Class<?> c = (Class<?>) type;
			System.out.println("Class<?>: " + c.getName());
		} else if (type instanceof ParameterizedType) {
			printParameterizedType((ParameterizedType) type);
		}
		System.out.println("----------------- End CheckType -----------------");
		System.out.println();
	}

	private void printTypeToken(TypeToken<?> typeToken) {
		System.out.println();
		System.out.println("-------------Start printTypeToken----------------");

		System.out.println("TypeToken: " + typeToken.toString());
		System.out.println("RawType: " + typeToken.getRawType().getName());
		System.out.println("Type: " + typeToken.getType().toString());
		checkType(typeToken.getType());

		System.out.println("-------------End printTypeToken----------------");
		System.out.println();
	}

	private void printSuperclassTypeParameter(Class<?> type) {
		System.out.println();
		System.out
				.println("-------------Start PrintSuperclassTypeParameter----------------");
		System.out.println("Class<?> is " + type.getName());
		Type[] types = TypeUtility.getSuperclassTypeParameter(type);
		if (types == null) {
			System.out.println("null");
		} else {
			for (Type t : types) {
				System.out.println("TypeArgument: " + t.toString());
			}
		}
		System.out
				.println("-------------End PrintSuperclassTypeParameter----------------");
		System.out.println();
	}

	private void printParameterizedType(ParameterizedType pt) {
		System.out.println();
		System.out
				.println("-------------------Start PrintParameterizedType---------------------");
		System.out.println("RawType: " + pt.getRawType());
		System.out.println("OwnerType: " + pt.getOwnerType());
		if (pt.getActualTypeArguments() != null) {
			for (Type t : pt.getActualTypeArguments()) {
				System.out.println("ActualTypeArgument: " + t.toString());
			}
		}
		System.out
				.println("-------------------End PrintParameterizedType---------------------");
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
