package de.tum.in.niedermr.ta.runner.analysis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.artifacts.exceptions.IteratorException;
import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunner;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;
import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.core.execution.id.IFullExecutionId;
import de.tum.in.niedermr.ta.runner.analysis.infocollection.InformationCollectorParameters;
import de.tum.in.niedermr.ta.runner.analysis.result.presentation.ResultPresentationUtil;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.args.ProgramArgsReader;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;
import de.tum.in.niedermr.ta.runner.execution.infocollection.IInformationCollectionLogic;
import de.tum.in.niedermr.ta.runner.logging.LoggingConstants;
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

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(InformationCollector.class);

	/** Main method. */
	public static void main(String[] args) {
		if (args.length == 0) {
			LoggingUtil.printDontStartThisClass(InformationCollector.class, AnalyzerRunnerStart.class);
			return;
		}

		IFullExecutionId executionId = InformationCollectorParameters.getExecutionId(args);

		try {
			readParametersAndStartLogic(executionId, args);
			System.exit(0);
		} catch (Throwable t) {
			LOGGER.error(t);
			throw new ExecutionException(executionId, t);
		}
	}

	/**
	 * Read the parameters and start the logic.
	 * 
	 * @see InformationCollectorParameters
	 */
	public static void readParametersAndStartLogic(IFullExecutionId executionId, String[] args)
			throws IteratorException, ReflectiveOperationException {
		ProgramArgsReader argsReader = InformationCollectorParameters.createProgramArgsReader(args);
		logInfos(executionId, argsReader);

		String[] jarsWithTests = argsReader.getArgument(InformationCollectorParameters.ARGS_FILE_WITH_TESTS_TO_RUN)
				.split(CommonConstants.SEPARATOR_DEFAULT);
		String dataOutputPath = argsReader.getArgument(InformationCollectorParameters.ARGS_FILE_WITH_RESULTS);
		ITestRunner testRunner = JavaUtility
				.createInstance(argsReader.getArgument(InformationCollectorParameters.ARGS_TEST_RUNNER_CLASS));
		IInformationCollectionLogic informationCollectionLogic = JavaUtility.createInstance(
				argsReader.getArgument(InformationCollectorParameters.ARGS_INFORMATION_COLLECTOR_LOGIC_CLASS));
		informationCollectionLogic.setExecutionId(executionId);
		boolean operateFaultTolerant = Boolean.parseBoolean(argsReader
				.getArgument(InformationCollectorParameters.ARGS_OPERATE_FAULT_TOLERANT, Boolean.FALSE.toString()));
		boolean includeFailingTests = Boolean.parseBoolean(argsReader
				.getArgument(InformationCollectorParameters.ARGS_INCLUDE_FAILING_TESTCASES, Boolean.FALSE.toString()));
		String[] testClassIncludes = ProcessExecution.unwrapAndSplitPattern(
				argsReader.getArgument(InformationCollectorParameters.ARGS_TEST_CLASS_INCLUDES, true));
		String[] testClassExcludes = ProcessExecution.unwrapAndSplitPattern(
				argsReader.getArgument(InformationCollectorParameters.ARGS_TEST_CLASS_EXCLUDES, true));
		String resultPresentationChoice = argsReader
				.getArgument(InformationCollectorParameters.ARGS_RESULT_PRESENTATION);
		boolean useMultiFileOutput = Boolean.parseBoolean(argsReader
				.getArgument(InformationCollectorParameters.ARGS_USE_MULTI_FILE_OUTPUT, Boolean.FALSE.toString()));

		informationCollectionLogic.setTestRunner(testRunner);
		informationCollectionLogic.setOutputFile(dataOutputPath);
		informationCollectionLogic.setResultPresentation(
				ResultPresentationUtil.createResultPresentation(resultPresentationChoice, executionId));
		informationCollectionLogic.setUseMultiFileOutput(useMultiFileOutput);
		informationCollectionLogic.setIncludeFailingTests(includeFailingTests);
		informationCollectionLogic.execute(jarsWithTests, testClassIncludes, testClassExcludes, operateFaultTolerant);

	}

	private static void logInfos(IFullExecutionId executionId, ProgramArgsReader argsReader) {
		LOGGER.info(LoggingConstants.EXECUTION_ID_TEXT + executionId.get());
		LOGGER.info(LoggingUtil.getInputArgumentsF1(argsReader));
	}
}
