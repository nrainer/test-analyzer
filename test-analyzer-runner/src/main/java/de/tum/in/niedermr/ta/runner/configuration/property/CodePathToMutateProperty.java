package de.tum.in.niedermr.ta.runner.configuration.property;

import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractClasspathProperty;

public class CodePathToMutateProperty extends AbstractClasspathProperty {

	@Override
	public String getName() {
		return "codepathToMutate";
	}

	@Override
	public String getDescription() {
		return "Path to the jars / class files to mutate";
	}

	@Override
	protected boolean isEmptyAllowed() {
		return false;
	}
}
