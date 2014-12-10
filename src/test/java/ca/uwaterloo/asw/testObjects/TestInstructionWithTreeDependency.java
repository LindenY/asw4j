package ca.uwaterloo.asw.testObjects;

import java.util.Date;

import ca.uwaterloo.asw4j.DataNode;
import ca.uwaterloo.asw4j.Instruction;
import ca.uwaterloo.asw4j.ToolResolver;
import ca.uwaterloo.asw4j.meta.DependOn;
import ca.uwaterloo.asw4j.meta.ProduceData;
import ca.uwaterloo.asw4j.meta.RequireData;
import ca.uwaterloo.asw4j.meta.Singleton;

/**
 * Dependency Structure:
 * 		                       TreeRootInstruction
 * 		                       /                \
 *               TreeLevelInstrucionA    TreeLevelInstructionB(Asyc=false)
 *                  /          \                       \
 *	TreeLeafInstructionA    TreeLeafInstructionB    TreeLeafInstructionC(Singleton)
 */
public class TestInstructionWithTreeDependency {

	
	@RequireData(names={"LevelA", "LevelB"}, types={String.class, Long.class})
	@DependOn(instructions={TreeLevelInstructionA.class, TreeLevelInstructionB.class})
	public static class TreeRootInstruction extends Instruction<DataNode, Long> {

		public TreeRootInstruction(ToolResolver toolResolver) {
			super(toolResolver);
		}

		@Override
		public void preExecution() {
		}

		@Override
		public Long execute(DataNode requireData) {
			return null;
		}

		@Override
		public void postExecution() {
		}
	}
	
	@RequireData(names={"LeafA", "LeafB"}, types={String.class, String.class})
	@ProduceData(name="LevelA")
	@DependOn(instructions={TreeLeafInstructionA.class, TreeLeafInstructionB.class}, async=false)
	public static class TreeLevelInstructionA extends Instruction<DataNode, String> {

		public TreeLevelInstructionA(ToolResolver toolResolver) {
			super(toolResolver);
		}

		@Override
		public void preExecution() {
		}

		@Override
		public String execute(DataNode requireData) {
			return null;
		}

		@Override
		public void postExecution() {
		}
		
	}
	
	@RequireData(names={"LeafC"}, types={Date.class})
	@ProduceData(name="LevelB")
	@DependOn(instructions={TreeLeafInstructionC.class})
	public static class TreeLevelInstructionB extends Instruction<Date, Long> {

		public TreeLevelInstructionB(ToolResolver toolResolver) {
			super(toolResolver);
		}

		@Override
		public void preExecution() {
		}

		@Override
		public Long execute(Date requireData) {
			return null;
		}

		@Override
		public void postExecution() {
			
		}
	}
	
	@ProduceData(name="LeafA")
	public static class TreeLeafInstructionA extends Instruction<Date, String> {

		public TreeLeafInstructionA(ToolResolver toolResolver) {
			super(toolResolver);
		}

		@Override
		public void preExecution() {
		}

		@Override
		public String execute(Date requireData) {
			return null;
		}

		@Override
		public void postExecution() {
		}
		
	}
	
	@ProduceData(name="LeafB")
	public static class TreeLeafInstructionB extends Instruction<Long, String> {

		public TreeLeafInstructionB(ToolResolver toolResolver) {
			super(toolResolver);
		}

		@Override
		public void preExecution() {
		}

		@Override
		public String execute(Long requireData) {
			return null;
		}

		@Override
		public void postExecution() {	
		}
		
	}
	
	@ProduceData(name="LeafC")
	@Singleton
	public static class TreeLeafInstructionC extends Instruction<String, String> {

		public TreeLeafInstructionC(ToolResolver toolResolver) {
			super(toolResolver);
		}

		@Override
		public void preExecution() {
			
		}

		@Override
		public String execute(String requireData) {
			return null;
		}

		@Override
		public void postExecution() {
		}
		
	}
}
