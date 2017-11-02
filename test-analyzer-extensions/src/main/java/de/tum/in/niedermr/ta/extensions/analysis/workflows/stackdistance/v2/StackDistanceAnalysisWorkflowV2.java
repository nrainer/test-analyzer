package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.v2;

import de.tum.in.niedermr.ta.extensions.analysis.workflows.ExtensionEnvironmentConstants;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.AbstractStackDistanceAnalysisWorkflow;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.datamanager.v2.StackInformationCollectionLogicV2;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.recording.v2.StackLogRecorderV2;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.v1.StackDistanceAnalysisWorkflowV1;
import de.tum.in.niedermr.ta.extensions.threads.IModifiedThreadClass;
import de.tum.in.niedermr.ta.runner.execution.infocollection.IInformationCollectionLogic;

/**
 * Computes the minimum and maximum distance on the call stack between test case
 * and method. <br/>
 * Improved version of {@link StackDistanceAnalysisWorkflowV1} that is
 * thread-aware and produces valid results for multi-threaded code. Therefore,
 * this class should be favored over {@link StackDistanceAnalysisWorkflowV1}.
 * Note that V2 also counts method invocations in external libraries.<br/>
 * <br/>
 * However, this workflow requires the use of an endorsed jar file that replaces
 * the {@link Thread} class. <br/>
 * The modified <code>java.lang.Thread</code> class must:
 * <li>implement {@link IModifiedThreadClass}</li>
 * <li>invoke <code>ThreadNotifier.INSTANCE.sendThreadStartedEvent(this);</code>
 * in {@link Thread#start()} (directly before invoking
 * <code>start0()</code>)</li> Moreover, this version can be slow.
 */
public class StackDistanceAnalysisWorkflowV2 extends AbstractStackDistanceAnalysisWorkflow {

	/** {@inheritDoc} */
	@Override
	protected Class<?> getStackLogRecorderClass() {
		return StackLogRecorderV2.class;
	}

	/** {@inheritDoc} */
	@Override
	protected Class<? extends IInformationCollectionLogic> getInformationCollectorLogicClass() {
		return StackInformationCollectionLogicV2.class;
	}

	/** {@inheritDoc} */
	@Override
	protected String getResultOutputFile() {
		return ExtensionEnvironmentConstants.FILE_OUTPUT_STACK_DISTANCES_V2;
	}
}
