package de.tum.in.niedermr.ta.runner.configuration.property.templates;

public abstract class AbstractIntegerProperty extends AbstractConfigurationProperty<Integer> {
	@Override
	protected Integer parseValue(String value) {
		return Integer.parseInt(value);
	}
}
