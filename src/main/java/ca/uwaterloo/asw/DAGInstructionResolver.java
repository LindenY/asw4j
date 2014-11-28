package ca.uwaterloo.asw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import ca.uwaterloo.asw.internal.InstructionNode;

public class DAGInstructionResolver extends AbstractInstructionResolver {

	private List<DependencyNode> dependencyNodes;
	private DependencyTree dependencyTree;

	public DAGInstructionResolver(ToolResolver toolResolver, DataStore dataStore) {
		super(toolResolver, dataStore);
		dependencyTree = new DependencyTree();
		dependencyNodes = new ArrayList<DAGInstructionResolver.DependencyNode>();
	}

	public void register(String[] requiredDataNames,
			Class<?>[] requiredDataTypes, String producedDataName,
			Class<?> producedDataType,
			Class<? extends Instruction<?, ?>> instructionClass) {

		DependencyNode newDN = new DependencyNode(requiredDataNames,
				requiredDataTypes, producedDataName, producedDataType,
				instructionClass);
		dependencyNodes.add(newDN);

		for (Class<? extends Instruction<?, ?>> ins : newDN.getDependencies()) {
			register(ins);
		}
		
		dependencyTree.solveDependencyTree();
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
		return dependencyNodes.size();
	}

	public Instruction<?, ?> resolveInstruction() {

		Iterator<DependencyNode> iterator = dependencyTree.dependentsOrder.iterator();
		
		while (iterator.hasNext()) {
			DependencyNode nextDN = iterator.next();
			
			if (nextDN.state == DependencyNode.STATE.blocking || 
					nextDN.state == DependencyNode.STATE.terminated ) {
				continue;
			} else {
				
				if (dataStore.contain(nextDN.getRequireDatas())) {
					
					Instruction<?, ?> instruction = nextDN.getInstructionInstance(toolResolver);
					instruction.setRequiredData(dataStore.getAndRemoveAll(nextDN.getRequireDatas()));
					return instruction;
					
				} else {
					
					if (nextDN.issuedNum.get() <= 0) {
						boolean readyToTerminate = true;
						for (DependencyNode cdn : nextDN.children) {
							if (cdn.state != DependencyNode.STATE.terminated) {
								readyToTerminate = false;
								break;
							}
						}
						
						if (readyToTerminate) {
							nextDN.state = DependencyNode.STATE.terminated;
						}
					}
				}
			}
		}
		
		
		return null;
	}

	public void beforInstructionExecution(Instruction<?, ?> instruction) {

	}

	public void afterInstructionExecution(Instruction<?, ?> instruction) {
		DependencyNode dn = dependencyTree.instructionClassMap.get(instruction.getClass());
		dn.returnInstructionInstance(instruction);
	}

	private class DependencyTree {
		
		//private ConcurrentHashMap<Class<? extends Instruction<?, ?>>, DependencyNode> instructionClassMap;
		private HashMap<Class<? extends Instruction<?, ?>>, DependencyNode> instructionClassMap;
		private List<DependencyNode> dependentsOrder;
		
		// TODO : Using Instruction Class as key in map can potentially have
		// bug, because all parameterized arguments are removed from
		// GenericType, which means that the key would be identical for
		// different instruction class. We can use TypeToken as the literal
		// representation of instruction class, which requires us to fix the TypeToken
		// object and make it partial support GenericType.
		public void solveDependencyTree() {

			Map<Class<? extends Instruction<?, ?>>, DependencyNode> dependentMap = 
					new HashMap<Class<? extends Instruction<?, ?>>, DAGInstructionResolver.DependencyNode>();
			/*instructionClassMap =
					new ConcurrentHashMap<Class<? extends Instruction<?,?>>, DAGInstructionResolver.DependencyNode>();*/
			instructionClassMap =
					new HashMap<Class<? extends Instruction<?,?>>, DAGInstructionResolver.DependencyNode>();
			
			Iterator<DependencyNode> dnListIterator = dependencyNodes.iterator();
			while (dnListIterator.hasNext()) {
				DependencyNode nextDNinList = dnListIterator.next();
				
				nextDNinList.clear();
				
				DependencyNode duplicateDependent = dependentMap.put(nextDNinList.getInstruction(), nextDNinList);
				if (duplicateDependent != null) {
					// TODO : change to a more explicit exception
					throw new IllegalArgumentException("Duplicated parent node");
				}
				
				instructionClassMap.put(nextDNinList.getInstruction(), nextDNinList);
			}
			
			
			Iterator<Class<? extends Instruction<?, ?>>> dependentKeySetIterator 
				= dependentMap.keySet().iterator();
			while (dependentKeySetIterator.hasNext()) {
				Class<? extends Instruction<?, ?>> nextIC = dependentKeySetIterator.next();
				DependencyNode child = instructionClassMap.get(nextIC);
				DependencyNode parent = dependentMap.get(nextIC);
				
				child.setParent(parent);
				parent.addChild(child);
			}
			
			generateDependentOrder();
		}
		
