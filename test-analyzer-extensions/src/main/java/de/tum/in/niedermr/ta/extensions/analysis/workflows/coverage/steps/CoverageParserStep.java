package de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.steps;

import java.io.File;
import java.util.List;

import de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.parser.ICoverageParser;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.parser.JaCoCoXmlParser;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;

/** Step to parse coverage files. */
public class CoverageParserStep extends AbstractExecutionStep {

	/** Result. */
	private List<String> m_result;

	/** File to be parsed. */
	private String m_coverageFileName;

	/** {@inheritDoc} */
	@Override
	protected String getSuffixForFullExecutionId() {
		return "COVPAR";
	}

	/** {@inheritDoc} */
	@Override
	protected void runInternal(Configuration configuration, ProcessExecution processExecution) throws Throwable {
		ICoverageParser coverageParser = new JaCoCoXmlParser(getExecutionId());
		coverageParser.initialize();
		coverageParser.parse(new File(m_coverageFileName));
		m_result = coverageParser.getResult();
	}

	public void setCoverageFileName(String coverageFileName) {
		m_coverageFileName = coverageFileName;

		if (!new File(coverageFileName).isAbsolute()) {
			m_coverageFileName = getFileInWorkingArea(EnvironmentConstants.FOLDER_WORKING_AREA + m_coverageFileName);
		}
	}

	public List<String> getResult() {
		return m_result;
	}

	/** {@inheritDoc} */
	@Override
	protected String getDescription() {
		return "Parse coverage files";
	}
}
