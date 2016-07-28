package de.tum.in.niedermr.ta.runner.analysis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.analysis.result.presentation.IResultPresentation;
import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunner;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;
import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.core.execution.id.IFullExecutionId;
import de.tum.in.niedermr.ta.runner.analysis.result.presentation.ResultPresentationUtil;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.args.ProgramArgsKey;
import de.tum.in.niedermr.ta.runner.execution.args.ProgramArgsReader;
import de.tum.in.niedermr.ta.runner.execution.args.ProgramArgsWriter;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;
import de.tum.in.niedermr.ta.runner.execution.id.ExecutionIdFactory;
import de.tum.in.niedermr.ta.runner.execution.infocollection.InformationCollectionLogic;
import de.tum.in.niedermr.ta.runner.logging.LoggingConstants;
import de.tum.in.niedermr.ta.runner.logging.LoggingUtil;
import de.tum.in.niedermr.ta.runner.start.AnalyzerRunnerStart;

/**
 * <b>INFCOL:</b> Collects <b>information about test classes, testcases and methods under test.</b><br/>
 * The instrumentation step (INSTRU) must have been executed before.<br/>
 * <br/>
 * Note: It does not yet filter the methods under test, because
 * <ul>
 * <li>the method descriptor is not available at this point</li>
 * <li>different return value generators might want to work on different methods</li>
 * </ul>
 * <br/>
 * Dependencies: ASM, log4j, jUnit, core.<br/>
 * Further classpath entries: jars to be processed and dependencies.
 * 
 */
public class InformationCollector {
	private static final Logger LOG = LogManager.getLogger(InformationCollector.class);

	/** Number of args. */
	private static final int ARGS_COUNT = 8;
	public static final ProgramArgsKey ARGS_EXECUTION_ID = new ProgramArgsKey(InformationCollector.class, 0);
	public static final ProgramArgsKey ARGS_FILE_WITH_TESTS_TO_RUN = new ProgramArgsKey(InformationCollector.class, 1);
	public static final ProgramArgsKey ARGS_FILE_WITH_RESULTS = new ProgramArgsKey(InformationCollector.class, 2);
	public static final ProgramArgsKey ARGS_TEST_RUNNER_CLASS = new ProgramArgsKey(InformationCollector.class, 3);
	public static final ProgramArgsKey ARGS_OPERATE_FAULT_TOLERANT = new ProgramArgsKey(InformationCollector.class, 4);
	public static final ProgramArgsKey ARGS_TEST_CLASS_INCLUDES = new ProgramArgsKey(InformationCollector.class, 5);
	public static final ProgramArgsKey ARGS_TEST_CLASS_EXCLUDES = new ProgramArgsKey(InformationCollector.class, 6);
	public static final ProgramArgsKey ARGS_RESULT_PRESENTATION = new ProgramArgsKey(InformationCollector.class, 7);

	public static void main(String[] args) {
		if (args.length == 0) {
			LoggingUtil.printDontStartThisClass(InformationCollector.class, AnalyzerRunnerStart.class);
			return;
		}

		ProgramArgsReader argsReader = new ProgramArgsReader(InformationCollector.class, args);

		IFullExecutionId executionId = ExecutionIdFactory
				.parseFullExecutionId(argsReader.getArgument(ARGS_EXECUTION_ID));
		InformationCollectionLogic informationCollectionLogic = new InformationCollectionLogic(executionId);

		main(argsReader, executionId, informationCollectionLogic);
	}

	public static void main(ProgramArgsReader argsReader, IFullExecutionId executionId,
			InformationCollectionLogic informationCollectionLogic) {
		LOG.info(LoggingConstants.EXECUTION_ID_TEXT + executionId.get());
		LOG.info(LoggingUtil.getInputArgumentsF1(argsReader));

		try {
			final String[] jarsWithTests = argsReader.getArgument(ARGS_FILE_WITH_TESTS_TO_RUN)
					.split(CommonConstants.SEPARATOR_DEFAULT);
			final String dataOutputPath = argsReader.getArgument(ARGS_FILE_WITH_RESULTS);
			final ITestRunner testRunner = JavaUtility.createInstance(argsReader.getArgument(ARGS_TEST_RUNNER_CLASS));
			final boolean operateFaultTolerant = Boolean
					.parseBoolean(argsReader.getArgument(ARGS_OPERATE_FAULT_TOLERANT, Boolean.FALSE.toString()));
			final String[] testClassIncludes = ProcessExecution
					.unwrapAndSplitPattern(argsReader.getArgument(ARGS_TEST_CLASS_INCLUDES, true));
			final String[] testClassExcludes = ProcessExecution
					.unwrapAndSplitPattern(argsReader.getArgument(ARGS_TEST_CLASS_EXCLUDES, true));
			final String resultPresentationChoice = argsReader.getArgument(ARGS_RESULT_PRESENTATION);

			final IResultPresentation resultPresentation = ResultPresentationUtil
					.createResultPresentation(resultPresentationChoice, executionId);

			informationCollectionLogic.setTestRunner(testRunner);
			informationCollectionLogic.setOutputFile(dataOutputPath);
			informationCollectionLogic.setResultPresentation(resultPresentation);
			informationCollectionLogic.execute(jarsWithTests, testClassIncludes, testClassExcludes,
					operateFaultTolerant);

			System.exit(0);
		} catch (Throwable t) {
			LOG.error(t);
			throw new ExecutionException(executionId, t);
		}
	}

	public static ProgramArgsWriter createProgramArgsWriter() {
		return new ProgramArgsWriter(InformationCollector.class, ARGS_COUNT);
	}
}
