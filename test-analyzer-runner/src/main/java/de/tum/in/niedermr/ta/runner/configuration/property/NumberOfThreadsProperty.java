package de.tum.in.niedermr.ta.runner.configuration.property;

import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractIntegerProperty;

public class NumberOfThreadsProperty extends AbstractIntegerProperty {

	@Override
	public String getName() {
		return "numberOfThreads";
	}

	@Override
	protected Integer getDefault() {
		return 1;
	}

	@Override
	public String getDescription() {
		return "Number of threads to use for the steps method mutation and test running";
	}

	@Override
	public void validate() throws ConfigurationException {
		if (getValue() < 1) {
			throw new ConfigurationException(this, "Invalid value. Value must be > 0.");
		}
	}
}