package ca.uwaterloo.asw4j.internal;

import java.util.List;

import ca.uwaterloo.asw4j.Instruction;
import ca.uwaterloo.asw4j.reflection.TypeToken;

/**
 * <p>
 * 	Provide basic information about a {@link Instruction} class.
 * </p>
 * 
 * @author Desmond Lin
 * @since 1.0.0
 *
 */
public interface InstructionClassInfo {

	/**
	 * <p>
	 *  Get the produce data of {@link Instruction} class
	 * </p>
	 * 
	 * @return {@link TypeToken}
	 */
	public TypeToken<?> getProduceData();

	/**
	 * <p>
	 *  Get the require data of {@link Instruction} class
	 * </p>
	 * 
	 * @return {@link TypeToken}s
	 */
	public List<TypeToken<?>> getRequireDatas();

	/**
	 * <p>
	 *  Get if  {@link Instruction} class supports singleton property
	 * </p>
	 * 
	 * @return {@code boolean}
	 */
	public boolean isSupportSingleton();
	
	
	/**
	 * <p>
	 * 	Get the {@link STATE} of {@link Instruction} class
	 * </p>
	 * @return {@link STATE}
	 */
	public STATE getState();
	
	
	/**
	 * <p>
	 * 	Representation for the state of {@link Instruction} class
	 * </p>
	 * 
	 * <p>
	 * 	States:
	 * 	<ul>
	 * 		<li>Ready: the {@link Instruction} class is ready to run.</li>
	 * 		<li>Running: the {@link Instruction} class is currently running.</li>
	 *   	<li>Blocking: the {@link Instruction} class is blocked.</li>
	 *    	<li>Terminated: the {@link Instruction} class finished running.</li>
	 *  </ul>
	 * </p>
	 * 
	 * @author Desmond Lin
	 * @since 1.0.0
	 */
	public static enum STATE {
		Ready,
		Running,
		Blocking,
		Terminated
	}
}
