package de.tum.in.niedermr.ta.runner.analysis;

import de.tum.in.niedermr.ta.core.execution.id.IFullExecutionId;
import de.tum.in.niedermr.ta.runner.analysis.infocollection.InformationCollectorParameters;
import de.tum.in.niedermr.ta.runner.analysis.infocollection.InformationCollectorUtility;
import de.tum.in.niedermr.ta.runner.execution.infocollection.InformationCollectionLogic;
import de.tum.in.niedermr.ta.runner.logging.LoggingUtil;
import de.tum.in.niedermr.ta.runner.start.AnalyzerRunnerStart;

/**
 * <b>INFCOL:</b> Collects <b>information about test classes, testcases and
 * methods under test.</b><br/>
 * The instrumentation step (INSTRU) must have been executed before.<br/>
 * <br/>
 * Note: It does not yet filter the methods under test, because
 * <ul>
 * <li>the method descriptor is not available at this point</li>
 * <li>different return value generators might want to work on different
 * methods</li>
 * </ul>
 * <br/>
 * Dependencies: ASM, log4j, jUnit, core.<br/>
 * Further classpath entries: jars to be processed and dependencies.
 * 
 */
public class InformationCollector {

	/** Main method. */
	public static void main(String[] args) {
		if (args.length == 0) {
			LoggingUtil.printDontStartThisClass(InformationCollector.class, AnalyzerRunnerStart.class);
			return;
		}

		IFullExecutionId executionId = InformationCollectorParameters.getExecutionId(args);
		InformationCollectionLogic informationCollectionLogic = new InformationCollectionLogic(executionId);
		InformationCollectorUtility.readParametersAndStartLogic(executionId, informationCollectionLogic, args);
	}
}
