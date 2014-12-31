package ca.uwaterloo.asw4j;

import java.lang.reflect.Type;

import ca.uwaterloo.asw4j.internal.InstructionClassDependencyNode;
import ca.uwaterloo.asw4j.internal.InstructionClassNode;
import ca.uwaterloo.asw4j.internal.InstructionClassUtility;

/**
 * A sub abstract class of {@link AbstractInstructionResolver}.
 * 
 * <p>
 * {@link AbstractDependencyInstructionResolver} defined to extend more
 * dependencies related functions and fields from
 * {@link AbstractInstructionResolver} for dependencies related
 * {@link InstructionResolver}.
 * </p>
 * 
 * @author Desmond Lin
 * @since 1.0.1
 */
public abstract class AbstractDependencyInstructionResolver extends
		AbstractInstructionResolver implements DependencyInstructionResolver {

	public AbstractDependencyInstructionResolver(DataStore dataStore,
			ToolResolver toolResolver, boolean enablePooling) {
		super(dataStore, toolResolver, enablePooling);
	}

	/**
	 * // *
	 * <p>
	 * {@inheritDoc}
	 * </p>
	 * <p>
	 * The depending {@link Instruction} classes of the registered
	 * {@link Instruction} are recursively registered.
	 * </p>
	 */
	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass) {

		registerInstructionClass(instructionClass,
				InstructionClassUtility
						.getInstructionRequireDataNames(instructionClass),
				InstructionClassUtility
						.getInstructionRequireDataTypes(instructionClass),
				InstructionClassUtility
						.getInstructionProduceDataName(instructionClass),
				InstructionClassUtility
						.getInstructionSingletonSupport(instructionClass),
				InstructionClassUtility
						.getInstructionDependencies(instructionClass),
				InstructionClassUtility
						.getInstructionAsyncSupport(instructionClass));
	}

	/**
	 * <p>
	 * {@inheritDoc}
	 * </p>
	 * <p>
	 * The depending {@link Instruction} classes of the registered
	 * {@link Instruction} are recursively registered.
	 * </p>
	 */
	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String produceDataName) {

		registerInstructionClass(instructionClass,
				InstructionClassUtility
						.getInstructionRequireDataNames(instructionClass),
				InstructionClassUtility
						.getInstructionRequireDataTypes(instructionClass),
				produceDataName,
				InstructionClassUtility
						.getInstructionSingletonSupport(instructionClass),
				InstructionClassUtility
						.getInstructionDependencies(instructionClass),
				InstructionClassUtility
						.getInstructionAsyncSupport(instructionClass));
	}

	/**
	 * <p>
	 * {@inheritDoc}
	 * </p>
	 * <p>
	 * The depending {@link Instruction} classes of the registered
	 * {@link Instruction} are recursively registered.
	 * </p>
	 */
	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes) {

		registerInstructionClass(instructionClass, requireDataNames,
				requireDataTypes,
				InstructionClassUtility
						.getInstructionProduceDataName(instructionClass),
				InstructionClassUtility
						.getInstructionSingletonSupport(instructionClass),
				InstructionClassUtility
						.getInstructionDependencies(instructionClass),
				InstructionClassUtility
						.getInstructionAsyncSupport(instructionClass));
	}

	/**
	 * <p>
	 * {@inheritDoc}
	 * </p>
	 * <p>
	 * The depending {@link Instruction} classes of the registered
	 * {@link Instruction} are recursively registered.
	 * </p>
	 */
	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes,
			String produceDataName) {

		registerInstructionClass(instructionClass, requireDataNames,
				requireDataTypes, produceDataName,
				InstructionClassUtility
						.getInstructionSingletonSupport(instructionClass),
				InstructionClassUtility
						.getInstructionDependencies(instructionClass),
				InstructionClassUtility
						.getInstructionAsyncSupport(instructionClass));
	}

	/**
	 * <p>
	 * {@inheritDoc}
	 * </p>
	 * <p>
	 * The depending {@link Instruction} classes of the registered
	 * {@link Instruction} are recursively registered.
	 * </p>
	 */
	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes,
			String produceDataName, boolean supportSingleton) {

		registerInstructionClass(instructionClass, requireDataNames,
				requireDataTypes, produceDataName, supportSingleton,
				InstructionClassUtility
						.getInstructionDependencies(instructionClass),
				InstructionClassUtility
						.getInstructionAsyncSupport(instructionClass));
	}

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			Class<? extends Instruction<?, ?>>[] dependencies) {

		registerInstructionClass(instructionClass,
				InstructionClassUtility
						.getInstructionRequireDataNames(instructionClass),
				InstructionClassUtility
						.getInstructionRequireDataTypes(instructionClass),
				InstructionClassUtility
						.getInstructionProduceDataName(instructionClass),
				InstructionClassUtility
						.getInstructionSingletonSupport(instructionClass),
				dependencies,
				InstructionClassUtility
						.getInstructionAsyncSupport(instructionClass));
	}

	public void registerInstructionClass(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes,
			String produceDataName, boolean supportSingleton,
			Class<? extends Instruction<?, ?>>[] dependencies) {

		registerInstructionClass(instructionClass, requireDataNames,
				requireDataTypes, produceDataName, supportSingleton,
				dependencies,
				InstructionClassUtility
						.getInstructionAsyncSupport(instructionClass));
	}

	@Override
	protected void prepare() {
		if (prepared) {
			return;
		}
		prepared = true;
		resolveDependencies();
	}

	protected void resolveDependencies() {
		for (InstructionClassNode node : instructionClassMap.values()) {
			InstructionClassDependencyNode dependencyNode = (InstructionClassDependencyNode) node;
			node.clear();
			if (dependencyNode.getDependencies() == null) {
				continue;
			}

			for (Class<?> clz : dependencyNode.getDependencies()) {
				InstructionClassDependencyNode dependent = (InstructionClassDependencyNode) instructionClassMap
						.get(clz);

				if (dependent != null) {
					dependencyNode.addOutgoing(dependent);
					dependent.addIncoming(dependencyNode);
				}
			}
		}
	}

}
