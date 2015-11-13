package de.tum.in.niedermr.ta.runner.configuration.property;

import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractClasspathProperty;

public class TestAnalyzerClasspathProperty extends AbstractClasspathProperty {

	@Override
	public String getName() {
		return "testanalyzerClasspath";
	}

	@Override
	public String getDescription() {
		return "Classpath of the program. Value is set automatically.";
	}

	@Override
	protected boolean elementsMustExistWhenValidating() {
		return false;
	}
}
