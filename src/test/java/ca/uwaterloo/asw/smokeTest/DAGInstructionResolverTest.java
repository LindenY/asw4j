package ca.uwaterloo.asw.smokeTest;

import java.lang.IllegalArgumentException;

import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
import ca.uwaterloo.asw.ConcurrentMapDataStore;
import ca.uwaterloo.asw.DAGInstructionResolver;
import ca.uwaterloo.asw.DataStore;
import ca.uwaterloo.asw.Instruction;
import ca.uwaterloo.asw.InstructionResolver;
import ca.uwaterloo.asw.ToolResolver;
import ca.uwaterloo.asw.testObjects.TestInstructionWithoutDependency.InstructionWithoutAnnotation;
import ca.uwaterloo.asw.testObjects.TestInstructionWithoutDependency.InstructionWithAnnotation;
import ca.uwaterloo.asw.testObjects.TestInstructionWithoutDependency.InstructionWithIncorrectRequireType;
import ca.uwaterloo.asw.testObjects.TestInstructionWithTreeDependency.TreeRootInstruction;

public class DAGInstructionResolverTest {

	
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
	
	private ToolResolver getToolResolver() {
		return new ToolResolver() {
			public void registerTool(String name, Object tool) {
			}
			public Object getTool(String name) {
				return null;
			}
		};
	}
	
	private  InstructionResolver getInstructionResolver() {
		return new DAGInstructionResolver(getToolResolver(), getDataStore());
	}
	
	
	@Ignore
	@Test
	public void testSingleInstruction() {
		
		Class<? extends Instruction<Date, String>> instructionClass = InstructionWithoutAnnotation.class;
		
		InstructionResolver instructionResolver = getInstructionResolver();
		instructionResolver.register(instructionClass);
		
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
		
		
		instruction0.run();
		instruction1.run();
		instruction2.run();
		instruction3.run();
		instruction4.run();
		
		assertFalse(instruction0.getResult() == null || instruction0.getResult().equals(""));
		assertFalse(instruction1.getResult() == null || instruction1.getResult().equals(""));
		assertFalse(instruction2.getResult() == null || instruction2.getResult().equals(""));
		assertFalse(instruction3.getResult() == null || instruction3.getResult().equals(""));
		assertFalse(instruction4.getResult() == null || instruction4.getResult().equals(""));
	}

