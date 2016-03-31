package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.analysis.result.presentation.IResultPresentation;
import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunner;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;
import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.logic.collection.AnalysisInformationCollectionLogic;
import de.tum.in.niedermr.ta.runner.analysis.result.presentation.ResultPresentationUtil;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.args.ProgramArgsKey;
import de.tum.in.niedermr.ta.runner.execution.args.ProgramArgsReader;
import de.tum.in.niedermr.ta.runner.execution.args.ProgramArgsWriter;
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

	/** Number of args. */
	private static final int ARGS_COUNT = 8;
	public static final ProgramArgsKey ARGS_EXECUTION_ID = new ProgramArgsKey(AnalysisInformationCollector.class, 0);
	public static final ProgramArgsKey ARGS_FILE_WITH_TESTS_TO_RUN = new ProgramArgsKey(
			AnalysisInformationCollector.class, 1);
	public static final ProgramArgsKey ARGS_RESULT_FILE = new ProgramArgsKey(AnalysisInformationCollector.class, 2);
	public static final ProgramArgsKey ARGS_TEST_RUNNER_CLASS = new ProgramArgsKey(AnalysisInformationCollector.class,
			3);
	public static final ProgramArgsKey ARGS_OPERATE_FAULT_TOLERANT = new ProgramArgsKey(
			AnalysisInformationCollector.class, 4);
	public static final ProgramArgsKey ARGS_TEST_CLASS_INCLUDES = new ProgramArgsKey(AnalysisInformationCollector.class,
			5);
	public static final ProgramArgsKey ARGS_TEST_CLASS_EXCLUDES = new ProgramArgsKey(AnalysisInformationCollector.class,
			6);
	/** Result presentation: 'TEXT', 'DB' or the name of a class implementing {@link IResultPresentation}. */
	public static final ProgramArgsKey ARGS_RESULT_PRESENTATION = new ProgramArgsKey(AnalysisInformationCollector.class,
			7);

	public static void main(String[] args) {
		if (args.length == 0) {
			LoggingUtil.printDontStartThisClass(AnalysisInformationCollector.class, AnalyzerRunnerStart.class);
			return;
		}

		ProgramArgsReader argsReader = new ProgramArgsReader(AnalysisInformationCollector.class, args);

		final String executionId = argsReader.getArgument(ARGS_EXECUTION_ID);
		final AnalysisInformationCollectionLogic analysisInformationCollectionLogic = new AnalysisInformationCollectionLogic(
				executionId);

		main(argsReader, executionId, analysisInformationCollectionLogic);
	}

	private static void main(ProgramArgsReader argsReader, String executionId,
			AnalysisInformationCollectionLogic analysisInformationCollectionLogic) {

		LOG.info(LoggingConstants.EXECUTION_ID_PREFIX + executionId);
		LOG.info(LoggingUtil.getInputArgumentsF1(argsReader));

		try {
			final String[] jarsWithTests = argsReader.getArgument(ARGS_FILE_WITH_TESTS_TO_RUN)
					.split(CommonConstants.SEPARATOR_DEFAULT);
			final String dataOutputPath = argsReader.getArgument(ARGS_RESULT_FILE);
			final ITestRunner testRunner = JavaUtility.createInstance(argsReader.getArgument(ARGS_TEST_RUNNER_CLASS));
			final boolean operateFaultTolerant = Boolean
					.parseBoolean(argsReader.getArgument(ARGS_OPERATE_FAULT_TOLERANT, Boolean.FALSE.toString()));
			final String[] testClassIncludes = ProcessExecution
					.unwrapAndSplitPattern(argsReader.getArgument(ARGS_TEST_CLASS_INCLUDES, true));
			final String[] testClassExcludes = ProcessExecution
					.unwrapAndSplitPattern(argsReader.getArgument(ARGS_TEST_CLASS_EXCLUDES, true));
			final String resultPresentationChoice = argsReader.getArgument(ARGS_RESULT_PRESENTATION);

			final IResultPresentation resultPresentation = ResultPresentationUtil
					.getResultPresentation(resultPresentationChoice, executionId);

			analysisInformationCollectionLogic.setTestRunner(testRunner);
			analysisInformationCollectionLogic.setOutputFile(dataOutputPath);
			analysisInformationCollectionLogic.setResultPresentation(resultPresentation);
			analysisInformationCollectionLogic.execute(jarsWithTests, testClassIncludes, testClassExcludes,
					operateFaultTolerant);

			System.exit(0);
		} catch (Throwable t) {
			LOG.error(t);
			throw new ExecutionException(executionId, t);
		}
	}

	public static ProgramArgsWriter createProgramArgsWriter() {
		return new ProgramArgsWriter(AnalysisInformationCollector.class, ARGS_COUNT);
	}
}
