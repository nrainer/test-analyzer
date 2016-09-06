package de.tum.in.niedermr.ta.core.analysis.result.receiver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

/** Test {@link InMemoryResultReceiver}. */
public class InMemoryResultReceiverTest {

	/** Test. */
	@Test
	public void testReceiver() {
		InMemoryResultReceiver receiver = new InMemoryResultReceiver();
		assertTrue(receiver.isEmpty());

		receiver.append("a");
		assertFalse(receiver.isEmpty());

		receiver.append(Arrays.asList("b", "c"));

		assertEquals(3, receiver.getResult().size());
		assertEquals("a", receiver.getResult().get(0));
	}
}
