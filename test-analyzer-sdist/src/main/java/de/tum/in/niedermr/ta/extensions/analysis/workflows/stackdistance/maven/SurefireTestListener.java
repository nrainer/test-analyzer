package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.maven;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;

/**
 * Surefire listener with SQL output.
 * 
 * @deprecated use {@link SurefireSqlTestListener}
 */
@Deprecated
public class SurefireTestListener extends SurefireSqlTestListener {

	@Override
	protected void execBeforeAllTests(IResultReceiver resultReceiver) {
		super.execBeforeAllTests(resultReceiver);

		writeCommentToResultFile(
				getClass().getName() + " is deprecated. Use " + SurefireSqlTestListener.class.getName() + " instead.");
	}
}
