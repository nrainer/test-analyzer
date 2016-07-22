package de.tum.in.ma.simpleproject.special;

import org.junit.Test;

public class TimeoutInTest {

	@Test
	public void testTimeoutIfMutated() throws InterruptedException {
		if (Special.returnFiveForTestNotToCauseATimeout() != 5) {
			// set the maximum timeout to <= 15 seconds in the configuration
			Thread.sleep(20 * 1000);
		}
	}

	@Test
	public void testDieIfMutated() throws InterruptedException {
		if (Special.returnFiveForTestNotToExit() != 5) {
			System.exit(1);
		}
	}
}
