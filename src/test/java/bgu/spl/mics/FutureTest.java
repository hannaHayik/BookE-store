package bgu.spl.mics;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import bgu.spl.mics.application.passiveObjects.OrderResult;

public class FutureTest {

	private Future<String> f1;

	@Before
	public void setUp() {
		f1 = new Future<String>();
	}

	@Test
	public void testGet() {
		String s = "ok";
		assertTrue(f1.isDone() == false);
		f1.resolve(s);
		assertFalse(f1.get() == null);
		if (f1.isDone())
			assertFalse(f1.get() == null);
		f1.resolve(s);
		if (!f1.isDone())
			assertTrue(f1.get() == null);
	}

	@Test
	public void testResolve() {
		String str = "yes";
		String s = f1.get();
		assertEquals(s, null);
		f1.resolve(str);
		assertFalse(f1.get() == null);
	}

	@Test
	public void testIsDone() {
		String s = "ok";
		if (f1.get() == null) {
			assertTrue(f1.isDone() == false);
		}
		f1.resolve(s);

		if (f1.get() != null)
			assertTrue(f1.isDone() == true);
	}

	@Test
	public void testGetLongTimeUnit() {
		fail("Not yet implemented");
	}

}
