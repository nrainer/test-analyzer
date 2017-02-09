package de.tum.in.niedermr.ta.test.integration.jacoco;

import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.factory.DefaultFactory;

/** Adjusted factory which creates {@link ProcessExecutionWithJaCoCo} to execute processes. */
public class DefaultFactoryWithJaCoCoRecording extends DefaultFactory {

	/** {@inheritDoc} */
	@Override
	public ProcessExecution createNewProcessExecution(Configuration configuration, String executionDirectory,
			String programFolderForClasspath, String workingFolderForClasspath) {
		return new ProcessExecutionWithJaCoCo(configuration, executionDirectory, programFolderForClasspath,
				workingFolderForClasspath);
	}

}
