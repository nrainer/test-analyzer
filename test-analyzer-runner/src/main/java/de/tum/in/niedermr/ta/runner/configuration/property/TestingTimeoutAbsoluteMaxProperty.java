package de.tum.in.niedermr.ta.runner.configuration.property;

import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractIntegerProperty;

public class TestingTimeoutAbsoluteMaxProperty extends AbstractIntegerProperty {

	public static final int TESTING_TIME_OUT_ABSOLUTE_MAX_DISABLED = -1;

	@Override
	public String getName() {
		return "testingTimeoutAbsoluteMax";
	}

	@Override
	protected Integer getDefault() {
		return TESTING_TIME_OUT_ABSOLUTE_MAX_DISABLED;
	}

	@Override
	public String getDescription() {
		return "Absolute maximum duration (in seconds) for running all tests concerning one method under test";
	}

	@Override
	public void validate() throws ConfigurationException {
		if (getValue() <= 0 && getValue() != TESTING_TIME_OUT_ABSOLUTE_MAX_DISABLED) {
			throw new ConfigurationException(this,
					"Invalid value. Value must be > 0 or " + TESTING_TIME_OUT_ABSOLUTE_MAX_DISABLED + " (= disabled).");
		}
	}

	public boolean isActive() {
		return getValue() != TESTING_TIME_OUT_ABSOLUTE_MAX_DISABLED;
	}
}