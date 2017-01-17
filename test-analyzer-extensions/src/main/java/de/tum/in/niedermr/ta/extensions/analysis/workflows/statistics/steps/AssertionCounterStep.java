package de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.analysis.jars.iteration.IteratorFactory;
import de.tum.in.niedermr.ta.core.analysis.jars.iteration.JarAnalyzeIterator;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.core.code.iteration.IteratorException;
import de.tum.in.niedermr.ta.core.code.tests.collector.ITestCollector;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.operation.AssertionCounterOperation;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.tests.AssertionInformation;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;
import de.tum.in.niedermr.ta.runner.tests.TestRunnerUtil;

public class AssertionCounterStep extends AbstractExecutionStep {
	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(AssertionCounterStep.class);

	private static final String PREFIX_UNREGISTERED_ASSERT_METHODS_1 = "assert";
	private static final String PREFIX_UNREGISTERED_ASSERT_METHODS_2 = "check";

	private static final String[] KNOWN_FURTHER_ASSERTION_CLASS_NAMES = new String[] {
			"org.conqat.lib.commons.assertion.CCSMAssert", "org.conqat.lib.commons.assertion.CCSMPre",
			"org.testng.asserts.Assertion", "org.apache.commons.math3.TestUtils", "ru.histone.utils.Assert",
			"org.matheclipse.core.system.AbstractTestCase" };

	private final Map<TestcaseIdentifier, Integer> m_assertionsPerTestcase;
	private final Map<Class<?>, Set<String>> m_allTestcases;
	private final AssertionInformation m_assertionInformation;

	public AssertionCounterStep() {
		this.m_assertionsPerTestcase = new HashMap<>();
		this.m_allTestcases = new HashMap<>();
		this.m_assertionInformation = getAssertionInformation();
	}

	/** {@inheritDoc} */
	@Override
	protected String getSuffixForFullExecutionId() {
		return "ASSCOUNT";
	}

	private AssertionInformation getAssertionInformation() {
		Class<?>[] assertionClasses = getAvailableAssertionClasses().toArray(new Class[0]);
		return new AssertionInformation(assertionClasses) {
			@Override
			public AssertionResult isAssertionMethod(MethodIdentifier methodIdentifier) throws ClassNotFoundException {
				if (methodIdentifier.getOnlyMethodName().startsWith(PREFIX_UNREGISTERED_ASSERT_METHODS_1)
						|| methodIdentifier.getOnlyMethodName().startsWith(PREFIX_UNREGISTERED_ASSERT_METHODS_2)) {
					return new AssertionResult(true, methodIdentifier);
				} else {
					return super.isAssertionMethod(methodIdentifier);
				}
			}
		};
	}

	private List<Class<?>> getAvailableAssertionClasses() {
		List<Class<?>> result = new LinkedList<>();

		for (String className : KNOWN_FURTHER_ASSERTION_CLASS_NAMES) {
			try {
				Class<?> cls = Class.forName(className);

				result.add(cls);

				LOGGER.info("Further assertion class " + className + " is available and will be used.");
			} catch (ClassNotFoundException ex) {
				continue;
			}
		}

		return result;
	}

	/** {@inheritDoc} */
	@Override
	protected void runInternal(Configuration configuration, ProcessExecution processExecution)
			throws ExecutionException, ReflectiveOperationException {
		ITestCollector testCollector = TestRunnerUtil.getAppropriateTestCollector(configuration, true);
		boolean operateFaultTolerant = configuration.getOperateFaultTolerant().getValue();

		try {
			countAssertionsInTestcases(configuration, testCollector, operateFaultTolerant);
		} catch (IteratorException e) {
			throw new ExecutionException(getExecutionId(), e);
		}

		TestcaseInheritanceHelper.postProcessAllTestcases(m_allTestcases, m_assertionsPerTestcase);
	}

	private void countAssertionsInTestcases(Configuration configuration, ITestCollector testCollector,
			boolean operateFaultTolerant) throws IteratorException {
		for (String testJar : configuration.getCodePathToTest().getElements()) {
			m_assertionsPerTestcase.putAll(getCountAssertionsData(testJar, testCollector, operateFaultTolerant));
		}
	}

	private Map<TestcaseIdentifier, Integer> getCountAssertionsData(String inputJarFile, ITestCollector testCollector,
			boolean operateFaultTolerant) throws IteratorException {
		JarAnalyzeIterator iterator = IteratorFactory.createJarAnalyzeIterator(inputJarFile, operateFaultTolerant);

		iterator.execute(testCollector);
		this.m_allTestcases.putAll(testCollector.getTestClassesWithTestcases());

		AssertionCounterOperation operation = new AssertionCounterOperation(testCollector.getTestClassDetector(),
				m_assertionInformation);

		iterator.execute(operation);

		return operation.getAssertionsPerTestcase();
	}

	public Map<TestcaseIdentifier, Integer> getAssertionsPerTestcase() {
		return m_assertionsPerTestcase;
	}

	/** {@inheritDoc} */
	@Override
	protected String getDescription() {
		return "Counting the assertions per testcase";
	}
}
