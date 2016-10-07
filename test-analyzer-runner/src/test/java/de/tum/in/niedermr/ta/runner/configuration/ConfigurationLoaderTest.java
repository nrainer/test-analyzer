package de.tum.in.niedermr.ta.runner.configuration;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKey;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractStringProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.IConfigurationProperty;

public class ConfigurationLoaderTest {
	@After
	public void after() {
		ConfigurationLoader.setFastFail(false);
	}

	@Test
	public void testConfigurationFromArgs() throws ConfigurationException {
		Configuration configuration1 = new Configuration();

		String[] configArgs = ConfigurationLoader.toArgsArray(configuration1);

		Configuration configuration2 = ConfigurationLoader.getConfigurationFromArgs(configArgs);
		assertConfigurationEquals(configuration1, configuration2);
	}

	@Test
	public void testConfigurationFromFile() throws ConfigurationException, IOException {
		Configuration expected = new Configuration();
		expected.getConfigurationVersion().setConfigurationVersionOfProgram();
		expected.getCodePathToTest().setValue("a.jar");

		DynamicConfigurationKey extensionKeyForTuningAlgorithm = DynamicConfigurationKey
				.create("tuning.speedup.algorithm");
		expected.getDynamicValues().setRawValue(extensionKeyForTuningAlgorithm, "ER3z");
		DynamicConfigurationKey extensionKeyForTuningFactor = DynamicConfigurationKey
				.create("tuning.speedup.factor");
		expected.getDynamicValues().setRawValue(extensionKeyForTuningFactor, "4");

		Configuration result = ConfigurationLoader.getConfigurationFromFile("testConfigurationFromFile.config",
				"./src/test/data/ConfigurationLoaderTest/");

		assertConfigurationEquals(expected, result);
	}

	@Test
	public void testConfigurationToArgs() {
		Configuration configuration = new Configuration();

		List<IConfigurationProperty<?>> properties = configuration.getAllPropertiesOrdered();
		String[] configArgs = ConfigurationLoader.toArgsArray(configuration);

		assertEquals(properties.size(), configArgs.length);

		for (int i = 0; i < configArgs.length; i++) {
			assertEquals(properties.get(i).getValueAsString(),
					configArgs[i].replace(AbstractStringProperty.PLACEHOLDER_EMPTY, ""));
		}
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
