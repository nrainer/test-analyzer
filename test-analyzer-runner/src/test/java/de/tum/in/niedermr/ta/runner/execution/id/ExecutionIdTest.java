package de.tum.in.niedermr.ta.runner.execution.id;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.core.execution.id.IFullExecutionId;

public class ExecutionIdTest {

	@Test
	public void testShortExecutionId() {
		IExecutionId shortExecutionId = ExecutionIdFactory.createNewShortExecutionId();
		assertEquals(shortExecutionId.getShortId(), shortExecutionId.get());
		assertEquals(shortExecutionId, ExecutionIdFactory.parseShortExecutionId(shortExecutionId.get()));
	}

	@Test
	public void testFullExecutionId() {
		IExecutionId shortExecutionId = ExecutionIdFactory.createNewShortExecutionId();
		IFullExecutionId fullExecutionId = shortExecutionId.createFullExecutionId("FULL1");
		assertEquals(shortExecutionId.getShortId(), fullExecutionId.getShortId());
		assertEquals(shortExecutionId.getShortId() + ExecutionId.SEPARATOR + "FULL1", fullExecutionId.getFullId());
		assertEquals(shortExecutionId.getShortId() + ExecutionId.SEPARATOR + "FULL2",
				fullExecutionId.createFullExecutionId("FULL2").getFullId());
		assertEquals(fullExecutionId, ExecutionIdFactory.parseFullExecutionId(fullExecutionId.get()));
		assertEquals(shortExecutionId, fullExecutionId.convertToShortExecutionId());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testParseShortAsFullExecutionId() {
		IExecutionId shortExecutionId = ExecutionIdFactory.createNewShortExecutionId();
		ExecutionIdFactory.parseFullExecutionId(shortExecutionId.get());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testParseFullAsShortExecutionId() {
		IExecutionId shortExecutionId = ExecutionIdFactory.createNewShortExecutionId();
		IFullExecutionId fullExecutionId = shortExecutionId.createFullExecutionId("FULL");

		ExecutionIdFactory.parseShortExecutionId(fullExecutionId.get());
	}

	@Test
	public void testParseFullAsShortExecutionIdWithTrimmingAllowed() {
		IExecutionId shortExecutionId = ExecutionIdFactory.createNewShortExecutionId();
		IFullExecutionId fullExecutionId = shortExecutionId.createFullExecutionId("FULL");

		assertEquals(shortExecutionId, ExecutionIdFactory.parseShortExecutionId(fullExecutionId.get(), true));
	}
}
