package de.tum.in.niedermr.ta.runner.execution.infocollection;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.analysis.instrumentation.InvocationLogger;
import de.tum.in.niedermr.ta.core.analysis.result.receiver.FileResultReceiver;
import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;
import de.tum.in.niedermr.ta.core.analysis.result.receiver.MultiFileResultReceiver;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.core.code.tests.TestInformation;
import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;
import de.tum.in.niedermr.ta.core.execution.id.IFullExecutionId;

public class InformationCollectionLogic extends AbstractInformationCollectionLogic {
	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(InformationCollectionLogic.class);

	protected final Map<MethodIdentifier, TestInformation> m_methodInformation;

	public InformationCollectionLogic(IFullExecutionId executionId) {
		super(executionId);
		this.m_methodInformation = new HashMap<>();
	}

	/** {@inheritDoc} */
	@Override
	protected void execBeforeExecutingTestcase(TestcaseIdentifier testCaseIdentifier) {
		resetInvocationLog();
	}

	/** {@inheritDoc} */
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

			testInformation.addTestcase(testCaseIdentifier.resolveTestClassNoEx(),
					testCaseIdentifier.getTestcaseName());
		}
	}

	/** {@inheritDoc} */
	@Override
	protected void execAllTestsExecuted(Map<Class<?>, Set<String>> testClassesWithTestcases) {
		Collection<TestInformation> result = m_methodInformation.values();

		LOGGER.info("Collected " + result.size() + " methods that are directly or indirectly invoked by testcases.");
		LOGGER.info("Collected " + countTestcases(result) + " successful testcases from "
				+ testClassesWithTestcases.size() + " test classes.");

		try {
			writeResultToFiles(result);
		} catch (IOException ex) {
			LOGGER.error("When writing data to file", ex);
		}
	}

	protected void writeResultToFiles(Collection<TestInformation> result) throws IOException {
		IResultReceiver plainTextResultReceiver;
		IResultReceiver additionalResultResultReceiver;

		if (isUseMultiFileOutput()) {
			plainTextResultReceiver = new MultiFileResultReceiver(getOutputFile());
			additionalResultResultReceiver = new MultiFileResultReceiver(
					getAdditionalResultOutputFile(getOutputFile()));
		} else {
			plainTextResultReceiver = new FileResultReceiver(getOutputFile(), true);
			additionalResultResultReceiver = new FileResultReceiver(getAdditionalResultOutputFile(getOutputFile()),
					true);
		}

		CollectedInformationUtility.convertToParseableMethodTestcaseText(result, plainTextResultReceiver);
		CollectedInformationUtility.convertToMethodTestcaseMappingResult(result, getResultPresentation(),
				additionalResultResultReceiver);

		plainTextResultReceiver.markResultAsComplete();
		additionalResultResultReceiver.markResultAsComplete();
	}

	protected String getAdditionalResultOutputFile(String mainOutputFile) {
		String additionalResultOutputFile = mainOutputFile;

		if (mainOutputFile.endsWith(FileSystemConstants.FILE_EXTENSION_TXT)) {
			additionalResultOutputFile = additionalResultOutputFile.substring(0,
					additionalResultOutputFile.lastIndexOf(FileSystemConstants.FILE_EXTENSION_TXT));
		}

		additionalResultOutputFile += FileSystemConstants.FILE_EXTENSION_SQL_TXT;

		return additionalResultOutputFile;
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
