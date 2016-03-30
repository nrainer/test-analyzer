package de.tum.in.niedermr.ta.runner.configuration.extension;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class ConfigurationExtensionTest {

	@Test
	public void testExtension() {
		ConfigurationExtension configurationExtension = new ConfigurationExtension();

		String propertyName = "analysis.nesting.enabled";
		ConfigurationExtensionKey extensionKey1 = ConfigurationExtensionKey.create(propertyName);
		assertEquals(extensionKey1, ConfigurationExtensionKey.parse(extensionKey1.getName()));

		assertFalse(configurationExtension.isSet(extensionKey1));
		assertFalse(configurationExtension.getBooleanValue(extensionKey1));
		assertTrue(configurationExtension.getBooleanValue(extensionKey1, true));
		assertNull(configurationExtension.getStringValue(extensionKey1, null));
		assertTrue(configurationExtension.toStringLines().isEmpty());

		configurationExtension.setRawValue(extensionKey1, Boolean.TRUE.toString());
		assertTrue(configurationExtension.isSet(extensionKey1));
		assertEquals(Boolean.TRUE.toString(), configurationExtension.getStringValue(extensionKey1));
		assertEquals(Boolean.TRUE.toString(), configurationExtension.getStringValue(extensionKey1, null));
		assertTrue(configurationExtension.getBooleanValue(extensionKey1));

		List<String> configurationExtensionLines = configurationExtension.toStringLines();
		assertEquals(1, configurationExtensionLines.size());
		assertEquals(ConfigurationExtensionKey.EXTENSION_PROPERTY_PREFIX + propertyName + " = true",
				configurationExtensionLines.get(0));

		configurationExtension.removeEntry(extensionKey1);
		assertFalse(configurationExtension.isSet(extensionKey1));
	}
}
