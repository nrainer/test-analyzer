package de.tum.in.niedermr.ta.runner.configuration.property;

import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractBooleanProperty;

public class ExecuteCollectInformationProperty extends AbstractBooleanProperty {

	@Override
	public String getName() {
		return "executeCollectInformation";
	}

	@Override
	protected Boolean getDefault() {
		return true;
	}

	@Override
	public String getDescription() {
		return "Execute the steps of instrumentation and information collection";
	}
}