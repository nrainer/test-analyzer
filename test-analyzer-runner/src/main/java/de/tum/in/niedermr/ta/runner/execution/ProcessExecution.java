package de.tum.in.niedermr.ta.runner.execution;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.conqat.lib.commons.io.ProcessUtils;
import org.conqat.lib.commons.io.ProcessUtils.ExecutionResult;

import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.core.common.util.StringUtility;
import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.runner.execution.args.ProgramArgsWriter;
import de.tum.in.niedermr.ta.runner.execution.environment.Environment;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ProcessExecutionFailedException;
import de.tum.in.niedermr.ta.runner.execution.exceptions.TimeoutException;
import de.tum.in.niedermr.ta.runner.factory.IFactory;
import de.tum.in.niedermr.ta.runner.factory.IRequiresFactoryCreation;

public class ProcessExecution implements IRequiresFactoryCreation {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(ProcessExecution.class);

	private static final String WRAPPED_EMPTY_PATTERN = "!EMPTY!";
	private static final boolean PRINT_SYS_ERR_TO_CONSOLE = true;
	private static final String COMMAND_JAVA = "java";
	private static final String PARAM_CLASSPATH = "-classpath";

	public static final int NO_TIMEOUT = 0;

	private final String m_directory;
	private final String m_programFolderForClasspath;
	private final String m_workingFolderForClasspath;

	/**
	 * Constructor for {@link IFactory}.
	 * 
	 * @param executionDirectory
	 *            directory to execute the process
	 * @param programFolderForClasspath
	 *            path to the folder TestAnalyzer (from the current execution
	 *            directory)
	 * @param workingFolderForClasspath
	 *            path to the working folder (from the current execution
	 *            directory)
	 */
	public ProcessExecution(String executionDirectory, String programFolderForClasspath,
			String workingFolderForClasspath) {
		this.m_directory = executionDirectory;
		this.m_programFolderForClasspath = programFolderForClasspath;
		this.m_workingFolderForClasspath = workingFolderForClasspath;
	}

	/** @see #execute(IExecutionId, int, String, String, String[]) */
	public ExecutionResult execute(IExecutionId executionId, int timeout, Class<?> mainClass, String classpath,
			ProgramArgsWriter argsWriter) throws ExecutionException, IOException {
		return execute(executionId, timeout, mainClass.getName(), classpath, argsWriter.getArgs());
	}

	/** @see #execute(IExecutionId, int, String, String, String[]) */
	public ExecutionResult execute(IExecutionId executionId, int timeout, String mainClassName, String classpath,
			ProgramArgsWriter argsWriter) throws ExecutionException, IOException {
		return execute(executionId, timeout, mainClassName, classpath, argsWriter.getArgs());
	}

	/** Start a java process and wait until it terminates. */
	public ExecutionResult execute(IExecutionId executionId, int timeout, String mainClassName, String classpath,
			String[] arguments) throws ExecutionException, IOException {
		List<String> command = new LinkedList<>();

		command.add(COMMAND_JAVA);
		command.add(PARAM_CLASSPATH);
		command.add(Environment.makeClasspathCanonical(Environment.replaceFolders(classpath,
				this.m_programFolderForClasspath, this.m_workingFolderForClasspath)));
		command.add(mainClassName);

		for (String arg : arguments) {
			command.add(CommonConstants.QUOTATION_MARK + arg + CommonConstants.QUOTATION_MARK);
		}

		ProcessBuilder processBuilder = new ProcessBuilder(command);
		processBuilder.directory(new File(m_directory));

		LOGGER.info("EXECUTING PROCESS: '" + executionId.get() + "' " + command.toString());

		ExecutionResult result = ProcessUtils.execute(processBuilder, null, timeout);

		if (PRINT_SYS_ERR_TO_CONSOLE && !StringUtility.isNullOrEmpty(result.getStderr())) {
			writeToConsole("===== BEGIN SYSERR OF EXECUTED PROCESS =====");
			writeToConsole(result.getStderr());
			writeToConsole("=====  END SYSERR OF EXECUTED PROCESS  =====");
		}

		if (!result.isNormalTermination()) {
			throw new TimeoutException(executionId, timeout);
		}

		if (result.getReturnCode() != 0) {
			throw new ProcessExecutionFailedException(executionId,
					"Execution id '" + executionId.get() + "' returned with other code than 0");
		}

		return result;
	}

	private static void writeToConsole(String output) {
		System.out.println(output);
	}

	public static String wrapPattern(String pattern) {
		if (StringUtility.isNullOrEmpty(pattern)) {
			return WRAPPED_EMPTY_PATTERN;
		}

		return pattern;
	}

	public static String[] unwrapAndSplitPattern(String pattern) {
		if (WRAPPED_EMPTY_PATTERN.equals(pattern)) {
			return new String[0];
		}

		return pattern.split(CommonConstants.SEPARATOR_DEFAULT);
	}
}
