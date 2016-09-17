package de.tum.in.niedermr.ta.runner.start;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.conqat.lib.commons.filesystem.FileSystemUtils;

import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;
import de.tum.in.niedermr.ta.core.common.util.ClasspathUtility;
import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.runner.analysis.AnalyzerRunnerInternal;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.configuration.ConfigurationLoader;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.args.ProgramArgsWriter;
import de.tum.in.niedermr.ta.runner.execution.environment.Environment;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;
import de.tum.in.niedermr.ta.runner.execution.id.ExecutionIdFactory;
import de.tum.in.niedermr.ta.runner.factory.FactoryUtil;
import de.tum.in.niedermr.ta.runner.factory.IFactory;

/**
 * <b>Executes AnalyzerRunnerInternal</b> in a new process with the needed
 * classpath.<br/>
 * The process will be started in the working area which is specified in the
 * configuration.<br/>
 *
 */
public class AnalyzerRunnerStart {
	private static boolean s_inTestMode = false;

	/**
	 * Main method.
	 * 
	 * @see #execute(Configuration)
	 * @see #execute(Configuration, File)
	 * 
	 * @param args
	 *            as specified in {@link Configuration} (If no arguments are
	 *            specified, the values will be requested using System.in.)
	 */
	public static void main(String[] args) throws ConfigurationException, IOException {
		Configuration configuration = null;

		try {
			configuration = ConfigurationLoader.getConfiguration(args);
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

	public static void execute(Configuration configuration, File locationTestAnalyzer) throws IOException {
		IExecutionId executionId = ExecutionIdFactory.createNewShortExecutionId();

		if (s_inTestMode) {
			executionId = ExecutionIdFactory.ID_FOR_TESTS;
		}

		final String currentCanonicalPath = locationTestAnalyzer.getCanonicalPath();
		final String workingFolder = configuration.getWorkingFolder().getValue();

		print("Working folder is: " + workingFolder);

		if (configuration.getTestAnalyzerClasspath().isEmpty()) {
			configuration.getTestAnalyzerClasspath().setValue(ClasspathUtility.getCurrentProgramClasspath());
		} else {
			print("Using the test analyzer classpath from the configuration!");
		}

		copyConfigurationIntoWorkingFolder(
				Environment.replaceWorkingFolder(EnvironmentConstants.FILE_INPUT_USED_CONFIG, workingFolder),
				configuration);

		try {
			startExecutionInNewProcess(configuration, executionId, currentCanonicalPath, workingFolder);
			print("DONE.");
		} catch (ExecutionException ex) {
			print("ERROR. (" + ex.getMessage() + ")");
		}
	}

	/** Start the execution in a new process. */
	private static void startExecutionInNewProcess(Configuration configuration, IExecutionId executionId,
			final String currentCanonicalPath, final String workingFolder) throws IOException {
		// use the default factory in this case because another factory may not
		// be on the classpath yet (until AnalyzerRunnerInternal is started)
		IFactory defaultFactory = FactoryUtil.createDefaultFactory();
		ProcessExecution processExecution = defaultFactory.createNewProcessExecution(workingFolder,
				currentCanonicalPath, workingFolder);

		final String classpath = configuration.getTestAnalyzerClasspath().getValue() + FileSystemConstants.CP_SEP
				+ Environment.prefixClasspathInWorkingFolder(configuration.getFullClasspath());

		ProgramArgsWriter argsWriter = AnalyzerRunnerInternal.createProgramArgsWriter();
		argsWriter.setValue(AnalyzerRunnerInternal.ARGS_EXECUTION_ID, executionId.get());
		argsWriter.setValue(AnalyzerRunnerInternal.ARGS_PROGRAM_PATH, currentCanonicalPath);
		argsWriter.setValue(AnalyzerRunnerInternal.ARGS_CONFIG_FILE, EnvironmentConstants.FILE_INPUT_USED_CONFIG);

		processExecution.execute(executionId, ProcessExecution.NO_TIMEOUT, AnalyzerRunnerInternal.class, classpath,
				argsWriter);
	}

	private static void copyConfigurationIntoWorkingFolder(String file, Configuration configuration)
			throws IOException {
		FileSystemUtils.ensureDirectoryExists(new File(file).getParentFile());
		ConfigurationLoader.writeToFile(configuration, file);
	}

	private static void print(String value) {
		System.out.println(value);
	}

	/**
	 * Sets the execution id to
	 * {@link AnalyzerRunnerInternal#EXECUTION_ID_FOR_TESTS} in order to allow a
	 * result comparison.
	 */
	public static void setTestMode() {
		s_inTestMode = true;
	}
}
