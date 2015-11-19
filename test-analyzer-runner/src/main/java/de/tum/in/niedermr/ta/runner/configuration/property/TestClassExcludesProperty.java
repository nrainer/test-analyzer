package de.tum.in.niedermr.ta.runner.configuration.property;

import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractMultiStringProperty;

public class TestClassExcludesProperty extends AbstractMultiStringProperty {

	@Override
	public String getName() {
		return "testClassExcludes";
	}

	@Override
	public String getDescription() {
		return "Patterns to skip test classes by their (qualified) name";
	}

	@Override
	public String getSeparator() {
		return CommonConstants.SEPARATOR_DEFAULT;
	}
}