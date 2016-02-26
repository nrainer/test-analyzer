package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunner;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;
import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.core.common.util.CommonUtility;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.logic.collection.AnalysisInformationCollectionLogic;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;
import de.tum.in.niedermr.ta.runner.logging.LoggingConstants;
import de.tum.in.niedermr.ta.runner.logging.LoggingUtil;
import de.tum.in.niedermr.ta.runner.start.AnalyzerRunnerStart;

/**
 * <b>ANACOL:</b> Collects <b>information about the minimal and maximal stack distance between test case and method
 * under test.</b><br/>
 * The instrumentation step (ANAINS) must have been executed before.<br/>
 * <br/>
 * Dependencies: same as InformationCollector (INFCOL)<br/>
 * Further classpath entries: jars to be processed and dependencies.
 * 
 */
public class AnalysisInformationCollector {
	private static final Logger LOG = LogManager.getLogger(AnalysisInformationCollector.class);

	/**
	 * args[0]: execution id args[1]: path to the jars with tests to run (separated by
	 * {@link de.tum.in.niedermr.ta.core.common.constants.CommonConstants#SEPARATOR_DEFAULT}) args[2]: path to the
	 * output text file (result will be written there) args[3]: name of the test runner args[4]: operate in fault
	 * tolerant mode args[5]: (optional): name of test classes not to be checked (separated by
	 * {@link de.tum.in.niedermr.ta.core.common.constants.CommonConstants#SEPARATOR_DEFAULT})
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			LoggingUtil.printDontStartThisClass(AnalysisInformationCollector.class, AnalyzerRunnerStart.class);
			return;
		}

		final String executionId = CommonUtility.getArgument(args, 0);
		final AnalysisInformationCollectionLogic analysisInformationCollectionLogic = new AnalysisInformationCollectionLogic(
				executionId);

		main(args, analysisInformationCollectionLogic);
	}

	public static void main(String[] args, AnalysisInformationCollectionLogic analysisInformationCollectionLogic) {
		final String executionId = CommonUtility.getArgument(args, 0);

		LOG.info(LoggingConstants.EXECUTION_ID_PREFIX + executionId);
		LOG.info(LoggingUtil.getInputArgumentsF1(args));

		try {
			final String[] jarsWithTests = CommonUtility.getArgument(args, 1).split(CommonConstants.SEPARATOR_DEFAULT);
			final String dataOutputPath = CommonUtility.getArgument(args, 2);
			final ITestRunner testRunner = JavaUtility.createInstance(CommonUtility.getArgument(args, 3));
			final boolean operateFaultTolerant = Boolean
					.parseBoolean(CommonUtility.getArgument(args, 4, Boolean.FALSE.toString()));
			final String[] testClassIncludes = ProcessExecution
					.unwrapAndSplitPattern(CommonUtility.getArgument(args, 5));
			final String[] testClassExcludes = ProcessExecution
					.unwrapAndSplitPattern(CommonUtility.getArgument(args, 6));

			analysisInformationCollectionLogic.setTestRunner(testRunner);
			analysisInformationCollectionLogic.setOutputFile(dataOutputPath);
			analysisInformationCollectionLogic.execute(jarsWithTests, testClassIncludes, testClassExcludes,
					operateFaultTolerant);

			System.exit(0);
		} catch (Throwable t) {
			LOG.error(t);
			throw new ExecutionException(executionId, t);
		}
	}
}
