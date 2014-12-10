package ca.uwaterloo.asw4j.internal;

public class Assert {

	public static void assertNull(Object obj, String message) {
		if (obj == null) {
			throw new NullPointerException(message);
		}
	}
}
