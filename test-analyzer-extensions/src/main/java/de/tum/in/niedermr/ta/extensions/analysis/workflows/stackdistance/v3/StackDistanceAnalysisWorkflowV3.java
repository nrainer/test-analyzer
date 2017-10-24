package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.v3;

import de.tum.in.niedermr.ta.extensions.analysis.workflows.ExtensionEnvironmentConstants;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.AbstractStackDistanceAnalysisWorkflow;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.datamanager.v3.StackInformationCollectionLogicV3;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.recording.v3.StackLogRecorderV3;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.steps.AnalysisInformationCollectorStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.steps.AnalysisInstrumentationStep;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.v1.StackDistanceAnalysisWorkflowV1;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.v2.StackDistanceAnalysisWorkflowV2;
import de.tum.in.niedermr.ta.extensions.threads.IModifiedThreadClass;

/**
 * Computes the minimum and maximum distance on the call stack between test case and method. <br/>
 * Combines the upsides of {@link StackDistanceAnalysisWorkflowV1} (performance) and
 * {@link StackDistanceAnalysisWorkflowV2} (thread-awareness). Note that unlike V2, it does not count method invocations
 * in external libraries and constructors.<br/>
 * <br/>
 * As V2, this workflow requires the use of an endorsed jar file that replaces the {@link Thread} class. <br/>
 * The modified <code>java.lang.Thread</code> class must:
 * <li>implement {@link IModifiedThreadClass}</li>
 * <li>invoke <code>ThreadNotifier.INSTANCE.sendThreadStartedEvent(this);</code> in {@link Thread#start()} (directly
 * before invoking <code>start0()</code>)</li>
 * 
 */
public class StackDistanceAnalysisWorkflowV3 extends AbstractStackDistanceAnalysisWorkflow {

	/** {@inheritDoc} */
	@Override
	protected AnalysisInstrumentationStep createAnalysisInstrumentationStep() {
		AnalysisInstrumentationStep step = createAndInitializeExecutionStep(AnalysisInstrumentationStep.class);
		step.setStackLogRecorderClass(StackLogRecorderV3.class);
		return step;
	}

	/** {@inheritDoc} */
	@Override
	protected AnalysisInformationCollectorStep createAnalysisInformationCollectorStep() {
		AnalysisInformationCollectorStep step = createAndInitializeExecutionStep(
				AnalysisInformationCollectorStep.class);
		step.setResultOutputFile(ExtensionEnvironmentConstants.FILE_OUTPUT_STACK_DISTANCES_V3);
		step.setInformationCollectorLogicClass(StackInformationCollectionLogicV3.class);
		return step;
	}
}
