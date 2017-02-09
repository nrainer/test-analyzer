package de.tum.in.niedermr.ta.runner.execution;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;

/** Builder for executing a Java process. */
public class JavaProcessCommandBuilder {

	/** {@value} */
	private static final String COMMAND_JAVA = "java";
	/** {@value} */
	private static final String PARAM_CLASSPATH = "-classpath";

	private String m_mainClassName;
	private String m_classPath;
	private final List<String> m_javaArguments = new ArrayList<>();
	private final List<String> m_programArguments = new ArrayList<>();

	/** {@link #m_mainClassName} */
	public void setMainClassName(String mainClassName) {
		m_mainClassName = mainClassName;
	}

	/** {@link #m_classPath} */
	public void setClassPath(String classPath) {
		m_classPath = classPath;
	}

	/** {@link #m_programArguments} */
	public void addProgramArgument(String argument) {
		m_programArguments.add(argument);
	}

	/** {@link #m_programArguments} */
	public void addProgramArguments(List<String> arguments) {
		m_programArguments.addAll(arguments);
	}

	/** {@link #m_javaArguments} */
	public void addJavaArgument(String argument) {
		m_javaArguments.add(argument);
	}

	/** {@link #m_javaArguments} */
	public void addJavaArguments(List<String> javaArguments) {
		m_javaArguments.addAll(javaArguments);
	}

	/** Complete the command. */
	public List<String> complete() {
		if (m_mainClassName == null || m_classPath == null) {
			throw new IllegalStateException("Main class name or classpath not set");
		}

		List<String> command = new LinkedList<>();

		command.add(COMMAND_JAVA);

		for (String argument : m_javaArguments) {
			command.add(argument);
		}

		command.add(PARAM_CLASSPATH);
		command.add(m_classPath);
		command.add(m_mainClassName);

		for (String argument : m_programArguments) {
			command.add(CommonConstants.QUOTATION_MARK + argument + CommonConstants.QUOTATION_MARK);
		}

		return command;
	}
}