		private void generateDependentOrder() {
			
			dependentsOrder = new ArrayList<DAGInstructionResolver.DependencyNode>();
			
			Iterator<DependencyNode> iterator = dependencyNodes.iterator();
			while (iterator.hasNext()) {
				DependencyNode nextDN = iterator.next();
				visitDAGNode(nextDN);
			}
		}
		
		private void visitDAGNode(DependencyNode dependencyNode) {
			
			if (dependencyNode == null) {
				return;
			}
			
			if (dependencyNode.mark(DependencyNode.MARK.marked)) {
				return;
			} else if (dependencyNode.mark(DependencyNode.MARK.temp)) {
				// TODO : change to a more explicit exception
				throw new IllegalArgumentException("There is at least on cirlce in dependency");
			} else {
				dependencyNode.mark(DependencyNode.MARK.temp);
				visitDAGNode(dependencyNode.parent);
				dependencyNode.mark(DependencyNode.MARK.marked);
				
				dependentsOrder.add(0, dependencyNode);
			}
		}
	}

	private static class DependencyNode extends InstructionNode {
		
		public enum MARK {
			unmark,
			temp,
			marked
		}
		
		public enum STATE {
			ready,
			blocking,
			terminated
		}

		private DependencyNode parent;
		private List<DependencyNode> children;
		
		private List<Instruction<?, ?>> pool;
		private AtomicInteger issuedNum = new AtomicInteger(0);
		private volatile STATE state = STATE.ready;
		
		private MARK mark;
		
		public DependencyNode(String[] requiredDataNames,
				Class<?>[] requiredDataTypes, String producedDataName,
				Class<?> producedDataType,
				Class<? extends Instruction<?, ?>> instructionClass) {
			super(requiredDataNames, requiredDataTypes, producedDataName,
					producedDataType, instructionClass);
			
			pool = new ArrayList<Instruction<?,?>>();
		}

		public DependencyNode getParent() {
			return parent;
		}

		public List<DependencyNode> getChildren() {
			return children;
		}

		public void setParent(DependencyNode parent) {
			this.parent = parent;
		}

		public void addChild(DependencyNode child) {
			children.add(child);
		}
		
		public void clear() {
			mark = MARK.unmark;
			parent = null;
			children = new ArrayList<DAGInstructionResolver.DependencyNode>();
		}
		
		public boolean mark(MARK mark) {
			if (this.mark == mark) {
				return true;
			}
			this.mark = mark;
			return false;
		}

		
		// TODO: Two singleton checking is here, because we want to prevent the
		// race condition. we can think of better way to write this later
		@Override
		public Instruction<?, ?> getInstructionInstance(ToolResolver toolResolver) {
			
			if (issuedNum.get() > 0 && supportSingleton) {
				return null;
			}
			
			issuedNum.incrementAndGet();
			Instruction<?, ?> instruction = null;
			
			if (pool.size() <= 0) {
				instruction = super.getInstructionInstance(toolResolver);
			} else {
				synchronized (pool) {
					instruction = pool.remove(pool.size());
				}
			}
			
			if (issuedNum.get() > 0 && supportSingleton) {
				state = STATE.blocking;
			}
			
			return instruction;
		}
		
		public void returnInstructionInstance(Instruction<?, ?> instruction) {
			
			synchronized (pool) {
				pool.add(instruction);
			}
			
			issuedNum.decrementAndGet();
		}
		
	}
}
