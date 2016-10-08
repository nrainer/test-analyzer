package de.tum.in.niedermr.ta.extensions.analysis.workflows.testworkflow.steps;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.MultiFileResultReceiver;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.testworkflow.FinalizeResultStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;

/**
 * An adjusted version of {@link FinalizeResultStep} that supports multiple
 * output files.
 */
public class MultiFileFinalizeResultStep extends FinalizeResultStep {

	/** Index of the current file. */
	private int m_index;

	/** {@link #m_index} */
	public void setIndex(int index) {
		this.m_index = index;
	}

	/** {@link #m_index} */
	public int getIndex() {
		return m_index;
	}

	/** {@inheritDoc} */
	@Override
	protected String computePathToDestinationFile(Configuration configuration) {
		String path = super.computePathToDestinationFile(configuration);
		return MultiFileResultReceiver.getFileName(path, m_index);
	}
}
