package ca.uwaterloo.asw4j.meta;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ca.uwaterloo.asw4j.Instruction;
import ca.uwaterloo.asw4j.DAGInstructionResolver;

/**
 * <p>
 * Provide dependencies meta data about {@link Instruction} classes.
 * </p>
 * <p>
 * This {@link Annotation} is only used by {@link DAGInstructionResolver}.
 * </p>
 * 
 * @author Desmond Lin
 * @since 1.0.0
 */
@Documented
@Target(value = { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DependOn {

	/**
	 * Get the depending {@link Instruction} classes of the annotated
	 * {@link Instruction} class.
	 * 
	 * @return The depending {@link Instruction} classes of the annotated
	 *         {@link Instruction} class.
	 */
	Class<? extends Instruction<?, ?>>[] instructions();

	/**
	 * <p>
	 * Get if the annotated {@link Instruction} class support async execution.
	 * </p>
	 * <p>
	 * Async execution means that if the annotated {@link Instruction} class can
	 * be executed at the same time while its dependencies are executing. If
	 * {@code true}, the annotated {@link Instruction} class will be run at the
	 * same while its depending {@link Instruction} classes are running;
	 * otherwise, the annotated {@link Instruction} class will only be run when
	 * its depending {@link Instruction} are finished.
	 * </p>
	 * <p>
	 * Default is {@code true}.
	 * </p>
	 * 
	 * @return If the annotated {@link Instruction} class support async
	 *         execution.
	 */
	boolean async() default true;
}
