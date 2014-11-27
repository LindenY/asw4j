package ca.uwaterloo.asw;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.uwaterloo.asw.internal.InstructionNode;
import ca.uwaterloo.asw.reflection.TypeToken;

public class DAGInstructionResolver extends AbstractInstructionResolver {

	private DependencyTree dependencyTree;

	public DAGInstructionResolver(ToolResolver toolResolver, DataStore dataStore) {
		super(toolResolver, dataStore);
		dependencyTree = new DependencyTree();
	}

	public void register(String[] requiredDataNames,
			Class<?>[] requiredDataTypes, String producedDataName,
			Class<?> producedDataType,
			Class<? extends Instruction<?, ?>> instructionClass) {

		dependencyTree.addDependencyNode(new DependencyNode(requiredDataNames,
				requiredDataTypes, producedDataName, producedDataType,
				instructionClass));
	}

	public void register(Class<?>[] requiredDataTypes,
			Class<?> producedDataType,
			Class<? extends Instruction<?, ?>> instructionClass) {

	}

	public void register(String[] requiredDataNames, String producedDataName,
			Class<? extends Instruction<?, ?>> instructionClass) {
	}

	public void register(String[] requiredDataNames,
			Class<? extends Instruction<?, ?>> instructionClass) {
		// TODO Auto-generated method stub

	}

	public void register(String producedDataName,
			Class<? extends Instruction<?, ?>> instructionClass) {
		// TODO Auto-generated method stub

	}

	public void register(Class<? extends Instruction<?, ?>> instructionClass) {

		String[] requireDataNames = InstructionNode
				.getInstructionRequireDataNames(instructionClass);
		Class<?>[] requireDataTypes = InstructionNode
				.getInstructionRequireDataTypes(instructionClass);
		String produceDataName = InstructionNode
				.getInstructionProduceDataName(instructionClass);
		Class<?> produceDataType = InstructionNode
				.getInstructionProduceDataType(instructionClass);

		register(requireDataNames, requireDataTypes, produceDataName,
				produceDataType, instructionClass);
	}

	public int numberOfRegisteredInstruction() {
		return instructionNodes.size();
	}

	public Instruction<?, ?> resolveInstruction() {
		// TODO Auto-generated method stub
		return null;
	}

	public void beforInstructionExecution(Instruction<?, ?> instruction) {
		// TODO Auto-generated method stub

	}

	public void afterInstructionExecution(Instruction<?, ?> instruction) {
		// TODO Auto-generated method stub

	}

	public void flush() {

	}

	private final class DependencyTree {

		private Map<TypeToken<?>, DependencyNode> produceDataMap;
		private DependencyNode root;
		private Set<DependencyNode> mud;

		public void addDependencyNode(DependencyNode dependencyNode) {

		}

		public static DependencyNode solveDependencyByProduceData(
				DependencyNode dependencyNode) {

		}

	}

	private static final class DependencyNode extends InstructionNode {

		private DependencyNode parentNode;
		private List<DependencyNode> childrenNodes;

		private boolean finished;

		public DependencyNode(String[] requiredDataNames,
				Class<?>[] requiredDataTypes, String producedDataName,
				Class<?> producedDataType,
				Class<? extends Instruction<?, ?>> instructionClass) {
			super(requiredDataNames, requiredDataTypes, producedDataName,
					producedDataType, instructionClass);

			childrenNodes = new ArrayList<DAGInstructionResolver.DependencyNode>();
		}

		public void addDependency(
				Class<? extends Instruction<?, ?>> instructionClass) {
			dependencies.add(instructionClass);
		}

		public void addChildrenNode(DependencyNode dependencyNode) {
			dependencyNode.setParentNode(this);
			childrenNodes.add(dependencyNode);
		}

		public DependencyNode setParentNode(DependencyNode dependencyNode) {
			DependencyNode temp = parentNode;
			parentNode = dependencyNode;
			return temp;
		}

		public List<Class<? extends Instruction<?, ?>>> getDependencies() {
			return dependencies;
		}

	}
}
