package de.tum.in.niedermr.ta.runner.configuration.property;

import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractMultiStringProperty;

public class TestClassIncludesProperty extends AbstractMultiStringProperty {

	@Override
	public String getName() {
		return "testClassIncludes";
	}

	@Override
	public String getDescription() {
		return "Patterns for test classes by their (qualified) name. All test classes will be included if empty.";
	}

	@Override
	public String getSeparator() {
		return CommonConstants.SEPARATOR_DEFAULT;
	}
}