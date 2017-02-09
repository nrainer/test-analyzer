package de.tum.in.niedermr.ta.test.integration.jacoco;

import java.io.IOException;

import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKey;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKeyNamespace;
import de.tum.in.niedermr.ta.runner.execution.JavaProcessCommandBuilder;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;

/** Adjusted {@link ProcessExecution} which invokes processes with JaCoCo parameters. */
public class ProcessExecutionWithJaCoCo extends ProcessExecution {

	/**
	 * <code>extension.integrationTest.jacoco.enabled</code>: Whether recording code coverage is enabled.
	 */
	public static final DynamicConfigurationKey CONFIGURATION_KEY_PATH_TO_JACOCO_ENABLED = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.EXTENSION, "integrationTest.jacoco.enabled", false);

	/**
	 * <code>extension.integrationTest.jacoco.agent</code>: Path to the JaCoCo agent to record the code coverage.
	 */
	public static final DynamicConfigurationKey CONFIGURATION_KEY_PATH_TO_JACOCO_AGENT = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.EXTENSION, "integrationTest.jacoco.agent", "");

	/**
	 * <code>extension.integrationTest.jacoco.output</code>: Coverage output file.
	 */
	public static final DynamicConfigurationKey CONFIGURATION_KEY_PATH_TO_JACOCO_OUTPUT = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.EXTENSION, "integrationTest.jacoco.output", "");

	/** Configuration. */
	private final Configuration m_configuration;
	/** Path to the JaCoCo agent. */
	private String m_jaCoCoAgentPath;
	/** Path to the JaCoCo output. */
	private String m_jaCoCoOutputPath;

	/** Constructor. */
	public ProcessExecutionWithJaCoCo(Configuration configuration, String executionDirectory,
			String programFolderForClasspath, String workingFolderForClasspath) {
		super(executionDirectory, programFolderForClasspath, workingFolderForClasspath);
		m_configuration = configuration;
		m_jaCoCoAgentPath = m_configuration.getDynamicValues().getStringValue(CONFIGURATION_KEY_PATH_TO_JACOCO_AGENT);
		m_jaCoCoOutputPath = m_configuration.getDynamicValues().getStringValue(CONFIGURATION_KEY_PATH_TO_JACOCO_OUTPUT);
	}

	/** {@inheritDoc} */
	@Override
	protected JavaProcessCommandBuilder createProcessCommand(String mainClassName, String classpath, String[] arguments)
			throws IOException {
		JavaProcessCommandBuilder command = super.createProcessCommand(mainClassName, classpath, arguments);
		command.addJavaArgument("-javaagent:" + m_jaCoCoAgentPath + "=destfile=" + m_jaCoCoOutputPath);
		return command;
	}

}
