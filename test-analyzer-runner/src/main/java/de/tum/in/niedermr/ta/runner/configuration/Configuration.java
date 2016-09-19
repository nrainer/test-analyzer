package de.tum.in.niedermr.ta.runner.configuration;

import java.util.ArrayList;
import java.util.List;

import de.tum.in.niedermr.ta.core.analysis.filter.IMethodFilter;
import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunner;
import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;
import de.tum.in.niedermr.ta.core.common.util.FileUtility;
import de.tum.in.niedermr.ta.runner.analysis.workflow.IWorkflow;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.configuration.property.ClasspathProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.CodePathToMutateProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.CodePathToTestProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.ExecuteCollectInformationProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.ExecuteMutateAndTestProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.FactoryClassProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.MethodFiltersProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.NumberOfThreadsProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.OperateFaultTolerantProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.RemoveTempDataProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.ResultPresentationProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.ReturnValueGeneratorsProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.TestAnalyzerClasspathProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.TestClassExcludesProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.TestClassIncludesProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.TestRunnerProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.TestWorkflowsProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.TestingTimeoutAbsoluteMaxProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.TestingTimeoutConstantProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.TestingTimeoutPerTestcaseProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.WorkingFolderProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractClasspathProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.IConfigurationProperty;
import de.tum.in.niedermr.ta.runner.factory.IFactory;

/**
 * Configuration
 */
public class Configuration extends AbstractConfiguration implements FileSystemConstants {
	private static final int CURRENT_VERSION = 3;

	private final TestAnalyzerClasspathProperty m_testAnalyzerClasspath;
	private final WorkingFolderProperty m_workingFolder;
	private final CodePathToMutateProperty m_codePathToMutate;
	private final CodePathToTestProperty m_codePathToTest;
	private final ClasspathProperty m_classpath;
	private final TestWorkflowsProperty m_testWorkflows;
	private final FactoryClassProperty m_factoryClass;
	private final TestRunnerProperty m_testRunner;
	private final MethodFiltersProperty m_methodFilters;
	private final ReturnValueGeneratorsProperty m_returnValueGenerators;
	private final ResultPresentationProperty m_resultPresentation;
	private final TestClassIncludesProperty m_testClassIncludes;
	private final TestClassExcludesProperty m_testClassExcludes;
	private final TestingTimeoutAbsoluteMaxProperty m_testingTimeoutAbsoluteMax;
	private final TestingTimeoutConstantProperty m_testingTimeoutConstant;
	private final TestingTimeoutPerTestcaseProperty m_testingTimeoutPerTestcase;
	private final OperateFaultTolerantProperty m_operateFaultTolerant;
	private final NumberOfThreadsProperty m_numberOfThreads;
	private final ExecuteCollectInformationProperty m_executeCollectInformation;
	private final ExecuteMutateAndTestProperty m_executeMutateAndTest;
	private final RemoveTempDataProperty m_removeTempData;

	public Configuration() {
		super(CURRENT_VERSION);
		m_testAnalyzerClasspath = new TestAnalyzerClasspathProperty();
		m_workingFolder = new WorkingFolderProperty();
		m_codePathToMutate = new CodePathToMutateProperty();
		m_codePathToTest = new CodePathToTestProperty();
		m_classpath = new ClasspathProperty();
		m_testWorkflows = new TestWorkflowsProperty();
		m_factoryClass = new FactoryClassProperty();
		m_testRunner = new TestRunnerProperty();
		m_methodFilters = new MethodFiltersProperty();
		m_returnValueGenerators = new ReturnValueGeneratorsProperty();
		m_resultPresentation = new ResultPresentationProperty();
		m_testClassIncludes = new TestClassIncludesProperty();
		m_testClassExcludes = new TestClassExcludesProperty();
		m_testingTimeoutAbsoluteMax = new TestingTimeoutAbsoluteMaxProperty();
		m_testingTimeoutConstant = new TestingTimeoutConstantProperty();
		m_testingTimeoutPerTestcase = new TestingTimeoutPerTestcaseProperty();
		m_operateFaultTolerant = new OperateFaultTolerantProperty();
		m_numberOfThreads = new NumberOfThreadsProperty();
		m_executeCollectInformation = new ExecuteCollectInformationProperty();
		m_executeMutateAndTest = new ExecuteMutateAndTestProperty();
		m_removeTempData = new RemoveTempDataProperty();
	}

