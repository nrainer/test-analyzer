package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance;

import de.tum.in.niedermr.ta.core.execution.id.IFullExecutionId;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.logic.collection.AnalysisInformationCollectionLogic;
import de.tum.in.niedermr.ta.runner.analysis.infocollection.InformationCollectorParameters;
import de.tum.in.niedermr.ta.runner.analysis.infocollection.InformationCollectorUtility;
import de.tum.in.niedermr.ta.runner.logging.LoggingUtil;
import de.tum.in.niedermr.ta.runner.start.AnalyzerRunnerStart;

/**
 * <b>ANACOL:</b> Collects <b>information about the minimal and maximal stack
 * distance between test case and method under test.</b><br/>
 * The instrumentation step (ANAINS) must have been executed before.<br/>
 * <br/>
 * Dependencies: same as InformationCollector (INFCOL)<br/>
 * Further classpath entries: jars to be processed and dependencies.
 * 
 */
public class AnalysisInformationCollector {

	/** Main method. */
	public static void main(String[] args) {
		if (args.length == 0) {
			LoggingUtil.printDontStartThisClass(AnalysisInformationCollector.class, AnalyzerRunnerStart.class);
			return;
		}

		IFullExecutionId executionId = InformationCollectorParameters.getExecutionId(args);
		AnalysisInformationCollectionLogic informationCollectionLogic = new AnalysisInformationCollectionLogic(
				executionId);
		InformationCollectorUtility.readParametersAndStartLogic(executionId, informationCollectionLogic, args);
	}
}
