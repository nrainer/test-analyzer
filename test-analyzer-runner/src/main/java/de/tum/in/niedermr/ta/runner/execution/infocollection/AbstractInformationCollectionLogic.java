package de.tum.in.niedermr.ta.runner.execution.infocollection;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.analysis.jars.iteration.IteratorFactory;
import de.tum.in.niedermr.ta.core.analysis.jars.iteration.JarAnalyzeIterator;
import de.tum.in.niedermr.ta.core.analysis.result.presentation.IResultPresentation;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.core.code.iteration.IteratorException;
import de.tum.in.niedermr.ta.core.code.tests.collector.ITestCollector;
import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunResult;
import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunner;
import de.tum.in.niedermr.ta.core.execution.id.IFullExecutionId;
import de.tum.in.niedermr.ta.runner.tests.TestRunnerUtil;

public abstract class AbstractInformationCollectionLogic implements IInformationCollectionLogic {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(AbstractInformationCollectionLogic.class);
	private static final boolean LOG_FULL_STACKTRACE_OF_FAILED_UNMODIFIED_TESTS = false;

	private IFullExecutionId m_executionId;

	private ITestRunner m_testRunner;
	private String m_outputFile;
	private IResultPresentation m_resultPresentation;
	private boolean m_useMultiFileOutput;

	/** {@inheritDoc} */
	@Override
	public void setExecutionId(IFullExecutionId executionId) {
		m_executionId = executionId;
	}

	/** {@inheritDoc} */
	@Override
	public IFullExecutionId getExecutionId() {
		return m_executionId;
	}

	/** {@inheritDoc} */
	@Override
	public void setTestRunner(ITestRunner testRunner) {
		this.m_testRunner = testRunner;
	}

	/** {@inheritDoc} */
	@Override
	public ITestRunner getTestRunner() {
		return m_testRunner;
	}

	/** {@inheritDoc} */
	@Override
	public String getOutputFile() {
		return m_outputFile;
	}

	/** {@inheritDoc} */
	@Override
	public void setOutputFile(String outputFile) {
		this.m_outputFile = outputFile;
	}

	/** {@inheritDoc} */
	@Override
	public void setResultPresentation(IResultPresentation resultPresentation) {
		this.m_resultPresentation = resultPresentation;
	}

	/** {@inheritDoc} */
	@Override
	public IResultPresentation getResultPresentation() {
		return m_resultPresentation;
	}