	@Override
	public final List<IConfigurationProperty<?>> getAllPropertiesOrdered() {
		List<IConfigurationProperty<?>> properties = new ArrayList<>();

		properties.add(super.getConfigurationVersion());
		properties.add(m_testAnalyzerClasspath);
		properties.add(m_workingFolder);
		properties.add(m_codePathToMutate);
		properties.add(m_codePathToTest);
		properties.add(m_classpath);
		properties.add(m_testWorkflows);
		properties.add(m_factoryClass);
		properties.add(m_testRunner);
		properties.add(m_methodFilters);
		properties.add(m_returnValueGenerators);
		properties.add(m_resultPresentation);
		properties.add(m_testClassIncludes);
		properties.add(m_testClassExcludes);
		properties.add(m_testingTimeoutConstant);
		properties.add(m_testingTimeoutPerTestcase);
		properties.add(m_testingTimeoutAbsoluteMax);
		properties.add(m_operateFaultTolerant);
		properties.add(m_numberOfThreads);
		properties.add(m_executeCollectInformation);
		properties.add(m_executeMutateAndTest);
		properties.add(m_removeTempData);

		return properties;
	}

	/**
	 * Allows for overriding the classpath of the TestAnalyzer. <b>Only for internal use.</b>
	 */
	public TestAnalyzerClasspathProperty getTestAnalyzerClasspath() {
		return m_testAnalyzerClasspath;
	}

	/**
	 * Folder to store the result and the temporary data.<br/>
	 * Relative to TestAnalyzer!<br/>
	 * <br/>
	 * The default value can be used.
	 */
	public WorkingFolderProperty getWorkingFolder() {
		return m_workingFolder;
	}

	/**
	 * Path to the jar files which contain methods supposed to be mutated. The jar files must have been compiled with
	 * Java 1.5+.<br/>
	 * The files won't be modified. A jar file can be both in 'jarsWithMethodsToMutate' and 'jarsWithTestsToRun'.<br/>
	 * The files should not be added to the classpath property!<br/>
	 * Note that <b>jar files used by this project must not be tested.</b> Among these are:
	 * <ul>
	 * <li>ASM</li>
	 * <li>ccsm-commons</li>
	 * <li>commons-io</li>
	 * <li>hamcrest</li>
	 * <li>JUnit</li>
	 * <li>log4j</li>
	 * <li>(jar files with code of this project)</li>
	 * </ul>
	 * Relative to the working folder.<br/>
	 * <br/>
	 * Required.
	 */
	public CodePathToMutateProperty getCodePathToMutate() {
		return m_codePathToMutate;
	}

	/**
	 * Path to the jar files which contain tests supposed to be run. The files won't be modified. A jar file can be both
	 * in 'jarsWithMethodsToMutate' and 'jarsWithTestsToRun'.<br/>
	 * It is not necessary to add the files to the classpath property.<br/>
	 * Relative to the working folder.<br/>
	 * 
	 * Required.
	 */
	public CodePathToTestProperty getCodePathToTest() {
		return m_codePathToTest;
	}

	/**
	 * Class path needed for the classes in the jars of 'jarsWithMethodsToMutate' and 'jarsWithTestsToRun'.<br/>
	 * The jars in 'jarsWithMethodsToMutate' and 'jarsWithTestsToRun' should not to be specified. Furthermore, it is not
	 * necessary to specify JUnit (4.8) or hamcrest (1.1.0).<br/>
	 * It is ensured that it ends with the classpath separator (if not empty). <br/>
	 * The {@link #CLASSPATH_SEPARATOR_WINDOWS} can be used both for windows and linux, the
	 * {@link #CLASSPATH_SEPARATOR_LINUX} works only under linux. <br/>
	 * <br/>
	 * Relative to the working folder.<br/>
	 * <br/>
	 * Can be empty.
	 * 
	 * @see AbstractClasspathProperty#parseValue(String)
	 */
	public ClasspathProperty getClasspath() {
		return m_classpath;
	}

	/**
	 * Qualified class name of the test workflow to be used.<br/>
	 * The class must implement {@link IWorkflow} and be on the classpath.<br/>
	 * <br/>
	 * The default value can be used.
	 */
	public TestWorkflowsProperty getTestWorkflows() {
		return m_testWorkflows;
	}

	/**
	 * Qualified class name of the factory class to be used.<br/>
	 * The class must implement {@link IFactory} and be on the classpath.<br/>
	 * <br/>
	 * The default value can be used.
	 */
	public FactoryClassProperty getFactoryClass() {
		return m_factoryClass;
	}

