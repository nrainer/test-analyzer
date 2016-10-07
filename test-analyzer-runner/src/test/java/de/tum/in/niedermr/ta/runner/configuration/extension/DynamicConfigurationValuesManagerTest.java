package de.tum.in.niedermr.ta.runner.configuration.extension;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

/** Test {@link DynamicConfigurationValuesManager} */
public class DynamicConfigurationValuesManagerTest {

	/** Test. */
	@Test
	public void testDynamicValues() {
		DynamicConfigurationValuesManager configurationExtension = new DynamicConfigurationValuesManager();

		String propertyName = "analysis.nesting.enabled";
		DynamicConfigurationKey extensionKey1 = DynamicConfigurationKey.create(propertyName);
		assertEquals(extensionKey1, DynamicConfigurationKey.parse(extensionKey1.getName()));

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
		assertEquals(DynamicConfigurationKey.EXTENSION_PROPERTY_PREFIX + propertyName + " = true",
				configurationExtensionLines.get(0));

		configurationExtension.removeEntry(extensionKey1);
		assertFalse(configurationExtension.isSet(extensionKey1));
	}
}
