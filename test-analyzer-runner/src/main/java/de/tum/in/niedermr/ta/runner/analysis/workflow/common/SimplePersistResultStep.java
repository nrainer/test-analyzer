package de.tum.in.niedermr.ta.runner.analysis.workflow.common;

import java.util.LinkedList;
import java.util.List;

import de.tum.in.niedermr.ta.core.common.io.TextFileData;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;

/** Execution step to write the result to a file. */
public class SimplePersistResultStep extends AbstractExecutionStep {

	private List<String> m_result = new LinkedList<>();
	private String m_resultFileName;

	/** {@inheritDoc} */
	@Override
	protected String getSuffixForFullExecutionId() {
		return "SMPPER";
	}

	public void setResult(List<String> result) {
		m_result = result;
	}

	public void appendToResult(String line) {
		m_result.add(line);
	}

	public void appendToResult(List<String> lines) {
		m_result.addAll(lines);
	}

	public void setResultFileName(String resultFileName) {
		m_resultFileName = resultFileName;
	}

	/** {@inheritDoc} */
	@Override
	protected void runInternal(Configuration configuration, ProcessExecution processExecution) throws Throwable {
		TextFileData.writeToFile(getFileInWorkingArea(m_resultFileName), m_result);
	}

	/** {@inheritDoc} */
	@Override
	protected String getDescription() {
		return "Persisting the result in a single file";
	}
}