	/**
	 * Qualified class names of the method filters to be used.<br/>
	 * The class must implement {@link IMethodFilter} and be on the classpath. <br/>
	 * <br/>
	 * The default value can be used. Values separated by:
	 * {@link de.tum.in.niedermr.ta.core.common.constants.CommonConstants#SEPARATOR_DEFAULT}
	 */
	public MethodFiltersProperty getMethodFilters() {
		return m_methodFilters;
	}

	/**
	 * Qualified class name of the test runner to be used.<br/>
	 * The class must implement {@link ITestRunner} and be on the classpath. <br/>
	 * <br/>
	 * The default value can be used.
	 */
	public TestRunnerProperty getTestRunner() {
		return m_testRunner;
	}

	/**
	 * Class name of the return value generator(s).<br/>
	 * <br/>
	 * Core provides the following built-in generators:</br>
	 * <ul>
	 * <li>{@link de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.VoidReturnValueGenerator}</li>
	 * <li>{@link de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.SimpleReturnValueGeneratorWith0}</li>
	 * <li>{@link de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.SimpleReturnValueGeneratorWith1}</li>
	 * <li>
	 * {@link de.tum.in.niedermr.ta.extensions.testing.factories.ma.logic.mutation.returnValues.CommonInstancesReturnValueGenerator}
	 * </li>
	 * <li>{@link de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.SimpleInstancesReturnValueGenerator}</li>
	 * </ul>
	 * <br/>
	 * Other generators can be used too (they must implement
	 * {@link de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.IReturnValueGenerator} and be on the classpath).
	 * For generators which use factories it is recommended to extend
	 * {@link de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.AbstractFactoryReturnValueGenerator} . <br/>
	 * 
	 * The default value can be used. <br/>
	 * Values separated by: {@link de.tum.in.niedermr.ta.core.common.constants.CommonConstants#SEPARATOR_DEFAULT}
	 * 
	 * @see de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.AbstractFactoryReturnValueGenerator
	 */
	public ReturnValueGeneratorsProperty getReturnValueGenerators() {
		return m_returnValueGenerators;
	}

	/**
	 * Way of the result presentation.<br/>
	 * Either {@link ResultPresentationProperty#RESULT_PRESENTATION_DB} ,
	 * {@link ResultPresentationProperty#RESULT_PRESENTATION_TEXT} or the name of a class implementing
	 * {@link de.tum.in.niedermr.ta.core.analysis.result.presentation.IResultPresentation} (which then needs to be on
	 * the classpath).<br/>
	 * <br/>
	 * The default value can be used.
	 */
	public ResultPresentationProperty getResultPresentation() {
		return m_resultPresentation;
	}

	/**
	 * Patterns (regular expressions!) to select test classes by their (qualified) name. <br/>
	 * All test classes will be used if empty. </br>
	 * <br/>
	 * The default value can be used. <br/>
	 * Values separated by: {@value CommonConstants#SEPARATOR_DEFAULT}
	 */
	public TestClassIncludesProperty getTestClassIncludes() {
		return m_testClassIncludes;
	}

	/**
	 * Patterns (regular expressions) for to skip test classes by their (qualified) name.<br/>
	 * Excludes have a higher priority than includes. <br/>
	 * (Note: Test classes which contain System.exit(0); must be skipped.)<br/>
	 * <br/>
	 * The default value can be used. <br/>
	 * Values separated by: {@value CommonConstants#SEPARATOR_DEFAULT}
	 */
	public TestClassExcludesProperty getTestClassExcludes() {
		return m_testClassExcludes;
	}

	/**
	 * Duration (in seconds) of the constant part during the step run tests (RUNTST).<br/>
	 * The timeout for running all tests for a given method under test is calculated as follows:<br/>
	 * <code>min({@link #testingTimeoutAbsoluteMax}, {@link #testingTimeoutConstant} + {@link #testingTimeoutPerTestcase} * number of testcases)</code>
	 * <br/>
	 * <br/>
	 * The default value can be used.
	 * 
	 * @see #computeTestingTimeout(int)
	 */
	public TestingTimeoutConstantProperty getTestingTimeoutConstant() {
		return m_testingTimeoutConstant;
	}

