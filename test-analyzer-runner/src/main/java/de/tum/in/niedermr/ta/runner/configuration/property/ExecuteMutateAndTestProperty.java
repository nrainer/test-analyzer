package de.tum.in.niedermr.ta.runner.configuration.property;

import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractBooleanProperty;

public class ExecuteMutateAndTestProperty extends AbstractBooleanProperty {

	@Override
	public String getName() {
		return "executeMutateAndTest";
	}

	@Override
	protected Boolean getDefault() {
		return true;
	}

	@Override
	public String getDescription() {
		return "Execute the steps of mutating methods and running tests";
	}
}