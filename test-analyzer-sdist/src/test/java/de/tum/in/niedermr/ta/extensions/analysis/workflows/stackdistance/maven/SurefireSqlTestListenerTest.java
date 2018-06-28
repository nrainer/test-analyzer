package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.maven;

public class SurefireSqlTestListenerTest extends AbstractSurefireTestListenerTest {

	/** {@inheritDoc} */
	@Override
	protected AbstractSurefireTestListener createListenerInstance() {
		return new SurefireSqlTestListener();
	}
}