	/**
	 * Duration (in seconds) of the variable part during the step run tests (RUNTST).<br/>
	 * <br/>
	 * The default value can be used.
	 * 
	 * @see #m_testingTimeoutConstant
	 * @see #computeTestingTimeout(int)
	 */
	public TestingTimeoutPerTestcaseProperty getTestingTimeoutPerTestcase() {
		return m_testingTimeoutPerTestcase;
	}

	/**
	 * Absolute maximum duration (in seconds) for running all tests concerning one method under test.<br/>
	 * Can be disabled by setting the value to "-1".<br/>
	 * <br/>
	 * The default value can be used.
	 * 
	 * @see #m_testingTimeoutConstant
	 * @see #computeTestingTimeout(int)
	 */
	public TestingTimeoutAbsoluteMaxProperty getTestingTimeoutAbsoluteMax() {
		return m_testingTimeoutAbsoluteMax;
	}

	/**
	 * Operate in fault tolerant mode. Permits at present:
	 * <ul>
	 * <li>classes not to be on the classpath (at INSTRU)</li>
	 * </ul>
	 * <br/>
	 * It should not be used in the normal case. Use it with care and check the results! <br/>
	 * The default value can be used.
	 */
	public OperateFaultTolerantProperty getOperateFaultTolerant() {
		return m_operateFaultTolerant;
	}

	/**
	 * Number of threads to use for the steps method mutation (METKIL) and test running (TSTRUN).<br/>
	 * <br/>
	 * The default value can be used.
	 */
	public NumberOfThreadsProperty getNumberOfThreads() {
		return m_numberOfThreads;
	}

	/**
	 * If false, the steps of instrumentation (INSTRU) and information collection (INFCOL) will be skipped.<br/>
	 * In this case the file
	 * {@link de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants#FILE_OUTPUT_COLLECTED_INFORMATION}
	 * must exist in the working folder!<br/>
	 * <br/>
	 * The default value can be used.
	 */
	public ExecuteCollectInformationProperty getExecuteCollectInformation() {
		return m_executeCollectInformation;
	}

	/**
	 * If false, the steps of mutating methods (METKIL) and running tests (TSTRUN) will be skipped.<br/>
	 * <br/>
	 * The default value can be used.
	 */
	public ExecuteMutateAndTestProperty getExecuteMutateAndTest() {
		return m_executeMutateAndTest;
	}

	/**
	 * If false, the temporary data won't be removed.<br/>
	 * <br/>
	 * The default value can be used.
	 */
	public RemoveTempDataProperty getRemoveTempData() {
		return m_removeTempData;
	}

	public final boolean isMultiThreaded() {
		return getNumberOfThreads().getValue() > 1;
	}

	public final int computeTestingTimeout(int numberOfTestcases) {
		int actualValue = m_testingTimeoutConstant.getValue()
				+ (numberOfTestcases * m_testingTimeoutPerTestcase.getValue());

		if (m_testingTimeoutAbsoluteMax.isActive() && actualValue > m_testingTimeoutAbsoluteMax.getValue()) {
			return m_testingTimeoutAbsoluteMax.getValue();
		} else {
			return actualValue;
		}
	}

	/**
	 * Get the full classpath consisting of the properties {@link #jarsWithMethodsToMutate}, {@link #jarsWithTestsToRun}
	 * and {@link #classpath}.
	 */
	public final String getFullClasspath() {
		return m_codePathToMutate.getValue() + CP_SEP + m_codePathToTest.getValue() + CP_SEP + m_classpath.getValue()
				+ CP_SEP;
	}

	public final String getPrefixedWorkingFolderIfNotAbsolute(String prefix) {
		return FileUtility.prefixFileNameIfNotAbsolute(getWorkingFolder().getValue(), prefix);
	}

	public final void setJarsWithMethodsToMutateAndJarsWithTestsToRun(String value) {
		m_codePathToMutate.setValue(value);
		m_codePathToTest.setValue(value);
	}

	public final void validateAndAdjust() throws ConfigurationException {
		validate();
		adjust();
	}

	public final void adjust() {
		ensureJarsToMutateNotOnClasspath();
	}

	private void ensureJarsToMutateNotOnClasspath() {
		String classpathTemp = m_classpath.getValue();
		boolean changed = false;

		for (String jarToMutate : m_codePathToMutate.getElements()) {
			final String jarToMutatePath = jarToMutate + CP_SEP;

			if (classpathTemp.contains(jarToMutatePath)) {
				classpathTemp = classpathTemp.replace(jarToMutatePath, "");
				changed = true;
			}
		}

		if (changed) {
			m_classpath.setValue(classpathTemp);
		}
	}
}