package de.tum.in.niedermr.ta.runner.start;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.conqat.lib.commons.filesystem.FileSystemUtils;

import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;
import de.tum.in.niedermr.ta.core.common.io.TextFileUtility;
import de.tum.in.niedermr.ta.core.common.util.ClasspathUtility;
import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.runner.analysis.AnalyzerRunnerInternal;
import de.tum.in.niedermr.ta.runner.analysis.result.presentation.DatabaseResultPresentation;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.configuration.ConfigurationManager;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKey;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKeyNamespace;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationValuesManager;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.args.ProgramArgsWriter;
import de.tum.in.niedermr.ta.runner.execution.environment.Environment;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;
import de.tum.in.niedermr.ta.runner.execution.id.ExecutionIdFactory;
import de.tum.in.niedermr.ta.runner.factory.FactoryUtil;
import de.tum.in.niedermr.ta.runner.factory.IFactory;

/**
 * <b>Executes AnalyzerRunnerInternal</b> in a new process with the needed classpath.<br/>
 * The process will be started in the working area which is specified in the configuration.<br/>
 */
public class AnalyzerRunnerStart {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(AnalyzerRunnerStart.class);

	/** <code>advanced.executionId.value</code>: Force the use of a certain executionId. */
	private static final DynamicConfigurationKey CONFIGURATION_KEY_USE_SPECIFIED_EXECUTION_ID = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.ADVANCED, "executionId.value", null);

	/**
	 * <code>advanced.executionId.reusePreviousId</code>: Use the id of the last execution if available (only working
	 * with the database result presentation).
	 */
	private static final DynamicConfigurationKey CONFIGURATION_KEY_REUSE_PREVIOUS_EXECUTION_ID = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.ADVANCED, "executionId.reusePreviousId", false);

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
	public static void main(String[] args) throws ConfigurationException, IOException, ReflectiveOperationException {
		Configuration configuration;

		try {
			configuration = ConfigurationManager.loadConfiguration();
		} catch (FileNotFoundException e) {
			LOGGER.error("Configuration file not found.");
			return;
		}

		execute(configuration);
	}

	public static void execute(Configuration configuration) throws IOException, ReflectiveOperationException {
		execute(configuration, new File(FileSystemConstants.CURRENT_FOLDER));
	}

	public static void execute(Configuration configuration, File locationTestAnalyzer)
			throws IOException, ReflectiveOperationException {
		IExecutionId executionId = createExecutionId(configuration);

		final String currentCanonicalPath = locationTestAnalyzer.getCanonicalPath();
		final String workingFolder = configuration.getWorkingFolder().getValue();

		LOGGER.info("Working folder is: " + workingFolder);

		if (configuration.getTestAnalyzerClasspath().isEmpty()) {
			configuration.getTestAnalyzerClasspath().setValue(ClasspathUtility.getCurrentProgramClasspath());
		} else {
			LOGGER.info("Using the test analyzer classpath from the configuration!");
		}

		copyConfigurationIntoWorkingFolder(
				Environment.replaceWorkingFolder(EnvironmentConstants.FILE_INPUT_USED_CONFIG, workingFolder),
				configuration);

		try {
			startExecutionInNewProcess(configuration, executionId, currentCanonicalPath, workingFolder);
			LOGGER.info("DONE.");
		} catch (ExecutionException ex) {
			LOGGER.error("Execution failed: " + ex.getMessage());
		}
	}

	/** Create an execution id. The id is either random, or specified in the configuration, or the test id. */
	private static IExecutionId createExecutionId(Configuration configuration) {
		DynamicConfigurationValuesManager dynamicConfigurationValues = configuration.getDynamicValues();
		if (dynamicConfigurationValues.isSet(CONFIGURATION_KEY_USE_SPECIFIED_EXECUTION_ID)) {
			String executionId = dynamicConfigurationValues
					.getStringValue(CONFIGURATION_KEY_USE_SPECIFIED_EXECUTION_ID);
			dynamicConfigurationValues.removeEntry(CONFIGURATION_KEY_USE_SPECIFIED_EXECUTION_ID);
			return ExecutionIdFactory.parseShortExecutionId(executionId.trim());
		}

		if (dynamicConfigurationValues.getBooleanValue(CONFIGURATION_KEY_REUSE_PREVIOUS_EXECUTION_ID)) {
			Optional<IExecutionId> previousExecutionId = tryRetrievePreviousExecutionId(configuration);

			if (previousExecutionId.isPresent()) {
				LOGGER.info("Reusing previous execution id: " + previousExecutionId.get());
				return previousExecutionId.get();
			} else {
				LOGGER.info("Retrieving last execution id failed. Using a new id.");
			}
		}

		return ExecutionIdFactory.createNewShortExecutionId();
	}

	/** Try to retrieve the previous execution id from the last execution information result file. */
	protected static Optional<IExecutionId> tryRetrievePreviousExecutionId(Configuration configuration) {
		String pathToLastExecutionInformationFile = Environment.replaceWorkingFolder(
				EnvironmentConstants.FILE_OUTPUT_EXECUTION_INFORMATION, configuration.getWorkingFolder().getValue());

		if (!Files.exists(Paths.get(pathToLastExecutionInformationFile))) {
			LOGGER.warn("Previous execution id cannot be retrieved because the file does not exist.");
			return Optional.empty();
		}

		try {
			return DatabaseResultPresentation.tryParseExecutionIdFromExecutionInformation(
					TextFileUtility.readFromFile(pathToLastExecutionInformationFile));
		} catch (IOException e) {
			LOGGER.warn("Previous execution id cannot be retrieved.", e);
			return Optional.empty();
		}
	}

	/** Start the execution in a new process. */
	private static void startExecutionInNewProcess(Configuration configuration, IExecutionId executionId,
			final String currentCanonicalPath, final String workingFolder)
			throws IOException, ReflectiveOperationException {
		IFactory defaultFactory = FactoryUtil.tryCreateFactoryOrUseDefault(configuration);
		ProcessExecution processExecution = defaultFactory.createNewProcessExecution(configuration, workingFolder,
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
		ConfigurationManager.writeToFile(configuration, file);
	}
}
