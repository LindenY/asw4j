package ca.uwaterloo.asw4j.unitTest;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;

import ca.uwaterloo.asw4j.unitTest.TestInstructionWithTreeDependency.TreeRootInstruction;
import ca.uwaterloo.asw4j.ConcurrentMapDataStore;
import ca.uwaterloo.asw4j.DAGInstructionResolver;
import ca.uwaterloo.asw4j.DataStore;
import ca.uwaterloo.asw4j.Instruction;
import ca.uwaterloo.asw4j.meta.Singleton;

public class DAGInstructionResolverTest {

	@Test
	public void testRegisterInstructionClass() {

		DataStore dataStore = new ConcurrentMapDataStore();

		DAGInstructionResolver instructionResolver = new DAGInstructionResolver(
				dataStore);

		instructionResolver
				.registerInstructionClass(TestInstructionWithTreeDependency.TreeRootInstruction.class);

		assertTrue(instructionResolver.numberOfRegisteredInstruction() == 6);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMultipleRegisterInstructionClass() {

		DataStore dataStore = new ConcurrentMapDataStore();

		DAGInstructionResolver instructionResolver = new DAGInstructionResolver(
				dataStore);

		instructionResolver
				.registerInstructionClass(TestInstructionWithTreeDependency.TreeRootInstruction.class);

		instructionResolver
				.registerInstructionClass(TestInstructionWithTreeDependency.TreeLeafInstructionA.class);
	}

	@Test
	public void testMultipleInstructionWithSingleton() throws SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		
		DataStore dataStore = new ConcurrentMapDataStore();
		dataStore.add("String");
		dataStore.add("String");
		
		DAGInstructionResolver instructionResolver = new DAGInstructionResolver(dataStore);
		
		instructionResolver.registerInstructionClass(SingletonInstruction.class);
		
		Instruction<?, ?> leafInstruction0 = instructionResolver.resolveInstruction();
		Instruction<?, ?> leafInstruction1 = instructionResolver.resolveInstruction();
		
		assertFalse(leafInstruction0 == null);
		assertTrue(leafInstruction1 == null);
		
		instructionResolver.afterInstructionExecution(leafInstruction0, null);
		leafInstruction0 = instructionResolver.resolveInstruction();
		leafInstruction1 = instructionResolver.resolveInstruction();
		
		assertFalse(leafInstruction0 == null);
		assertTrue(leafInstruction1 == null);
	}
	
	@Test
	public void testDependingInstructions() throws SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		
		DataStore dataStore = new ConcurrentMapDataStore();
		dataStore.add(new Date());
		dataStore.add(100l);
		dataStore.add("leafC");
		dataStore.add("levelA", "LeafA");
		dataStore.add("levelA", "LeafB");
		dataStore.add(new Date(), "LeafC");
		dataStore.add("Root", "LevelA");
		dataStore.add(1000l, "LevelB");
		
		DAGInstructionResolver instructionResolver = new DAGInstructionResolver(dataStore);
		
		instructionResolver.registerInstructionClass(TreeRootInstruction.class);
		
		Instruction<?, ?> leafInstruction0 = instructionResolver.resolveInstruction();
		instructionResolver.afterInstructionExecution(leafInstruction0, null);
		Instruction<?, ?> leafInstruction1 = instructionResolver.resolveInstruction();
		instructionResolver.afterInstructionExecution(leafInstruction1, null);
		Instruction<?, ?> leafInstruction2 = instructionResolver.resolveInstruction();
		instructionResolver.afterInstructionExecution(leafInstruction2, null);
		Instruction<?, ?> leafInstruction3 = instructionResolver.resolveInstruction();
		instructionResolver.afterInstructionExecution(leafInstruction3, null);
		Instruction<?, ?> leafInstruction4 = instructionResolver.resolveInstruction();
		instructionResolver.afterInstructionExecution(leafInstruction4, null);
		Instruction<?, ?> leafInstruction5 = instructionResolver.resolveInstruction();
		instructionResolver.afterInstructionExecution(leafInstruction5, null);
		Instruction<?, ?> leafInstruction6 = instructionResolver.resolveInstruction();
		
		assertFalse(leafInstruction0 == null);
		assertFalse(leafInstruction1 == null);
		assertFalse(leafInstruction2 == null);
		assertFalse(leafInstruction3 == null);
		assertFalse(leafInstruction4 == null);
		assertFalse(leafInstruction5 == null);
		assertTrue(leafInstruction6 == null);
	}

	
	/**
	 *  Test use {@link Instruction} class
	 */
	@Singleton
	public static class SingletonInstruction extends Instruction<String, String> {

		@Override
		public String execute(String requireData) {
			return null;
		}
	}
}
