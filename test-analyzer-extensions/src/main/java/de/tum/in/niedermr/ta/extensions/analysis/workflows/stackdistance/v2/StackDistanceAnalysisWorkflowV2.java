package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.v2;

import de.tum.in.niedermr.ta.extensions.analysis.workflows.ExtensionEnvironmentConstants;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.common.AbstractStackDistanceAnalysisWorkflow;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.common.steps.AnalysisInformationCollectorStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.common.steps.AnalysisInstrumentationStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.v1.StackDistanceAnalysisWorkflowV1;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.v2.logic.collection.AnalysisInformationCollectionLogicV2;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.v2.logic.recording.StackLogRecorderV2;
import de.tum.in.niedermr.ta.extensions.threads.IModifiedThreadClass;

/**
 * Computes the minimum and maximum distance on the call stack between test case and method. <br/>
 * Improved version of {@link StackDistanceAnalysisWorkflowV1} that is thread-aware and produces valid results for
 * multi-threaded code. Therefore, this class should be favored over {@link StackDistanceAnalysisWorkflowV1}. Note that
 * V2 also counts method invocations in external libraries.<br/>
 * <br/>
 * However, this workflow requires the use of an endorsed jar file that replaces the {@link Thread} class. <br/>
 * The modified <code>java.lang.Thread</code> class must:
 * <li>implement {@link IModifiedThreadClass}</li>
 * <li>invoke <code>ThreadNotifier.INSTANCE.sendThreadStartedEvent(this);</code> in {@link Thread#start()} (directly
 * before invoking <code>start0()</code>)</li>
 * 
 */
public class StackDistanceAnalysisWorkflowV2 extends AbstractStackDistanceAnalysisWorkflow {

	/** {@inheritDoc} */
	@Override
	protected AnalysisInstrumentationStep createAnalysisInstrumentationStep() {
		AnalysisInstrumentationStep step = createAndInitializeExecutionStep(AnalysisInstrumentationStep.class);
		step.setStackLogRecorderClass(StackLogRecorderV2.class);
		return step;
	}

	/** {@inheritDoc} */
	@Override
	protected AnalysisInformationCollectorStep createAnalysisInformationCollectorStep() {
		AnalysisInformationCollectorStep step = createAndInitializeExecutionStep(
				AnalysisInformationCollectorStep.class);
		step.setResultOutputFile(ExtensionEnvironmentConstants.FILE_OUTPUT_STACK_DISTANCES_V2);
		step.setInformationCollectorLogicClass(AnalysisInformationCollectionLogicV2.class);
		return step;
	}
}
