package ca.uwaterlo.asw4j.unitTest;

import java.util.Date;

import ca.uwaterloo.asw4j.DataNode;
import ca.uwaterloo.asw4j.Instruction;
import ca.uwaterloo.asw4j.meta.DependOn;
import ca.uwaterloo.asw4j.meta.ProduceData;
import ca.uwaterloo.asw4j.meta.RequireData;
import ca.uwaterloo.asw4j.meta.Singleton;

/**
 * <pre>
 * 	Test use {@link Instruction} classes with dependency relationship.
 * </pre>
 * 
 * <p>
 *  Dependency Structure:
 * </p>
 * <p>
 * 	TreeRootInstruction
 * </p>
 * <p>
 *  TreeLevelInstrucionA || TreeLevelInstructionB(Asyc=false)
 * </p>
 * <p>
 *	TreeLeafInstructionA || TreeLeafInstructionB <> TreeLeafInstructionC(Singleton)
 * </p>
 * 
 */
public class TestInstructionWithTreeDependency {

	
	@RequireData(names={"LevelA", "LevelB"}, types={String.class, Long.class})
	@DependOn(instructions={TreeLevelInstructionA.class, TreeLevelInstructionB.class})
	public static class TreeRootInstruction extends Instruction<DataNode, Long> {
		@Override
		public Long execute(DataNode requireData) {
			return null;
		}
	}
	
	@RequireData(names={"LeafA", "LeafB"}, types={String.class, String.class})
	@ProduceData(name="LevelA")
	@DependOn(instructions={TreeLeafInstructionA.class, TreeLeafInstructionB.class}, async=false)
	public static class TreeLevelInstructionA extends Instruction<DataNode, String> {
		@Override
		public String execute(DataNode requireData) {
			return null;
		}
	}
	
	@RequireData(names={"LeafC"}, types={Date.class})
	@ProduceData(name="LevelB")
	@DependOn(instructions={TreeLeafInstructionC.class})
	public static class TreeLevelInstructionB extends Instruction<Date, Long> {
		@Override
		public Long execute(Date requireData) {
			return null;
		}
	}
	
	@ProduceData(name="LeafA")
	public static class TreeLeafInstructionA extends Instruction<Date, String> {
		@Override
		public String execute(Date requireData) {
			return null;
		}
	}
	
	@ProduceData(name="LeafB")
	public static class TreeLeafInstructionB extends Instruction<Long, String> {
		@Override
		public String execute(Long requireData) {
			return null;
		}
	}
	
	@ProduceData(name="LeafC")
	@Singleton
	public static class TreeLeafInstructionC extends Instruction<String, String> {
		@Override
		public String execute(String requireData) {
			return null;
		}
	}
}
