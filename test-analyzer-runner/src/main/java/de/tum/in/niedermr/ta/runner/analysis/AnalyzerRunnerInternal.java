package de.tum.in.niedermr.ta.runner.analysis;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.analysis.result.presentation.IResultPresentation;
import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;
import de.tum.in.niedermr.ta.core.common.io.TextFileData;
import de.tum.in.niedermr.ta.core.common.util.ClasspathUtility;
import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.runner.analysis.workflow.IWorkflow;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.configuration.ConfigurationLoader;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.execution.args.ProgramArgsKey;
import de.tum.in.niedermr.ta.runner.execution.args.ProgramArgsReader;
import de.tum.in.niedermr.ta.runner.execution.args.ProgramArgsWriter;
import de.tum.in.niedermr.ta.runner.execution.environment.Environment;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;
import de.tum.in.niedermr.ta.runner.execution.id.ExecutionIdFactory;
import de.tum.in.niedermr.ta.runner.logging.LoggingUtil;
import de.tum.in.niedermr.ta.runner.start.AnalyzerRunnerStart;

/**
 * <b>Starts the whole workflow</b> which consists of the core steps INSTRU (instrumentation), INFCOL (information
 * collection), METKIL (mutation) and TSTRUN (test run).<br/>
 * This project <b>contains</b> the steps <b>INSTRU and METKIL</b> and invokes INFCOL and TSTRUN in own processes.<br/>
 * <br/>
 * Contains the jar files of: the testing projects<br/>
 * Dependencies: ASM, log4j, ccsm-commons, core.<br/>
 * Further dependencies: jars to be processed and dependencies
 * 
 */
public class AnalyzerRunnerInternal {
	private static final Logger LOG = LogManager.getLogger(AnalyzerRunnerInternal.class);

	/** Number of args. */
	private static final int ARGS_COUNT = 3;
	public static final ProgramArgsKey ARGS_EXECUTION_ID = new ProgramArgsKey(AnalyzerRunnerInternal.class, 0);
	public static final ProgramArgsKey ARGS_PROGRAM_PATH = new ProgramArgsKey(AnalyzerRunnerInternal.class, 1);
	public static final ProgramArgsKey ARGS_CONFIG_FILE = new ProgramArgsKey(AnalyzerRunnerInternal.class, 2);

	private static final String RELATIVE_WORKING_FOLDER = FileSystemConstants.CURRENT_FOLDER;

	/**
	 * args[0]: arbitrary (an ID will be generated) or {@link EXECUTION_ID_FOR_TESTS} args[1]: absolute path to the
	 * TestAnalyzer project (which is referenced by program libraries) args[2]: relative path to the configuration file
	 * in the working area
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			LoggingUtil.printDontStartThisClass(AnalyzerRunnerInternal.class, AnalyzerRunnerStart.class);
			return;
		}

		ProgramArgsReader argsReader = new ProgramArgsReader(AnalyzerRunnerInternal.class, args);

		final IExecutionId executionId = ExecutionIdFactory
				.parseShortExecutionId(argsReader.getArgument(ARGS_EXECUTION_ID));
		final String programPath = argsReader.getArgument(ARGS_PROGRAM_PATH);
		final String configurationFileToUse = Environment.replaceWorkingFolder(argsReader.getArgument(ARGS_CONFIG_FILE),
				RELATIVE_WORKING_FOLDER);

		try {
			LOG.info("TEST ANALYZER START");
			LOG.info("Classpath: " + ClasspathUtility.getCurrentClasspath());

			Configuration configuration = loadAndValidateTheConfiguration(configurationFileToUse);

			LOG.info("Configuration is valid.");
			LOG.info("Configuration is:" + CommonConstants.NEW_LINE + configuration.toMultiLineString());

			IWorkflow[] testWorkflows = createTestWorkflows(executionId, configuration);

			for (IWorkflow workFlow : testWorkflows) {
				executeWorkflow(executionId, programPath, configuration, workFlow);
			}

			// must be executed after the workflow because it requires the existence of the results folder
			writeExecutionInformationFile(executionId, configuration);

			LOG.info("TEST ANALYZER END");
		} catch (Throwable t) {
			t.printStackTrace();
			LOG.fatal("Execution failed", t);
			throw new ExecutionException(executionId, AnalyzerRunnerInternal.class.getName() + " was not successful.");
		}
	}

	/** Write a file with execution information. */
	private static void writeExecutionInformationFile(IExecutionId executionId, Configuration configuration)
			throws ReflectiveOperationException, IOException {
		IResultPresentation resultPresentation = configuration.getResultPresentation().createInstance(executionId);

		String fileName = Environment.replaceWorkingFolder(EnvironmentConstants.FILE_OUTPUT_EXECUTION_INFORMATION,
				RELATIVE_WORKING_FOLDER);
		List<String> configurationLines = ConfigurationLoader.toFileLines(configuration, false);
		List<String> formattedContent = Arrays
				.asList(resultPresentation.formatExecutionInformation(configurationLines));
		TextFileData.writeToFile(fileName, formattedContent);
	}

	/** Execute the given workflow. */
	private static void executeWorkflow(IExecutionId executionId, String programPath, Configuration configuration,
			IWorkflow workFlow) {
		LOG.info("WORKFLOW " + workFlow.getName() + " START (" + new Date() + ")");
		long startTime = System.currentTimeMillis();

		IWorkflow workflow = initializeTestWorkflow(executionId, workFlow, configuration, programPath);
		workflow.start();

		LOG.info("Workflow execution id was: '" + executionId.get() + "'");
		LOG.info("Workflow duration was: " + getDuration(startTime));
		LOG.info("WORKFLOW " + workFlow.getName() + " END (" + new Date() + ")");
	}

	public static ProgramArgsWriter createProgramArgsWriter() {
		return new ProgramArgsWriter(AnalyzerRunnerInternal.class, ARGS_COUNT);
	}

	private static Configuration loadAndValidateTheConfiguration(String configurationFileToUse)
			throws ConfigurationException, IOException {
		Configuration configuration = ConfigurationLoader.getConfigurationFromFile(configurationFileToUse);

		final String classpathBefore = configuration.getClasspath().getValue();

		configuration.validateAndAdjust();

		if (!configuration.getClasspath().getValue().equals(classpathBefore)) {
			LOG.warn("Fixed the classpath of the configuration: removed elements of "
					+ configuration.getCodePathToMutate().getName() + " from " + configuration.getClasspath().getName()
					+ "!");
		}

		return configuration;
	}

	private static IWorkflow[] createTestWorkflows(IExecutionId executionId, Configuration configuration) {
		try {
			return configuration.getTestWorkflows().createInstances();
		} catch (Throwable t) {
			throw new ExecutionException(executionId, "Error when creating the test workflows");
		}
	}

	private static IWorkflow initializeTestWorkflow(IExecutionId executionId, IWorkflow workflow,
			Configuration configuration, String programPath) {
		try {
			workflow.init(executionId, configuration, programPath, RELATIVE_WORKING_FOLDER);

			return workflow;
		} catch (Throwable t) {
			throw new ExecutionException(executionId, "Error when initializing the test workflow");
		}
	}

	private static String getDuration(final long startTime) {
		final long endTime = System.currentTimeMillis();
		final long duration = endTime - startTime;

		return (duration / 1000) + " seconds";
	}
}
