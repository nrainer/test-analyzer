package de.tum.in.niedermr.ta.runner.analysis;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.analysis.result.presentation.IResultPresentation;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunResult;
import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunner;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;
import de.tum.in.niedermr.ta.core.common.io.TextFileData;
import de.tum.in.niedermr.ta.core.common.util.CommonUtility;
import de.tum.in.niedermr.ta.runner.analysis.result.presentation.ResultPresentationUtil;
import de.tum.in.niedermr.ta.runner.configuration.property.ResultPresentationProperty;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;
import de.tum.in.niedermr.ta.runner.logging.LoggingConstants;
import de.tum.in.niedermr.ta.runner.logging.LoggingUtil;
import de.tum.in.niedermr.ta.runner.start.AnalyzerRunnerStart;

/**
 * <b>TSTRUN:</b> <b>Runs the specified testcases</b> and records the results.<br/>
 * <br/>
 * Dependencies: ASM, log4j, core.<br/>
 * Further classpath entries: jars to be processed and dependencies.
 *
 */
public class TestRun {
	private static final Logger LOG = LogManager.getLogger(TestRun.class);

	private static String s_executionId;

	/**
	 * Note that the jar file of the class to be tested must be on the classpath!
	 * 
	 * args[0]: execution id args[1]: path to file with tests to run args[2]: path to file with the results (will be
	 * appended) args[3]: identifier of the mutated method (to record the parameters) args[4]: name of the test runner
	 * args[5]: name of the return value generator (to record the parameters) args[6] (optional): result presentation:
	 * 'TEXT' (default), 'DB', or the name of a class implementing IResultPresentation
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		if (args.length == 0) {
			LoggingUtil.printDontStartThisClass(TestRun.class, AnalyzerRunnerStart.class);
			return;
		}

		TestRun.s_executionId = CommonUtility.getArgument(args, 0);
		LOG.info(LoggingConstants.EXECUTION_ID_PREFIX + s_executionId);
		LOG.info(LoggingUtil.getInputArgumentsF1(args));

		try {
			final String fileWithTestsToRun = CommonUtility.getArgument(args, 1);
			final String fileWithResults = CommonUtility.getArgument(args, 2);
			final MethodIdentifier mutatedMethod = MethodIdentifier.parse(CommonUtility.getArgument(args, 3));
			final ITestRunner testRunner = JavaUtility.createInstance(CommonUtility.getArgument(args, 4));
			final String usedReturnValueGenerator = CommonUtility.getArgument(args, 5);
			final String resultPresentationChoice = CommonUtility.getArgument(args, 6,
					ResultPresentationProperty.RESULT_PRESENTATION_TEXT);

			final IResultPresentation resultPresentation = ResultPresentationUtil
					.getResultPresentation(resultPresentationChoice, s_executionId);

			List<String> result = runTestsFromFile(testRunner, fileWithTestsToRun, resultPresentation, mutatedMethod,
					usedReturnValueGenerator);

			TextFileData.appendToFile(fileWithResults, result);

			System.exit(0);
		} catch (Throwable t) {
			LOG.error("Failed execution " + s_executionId, t);
			throw new ExecutionException(s_executionId, t);
		}
	}

	private static List<String> runTestsFromFile(ITestRunner testRunner, String fileWithTestsToRun,
			IResultPresentation resultPresentation, MethodIdentifier methodUnderTest, String usedReturnValueGenerator)
					throws IOException, ReflectiveOperationException {
		List<TestcaseIdentifier> allTestsToRun = parseTestcasesToRun(TextFileData.readFromFile(fileWithTestsToRun));
		List<String> result = new LinkedList<>();

		for (TestcaseIdentifier testcase : allTestsToRun) {
			ITestRunResult testResult = testRunner.runTest(testcase.resolveTestClass(), testcase.getTestcaseName());

			result.add(resultPresentation.formatResultInformation(testcase, testResult, methodUnderTest,
					usedReturnValueGenerator));
		}

		LOG.info(s_executionId + ": "
				+ LoggingUtil.singularOrPlural(allTestsToRun, "testcase was", "testcases were", true)
				+ " run successfully.");

		return result;
	}

	private static List<TestcaseIdentifier> parseTestcasesToRun(List<String> testcasesFromFile) {
		List<TestcaseIdentifier> result = new LinkedList<>();

		for (String testcaseString : testcasesFromFile) {
			result.add(TestcaseIdentifier.parse(testcaseString));
		}

		return result;
	}
}
