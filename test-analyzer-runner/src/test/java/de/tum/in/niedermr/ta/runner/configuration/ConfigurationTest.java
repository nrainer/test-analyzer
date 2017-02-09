package de.tum.in.niedermr.ta.runner.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKey;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKeyNamespace;
import de.tum.in.niedermr.ta.runner.configuration.property.TestingTimeoutAbsoluteMaxProperty;

/** Test {@link Configuration}. */
public class ConfigurationTest {

	/** Test. */
	@Test(expected = ConfigurationException.class)
	public void testInvalidConfig() throws ConfigurationException {
		Configuration configuration = new Configuration();

		configuration.getReturnValueGenerators().setValue("");

		configuration.validate();
	}

	/** Test. */
	@Test
	public void testTestingTimeout() {
		Configuration configuration = new Configuration();

		configuration.getTestingTimeoutConstant().setValue(10);
		configuration.getTestingTimeoutPerTestcase().setValue(5);
		configuration.getTestingTimeoutAbsoluteMax()
				.setValue(TestingTimeoutAbsoluteMaxProperty.TESTING_TIME_OUT_ABSOLUTE_MAX_DISABLED);
		assertEquals(10, configuration.computeTestingTimeout(0));

		configuration.getTestingTimeoutConstant().setValue(0);
		configuration.getTestingTimeoutPerTestcase().setValue(5);
		configuration.getTestingTimeoutAbsoluteMax()
				.setValue(TestingTimeoutAbsoluteMaxProperty.TESTING_TIME_OUT_ABSOLUTE_MAX_DISABLED);
		assertEquals(5 * 100, configuration.computeTestingTimeout(100));

		configuration.getTestingTimeoutConstant().setValue(10);
		configuration.getTestingTimeoutPerTestcase().setValue(5);
		configuration.getTestingTimeoutAbsoluteMax()
				.setValue(TestingTimeoutAbsoluteMaxProperty.TESTING_TIME_OUT_ABSOLUTE_MAX_DISABLED);
		assertEquals(10 + 5 * 100, configuration.computeTestingTimeout(100));

		configuration.getTestingTimeoutConstant().setValue(1);
		configuration.getTestingTimeoutPerTestcase().setValue(15);
		configuration.getTestingTimeoutAbsoluteMax()
				.setValue(TestingTimeoutAbsoluteMaxProperty.TESTING_TIME_OUT_ABSOLUTE_MAX_DISABLED);
		assertEquals(1 + 15 * 10, configuration.computeTestingTimeout(10));

		configuration.getTestingTimeoutConstant().setValue(2);
		configuration.getTestingTimeoutPerTestcase().setValue(5);
		configuration.getTestingTimeoutAbsoluteMax().setValue(20);
		assertEquals(2 + 5 * 2, configuration.computeTestingTimeout(2));

		configuration.getTestingTimeoutConstant().setValue(2);
		configuration.getTestingTimeoutPerTestcase().setValue(5);
		configuration.getTestingTimeoutAbsoluteMax().setValue(20);
		assertEquals(20, configuration.computeTestingTimeout(5));
	}

	/** Test. */
	@Test
	public void testToMultiLineString() {
		Configuration configuration = new Configuration();
		configuration.getNumberOfThreads().setValue(4);

		DynamicConfigurationKey configurationKey = DynamicConfigurationKey
				.create(DynamicConfigurationKeyNamespace.EXTENSION, "algorithm", "F1");
		configuration.getDynamicValues().setRawValue(configurationKey, "F4");

		String multiLineString = configuration.toMultiLineString();
		assertTrue(multiLineString.contains("[numberOfThreads=1]"));
		assertTrue(multiLineString.contains("extension.algorithm=F4"));
	}
}
