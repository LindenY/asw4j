package ca.uwaterloo.asw.smokeTest;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;

import ca.uwaterloo.asw.testObjects.TestInstructionWithoutDependency.InstructionWithAnnotation;
import ca.uwaterloo.asw.testObjects.TestInstructionWithoutDependency.InstructionWithoutAnnotation;
import ca.uwaterloo.asw4j.ConcurrentMapDataStore;
import ca.uwaterloo.asw4j.DataStore;
import ca.uwaterloo.asw4j.Instruction;
import ca.uwaterloo.asw4j.InstructionResolver;
import ca.uwaterloo.asw4j.SimpleInstructionResolver;
import ca.uwaterloo.asw4j.ToolResolver;
import ca.uwaterloo.asw4j.reflection.TypeToken;

public class SimpleInstructionResolverTest {

	private ToolResolver getToolResolver() {
		return new ToolResolver() {
			public void registerTool(String name, Object tool) {

			}

			public Object getTool(String name) {
				return null;
			}
		};
	}

	private DataStore getDataStore() {

		DataStore concurrentMapStore = new ConcurrentMapDataStore();

		String str0 = "string0";
		String str1 = "string1";
		String str2 = "string2";
		String str3 = "string4";
		String str4 = "string5";

		Long long0 = 0l;
		Long long1 = 1l;
		Long long2 = 2l;
		Long long3 = 3l;
		Long long4 = 4l;

		Date date0 = new Date();
		Date date1 = new Date();
		Date date2 = new Date();
		Date date3 = new Date();
		Date date4 = new Date();

		concurrentMapStore.add(str0);
		concurrentMapStore.add(str1);
		concurrentMapStore.add(str2);
		concurrentMapStore.add(str3);
		concurrentMapStore.add(str4);

		concurrentMapStore.add(long0);
		concurrentMapStore.add(long1);
		concurrentMapStore.add(long2);
		concurrentMapStore.add(long3);
		concurrentMapStore.add(long4);

		concurrentMapStore.add(date0);
		concurrentMapStore.add(date1);
		concurrentMapStore.add(date2);
		concurrentMapStore.add(date3);
		concurrentMapStore.add(date4);

		return concurrentMapStore;
	}

	@Test
	public void testSingleInstruction() {

		InstructionResolver instructionResolver = new SimpleInstructionResolver(
				getToolResolver(), getDataStore());
		
		instructionResolver.register(InstructionWithoutAnnotation.class);
		
		Instruction<?, ?> instruction0 = instructionResolver.resolveInstruction();
		Instruction<?, ?> instruction1 = instructionResolver.resolveInstruction();
		Instruction<?, ?> instruction2 = instructionResolver.resolveInstruction();
		Instruction<?, ?> instruction3 = instructionResolver.resolveInstruction();
		Instruction<?, ?> instruction4 = instructionResolver.resolveInstruction();
		Instruction<?, ?> instruction5 = instructionResolver.resolveInstruction();
		
		assertFalse(instruction0 == null);
		assertFalse(instruction1 == null);
		assertFalse(instruction2 == null);
		assertFalse(instruction3 == null);
		assertFalse(instruction4 == null);
		assertTrue(instruction5 == null);
	}

	@Test
	public void testMultipleInstruction() {

		DataStore dataStore = getDataStore();
		dataStore.add("String", "str");
		
		InstructionResolver instructionResolver = new SimpleInstructionResolver(
				getToolResolver(), dataStore);
		
		instructionResolver.register(InstructionWithoutAnnotation.class);
		instructionResolver.register(InstructionWithAnnotation.class);

		assertTrue(instructionResolver.numberOfRegisteredInstruction() == 2);
		
		Instruction<?, ?> instruction0 = instructionResolver.resolveInstruction();
		Instruction<?, ?> instruction1 = instructionResolver.resolveInstruction();
		Instruction<?, ?> instruction2 = instructionResolver.resolveInstruction();
		Instruction<?, ?> instruction3 = instructionResolver.resolveInstruction();
		Instruction<?, ?> instruction4 = instructionResolver.resolveInstruction();
		Instruction<?, ?> instruction5 = instructionResolver.resolveInstruction();
		
		assertFalse(instruction0 == null);
		assertFalse(instruction1 == null);
		assertFalse(instruction2 == null);
		assertFalse(instruction3 == null);
		assertFalse(instruction4 == null);
		assertTrue(instruction5 == null);
		
		dataStore.add("String", "str");
		
		Instruction<?, ?> instruction6 = instructionResolver.resolveInstruction();
		Instruction<?, ?> instruction7 = instructionResolver.resolveInstruction();
		
		assertFalse(instruction6 == null);
		assertTrue(instruction7 == null);
		
	}
}
