package ca.uwaterloo.asw4j.meta;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ca.uwaterloo.asw4j.InstructionResolver;

/**
 * <p>
 * Provide singleton meta data about {@link Instruction} classes.
 * </p>
 * <p>
 * This {@link Annotation} is used by {@link InstructionResolver}.
 * </p>
 * <p>
 * The {@link Instruction}s annotated with this {@link Annotation} are marked as
 * supporting singleton. Supporting singleton means that the annotated
 * {@link Instruction}s are only allowed to have no more that one instance
 * running at the given time.
 * </p>
 * 
 * @author Desmond Lin
 * @since 1.0.0
 *
 */
@Documented
@Target(value = { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Singleton {

}
