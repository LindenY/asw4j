package ca.uwaterloo.asw.smokeTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
import ca.uwaterloo.asw.reflection.*;

public class ReflectionTest {

	@Test
	public void testEqualityWithNormalType() {
		
		Date d0 = new Date();
		Date d1 = new Date();
		String s0 = "String0";
		String s1 = "String1";
		
		TypeToken<?> d0Token = TypeToken.get(d0.getClass(), null);
		TypeToken<?> d1Token = TypeToken.get(d1.getClass(), null);
		TypeToken<?> s0Token = TypeToken.get(s0.getClass(), null);
		TypeToken<?> s1Token = TypeToken.get(s1.getClass(), null);
		
		assertTrue(d0Token.equals(d1Token));
		assertTrue(s0Token.equals(s1Token));
		assertFalse(d0Token.equals(s0Token));
		assertFalse(d1Token.equals(s1Token));
	}

	@Test
	public void testEqualityWithNormalTypeAndName() {
		
		Date d0 = new Date();
		Date d1 = new Date();
		Date d2 = new Date();
		String s0 = "String0";
		String s1 = "String1";
		String s2 = "String3";
		
		TypeToken<?> d0Token = TypeToken.get(d0.getClass(), "d");
		TypeToken<?> d1Token = TypeToken.get(d1.getClass(), "d");
		TypeToken<?> d2Token = TypeToken.get(d2.getClass(), "dd");
		TypeToken<?> s0Token = TypeToken.get(s0.getClass(), "s");
		TypeToken<?> s1Token = TypeToken.get(s1.getClass(), "s");
		TypeToken<?> s2Token = TypeToken.get(s2.getClass(), "ss");
		
		assertTrue(d0Token.equals(d1Token));
		assertFalse(d0Token.equals(d2Token));
		assertFalse(d0Token.equals(s0Token));
		
		assertTrue(s0Token.equals(s1Token));
		assertFalse(s0Token.equals(s2Token));
		assertFalse(s0Token.equals(d0Token));
	}

	// TODO: Solving GenericType is taking too much time. Lets think about it
	// later.
	@Ignore
	@Test
	public void testEqualityWithGenericType() {
		
		List<String> ls0 = new ArrayList<String>();
		List<String> ls1 = new ArrayList<String>();
		List<Date> ld0 = new ArrayList<Date>();
		List<Date> ld1 = new ArrayList<Date>();
		
		TypeToken<?> ls0Token = TypeToken.get(ls0.getClass(), null);
		TypeToken<?> ls1Token = TypeToken.get(ls1.getClass(), null);
		TypeToken<?> ld0Token = TypeToken.get(ld0.getClass(), null);
		TypeToken<?> ld1Token = TypeToken.get(ld1.getClass(), null);
		
		assertTrue(ls0Token.equals(ls1Token));
		assertTrue(ld0Token.equals(ld1Token));
		
		assertFalse(ls0Token.equals(ld0Token));
		assertFalse(ls1Token.equals(ld1Token));
	}

	@Ignore
	@Test
	public void testEqualityWithGenericTypeAndName() {

		List<String> ls0 = new ArrayList<String>();
		List<String> ls1 = new ArrayList<String>();
		List<String> ls2 = new ArrayList<String>();
		List<String> ls3 = new ArrayList<String>();
		List<Date> ld0 = new ArrayList<Date>();
		List<Date> ld1 = new ArrayList<Date>();
		List<Date> ld2 = new ArrayList<Date>();
		List<Date> ld3 = new ArrayList<Date>();
		
		TypeToken<?> ls0Token = TypeToken.get(ls0.getClass(), "s");
		TypeToken<?> ls1Token = TypeToken.get(ls1.getClass(), "s");
		TypeToken<?> ls2Token = TypeToken.get(ls2.getClass(), "ss");
		TypeToken<?> ls3Token = TypeToken.get(ls3.getClass(), "asdf");
		TypeToken<?> ld0Token = TypeToken.get(ld0.getClass(), "d");
		TypeToken<?> ld1Token = TypeToken.get(ld1.getClass(), "d");
		TypeToken<?> ld2Token = TypeToken.get(ld2.getClass(), "dd");
		TypeToken<?> ld3Token = TypeToken.get(ld3.getClass(), "asdf");
		
		assertTrue(ls0Token.equals(ls1Token));
		assertFalse(ls1Token.equals(ls2Token));
		assertTrue(ld0Token.equals(ld1Token));
		assertFalse(ld1Token.equals(ld2Token));
		
		assertFalse(ls0Token.equals(ld0Token));
		assertFalse(ls3Token.equals(ld3Token));
	}

}
