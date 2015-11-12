package de.tum.in.niedermr.ta.runner.execution.infocollection;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.tum.in.niedermr.ta.core.analysis.instrumentation.InvocationLogger;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.core.code.tests.TestInformation;
import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;
import de.tum.in.niedermr.ta.core.common.io.TextFileData;
import de.tum.in.niedermr.ta.core.common.util.CommonUtility;
import de.tum.in.niedermr.ta.runner.logging.LoggingUtil;

public class InformationCollectionLogic extends AbstractInformationCollectionLogic {
	protected final Map<MethodIdentifier, TestInformation> m_methodInformation;
	private String m_outputFile;

	public InformationCollectionLogic(String executionId) {
		super(executionId);
		this.m_methodInformation = new HashMap<>();
	}

	public String getOutputFile() {
		return m_outputFile;
	}

	public void setOutputFile(String outputFile) {
		this.m_outputFile = outputFile;
	}

	@Override
	protected void execBeforeExecutingTestcase(TestcaseIdentifier testCaseIdentifier) {
		resetInvocationLog();
	}

	@Override
	protected void execTestcaseExecutedSuccessfully(TestcaseIdentifier testCaseIdentifier) {
		Set<String> methodsUnderTest = getInvocationLogContent();

		for (String loggedMethodIdentifier : methodsUnderTest) {
			MethodIdentifier identifier = MethodIdentifier.parse(loggedMethodIdentifier);

			TestInformation testInformation = m_methodInformation.get(identifier);

			if (testInformation == null) {
				testInformation = new TestInformation(identifier);
				m_methodInformation.put(identifier, testInformation);
			}

			testInformation.addTestcase(testCaseIdentifier.resolveTestClassNoEx(), testCaseIdentifier.getTestcaseName());
		}
	}

	@Override
	protected void execAllTestsExecuted(Map<Class<?>, Set<String>> testClassesWithTestcases) {
		Collection<TestInformation> result = m_methodInformation.values();

		LOG.info("Collected " + LoggingUtil.appendPluralS(result, "method", true) + " which are directly or indirectly invoked by testcases.");
		LOG.info("Collected " + LoggingUtil.appendPluralS(countTestcases(result), "testcase", true) + " in "
				+ LoggingUtil.singularOrPlural(testClassesWithTestcases.size(), "test class", "test classes", true) + ".");

		final String shortExecutionId = getExecutionId().substring(0, CommonUtility.LENGTH_OF_RANDOM_ID);

		try {
			writeResultToFiles(shortExecutionId, result);
		} catch (IOException ex) {
			LOG.error("When writing data to file", ex);
		}
	}

	protected void writeResultToFiles(String shortExecutionId, Collection<TestInformation> result) throws IOException {
		TextFileData.writeToFile(m_outputFile, CollectedInformation.toPlainText(result));
		TextFileData.writeToFile(getAdditionalSqlOutputFile(m_outputFile), CollectedInformation.toSQLStatements(result, shortExecutionId));
	}

	protected String getAdditionalSqlOutputFile(String mainOutputFile) {
		String additionalSqlOutputFile = mainOutputFile;

		if (mainOutputFile.endsWith(FileSystemConstants.FILE_EXTENSION_TXT)) {
			additionalSqlOutputFile = additionalSqlOutputFile.substring(0, additionalSqlOutputFile.lastIndexOf(FileSystemConstants.FILE_EXTENSION_TXT));
		}

		additionalSqlOutputFile += FileSystemConstants.FILE_EXTENSION_SQL_TXT;

		return additionalSqlOutputFile;
	}

	protected void resetInvocationLog() {
		InvocationLogger.reset();
	}

	protected Set<String> getInvocationLogContent() {
		return InvocationLogger.getTestingLog();
	}

	protected int countTestcases(Collection<TestInformation> testInformationCollection) {
		Set<TestcaseIdentifier> testcaseIdentifiers = new HashSet<>();

		for (TestInformation t : testInformationCollection) {
			testcaseIdentifiers.addAll(t.getTestcases());
		}

		return testcaseIdentifiers.size();
	}
}
