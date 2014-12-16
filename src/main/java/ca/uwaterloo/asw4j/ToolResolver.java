package ca.uwaterloo.asw4j;

/**
 * <p>
 * An interface defined to provide more control over {@link Instruction}
 * classes' instantiation.
 * </p>
 * <p>
 * Implement this interface, if addition dependencies are needed when
 * {@link Instruction}s are created. {@link ToolResolver} is only passed when
 * {@link Instruction}s are instantiated.
 * </p>
 * 
 * @see {@link InstructionResolver} and {@link Instruction}
 * @author Desmond Lin
 * @since 1.0.0
 */
public interface ToolResolver {

	/**
	 * Register a tool {@link Object} with given name {@link String}.
	 * 
	 * @param name
	 *            The name {@link String} which the {@link Object} is register
	 *            under.
	 * @param tool
	 *            The tool {@link Object} to be registered.
	 */
	public void registerTool(String name, Object tool);

	/**
	 * Get the tool {@link Object} for the given name {@link String}.
	 * 
	 * @param name
	 *            The name {@link String} to be checked against.
	 * @return The tool {@link Object} registered with the given name
	 *         {@link String}.
	 */
	public Object getTool(String name);
}
