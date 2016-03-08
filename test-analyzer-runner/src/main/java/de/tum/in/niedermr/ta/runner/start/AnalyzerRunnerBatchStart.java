package de.tum.in.niedermr.ta.runner.start;

import java.io.IOException;

import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.configuration.ConfigurationLoader;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.execution.args.ProgramArgsKey;
import de.tum.in.niedermr.ta.runner.execution.args.ProgramArgsReader;

/**
 * Allows running the TestAnalyzer with many configurations which will be executed sequentially.
 * 
 */
public class AnalyzerRunnerBatchStart {

	public static final ProgramArgsKey ARGS_CONFIG_FILES = new ProgramArgsKey(AnalyzerRunnerBatchStart.class, 0);

	/**
	 * Only considered if main is invoked without arguments.
	 * 
	 * @return path of the configuration files separated by {@link CommonConstants#SEPARATOR_DEFAULT} (relative to the
	 *         TestAnalyzer program path)
	 */
	private static String getConfigurationFiles() {
		return "";
	}

	/**
	 * args[0]: path of the configuration files separated by {@link CommonConstants#SEPARATOR_DEFAULT} (relative to
	 * TestAnalyzer program path)
	 */
	public static void main(String[] args) throws ConfigurationException, IOException {
		final String configurationFiles;

		if (args.length > 0) {
			ProgramArgsReader argsReader = new ProgramArgsReader(AnalyzerRunnerBatchStart.class, args);
			configurationFiles = argsReader.getArgument(ARGS_CONFIG_FILES);
		} else {
			configurationFiles = getConfigurationFiles();
		}

		String[] configurationFilesArray = configurationFiles.split(CommonConstants.SEPARATOR_DEFAULT);

		for (String configFile : configurationFilesArray) {
			runConfigFile(configFile);
		}
	}

	/** Run {@link AnalyzerRunnerStart} for a single config file. */
	private static void runConfigFile(String configFile) {
		if (configFile.isEmpty()) {
			return;
		}

		try {
			Configuration configuration = ConfigurationLoader.getConfigurationFromFile(configFile);
			AnalyzerRunnerStart.execute(configuration);
		} catch (Exception ex) {
			System.err.println("Skipped: " + configFile);
			ex.printStackTrace();
		}
	}
}
