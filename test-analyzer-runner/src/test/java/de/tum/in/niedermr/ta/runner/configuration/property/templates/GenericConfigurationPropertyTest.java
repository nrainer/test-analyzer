package de.tum.in.niedermr.ta.runner.configuration.property.templates;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;

public class GenericConfigurationPropertyTest {
	private static final String STRING_VALUE_ILLEGAL = "ILLEGAL";
	private static final String DEFAULT_VALUE = "XYZ";

	@Test
	public void testDefaultValue1() {
		StringTestProperty property = new StringTestProperty();
		property.setValue(AbstractConfigurationProperty.PLACEHOLDER_DEFAULT);

		assertEquals(property.getDefault(), property.getValue());
		assertEquals(DEFAULT_VALUE, property.getDefault());
	}

	@Test
	public void testDefaultValue2() {
		StringTestProperty property = new StringTestProperty();
		property.setValue("MNO");
		property.setDefault();

		assertEquals(DEFAULT_VALUE, property.getValue());
	}

	@Test
	public void testEmptyValue1() {
		StringTestProperty property = new StringTestProperty();
		property.setValue(AbstractStringProperty.PLACEHOLDER_EMPTY);

		assertEquals("", property.getValue());
	}

	@Test(expected = ConfigurationException.class)
	public void testEmptyValue2() throws ConfigurationException {
		StringTestProperty property = new StringTestProperty();
		property.setValue(AbstractStringProperty.PLACEHOLDER_EMPTY);

		property.validate();
	}

	@Test(expected = ConfigurationException.class)
	public void testValidation1() throws ConfigurationException {
		StringTestProperty property = new StringTestProperty();
		property.setValue(STRING_VALUE_ILLEGAL);

		property.validate();
	}

	@Test
	public void testValidation2() throws ConfigurationException {
		StringTestProperty property = new StringTestProperty();
		property.setValue("DEF");

		property.validate();
	}

	class StringTestProperty extends AbstractStringProperty {

		@Override
		public String getName() {
			return "stringTestProperty";
		}

		@Override
		public String getDescription() {
			return "description";
		}

		@Override
		protected void validateFurther() throws ConfigurationException {
			if (getValue().equals(STRING_VALUE_ILLEGAL)) {
				throw new ConfigurationException(this, "not valid");
			}
		}

		@Override
		protected boolean isEmptyAllowed() {
			return false;
		}

		@Override
		protected String getDefault() {
			return DEFAULT_VALUE;
		}
	}
}
