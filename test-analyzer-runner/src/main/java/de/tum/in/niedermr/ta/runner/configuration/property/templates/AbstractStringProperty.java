package de.tum.in.niedermr.ta.runner.configuration.property.templates;

import de.tum.in.niedermr.ta.core.common.util.StringUtility;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;

public abstract class AbstractStringProperty extends AbstractConfigurationProperty<String> {
	public static final String PLACEHOLDER_EMPTY = "@EMPTY";
	public static final String EMPTY_STRING = "";

	@Override
	public final void validate() throws ConfigurationException {
		if (!isEmptyAllowed() && isEmpty()) {
			throw new ConfigurationException(this, "Empty value not allowed");
		}

		validateFurther();
	}

	@Override
	public final void setValue(String value) {
		if (value.equals(PLACEHOLDER_DEFAULT)) {
			setDefault();
		} else {
			String valueToSet;

			if (value.equals(AbstractStringProperty.PLACEHOLDER_EMPTY)) {
				valueToSet = EMPTY_STRING;
			} else {
				valueToSet = value;
			}

			super.setValue(modifyValueToBeSet(valueToSet));
		}
	}

	protected String modifyValueToBeSet(String value) {
		return value;
	}

	protected void validateFurther() throws ConfigurationException {
		// NOP
	}

	protected boolean isEmptyAllowed() {
		return true;
	}

	public boolean isEmpty() {
		return StringUtility.isNullOrEmpty(getValue());
	}

	@Override
	protected String parseValue(String value) {
		return value;
	}
}
