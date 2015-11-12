package de.tum.in.niedermr.ta.runner.configuration.property;

import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractClasspathProperty;

public class CodePathToTestProperty extends AbstractClasspathProperty {

	@Override
	public String getName() {
		return "codepathToTest";
	}

	@Override
	public String getDescription() {
		return "Path to the jars / class files with tests";
	}

	@Override
	protected boolean isEmptyAllowed() {
		return false;
	}
}