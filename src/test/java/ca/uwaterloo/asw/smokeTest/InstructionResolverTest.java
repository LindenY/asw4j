package ca.uwaterloo.asw.smokeTest;

import java.util.Date;

import org.junit.Test;
import static org.junit.Assert.*;

import ca.uwaterloo.asw.ConcurrentMapDataStore;
import ca.uwaterloo.asw.DAGInstructionResolver;
import ca.uwaterloo.asw.DataStore;
import ca.uwaterloo.asw.Instruction;
import ca.uwaterloo.asw.InstructionResolver;
import ca.uwaterloo.asw.ToolResolver;
import ca.uwaterloo.asw.testObjects.TestInstruction0;


public class InstructionResolverTest {

	
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
	
	@Test
	public void testSingleInstruction() {
		
		Class<? extends Instruction<Date, String>> instructionClass = TestInstruction0.class;
		
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
		assertFalse(instruction5 == null);
		
		
		/*instruction0.run();
		System.out.println(instruction0.getResult().toString());*/
	}
}
