package de.tum.in.niedermr.ta.extensions.analysis.workflows.converter;

import java.io.File;
import java.util.Objects;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.parser.CoverageParserException;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;

/** Abstract parser step. */
public abstract class AbstractParserStep extends AbstractExecutionStep {

	/** File to be parsed. */
	private String m_inputFileName;

	/** Result receiver. */
	private IResultReceiver m_resultReceiver;

	/** {@inheritDoc} */
	@Override
	protected void runInternal(Configuration configuration, ProcessExecution processExecution)
			throws ExecutionException {
		Objects.requireNonNull(m_inputFileName);
		Objects.requireNonNull(m_resultReceiver);

		try {
			parse(new File(m_inputFileName), m_resultReceiver);
		} catch (CoverageParserException e) {
			throw new ExecutionException(getExecutionId(), e);
		}
	}

	/** Parse the data from the file and write the result to the result receiver. */
	protected abstract void parse(File inputFile, IResultReceiver resultReceiver) throws CoverageParserException;

	/** {@link #m_inputFileName} */
	public void setInputFileName(String coverageFileName) {
		m_inputFileName = coverageFileName;

		if (!new File(coverageFileName).isAbsolute()) {
			m_inputFileName = getFileInWorkingArea(EnvironmentConstants.FOLDER_WORKING_AREA + m_inputFileName);
		}
	}

	/** {@link #m_resultReceiver} */
	public void setResultReceiver(IResultReceiver coverageResultReceiver) {
		m_resultReceiver = coverageResultReceiver;
	}
}
