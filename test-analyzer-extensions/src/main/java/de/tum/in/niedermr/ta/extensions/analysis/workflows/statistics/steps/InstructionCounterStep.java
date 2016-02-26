package de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.analysis.jars.iteration.IteratorFactory;
import de.tum.in.niedermr.ta.core.analysis.jars.iteration.JarAnalyzeIterator;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.tests.collector.ITestCollector;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.operation.InstructionCounterOperation;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.execution.ExecutionInformation;
import de.tum.in.niedermr.ta.runner.tests.TestRunnerUtil;

public class InstructionCounterStep extends AbstractExecutionStep {
	private static final Logger LOG = LogManager.getLogger(InstructionCounterStep.class);

	private final Map<MethodIdentifier, Integer> m_instructionsPerMethod;
	private final Map<MethodIdentifier, Integer> m_instructionsPerTestcase;
	private final Map<Class<?>, Set<String>> m_allTestcases;

	public InstructionCounterStep(ExecutionInformation information) {
		super(information);

		this.m_instructionsPerMethod = new HashMap<>();
		this.m_allTestcases = new HashMap<>();

		this.m_instructionsPerTestcase = new HashMap<>();
	}

	@Override
	protected void runInternal() throws Throwable {
		ITestCollector testCollector = TestRunnerUtil.getAppropriateTestCollector(m_configuration, true);

		for (String sourceJar : m_configuration.getCodePathToMutate().getElements()) {
			this.m_instructionsPerMethod.putAll(getCountInstructionsData(Mode.METHOD, testCollector, sourceJar));
		}

		for (String testJar : m_configuration.getCodePathToTest().getElements()) {
			this.m_instructionsPerTestcase.putAll(getCountInstructionsData(Mode.TESTCASE, testCollector, testJar));
		}

		TestcaseInheritanceHelper.postProcessAllTestcases(m_allTestcases, m_instructionsPerTestcase);
	}

	private Map<MethodIdentifier, Integer> getCountInstructionsData(Mode mode, ITestCollector testCollector,
			String inputJarFile) throws Throwable {
		try {
			JarAnalyzeIterator iterator = IteratorFactory.createJarAnalyzeIterator(inputJarFile,
					m_configuration.getOperateFaultTolerant().getValue());

			if (mode == Mode.TESTCASE) {
				iterator.execute(testCollector);
				this.m_allTestcases.putAll(testCollector.getTestClassesWithTestcases());
			}

			InstructionCounterOperation operation = new InstructionCounterOperation(
					testCollector.getTestClassDetector(), mode);

			iterator.execute(operation);

			return operation.getResult();
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

	@Override
	protected String getDescription() {
		return "Counting the number of instructions per method / testcase";
	}

	public Map<MethodIdentifier, Integer> getInstructionsPerMethod() {
		return m_instructionsPerMethod;
	}

	public Map<MethodIdentifier, Integer> getInstructionsPerTestcase() {
		return m_instructionsPerTestcase;
	}

	public enum Mode {
		METHOD, TESTCASE;
	}
}
