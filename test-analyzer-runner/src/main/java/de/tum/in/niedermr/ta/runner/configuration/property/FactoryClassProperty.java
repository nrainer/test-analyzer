package de.tum.in.niedermr.ta.runner.configuration.property;

import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractClassnameProperty;
import de.tum.in.niedermr.ta.runner.factory.DefaultFactory;
import de.tum.in.niedermr.ta.runner.factory.IFactory;

/**
 * Configuration property for the factory to use. <br/>
 * This allows replacing default logic.
 */
public class FactoryClassProperty extends AbstractClassnameProperty<IFactory> {

	@Override
	protected Class<IFactory> getRequiredType() {
		return IFactory.class;
	}

	@Override
	public String getName() {
		return "factoryClass";
	}

	@Override
	public String getDescription() {
		return "Factory to create instances (advanced)";
	}

	@Override
	protected String getDefault() {
		return DefaultFactory.class.getName();
	}
}
