package de.tum.in.niedermr.ta.runner.configuration.property;

import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractBooleanProperty;

public class RemoveTempDataProperty extends AbstractBooleanProperty {

	@Override
	public String getName() {
		return "removeTempData";
	}

	@Override
	protected Boolean getDefault() {
		return true;
	}

	@Override
	public String getDescription() {
		return "Remove temporary data";
	}
}