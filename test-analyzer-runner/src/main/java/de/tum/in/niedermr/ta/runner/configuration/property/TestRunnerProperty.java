package de.tum.in.niedermr.ta.runner.configuration.property;

import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunner;
import de.tum.in.niedermr.ta.core.code.tests.runner.junit.JUnitTestRunner;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractClassnameProperty;

public class TestRunnerProperty extends AbstractClassnameProperty<ITestRunner> {

	@Override
	public String getName() {
		return "testRunner";
	}

	@Override
	protected String getDefault() {
		return JUnitTestRunner.class.getName();
	}

	@Override
	public String getDescription() {
		return "Test runner to use";
	}

	@Override
	protected Class<? extends ITestRunner> getRequiredType() {
		return ITestRunner.class;
	}
}