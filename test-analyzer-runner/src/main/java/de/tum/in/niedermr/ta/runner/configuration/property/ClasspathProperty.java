package de.tum.in.niedermr.ta.runner.configuration.property;

import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractClasspathProperty;

public class ClasspathProperty extends AbstractClasspathProperty {

	@Override
	public String getName() {
		return "classpath";
	}

	@Override
	public String getDescription() {
		return "Class path needed to use the original jar file";
	}
}
