package de.tum.in.niedermr.ta.test.integration.jacoco;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.factory.DefaultFactory;

/** Adjusted factory which creates {@link ProcessExecutionWithJaCoCo} to execute processes. */
public class DefaultFactoryWithJaCoCoRecording extends DefaultFactory {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(DefaultFactoryWithJaCoCoRecording.class);

	/** {@inheritDoc} */
	@Override
	public ProcessExecution createNewProcessExecution(Configuration configuration, String executionDirectory,
			String programFolderForClasspath, String workingFolderForClasspath) {
		if (!configuration.getDynamicValues()
				.getBooleanValue(ProcessExecutionWithJaCoCo.CONFIGURATION_KEY_PATH_TO_JACOCO_ENABLED)) {
			LOGGER.warn(DefaultFactoryWithJaCoCoRecording.class.getName()
					+ " is used but JaCoCo recording is disabled. The path to the JaCoCo agent is probably invalid.");
			return super.createNewProcessExecution(configuration, executionDirectory, programFolderForClasspath,
					workingFolderForClasspath);
		}

		return new ProcessExecutionWithJaCoCo(configuration, executionDirectory, programFolderForClasspath,
				workingFolderForClasspath);
	}

}
