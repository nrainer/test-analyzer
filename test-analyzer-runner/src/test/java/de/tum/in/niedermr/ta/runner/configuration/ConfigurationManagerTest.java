package de.tum.in.niedermr.ta.runner.configuration;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKey;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKeyNamespace;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.IConfigurationProperty;

/** Test {@link ConfigurationManager}. */
public class ConfigurationManagerTest {

	/** After. */
	@After
	public void after() {
		ConfigurationManager.setFastFail(false);
	}

	/** Test. */
	@Test
	public void testConfigurationFromFile() throws ConfigurationException, IOException {
		Configuration expected = new Configuration();
		expected.getConfigurationVersion().setConfigurationVersionOfProgram();
		expected.getCodePathToTest().setValue("a.jar");

		DynamicConfigurationKey extensionKeyForTuningAlgorithm = DynamicConfigurationKey
				.create(DynamicConfigurationKeyNamespace.EXTENSION, "tuning.speedup.algorithm", null);
		expected.getDynamicValues().setRawValue(extensionKeyForTuningAlgorithm, "ER3z");
		DynamicConfigurationKey extensionKeyForTuningFactor = DynamicConfigurationKey
				.create(DynamicConfigurationKeyNamespace.EXTENSION, "tuning.speedup.factor", null);
		expected.getDynamicValues().setRawValue(extensionKeyForTuningFactor, "4");

		Configuration result = ConfigurationManager.getConfigurationFromFile("testConfigurationFromFile.config",
				"./src/test/data/ConfigurationLoaderTest/");

		assertConfigurationEquals(expected, result);
	}

	public static void assertConfigurationEquals(Configuration configuration1, Configuration configuration2) {
		List<IConfigurationProperty<?>> cProperties1 = configuration1.getAllPropertiesOrdered();
		List<IConfigurationProperty<?>> cProperties2 = configuration2.getAllPropertiesOrdered();

		assertEquals(cProperties1.size(), cProperties2.size());

		for (int i = 0; i < cProperties1.size(); i++) {
			assertEquals(cProperties1.get(i).getName(), cProperties2.get(i).getName());
			assertEquals(cProperties1.get(i).getValue(), cProperties2.get(i).getValue());
		}

		assertEquals(configuration1.getDynamicValues().toStringLines(),
				configuration2.getDynamicValues().toStringLines());
	}
}
