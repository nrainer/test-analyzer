package de.tum.in.niedermr.ta.runner.configuration.property;

import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractIntegerProperty;

public class TestingTimeoutPerTestcaseProperty extends AbstractIntegerProperty {

	@Override
	public String getName() {
		return "testingTimeoutPerTestcase";
	}

	@Override
	protected Integer getDefault() {
		return 10;
	}

	@Override
	public String getDescription() {
		return "Variable part of the timeout value (in seconds) for running one single single testcase";
	}

	@Override
	public void validate() throws ConfigurationException {
		if (getValue() < 0) {
			throw new ConfigurationException(this, "Invalid value. Value must be >= 0.");
		}
	}
}