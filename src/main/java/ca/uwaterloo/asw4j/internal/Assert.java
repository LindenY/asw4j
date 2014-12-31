package ca.uwaterloo.asw4j.internal;

/**
 * A helper function defined to assert invalid state of arguments and throw {@link Exception} accordingly.
 * 
 * @author Desmond Lin
 * @since 1.0.1
 */
public class Assert {
	
	public final static void assertNull(Object... args) {
		assertNull(null, args);
	}
	
	public final static void assertNull(String message, Object... args) {
		for (int i=0; i<args.length; i++) {
			if (args[i] == null) {
				throw new NullPointerException(message);
			}
		}
	}
}
