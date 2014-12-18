package ca.uwaterloo.asw4j.meta;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Provide produce data name about {@link Instruction} classes.
 * </p>
 * <p>
 * This {@link Annotation} is used by {@link InstructionResolver}.
 * </p>
 * 
 * @author Desmond Lin
 * @since 1.0.0
 */
@Documented
@Target(value = { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ProduceData {

	/**
	 * Get the produce data name of the annotated {@link Instruction} class.
	 * 
	 * @return The produce data name of the annotated {@link Instruction} class.
	 */
	String name();
}
