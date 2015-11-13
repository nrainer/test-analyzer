package de.tum.in.niedermr.ta.runner.start;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.conqat.lib.commons.filesystem.FileSystemUtils;

import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;
import de.tum.in.niedermr.ta.core.common.io.TextFileData;
import de.tum.in.niedermr.ta.core.common.util.ClasspathUtility;
import de.tum.in.niedermr.ta.runner.analysis.AnalyzerRunnerInternal;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.configuration.ConfigurationLoader;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.constants.TestAnalyzerConstants;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.environment.Environment;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;
import de.tum.in.niedermr.ta.runner.execution.exceptions.FailedExecution;

/**
 * <b>Executes MagicStartInternal</b> in a new process with the needed classpath.<br/>
 * The process will be started in the working area which is specified in the configuration.<br/>
 * <br/>
 * <b>Classpath:</b>
 * <code>java -classpath "./bin/;../Core/lib/ccsm-commons.jar;../Core/bin/;../Core/lib/asm-5.0_ALPHA.jar;../Core/lib/asm-analysis-5.0_ALPHA.jar;../Core/lib/asm-commons-5.0_ALPHA.jar;../Core/lib/asm-tree-5.0_ALPHA.jar;../Core/lib/commons-io-2.4.jar;../Core/lib/log4j-api-2.0-beta4.jar;../Core/lib/log4j-core-2.0-beta4.jar;" de.tum.in.niedermr.ta.runner.MagicStart</code>
 *
 */
public class AnalyzerRunnerStart {
	private static final String DEFAULT_EXEC_ID = "ANALYS";
	private static String s_usedExecId = DEFAULT_EXEC_ID;

	/**
	 * Main method.
	 * 
	 * @see #execute(Configuration)
	 * @see #execute(Configuration, File)
	 * 
	 * @param args
	 *            as specified in {@link Configuration} (If no arguments are specified, the values will be requested
	 *            using System.in.)
	 */
	public static void main(String[] args) throws ConfigurationException, IOException {
		Configuration configuration = null;

		try {
			configuration = ConfigurationLoader.getConfiguration(args,
					TestAnalyzerConstants.CONFIGURATION_FOLDER_DEFAULT);
		} catch (ConfigurationException ex) {
			if (ex.getCause() instanceof FileNotFoundException) {
				print("Configuration file not found.");
				return;
			}

			throw ex;
		}

		execute(configuration);
	}

	public static void execute(Configuration configuration) throws IOException {
		execute(configuration, new File(FileSystemConstants.CURRENT_FOLDER));
	}

	public static void execute(Configuration configuration, File locationMagicStart) throws IOException {
		final String currentCanonicalPath = locationMagicStart.getCanonicalPath();
		final String workingFolder = configuration.getWorkingFolder().getValue();

		print("Working folder is: " + workingFolder);

		if (configuration.getTestAnalyzerClasspath().isEmpty()) {
			configuration.getTestAnalyzerClasspath().setValue(ClasspathUtility.getCurrentProgramClasspath());
		} else {
			print("Using TestAnalyzer classpath from configuration!");
		}

		copyConfigurationIntoWorkingFolder(
				Environment.replaceWorkingFolder(EnvironmentConstants.FILE_INPUT_USED_CONFIG, workingFolder),
				configuration);

		try {
			ProcessExecution pExecution = new ProcessExecution(workingFolder, currentCanonicalPath, workingFolder);

			final String classpath = configuration.getTestAnalyzerClasspath().getValue() + FileSystemConstants.CP_SEP
					+ Environment.prefixClasspathInWorkingFolder(configuration.getFullClasspath());

			List<String> arguments = new LinkedList<>();
			arguments.add(currentCanonicalPath);
			arguments.add(EnvironmentConstants.FILE_INPUT_USED_CONFIG);

			pExecution.execute(s_usedExecId, ProcessExecution.NO_TIMEOUT, AnalyzerRunnerInternal.class.getName(),
					classpath, arguments);

			print("DONE.");
		} catch (FailedExecution ex) {
			print("ERROR. (" + ex.getMessage() + ")");
		}
	}

	private static void copyConfigurationIntoWorkingFolder(String file, Configuration configuration)
			throws IOException {
		FileSystemUtils.ensureDirectoryExists(new File(file).getParentFile());
		TextFileData.writeToFile(file, ConfigurationLoader.toFileLines(configuration));
	}

	private static void print(String value) {
		System.out.println(value);
	}

	/**
	 * Sets the execution id to {@link AnalyzerRunnerInternal#EXECUTION_ID_FOR_TESTS} in order to allow a result
	 * comparison.
	 */
	public static void setTestMode() {
		s_usedExecId = AnalyzerRunnerInternal.EXECUTION_ID_FOR_TESTS;
	}
}
