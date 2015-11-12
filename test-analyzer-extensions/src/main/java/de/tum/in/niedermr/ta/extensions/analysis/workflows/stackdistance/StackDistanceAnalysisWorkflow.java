package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance;

import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.steps.s1.AnalysisInstrumentationStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.steps.s2.AnalysisInformationCollectorStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.steps.s3.CleanupStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.AbstractWorkflow;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.impl.s1.PrepareWorkingFolderStep;
import de.tum.in.niedermr.ta.runner.execution.exceptions.FailedExecution;

/**
 * Computes the minimum and maximum distance on the call stack between test case and method.
 */
public class StackDistanceAnalysisWorkflow extends AbstractWorkflow {
	protected PrepareWorkingFolderStep m_prepareStep;
	protected AnalysisInstrumentationStep m_analysisInstrumentationStep;
	protected AnalysisInformationCollectorStep m_analysisInformationCollectorStep;
	protected CleanupStep m_cleanupStep;

	@Override
	public void start() throws FailedExecution {
		m_prepareStep = new PrepareWorkingFolderStep(m_information);
		m_analysisInstrumentationStep = new AnalysisInstrumentationStep(m_information);
		m_analysisInformationCollectorStep = new AnalysisInformationCollectorStep(m_information);
		m_cleanupStep = new CleanupStep(m_information);

		m_prepareStep.run();
		m_analysisInstrumentationStep.run();
		m_analysisInformationCollectorStep.run();
		m_cleanupStep.run();
	}
}
