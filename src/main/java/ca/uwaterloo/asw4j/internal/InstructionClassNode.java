package ca.uwaterloo.asw4j.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import ca.uwaterloo.asw4j.Instruction;
import ca.uwaterloo.asw4j.ToolResolver;
import ca.uwaterloo.asw4j.meta.Singleton;
import ca.uwaterloo.asw4j.reflection.TypeToken;

/**
 * <p>
 * A helper class that provides useful functions for working
 * with {@link Instruction} class.
 * </p>
 * 
 * @author Desmond Lin
 * @since 1.0.0
 */
public class InstructionClassNode {

	protected final Class<? extends Instruction<?, ?>> instructionClass;
	protected final List<TypeToken<?>> requireDatas;
	protected final TypeToken<?> produceData;
	protected final boolean supportSingleton;

	protected AtomicReference<InstructionClassState> state;
	protected AtomicInteger numOfInstructionIssued;

	protected NonBlockingLinkedQueue<Instruction<?, ?>> pool;

	/**
	 * <p>
	 * Constructor for {@link InstructionClassNode} class
	 * </p>
	 * 
	 * @param instructionClass
	 *            The class object of {@link Instruction}
	 * @param requireDataNames
	 *            The require data names of {@link Instruction}
	 * @param requireDataTypes
	 *            The require data types of {@link Instruction}
	 * @param produceDataName
	 *            The produce data name of {@link Instruction}
	 * @param produceDataType
	 *            The produce data type of {@link Instruction}
	 * @param supportSingleton
	 *            if {@link Instruction} support {@link Singleton}
	 * @param enablePooling
	 *            if enable pooling
	 */
	public InstructionClassNode(
			Class<? extends Instruction<?, ?>> instructionClass,
			String[] requireDataNames, Type[] requireDataTypes,
			String produceDataName, Type produceDataType,
			boolean supportSingleton, boolean enablePooling) {

		this.instructionClass = instructionClass;

		InstructionClassUtility.checkPreconditionOfRequireDatas(
				requireDataNames, requireDataTypes);
		this.requireDatas = new ArrayList<TypeToken<?>>();
		for (int i = 0; i < requireDataTypes.length; i++) {
			this.requireDatas.add(TypeToken
					.get(requireDataTypes[i], (requireDataNames == null
							|| requireDataNames.length <= i ? null
							: requireDataNames[i])));
		}

		this.produceData = TypeToken.get(produceDataType, produceDataName);

		this.supportSingleton = supportSingleton;

		this.numOfInstructionIssued = new AtomicInteger(0);

		if (enablePooling) {
			this.pool = new NonBlockingLinkedQueue<Instruction<?, ?>>();
		}

		state = new AtomicReference<InstructionClassState>(InstructionClassState.Ready());
	}

	/**
	 * <p>
	 * Get an instance of {@link Instruction} class.
	 * </p>
	 * <p>
	 * If {@link Instruction} class is labeled with {@link Singleton}
	 * annotation, {@link STATE#BlockedBySingleton} and {@link STATE#Ready} are
	 * set when {@link #numberOfInstructionIssued()} > 0 and when
	 * {@link #numberOfInstructionIssued()} =0 respectively.
	 * </p>
	 * 
	 * @param toolResolver
	 *            if a new instance of {@link Instruction} needs to be created,
	 *            this {@link ToolResolver} would be passed to create the new
	 *            instance. if {@code null} is passed, then constructor
	 *            {@link Instruction#Instruction()} would be called instead of
	 *            {@link Instruction#Instruction(ToolResolver)}
	 * @return {@link Instruction}
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public Instruction<?, ?> getInstanceOfInstruction(ToolResolver toolResolver)
			throws NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {

		Instruction<?, ?> instruction = null;
		if (pool != null && pool.size() > 0) {
			instruction = pool.poll();
		} else {
			Constructor<? extends Instruction<?, ?>> constructor = null;

			if (toolResolver == null) {
				constructor = instructionClass.getDeclaredConstructor();
				instruction = constructor.newInstance();
			} else {
				constructor = instructionClass
						.getDeclaredConstructor(ToolResolver.class);
				instruction = constructor.newInstance(toolResolver);
			}
		}

		if (instruction != null) {
			numOfInstructionIssued.incrementAndGet();
		}

		if (isSupportSingleton() && numberOfInstructionIssued() > 0) {
			setState(InstructionClassState.BlockedBySingleton());
		}

		return instruction;

	}

	public Class<? extends Instruction<?, ?>> getInstructionClass() {
		return instructionClass;
	}

	public TypeToken<?> getProduceData() {
		return produceData;
	}

	public List<TypeToken<?>> getRequireDatas() {
		return requireDatas;
	}

	public boolean isSupportSingleton() {
		return supportSingleton;
	}

	public InstructionClassState getState() {
		return state.get();
	}

	public void setState(InstructionClassState state) {
		this.state.set(state);
	}

	/**
	 * <p>
	 * Get the number of {@link Instruction} has been issued
	 * </p>
	 * 
	 * @return {@code int}
	 */
	public int numberOfInstructionIssued() {
		return numOfInstructionIssued.get();
	}

	/**
	 * <p>
	 * Return the {@link Instruction} instance issued by this
	 * {@link InstructionClassOmitter}
	 * </p>
	 * <p>
	 * If {@link Instruction} class is labeled with {@link Singleton}
	 * annotation, {@link STATE#BlockedBySingleton} and {@link STATE#Ready} are
	 * set when {@link #numberOfInstructionIssued()} > 0 and when
	 * {@link #numberOfInstructionIssued()} =0 respectively.
	 * </p>
	 * <p>
	 * if pooling is enabled, then {@link Instruction} is store in the pool and
	 * waiting to be reused.
	 * </p>
	 * 
	 * @param instruction
	 *            The {@link Instruction} instance to be returned.
	 */
	public void returnInstanceOfInstruction(Instruction<?, ?> instruction) {

		numOfInstructionIssued.decrementAndGet();

		if (getState() == InstructionClassState.BlockedBySingleton() 
				&& isSupportSingleton()
				&& numberOfInstructionIssued() <= 0) {
			setState(InstructionClassState.Ready());
		}

		if (pool != null) {
			pool.add(instruction);
		}
	}

	/**
	 * <p>
	 * Restore {@link #state} property to {@link STATE#Ready}. Also, if pooling
	 * is enabled, all {@link Instruction} stored in the pool will be dismissed.
	 * </p>
	 */
	public void clear() {
		if (pool != null) {
			pool.clear();
		}
		setState(InstructionClassState.Ready());
	}
	
}
