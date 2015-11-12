package de.tum.in.niedermr.ta.runner.execution;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.conqat.lib.commons.io.ProcessUtils;
import org.conqat.lib.commons.io.ProcessUtils.ExecutionResult;

import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.core.common.util.StringUtility;
import de.tum.in.niedermr.ta.runner.analysis.AnalyzerRunnerInternal;
import de.tum.in.niedermr.ta.runner.execution.environment.Environment;
import de.tum.in.niedermr.ta.runner.execution.exceptions.FailedExecution;
import de.tum.in.niedermr.ta.runner.execution.exceptions.TimeoutException;

public class ProcessExecution {
	private static final Logger LOG = AnalyzerRunnerInternal.LOG;

	public static boolean s_showSysErrors = true;
	private static final String COMMAND_JAVA = "java";
	private static final String PARAM_CLASSPATH = "-classpath";

	public static final int NO_TIMEOUT = 0;

	private final String m_directory;
	private final String m_programFolderForClasspath;
	private final String m_workingFolderForClasspath;

	/**
	 * Constructor.
	 * 
	 * @param executionDirectory
	 *            directory to execute the process
	 * @param programFolderForClasspath
	 *            path to the folder MagicStart (from the current execution directory)
	 * @param workingFolderForClasspath
	 *            path to the working folder (from the current execution directory)
	 */
	public ProcessExecution(String executionDirectory, String programFolderForClasspath, String workingFolderForClasspath) {
		this.m_directory = executionDirectory;
		this.m_programFolderForClasspath = programFolderForClasspath;
		this.m_workingFolderForClasspath = workingFolderForClasspath;
	}

	public String executeGetSysout(String executionId, int timeout, String mainClass, String classpath, List<String> arguments) throws FailedExecution,
			IOException {
		ExecutionResult result = execute(executionId, timeout, mainClass, classpath, arguments);

		return result.getStdout();
	}

	public String executeAndGetSyserr(String executionId, int timeout, String mainClass, String classpath, List<String> arguments) throws FailedExecution,
			IOException {
		ExecutionResult result = execute(executionId, timeout, mainClass, classpath, arguments);

		return result.getStderr();
	}

	public ExecutionResult execute(String executionId, int timeout, String mainClass, String classpath, List<String> arguments) throws FailedExecution,
			IOException {
		List<String> command = new LinkedList<>();

		command.add(COMMAND_JAVA);
		command.add(PARAM_CLASSPATH);
		command.add(Environment.makeClasspathCanonical(Environment.replaceFolders(classpath, this.m_programFolderForClasspath, this.m_workingFolderForClasspath)));
		command.add(mainClass);
		command.add(executionId);

		for (String arg : arguments) {
			command.add(CommonConstants.QUOTATION_MARK + arg + CommonConstants.QUOTATION_MARK);
		}

		ProcessBuilder processBuilder = new ProcessBuilder(command);
		processBuilder.directory(new File(m_directory));

		LOG.info("EXECUTING PROCESS: '" + executionId + "' " + command.toString());

		ExecutionResult result = ProcessUtils.execute(processBuilder, null, timeout);

		if (s_showSysErrors && !StringUtility.isNullOrEmpty(result.getStderr())) {
			System.err.println("===== BEGIN SYSERR OF EXECUTED PROCESS =====");
			System.err.println(result.getStderr());
			System.err.println("=====  END SYSERR OF EXECUTED PROCESS  =====");
		}

		if (!result.isNormalTermination()) {
			throw new TimeoutException(executionId, timeout);
		}

		if (result.getReturnCode() != 0) {
			throw new FailedExecution(executionId, "Execution id '" + executionId + "' returned with other code than 0");
		}

		return result;
	}
}
