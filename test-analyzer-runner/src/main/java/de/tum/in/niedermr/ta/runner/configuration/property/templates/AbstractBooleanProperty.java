package de.tum.in.niedermr.ta.runner.configuration.property.templates;

public abstract class AbstractBooleanProperty extends AbstractConfigurationProperty<Boolean> {
	@Override
	protected Boolean parseValue(String value) {
		return Boolean.parseBoolean(value);
	}

	public final boolean isTrue() {
		return getValue();
	}

	public final boolean isFalse() {
		return !isTrue();
	}

	public final void setTrue() {
		setValue(true);
	}

	public final void setFalse() {
		setValue(false);
	}
}
