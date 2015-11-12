package de.tum.in.niedermr.ta.runner.configuration.property;

import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractBooleanProperty;

public class OperateFaultTolerantProperty extends AbstractBooleanProperty {

	@Override
	public String getName() {
		return "operateFaultTolerant";
	}

	@Override
	public String getDescription() {
		return "Operate in fault tolerant mode";
	}

	@Override
	protected Boolean getDefault() {
		return false;
	}
}