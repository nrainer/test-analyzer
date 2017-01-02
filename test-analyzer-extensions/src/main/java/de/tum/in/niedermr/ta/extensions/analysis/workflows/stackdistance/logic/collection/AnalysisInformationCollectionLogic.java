package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.logic.collection;

import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.core.execution.id.IFullExecutionId;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.StackLogRecorderV1;

/** Logic to collect information about the test cases and methods under test. */
public class AnalysisInformationCollectionLogic extends AbstractAnalysisInformationCollectionLogic {

	/** Constructor. */
	public AnalysisInformationCollectionLogic(IFullExecutionId executionId) {
		super(executionId);
	}

	/** {@inheritDoc} */
	@Override
	protected void startStackLogRecorder(TestcaseIdentifier testCaseIdentifier) {
		StackLogRecorderV1.startLog(testCaseIdentifier);
	}
}
