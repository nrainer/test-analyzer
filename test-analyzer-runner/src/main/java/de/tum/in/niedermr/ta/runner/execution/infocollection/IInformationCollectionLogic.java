package de.tum.in.niedermr.ta.runner.execution.infocollection;

import de.tum.in.niedermr.ta.core.analysis.result.presentation.IResultPresentation;
import de.tum.in.niedermr.ta.core.code.iteration.IteratorException;
import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunner;
import de.tum.in.niedermr.ta.core.execution.id.IFullExecutionId;

/** Information collector logic. */
public interface IInformationCollectionLogic {

	/** Set the execution id. */
	void setExecutionId(IFullExecutionId executionId);

	/** Get the execution id. */
	IFullExecutionId getExecutionId();

	/** {@link #m_testRunner} */
	void setTestRunner(ITestRunner testRunner);

	/** {@link #m_testRunner} */
	ITestRunner getTestRunner();

	/** {@link #m_outputFile} */
	String getOutputFile();

	/** {@link #m_outputFile} */
	void setOutputFile(String outputFile);

	/** {@link #m_resultPresentation} */
	void setResultPresentation(IResultPresentation resultPresentation);

	/** {@link #m_resultPresentation} */
	IResultPresentation getResultPresentation();

	/** {@link #m_useMultiFileOutput} */
	void setUseMultiFileOutput(boolean useMultiFileOutput);

	/** {@link #m_useMultiFileOutput} */
	boolean isUseMultiFileOutput();

	/** Execute the logic. */
	void execute(String[] jarsWithTests, String[] testClassIncludes, String[] testClassExcludes,
			boolean operateFaultTolerant) throws IteratorException, ReflectiveOperationException;
}