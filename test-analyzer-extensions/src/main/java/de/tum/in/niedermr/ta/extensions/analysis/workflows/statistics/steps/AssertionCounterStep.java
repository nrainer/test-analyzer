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
import de.tum.in.niedermr.ta.core.code.tests.assertions.AssertionInformation;
import de.tum.in.niedermr.ta.core.code.tests.collector.ITestCollector;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.operation.AssertionCounterOperation;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.execution.ExecutionContext;
import de.tum.in.niedermr.ta.runner.tests.TestRunnerUtil;

public class AssertionCounterStep extends AbstractExecutionStep {
	private static final Logger LOG = LogManager.getLogger(AssertionCounterStep.class);

	private static final String PREFIX_UNREGISTERED_ASSERT_METHODS_1 = "assert";
	private static final String PREFIX_UNREGISTERED_ASSERT_METHODS_2 = "check";

	private final static String[] KNOWN_FURTHER_ASSERTION_CLASS_NAMES = new String[] {
			"org.conqat.lib.commons.assertion.CCSMAssert", "org.conqat.lib.commons.assertion.CCSMPre",
			"org.testng.asserts.Assertion", "org.apache.commons.math3.TestUtils", "ru.histone.utils.Assert",
			"org.matheclipse.core.system.AbstractTestCase" };

	private final Map<MethodIdentifier, Integer> m_assertionsPerTestcase;
	private final Map<Class<?>, Set<String>> m_allTestcases;
	private final AssertionInformation m_assertionInformation;

	public AssertionCounterStep(ExecutionContext information) {
		super(information);

		this.m_assertionsPerTestcase = new HashMap<>();
		this.m_allTestcases = new HashMap<>();
		this.m_assertionInformation = getAssertionInformation();
	}

	private AssertionInformation getAssertionInformation() {
		Class<?>[] assertionClasses = getAvailableAssertionClasses().toArray(new Class[0]);
		return new AssertionInformation(assertionClasses) {
			@Override
			public Result isAssertionMethod(MethodIdentifier methodIdentifier) throws ClassNotFoundException {
				if (methodIdentifier.getOnlyMethodName().startsWith(PREFIX_UNREGISTERED_ASSERT_METHODS_1)
						|| methodIdentifier.getOnlyMethodName().startsWith(PREFIX_UNREGISTERED_ASSERT_METHODS_2)) {
					return new Result(true, methodIdentifier);
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

				LOG.info("Further assertion class " + className + " is available and will be used.");
			} catch (ClassNotFoundException ex) {
				continue;
			}
		}

		return result;
	}

	@Override
	protected void runInternal() throws Throwable {
		ITestCollector testCollector = TestRunnerUtil.getAppropriateTestCollector(m_configuration, true);

		for (String testJar : m_configuration.getCodePathToTest().getElements()) {
			this.m_assertionsPerTestcase.putAll(getCountAssertionsData(testJar, testCollector));
		}

		TestcaseInheritanceHelper.postProcessAllTestcases(m_allTestcases, m_assertionsPerTestcase);
	}

	private Map<MethodIdentifier, Integer> getCountAssertionsData(String inputJarFile, ITestCollector testCollector)
			throws Throwable {
		try {
			JarAnalyzeIterator iterator = IteratorFactory.createJarAnalyzeIterator(inputJarFile,
					m_configuration.getOperateFaultTolerant().getValue());

			iterator.execute(testCollector);
			this.m_allTestcases.putAll(testCollector.getTestClassesWithTestcases());

			AssertionCounterOperation operation = new AssertionCounterOperation(testCollector.getTestClassDetector(),
					m_assertionInformation);

			iterator.execute(operation);

			return operation.getAssertionsPerTestcase();
		} catch (Throwable t) {
			if (m_configuration.getOperateFaultTolerant().getValue()) {
				LOG.error("Skipping whole jar file " + inputJarFile
						+ " because of an error when operating in fault tolerant mode!", t);
				return new HashMap<>();
			} else {
				throw t;
			}
		}
	}

	public Map<MethodIdentifier, Integer> getAssertionsPerTestcase() {
		return m_assertionsPerTestcase;
	}

	@Override
	protected String getDescription() {
		return "Counting the assertions per testcase";
	}
}