	@Ignore
	@Test
	public void testSingleInstructionWithAnnotation() {
		
		DataStore dataStore = new ConcurrentMapDataStore();
		dataStore.add("str0", "str");
		dataStore.add("str1", "str");
		dataStore.add("str2", "str");
		dataStore.add("str3", "str");
		dataStore.add("str4", "str");
		
		InstructionResolver instructionResolver = new DAGInstructionResolver(getToolResolver(), dataStore);
		
		Class<? extends Instruction<?, ?>> instructionClass = InstructionWithAnnotation.class;
		instructionResolver.register(instructionClass);
	
		Instruction<?, ?> instruction0 = instructionResolver.resolveInstruction();
		Instruction<?, ?> instruction1 = instructionResolver.resolveInstruction();
		Instruction<?, ?> instruction2 = instructionResolver.resolveInstruction();
		
		assertFalse(instruction0 == null);
		assertFalse(instruction1 == null);
		assertTrue(instruction2 == null);
		
		instruction0.run();
		assertTrue(instruction0.getResult().equals("str0str1"));
		instruction1.run();
		assertTrue(instruction1.getResult().equals("str2str3"));
		
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void testSingleInstructionWithIncorrectRequireType() {
		
		InstructionResolver instructionResolver = getInstructionResolver();
		
		Class<? extends Instruction<?, ?>> instructionClass = InstructionWithIncorrectRequireType.class;
		
		instructionResolver.register(instructionClass);
	}

	
	@Ignore
	@Test
	public void testMultipleInstructionWithoutDependency() {
		
		DataStore dataStore = getDataStore();
		dataStore.add("str0", "str");
		dataStore.add("str1", "str");
		dataStore.add("str2", "str");
		dataStore.add("str3", "str");
		dataStore.add("str4", "str");
		
		InstructionResolver instructionResolver = new DAGInstructionResolver(getToolResolver(), dataStore);
		
		Class<? extends Instruction<?, ?>> instructionClassWithoutAnnotation = InstructionWithoutAnnotation.class;
		Class<? extends Instruction<?, ?>> instructionClassWithAnnotation = InstructionWithAnnotation.class;
		
		instructionResolver.register(instructionClassWithoutAnnotation);
		instructionResolver.register(instructionClassWithAnnotation);
		
		Instruction<?, ?> instruction0 = instructionResolver.resolveInstruction();
		Instruction<?, ?> instruction1 = instructionResolver.resolveInstruction();
		Instruction<?, ?> instruction2 = instructionResolver.resolveInstruction();
		Instruction<?, ?> instruction3 = instructionResolver.resolveInstruction();
		Instruction<?, ?> instruction4 = instructionResolver.resolveInstruction();
		Instruction<?, ?> instruction5 = instructionResolver.resolveInstruction();
		Instruction<?, ?> instruction6 = instructionResolver.resolveInstruction();
		Instruction<?, ?> instruction7 = instructionResolver.resolveInstruction();
		
		assertFalse(instruction0 == null);
		assertFalse(instruction1 == null);
		assertFalse(instruction2 == null);
		assertFalse(instruction3 == null);
		assertFalse(instruction4 == null);
		assertFalse(instruction5 == null);
		assertFalse(instruction6 == null);
		assertTrue(instruction7 == null);
		
		instruction0.run();
		instruction1.run();
		instruction2.run();
		instruction3.run();
		instruction4.run();
		instruction5.run();
		instruction6.run();
		
		assertFalse((instruction0.getResult() == null) || (instruction0.getResult().equals("")));
		assertFalse((instruction1.getResult() == null) || (instruction1.getResult().equals("")));
		assertFalse((instruction2.getResult() == null) || (instruction2.getResult().equals("")));
		assertFalse((instruction3.getResult() == null) || (instruction3.getResult().equals("")));
		assertFalse((instruction4.getResult() == null) || (instruction4.getResult().equals("")));
		assertFalse((instruction5.getResult() == null) || (instruction5.getResult().equals("")));
		assertFalse((instruction6.getResult() == null) || (instruction6.getResult().equals("")));
	}
	
	
	@Ignore
	@Test 
	public void testMultipleInstructionWithTreeDependency() {
		
		DataStore dataStore = new ConcurrentMapDataStore();
		dataStore.add(new Date());
		dataStore.add(100l);
		dataStore.add("leafC");
		dataStore.add("levelA", "LeafA");
		dataStore.add("levelA", "LeafB");
		dataStore.add(new Date(), "LeafC");
		dataStore.add("Root", "LevelA");
		dataStore.add(1000l, "LevelB");
		
		InstructionResolver instructionResolver = new DAGInstructionResolver(getToolResolver(), dataStore);
		
		Class<? extends Instruction<?, ?>> instructionClass = TreeRootInstruction.class;
		instructionResolver.register(instructionClass);
		
		Instruction<?, ?> leafInstruction0 = instructionResolver.resolveInstruction();
		instructionResolver.afterInstructionExecution(leafInstruction0);
		Instruction<?, ?> leafInstruction1 = instructionResolver.resolveInstruction();
		instructionResolver.afterInstructionExecution(leafInstruction1);
		Instruction<?, ?> leafInstruction2 = instructionResolver.resolveInstruction();
		instructionResolver.afterInstructionExecution(leafInstruction2);
		Instruction<?, ?> leafInstruction3 = instructionResolver.resolveInstruction();
		instructionResolver.afterInstructionExecution(leafInstruction3);
		Instruction<?, ?> leafInstruction4 = instructionResolver.resolveInstruction();
		instructionResolver.afterInstructionExecution(leafInstruction4);
		Instruction<?, ?> leafInstruction5 = instructionResolver.resolveInstruction();
		instructionResolver.afterInstructionExecution(leafInstruction5);
		Instruction<?, ?> leafInstruction6 = instructionResolver.resolveInstruction();
		
		assertFalse(leafInstruction0 == null);
		assertFalse(leafInstruction1 == null);
		assertFalse(leafInstruction2 == null);
		assertFalse(leafInstruction3 == null);
		assertFalse(leafInstruction4 == null);
		assertFalse(leafInstruction5 == null);
		assertTrue(leafInstruction6 == null);
		
	}

	
	@Ignore
	@Test
	public void testMultipleInstructionWithSingleton() {
		
		DataStore dataStore = new ConcurrentMapDataStore();
		dataStore.add("LeafC String");
		dataStore.add("LeafC String");
		
		InstructionResolver instructionResolver = new DAGInstructionResolver(getToolResolver(), dataStore);
		
		instructionResolver.register(TreeRootInstruction.class);
		
		Instruction<?, ?> leafInstruction0 = instructionResolver.resolveInstruction();
		Instruction<?, ?> leafInstruction1 = instructionResolver.resolveInstruction();
		
		assertFalse(leafInstruction0 == null);
		assertTrue(leafInstruction1 == null);
		
		instructionResolver.afterInstructionExecution(leafInstruction0);
		leafInstruction0 = instructionResolver.resolveInstruction();
		leafInstruction1 = instructionResolver.resolveInstruction();
		
		assertFalse(leafInstruction0 == null);
		assertTrue(leafInstruction1 == null);
	}

	
	@Test
	public void testMultipleInstructionWithAsync() {
		
		DataStore dataStore = new ConcurrentMapDataStore();
		dataStore.add(new Date());
		dataStore.add(100l);
		dataStore.add("LevelA String", "LeafA");
		dataStore.add("LevelA String", "LeafB");
		
		InstructionResolver instructionResolver = new DAGInstructionResolver(getToolResolver(), dataStore);
		instructionResolver.register(TreeRootInstruction.class);
		
		Instruction<?,?> leafInstruction0 = instructionResolver.resolveInstruction();
		Instruction<?,?> leafInstruction1 = instructionResolver.resolveInstruction();
		Instruction<?,?> levelInstruction0 = instructionResolver.resolveInstruction();
		
		assertFalse(leafInstruction0 == null);
		assertFalse(leafInstruction1 == null);
		assertTrue(levelInstruction0 == null);
	}
}
