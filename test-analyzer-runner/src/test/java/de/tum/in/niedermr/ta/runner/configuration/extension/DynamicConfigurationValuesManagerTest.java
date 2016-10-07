package de.tum.in.niedermr.ta.runner.configuration.extension;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

/** Test {@link DynamicConfigurationValuesManager} */
public class DynamicConfigurationValuesManagerTest {

	/** Test. */
	@Test
	public void testDynamicValues() {
		DynamicConfigurationValuesManager configurationExtension = new DynamicConfigurationValuesManager();

		DynamicConfigurationKey extensionKeyForStringValue = DynamicConfigurationKey
				.create(DynamicConfigurationKeyNamespace.EXTENSION, "analysis.nesting.algorithm", "default");
		DynamicConfigurationKey extensionKeyForBooleanValue = DynamicConfigurationKey
				.create(DynamicConfigurationKeyNamespace.EXTENSION, "analysis.nesting.enabled", false);
		assertEquals(extensionKeyForStringValue, DynamicConfigurationKey.parse(extensionKeyForStringValue.getName()));

		assertTrue(configurationExtension.toStringLines().isEmpty());

		assertFalse(configurationExtension.isSet(extensionKeyForBooleanValue));
		assertFalse(configurationExtension.getBooleanValue(extensionKeyForBooleanValue));
		configurationExtension.setRawValue(extensionKeyForBooleanValue, Boolean.TRUE.toString());
		assertTrue(configurationExtension.isSet(extensionKeyForBooleanValue));
		assertTrue(configurationExtension.getBooleanValue(extensionKeyForBooleanValue));

		assertFalse(configurationExtension.isSet(extensionKeyForStringValue));
		assertEquals("default", configurationExtension.getStringValue(extensionKeyForStringValue));
		configurationExtension.setRawValue(extensionKeyForStringValue, "X4");
		assertTrue(configurationExtension.isSet(extensionKeyForStringValue));
		assertEquals("X4", configurationExtension.getStringValue(extensionKeyForStringValue));

		List<String> configurationExtensionLines = configurationExtension.toStringLines();
		assertEquals(2, configurationExtensionLines.size());
		assertTrue(configurationExtensionLines.contains(
				DynamicConfigurationKeyNamespace.EXTENSION.getKeyPrefix() + "analysis.nesting.enabled" + " = true"));

		configurationExtension.removeEntry(extensionKeyForStringValue);
		assertFalse(configurationExtension.isSet(extensionKeyForStringValue));
	}
}
