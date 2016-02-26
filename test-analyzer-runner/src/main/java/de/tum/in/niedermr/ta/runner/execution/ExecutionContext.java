package de.tum.in.niedermr.ta.runner.execution;

import de.tum.in.niedermr.ta.runner.configuration.Configuration;

public class ExecutionContext {
	private final String m_executionId;
	private final Configuration m_configuration;
	private final String m_programPath;
	private final String m_workingFolder;

	public ExecutionContext(String executionId, Configuration configuration, String programPath,
			String workingFolder) {
		this.m_executionId = executionId;
		this.m_configuration = configuration;
		this.m_programPath = programPath;
		this.m_workingFolder = workingFolder;
	}

	public String getExecutionId() {
		return m_executionId;
	}

	public Configuration getConfiguration() {
		return m_configuration;
	}

	public String getProgramPath() {
		return m_programPath;
	}

	public String getWorkingFolder() {
		return m_workingFolder;
	}
}