package de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.parser;

import java.io.File;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;
import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;

/** Abstract parser step. */
public abstract class AbstractParserStep extends AbstractExecutionStep {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(AbstractParserStep.class);

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
			File inputFile = new File(m_inputFileName);
			LOGGER.info("File to parse is: " + inputFile.getAbsolutePath());
			parse(inputFile, m_resultReceiver);
		} catch (ContentParserException e) {
			throw new ExecutionException(getExecutionId(), e);
		}
	}

	/** Parse the data from the file and write the result to the result receiver. */
	protected void parse(File inputFile, IResultReceiver resultReceiver) throws ContentParserException {
		IContentParser coverageParser = createParser(getExecutionId());
		coverageParser.initialize();
		coverageParser.parse(inputFile, resultReceiver);
		resultReceiver.markResultAsComplete();
	}

	/** Create an instance of the parser. */
	protected abstract IContentParser createParser(IExecutionId executionId);

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
