package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.maven;

public class SurefireCsvTestListenerTest extends AbstractSurefireTestListenerTest {

	/** {@inheritDoc} */
	@Override
	protected AbstractSurefireTestListener createListenerInstance() {
		return new SurefireCsvTestListener();
	}
}
