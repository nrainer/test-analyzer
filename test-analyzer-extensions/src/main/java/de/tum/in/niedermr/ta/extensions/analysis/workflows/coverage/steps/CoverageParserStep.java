package de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.steps;

import java.io.File;
import java.util.Objects;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.parser.ICoverageParser;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.parser.JaCoCoXmlParser;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;

/** Step to parse coverage files. */
public class CoverageParserStep extends AbstractExecutionStep {

	/** File to be parsed. */
	private String m_coverageFileName;

	private IResultReceiver m_coverageResultReceiver;

	/** {@inheritDoc} */
	@Override
	protected String getSuffixForFullExecutionId() {
		return "COVPAR";
	}

	/** {@inheritDoc} */
	@Override
	protected void runInternal(Configuration configuration, ProcessExecution processExecution) throws Throwable {
		Objects.requireNonNull(m_coverageFileName);
		Objects.requireNonNull(m_coverageResultReceiver);

		ICoverageParser coverageParser = new JaCoCoXmlParser(getExecutionId());
		coverageParser.initialize();
		coverageParser.parse(new File(m_coverageFileName), m_coverageResultReceiver);
		m_coverageResultReceiver.markResultAsComplete();
	}

	public void setCoverageFileName(String coverageFileName) {
		m_coverageFileName = coverageFileName;

		if (!new File(coverageFileName).isAbsolute()) {
			m_coverageFileName = getFileInWorkingArea(EnvironmentConstants.FOLDER_WORKING_AREA + m_coverageFileName);
		}
	}

	public void setCoverageResultReceiver(IResultReceiver coverageResultReceiver) {
		m_coverageResultReceiver = coverageResultReceiver;
	}

	/** {@inheritDoc} */
	@Override
	protected String getDescription() {
		return "Parse coverage files";
	}
}
