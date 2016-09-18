package de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.tum.in.niedermr.ta.core.analysis.jars.iteration.IteratorFactory;
import de.tum.in.niedermr.ta.core.analysis.jars.iteration.JarAnalyzeIterator;
import de.tum.in.niedermr.ta.core.code.identifier.Identifier;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.core.code.iteration.IteratorException;
import de.tum.in.niedermr.ta.core.code.tests.collector.ITestCollector;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.operation.InstructionCounterOperation;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;
import de.tum.in.niedermr.ta.runner.tests.TestRunnerUtil;

public class InstructionCounterStep extends AbstractExecutionStep {

	private final Map<MethodIdentifier, Integer> m_instructionsPerMethod;
	private final Map<TestcaseIdentifier, Integer> m_instructionsPerTestcase;
	private final Map<Class<?>, Set<String>> m_allTestcases;

	public InstructionCounterStep() {
		this.m_instructionsPerMethod = new HashMap<>();
		this.m_allTestcases = new HashMap<>();

		this.m_instructionsPerTestcase = new HashMap<>();
	}

	/** {@inheritDoc} */
	@Override
	protected String getSuffixForFullExecutionId() {
		return "INSCOUNT";
	}

	/** {@inheritDoc} */
	@Override
	protected void runInternal(Configuration configuration, ProcessExecution processExecution)
			throws ExecutionException, ReflectiveOperationException {
		ITestCollector testCollector = TestRunnerUtil.getAppropriateTestCollector(configuration, true);

		try {
			countInstructionsInMethods(configuration, testCollector);
			countInstructionsInTestcases(configuration, testCollector);
		} catch (IteratorException e) {
			throw new ExecutionException(getExecutionId(), e);
		}
	}

	protected void countInstructionsInMethods(Configuration configuration, ITestCollector testCollector)
			throws IteratorException {
		for (String sourceJar : configuration.getCodePathToMutate().getElements()) {
			this.m_instructionsPerMethod
					.putAll(getCountInstructionsData(configuration, Mode.METHOD, testCollector, sourceJar));
		}
	}

	protected void countInstructionsInTestcases(Configuration configuration, ITestCollector testCollector)
			throws IteratorException {
		for (String testJar : configuration.getCodePathToTest().getElements()) {
			this.m_instructionsPerTestcase
					.putAll(getCountInstructionsData(configuration, Mode.TESTCASE, testCollector, testJar));
		}

		TestcaseInheritanceHelper.postProcessAllTestcases(m_allTestcases, m_instructionsPerTestcase);
	}

	private <T extends Identifier> Map<T, Integer> getCountInstructionsData(Configuration configuration, Mode mode,
			ITestCollector testCollector, String inputJarFile) throws IteratorException {
		JarAnalyzeIterator iterator = IteratorFactory.createJarAnalyzeIterator(inputJarFile,
				configuration.getOperateFaultTolerant().getValue());

		if (mode == Mode.TESTCASE) {
			iterator.execute(testCollector);
			this.m_allTestcases.putAll(testCollector.getTestClassesWithTestcases());
		}

		InstructionCounterOperation<T> operation = new InstructionCounterOperation<>(
				testCollector.getTestClassDetector(), mode);

		iterator.execute(operation);

		return operation.getResult();
	}

	/** {@inheritDoc} */
	@Override
	protected String getDescription() {
		return "Counting the number of instructions per method / testcase";
	}

	public Map<MethodIdentifier, Integer> getInstructionsPerMethod() {
		return m_instructionsPerMethod;
	}

	public Map<TestcaseIdentifier, Integer> getInstructionsPerTestcase() {
		return m_instructionsPerTestcase;
	}

	public enum Mode {
		METHOD, TESTCASE;
	}
}
