package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.maven;

public class SurefireTestListenerTest extends AbstractSurefireTestListenerTest {

	/** {@inheritDoc} */
	@Override
	protected AbstractSurefireTestListener createListenerInstance() {
		return new SurefireTestListener();
	}
}
