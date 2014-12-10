package ca.uwaterloo.asw4j;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ca.uwaterloo.asw4j.internal.InstructionClassDependencyNode;

public abstract class DAGInstructionResolver extends AbstractInstructionResolver {

	private final boolean enablePooling;
	private List<MarkableDependencyNode> instructionNodes;
	
	public DAGInstructionResolver(DataStore dataStore) {
		this(null, dataStore, true);
	}
	
	public DAGInstructionResolver(ToolResolver toolResolver, DataStore dataStore, boolean enablePooling) {
		super(toolResolver, dataStore);
		
		this.enablePooling = enablePooling;
		this.instructionNodes = new ArrayList<DAGInstructionResolver.MarkableDependencyNode>();
	}

	public int numberOfRegisteredInstruction() {
		return 0;
	}

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass) {
		
	}

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String produceDataName) {
		
	}

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes) {
		
	}

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes,
			String produceDataName) {
		
	}

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes,
			String produceDataName,
			boolean supportSingleton) {
		
	}
	
	public Instruction<?, ?> resolveInstruction() {
		return null;
	}

	@Override
	public void afterInstructionExecution(Instruction<?, ?> instruction,
			Throwable throwed) {
		super.afterInstructionExecution(instruction, throwed);
	}

	@Override
	public void beforInstructionExecution(Instruction<?, ?> instruction) {
		super.beforInstructionExecution(instruction);
	}

	private void solveDependentsOrder() {
		
	}
	
	private void visitDependencyNode(MarkableDependencyNode dependencyNode) {
		
	}
	
	private static class MarkableDependencyNode extends InstructionClassDependencyNode {
		
		public enum MARK {
			unmark,
			temp,
			marked
		}
		
		private MARK mark;
		
		public MarkableDependencyNode (
				Class<? extends Instruction<?, ?>> instructionClass,
				String[] requireDataNames, 
				Type[] requireDataTypes,
				String produceDataName,
				Type produceDataType,
				boolean supportSingleton,
				boolean enablePooling,
				Class<? extends Instruction<?, ?>>[] dependencies,
				boolean supportAsync ) {
			
			super(
					instructionClass,
					requireDataNames,
					requireDataTypes,
					produceDataName,
					produceDataType,
					supportSingleton,
					enablePooling,
					dependencies,
					supportAsync
				);
			
			mark = MARK.unmark;
		}
		
		public boolean mark(MARK mark) {
			if (this.mark == mark) {
				return false;
			}
			this.mark = mark;
			return true;
		}
	}
}
