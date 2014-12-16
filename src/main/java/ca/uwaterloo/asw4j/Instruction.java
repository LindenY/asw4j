package ca.uwaterloo.asw4j;

/**
 * <p>
 * A implementation of {@link Runnable}.
 * </p>
 * <p>
 * All {@link Instruction} classes are managed, resolved, and instantiated by
 * {@link InstructionResolver}; and run by {@link WorkerManager}. To achieve
 * that, {@link Instruction} classes should be registered in
 * {@link InstructionResolver}.
 * </p>
 * <p>
 * Once there is a free ready-to-run "worker"(A {@link Thread}) available in
 * {@link WorkerManager}, {@link InstructionResolver} will determine which
 * registered {@link Instruction} classes are ready-to-be-run by
 * {@link WorkerManager}. When such {@link Instruction} classes are found,
 * {@link InstructionResolver} will resolve such {@link Instruction} classes'
 * dependencies by passing a {@link ToolResolver} to {@link Instruction}
 * classes' constructor; and inject the require data to the {@link Instruction}
 * classes.
 * </p>
 * <p>
 * For those {@link Instruction} classes to have more than one require data,
 * these {@link Instruction} classes are bounded to have {@link DataNode} as
 * their first {@link ParameterizedType} argument, the {@link R}. All the
 * require data of these {@link Instruction} classes are wrapped in a
 * {@link DataNode} object, and injected into {@link #execute(Object)}. In this
 * way, {@link Instruction} can have easy to use but flexible
 * {@link #execute(Object)} method. Fail to do so, will result in an
 * {@link IllegalArgumentException}.
 * </p>
 * <p>
 * To distinguish the difference between same data type, {@link TypeToken}s are
 * used to identify and track the difference and make sure that desired data are
 * resolved and injected.
 * </p>
 * 
 * @author Desmond Lin
 *
 * @param <R>
 *            The require data type of {@link Instruction} class
 * @param <P>
 *            The produce data type of {@link Instruction} class
 */
public abstract class Instruction<R, P> implements Runnable {

	/**
	 * The default constructor. Override this constructor, when no addition
	 * dependencies is needed during instantiation.
	 */
	public Instruction() {
	}

	/**
	 * <p>
	 * A constructor. Override this and use the {@link ToolResolver} to setup
	 * the addition dependencies.
	 * </p>
	 * <p>
	 * Because {@link Instruction} classes are managed and instantiated by
	 * {@link InstructionResolver}, {@link ToolResolver} are introduced in order
	 * to have more controlled upon {@link Instruction} classes' instantiation.
	 * </p>
	 * 
	 * @param toolResolver
	 *            A {@link ToolResolver} is passed to resolve the dependencies
	 *            of this {@link Instruction}.
	 */
	public Instruction(ToolResolver toolResolver) {
	}

	/**
	 * <p>
	 * The actual logic of this {@link Instruction}.
	 * </p>
	 * <p>
	 * The require data are passed as parameter. If more than one data type are
	 * required, a {@link DataNode} is passed instead.
	 * </p>
	 * 
	 * @param requireData
	 *            {@link R}. The require data of this {@link Instruction}.
	 * @return {@link P}. The produce data of this {@link Instruction}.
	 */
	public abstract P execute(R requireData);

	public final void run() {

		Long st = System.currentTimeMillis();

		preExecution();
		setResult(execute(requireData));
		postExecution();

		setDuration(System.currentTimeMillis() - st);
	}

	/**
	 * Invoked before {@link #execute(Object)}.
	 */
	public void preExecution() {
	}

	/**
	 * Invoked after {@link #execute(Object)}.
	 */
	public void postExecution() {
	}

	protected Long duration;
	protected P result;
	protected R requireData;

	/**
	 * Set the execution duration of this {@link Instruction}.
	 * 
	 * @param duration
	 *            The execution duration.
	 */
	private void setDuration(Long duration) {
		this.duration = duration;
	}

	/**
	 * Set the result of execution.
	 * 
	 * @param result
	 *            The result of execution
	 */
	private void setResult(P result) {
		this.result = result;
	}

	/**
	 * Exact and resolve the require data.
	 * 
	 * @param requireData
	 *            The require data of this {@link Instruction} class.
	 */
	@SuppressWarnings("unchecked")
	public void setRequireData(DataNode requireData) {
		if (requireData.size() == 1) {
			this.requireData = (R) requireData.values().get(0);
			return;
		}
		this.requireData = (R) requireData;
	}

	/**
	 * Get the execution duration.
	 * 
	 * @return The execution duration.
	 */
	public Long getDuration() {
		return duration;
	}

	/**
	 * Get the result of the execution.
	 * 
	 * @return The produce data of {@link Instruction} class.
	 */
	public P getResult() {
		return result;
	}
}
