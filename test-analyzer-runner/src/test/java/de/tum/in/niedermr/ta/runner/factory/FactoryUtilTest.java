package de.tum.in.niedermr.ta.runner.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import de.tum.in.niedermr.ta.runner.configuration.Configuration;

/** Test {@link FactoryUtil}. */
public class FactoryUtilTest {

	@Test
	public void testCreateFactory() {
		Configuration configuration = new Configuration();
		IFactory factory = FactoryUtil.createFactory(configuration);
		assertNotNull(factory);
		assertEquals(configuration.getFactoryClass().getValue(), factory.getClass().getName());
	}
}
