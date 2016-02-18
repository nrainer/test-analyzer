package de.tum.in.niedermr.ta.runner.start;

import java.io.IOException;

import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.core.common.util.CommonUtility;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.configuration.ConfigurationLoader;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.constants.TestAnalyzerConstants;

/**
 * Allows running the TestAnalyzer with many configurations which will be executed sequentially.
 * 
 */
public class AnalyzerRunnerBatchStart {
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
		final String configurationFiles = CommonUtility.getArgument(args, 0, getConfigurationFiles());

		String[] configurationFilesArray = configurationFiles.split(CommonConstants.SEPARATOR_DEFAULT);

		for (String configFile : configurationFilesArray) {
			if (configFile.isEmpty()) {
				continue;
			}

			try {
				Configuration configuration = ConfigurationLoader.getConfigurationFromFile(configFile,
						TestAnalyzerConstants.CONFIGURATION_FOLDER_DEFAULT);
				AnalyzerRunnerStart.execute(configuration);
			} catch (Exception ex) {
				System.err.println("Skipped: " + configFile);
				ex.printStackTrace();
			}
		}
	}
}
