package de.tum.in.niedermr.ta.runner.configuration.property;

import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractIntegerProperty;

public class TestingTimeoutConstantProperty extends AbstractIntegerProperty {

	@Override
	public String getName() {
		return "testingTimeoutConstant";
	}

	@Override
	protected Integer getDefault() {
		return 15;
	}

	@Override
	public String getDescription() {
		return "Constant part of the timeout value (in seconds) when running tests";
	}

	@Override
	public void validate() throws ConfigurationException {
		if (getValue() < 0) {
			throw new ConfigurationException(this, "Invalid value. Value must be >= 0.");
		}
	}
}