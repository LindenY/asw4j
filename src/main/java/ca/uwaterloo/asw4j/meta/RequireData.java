package ca.uwaterloo.asw4j.meta;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Provide require data names and types about {@link Instruction} classes.
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
public @interface RequireData {

	/**
	 * Get the require data names of the annotated {@link Instruction} class.
	 * 
	 * @return The require data names of the annotated {@link Instruction} class.
	 */
	String[] names();

	/**
	 * Get the require data types of the annotated {@link Instruction} class.
	 * 
	 * @return The require data types of annotated {@link Instruction} class.
	 */
	Class<?>[] types() default { Object.class };
}
