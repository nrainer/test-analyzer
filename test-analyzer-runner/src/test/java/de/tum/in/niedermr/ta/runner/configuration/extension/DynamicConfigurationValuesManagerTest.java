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

		DynamicConfigurationKey extensionKeyForStringValue = DynamicConfigurationKey
				.create(DynamicConfigurationKeyNamespace.EXTENSION, "analysis.nesting.algorithm", null);
		DynamicConfigurationKey extensionKeyForBooleanValue = DynamicConfigurationKey
				.create(DynamicConfigurationKeyNamespace.EXTENSION, "analysis.nesting.enabled", true);
		assertEquals(extensionKeyForStringValue, DynamicConfigurationKey.parse(extensionKeyForStringValue.getName()));

		assertFalse(configurationExtension.isSet(extensionKeyForStringValue));
		assertFalse(configurationExtension.getBooleanValue(extensionKeyForStringValue));
		assertTrue(configurationExtension.getBooleanValue(extensionKeyForBooleanValue));
		assertNull(configurationExtension.getStringValue(extensionKeyForStringValue));
		assertTrue(configurationExtension.toStringLines().isEmpty());

		configurationExtension.setRawValue(extensionKeyForStringValue, Boolean.TRUE.toString());
		assertTrue(configurationExtension.isSet(extensionKeyForStringValue));
		assertEquals(Boolean.TRUE.toString(), configurationExtension.getStringValue(extensionKeyForStringValue));
		assertEquals(Boolean.TRUE.toString(), configurationExtension.getStringValue(extensionKeyForStringValue));
		assertTrue(configurationExtension.getBooleanValue(extensionKeyForStringValue));

		List<String> configurationExtensionLines = configurationExtension.toStringLines();
		assertEquals(2, configurationExtensionLines.size());
		assertTrue(configurationExtensionLines.contains(
				DynamicConfigurationKeyNamespace.EXTENSION.getKeyPrefix() + "analysis.nesting.enabled" + " = true"));

		configurationExtension.removeEntry(extensionKeyForStringValue);
		assertFalse(configurationExtension.isSet(extensionKeyForStringValue));
	}
}
