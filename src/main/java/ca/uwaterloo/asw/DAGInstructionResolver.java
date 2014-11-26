package ca.uwaterloo.asw;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DAGInstructionResolver extends AbstractInstructionResolver {

	public DAGInstructionResolver(ToolResolver toolResolver, DataStore dataStore) {
		super(toolResolver, dataStore);
		// TODO Auto-generated constructor stub
	}
	
	public void register(String[] requiredDataNames,
			Class<?>[] requiredDataTypes, String producedDataName,
			Class<?> producedDataType,
			Class<? extends Instruction<?, ?>> instructionClass) {
		
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
		// TODO Auto-generated method stub

	}

	public int numberOfRegisteredInstruction() {
		// TODO Auto-generated method stub
		return 0;
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

	private static class DependencyNode {

		private String dependencyName;

		private Class<? extends Instruction<?, ?>> self;
		private List<Class<? extends Instruction<?, ?>>> dependencies;
		private List<Class<? extends Instruction<?, ?>>> markList;

		public DependencyNode(
				Class<? extends Instruction<?, ?>> instructionClass) {
			self = instructionClass;
			dependencies = new ArrayList<Class<? extends Instruction<?, ?>>>();
		}

		public void addDependency(
				Class<? extends Instruction<?, ?>> instructionClass) {
			dependencies.add(instructionClass);
		}

		public String getDependencyName() {
			return dependencyName;
		}

		public Class<? extends Instruction<?, ?>> getSelf() {
			return self;
		}

		public List<Class<? extends Instruction<?, ?>>> getDependencies() {
			return dependencies;
		}

		private void mark(Class<? extends Instruction<?, ?>> instrucionClass) {
			if (dependencies.remove(instrucionClass)) {
				if (markList == null) {
					markList = new ArrayList<Class<? extends Instruction<?, ?>>>();
				}
				markList.add(instrucionClass);
			}
		}

		private void unmarkAll() {
			if (markList != null) {
				dependencies.addAll(markList);
				markList = null;
			}
		}

		public static List<DependencyNode> solveDAG(
				List<DependencyNode> dependencyNodes) {
			List<DependencyNode> resultList = new ArrayList<DependencyNode>();

			while (dependencyNodes.size() > 0) {

				boolean containCircle = true;

				Iterator<DependencyNode> iterator = dependencyNodes.iterator();
				while (iterator.hasNext()) {
					DependencyNode nextDn = iterator.next();
					if (nextDn.getDependencies().size() <= 0) {

						Iterator<DependencyNode> subIterator = dependencyNodes
								.iterator();
						while (subIterator.hasNext()) {
							subIterator.next().mark(nextDn.self);
						}

						nextDn.unmarkAll();
						resultList.add(nextDn);
						dependencyNodes.remove(nextDn);
						containCircle = false;
					}
				}

				if (containCircle) {
					throw new IllegalArgumentException(
							"Can't not solver DAG because there is at least one circle");
				}
			}

			return resultList;
		}
	}
}
