package de.tum.in.niedermr.ta.runner.analysis.infocollection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunner;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;
import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.core.execution.id.IFullExecutionId;
import de.tum.in.niedermr.ta.runner.analysis.result.presentation.ResultPresentationUtil;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.args.ProgramArgsReader;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;
import de.tum.in.niedermr.ta.runner.execution.infocollection.IInformationCollectionLogic;
import de.tum.in.niedermr.ta.runner.logging.LoggingConstants;
import de.tum.in.niedermr.ta.runner.logging.LoggingUtil;

/** Utility for InformationCollectors. */
public final class InformationCollectorUtility {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(InformationCollectorUtility.class);

	/**
	 * Read the parameters and start the logic.
	 * 
	 * @see InformationCollectorParameters
	 */
	public static void readParametersAndStartLogic(IFullExecutionId executionId,
			IInformationCollectionLogic informationCollectionLogic, String[] args) {

		ProgramArgsReader argsReader = InformationCollectorParameters.createProgramArgsReader(args);
		logInfos(executionId, argsReader);

		try {
			String[] jarsWithTests = argsReader.getArgument(InformationCollectorParameters.ARGS_FILE_WITH_TESTS_TO_RUN)
					.split(CommonConstants.SEPARATOR_DEFAULT);
			String dataOutputPath = argsReader.getArgument(InformationCollectorParameters.ARGS_FILE_WITH_RESULTS);
			ITestRunner testRunner = JavaUtility
					.createInstance(argsReader.getArgument(InformationCollectorParameters.ARGS_TEST_RUNNER_CLASS));
			boolean operateFaultTolerant = Boolean.parseBoolean(argsReader
					.getArgument(InformationCollectorParameters.ARGS_OPERATE_FAULT_TOLERANT, Boolean.FALSE.toString()));
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
			informationCollectionLogic.execute(jarsWithTests, testClassIncludes, testClassExcludes,
					operateFaultTolerant);

			System.exit(0);
		} catch (Throwable t) {
			LOGGER.error(t);
			throw new ExecutionException(executionId, t);
		}
	}

	private static void logInfos(IFullExecutionId executionId, ProgramArgsReader argsReader) {
		LOGGER.info(LoggingConstants.EXECUTION_ID_TEXT + executionId.get());
		LOGGER.info(LoggingUtil.getInputArgumentsF1(argsReader));
	}
}
