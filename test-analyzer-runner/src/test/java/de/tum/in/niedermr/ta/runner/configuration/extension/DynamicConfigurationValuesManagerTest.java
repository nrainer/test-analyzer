package de.tum.in.niedermr.ta.runner.configuration.extension;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractMultiStringProperty;

/** Test {@link DynamicConfigurationValuesManager} */
public class DynamicConfigurationValuesManagerTest {

	private static final DynamicConfigurationKey CONFIGURATION_KEY_FOR_STRING_VALUE = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.EXTENSION, "analysis.nesting.algorithm", "default");
	private static final DynamicConfigurationKey CONFIGURATION_KEY_FOR_BOOLEAN_VALUE = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.EXTENSION, "analysis.nesting.enabled", false);

	/** Test. */
	@Test
	public void testDynamicValues() {
		DynamicConfigurationValuesManager configurationExtension = new DynamicConfigurationValuesManager();

		assertEquals(CONFIGURATION_KEY_FOR_STRING_VALUE,
				DynamicConfigurationKey.parse(CONFIGURATION_KEY_FOR_STRING_VALUE.getName()));

		assertTrue(configurationExtension.toStringLines().isEmpty());

		assertFalse(configurationExtension.isSet(CONFIGURATION_KEY_FOR_BOOLEAN_VALUE));
		assertFalse(configurationExtension.getBooleanValue(CONFIGURATION_KEY_FOR_BOOLEAN_VALUE));
		configurationExtension.setRawValue(CONFIGURATION_KEY_FOR_BOOLEAN_VALUE, Boolean.TRUE.toString());
		assertTrue(configurationExtension.isSet(CONFIGURATION_KEY_FOR_BOOLEAN_VALUE));
		assertTrue(configurationExtension.getBooleanValue(CONFIGURATION_KEY_FOR_BOOLEAN_VALUE));

		assertFalse(configurationExtension.isSet(CONFIGURATION_KEY_FOR_STRING_VALUE));
		assertEquals("default", configurationExtension.getStringValue(CONFIGURATION_KEY_FOR_STRING_VALUE));
		configurationExtension.setRawValue(CONFIGURATION_KEY_FOR_STRING_VALUE, "X4");
		assertTrue(configurationExtension.isSet(CONFIGURATION_KEY_FOR_STRING_VALUE));
		assertEquals("X4", configurationExtension.getStringValue(CONFIGURATION_KEY_FOR_STRING_VALUE));

		List<String> configurationExtensionLines = configurationExtension.toStringLines();
		assertEquals(2, configurationExtensionLines.size());
		assertTrue(configurationExtensionLines.contains(
				DynamicConfigurationKeyNamespace.EXTENSION.getKeyPrefix() + "analysis.nesting.enabled" + "=true"));

		configurationExtension.removeEntry(CONFIGURATION_KEY_FOR_STRING_VALUE);
		assertFalse(configurationExtension.isSet(CONFIGURATION_KEY_FOR_STRING_VALUE));
	}

	/** Test. */
	@Test
	public void testDynamicValuesWithProperties() throws ConfigurationException {
		DynamicConfigurationValuesManager configurationExtension = new DynamicConfigurationValuesManager();

		configurationExtension.setRawValue(CONFIGURATION_KEY_FOR_STRING_VALUE,
				Long.class.getName() + CommonConstants.SEPARATOR_DEFAULT + Double.class.getName());
		assertTrue(configurationExtension.isSet(CONFIGURATION_KEY_FOR_STRING_VALUE));

		AbstractMultiStringProperty classnameProperty = new AbstractMultiStringProperty() {
			/** {@inheritDoc} */
			@Override
			public String getName() {
				return CONFIGURATION_KEY_FOR_STRING_VALUE.getName();
			}

			/** {@inheritDoc} */
			@Override
			public String getDescription() {
				return "";
			}

			@Override
			public String getSeparator() {
				return CommonConstants.SEPARATOR_DEFAULT;
			}
		};

		configurationExtension.getValueAsProperty(CONFIGURATION_KEY_FOR_STRING_VALUE, classnameProperty);

		assertEquals(2, classnameProperty.countElements());
		assertEquals(Double.class.getName(), classnameProperty.getElements()[1]);
	}

	/** Test. */
	@Test(expected = IllegalStateException.class)
	public void testRequireValue() {
		DynamicConfigurationValuesManager configurationExtension = new DynamicConfigurationValuesManager();
		configurationExtension.requireValueIsSet(CONFIGURATION_KEY_FOR_BOOLEAN_VALUE);
	}
}
