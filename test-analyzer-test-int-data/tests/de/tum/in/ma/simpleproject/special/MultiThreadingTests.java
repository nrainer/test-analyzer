package de.tum.in.ma.simpleproject.special;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MultiThreadingTests {

	@Test
	public void testInvokeSingleThreadedCode() {
		assertEquals(15, new MultiThreading().compute2(3));
		assertEquals(13, new MultiThreading().compute3(3));
	}

	@Test
	public void testInvokeMultiThreadingCode() throws InterruptedException {
		new MultiThreading().computeMultiThreading();
	}

	@Test(timeout = 1000)
	public void testInvokeMultiThreadingCodeWithTimeout() throws InterruptedException {
		new MultiThreading().computeMultiThreading();
	}
}