	/** {@inheritDoc} */
	@Override
	public void setUseMultiFileOutput(boolean useMultiFileOutput) {
		this.m_useMultiFileOutput = useMultiFileOutput;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isUseMultiFileOutput() {
		return m_useMultiFileOutput;
	}

	@Override
	public void execute(String[] jarsWithTests, String[] testClassIncludes, String[] testClassExcludes,
			boolean operateFaultTolerant) throws IteratorException, ReflectiveOperationException {
		Map<Class<?>, Set<String>> testClassesWithTestcases = collectTestClassesWithTestcases(jarsWithTests,
				testClassIncludes, testClassExcludes, operateFaultTolerant);

		execTestClassesCollected(testClassesWithTestcases);

		LOGGER.info(
				"Starting to analyze " + testClassesWithTestcases.size() + " test classes with at least one testcase.");

		execBeforeExecutingAllTests(testClassesWithTestcases);

		executeAllTestcases(testClassesWithTestcases);

		if (testClassExcludes.length > 0) {
			LOGGER.info("Skipped excluded test classes.");
		}

		execAllTestsExecuted(testClassesWithTestcases);
	}

	/**
	 * @param testClassesWithTestcases
	 */
	protected void execBeforeExecutingAllTests(Map<Class<?>, Set<String>> testClassesWithTestcases) {
		// NOP
	}

	/**
	 * @param testClassesWithTestcases
	 */
	protected void execTestClassesCollected(Map<Class<?>, Set<String>> testClassesWithTestcases) {
		// NOP
	}

	/**
	 * @param testClassesWithTestcases
	 */
	protected void execAllTestsExecuted(Map<Class<?>, Set<String>> testClassesWithTestcases) {
		// NOP
	}

	protected Map<Class<?>, Set<String>> collectTestClassesWithTestcases(String[] jarsWithTests,
			String[] testClassIncludes, String[] testClassExcludes, boolean operateFaultTolerant)
			throws IteratorException {
		final ITestCollector collectOperation = TestRunnerUtil.getAppropriateTestCollector(m_testRunner, false,
				testClassIncludes, testClassExcludes);

		for (String inputJar : jarsWithTests) {
			JarAnalyzeIterator jarWork = IteratorFactory.createJarAnalyzeIterator(inputJar, operateFaultTolerant);
			jarWork.execute(collectOperation);
		}

		return collectOperation.getTestClassesWithTestcases();
	}

	protected void executeAllTestcases(Map<Class<?>, Set<String>> testClassesWithTestcases)
			throws ReflectiveOperationException {
		int countUnmodifiedSuccessful = 0;
		int countUnmodifiedFailed = 0;

		for (Entry<Class<?>, Set<String>> entry : testClassesWithTestcases.entrySet()) {
			Class<?> testClass = entry.getKey();
			Set<String> testcasesOfCurrentClass = entry.getValue();

			LOGGER.info("Analyzing test class " + testClass.getName() + " with " + testcasesOfCurrentClass.size()
					+ " testcases.");

			for (String testcase : testcasesOfCurrentClass) {
				boolean testSuccessful = processTestcase(testClass, testcase);

				if (testSuccessful) {
					countUnmodifiedSuccessful++;
				} else {
					countUnmodifiedFailed++;
				}
			}
		}

		LOGGER.info("Executed " + countUnmodifiedSuccessful + " successful test cases on the unmodified jar.");

		if (countUnmodifiedFailed > 0) {
			LOGGER.info("Skipped " + countUnmodifiedFailed + " testcases which failed on the unmodified jar.");
		} else {
			LOGGER.info("No testcases failed on the unmodified jar.");
		}
	}

	/**
	 * @param methodInformation
	 *            the result will be inserted in this map
	 * @return true, if the test is executed with success
	 */
	protected boolean processTestcase(Class<?> testClass, String testCase) {
		TestcaseIdentifier testCaseIdentifier = TestcaseIdentifier.create(testClass, testCase);
		execBeforeExecutingTestcase(testCaseIdentifier);

		ITestRunResult testResult = m_testRunner.runTest(testClass, testCase);

		if (testResult.getFailureCount() > 0) {
			execTestcaseExecutedWithFailure(testCaseIdentifier, testResult);
			return false;
		} else {
			execTestcaseExecutedSuccessfully(testCaseIdentifier);
			return true;
		}
	}

	/**
	 * @param testCaseIdentifier
	 */
	protected void execBeforeExecutingTestcase(TestcaseIdentifier testCaseIdentifier) {
		// NOP
	}

	/** Handle a test case that was executed with a failure. */
	protected void execTestcaseExecutedWithFailure(TestcaseIdentifier testCaseIdentifier, ITestRunResult testResult) {
		Throwable firstException = testResult.getFirstException();

		LOGGER.warn("Testcase running on the unmodified jar failed! " + testCaseIdentifier.get() + " will be skipped! ("
				+ firstException.getClass().getName() + ": " + firstException.getMessage() + ")");

		if (LOG_FULL_STACKTRACE_OF_FAILED_UNMODIFIED_TESTS) {
			LOGGER.warn("Stacktrace is", testResult.getFirstException());
		}
	}

	/**
	 * @param testCaseIdentifier
	 */
	protected void execTestcaseExecutedSuccessfully(TestcaseIdentifier testCaseIdentifier) {
		// NOP
	}
}
