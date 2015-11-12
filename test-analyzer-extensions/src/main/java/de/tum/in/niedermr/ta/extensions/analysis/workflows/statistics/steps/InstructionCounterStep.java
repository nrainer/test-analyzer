package de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.tum.in.niedermr.ta.core.analysis.jars.iteration.JarAnalyzeIterator;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.tests.collector.ITestCollector;
import de.tum.in.niedermr.ta.core.code.tests.collector.TestCollector;
import de.tum.in.niedermr.ta.core.code.tests.detector.ITestClassDetector;
import de.tum.in.niedermr.ta.core.code.tests.runner.ITestRunner;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.operation.InstructionCounterOperation;
import de.tum.in.niedermr.ta.runner.analysis.jars.iteration.FaultTolerantJarAnalyzeIterator;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.execution.ExecutionInformation;

public class InstructionCounterStep extends AbstractExecutionStep {
	private final Map<MethodIdentifier, Integer> instructionsPerMethod;
	private final Map<MethodIdentifier, Integer> instructionsPerTestcase;
	private final Map<Class<?>, Set<String>> allTestcases;

	public InstructionCounterStep(ExecutionInformation information) {
		super(information);

		this.instructionsPerMethod = new HashMap<>();
		this.allTestcases = new HashMap<>();

		this.instructionsPerTestcase = new HashMap<>();
	}

	@Override
	protected void runInternal() throws Throwable {
		ITestRunner testRunner = m_configuration.getTestRunner().createInstance();
		ITestClassDetector testClassDetector = testRunner.getTestClassDetector(true,
				m_configuration.getTestClassesToSkip().getElements());

		for (String sourceJar : m_configuration.getCodePathToMutate().getElements()) {
			this.instructionsPerMethod.putAll(getCountInstructionsData(Mode.METHOD, testClassDetector, sourceJar));
		}

		for (String testJar : m_configuration.getCodePathToTest().getElements()) {
			this.instructionsPerTestcase.putAll(getCountInstructionsData(Mode.TESTCASE, testClassDetector, testJar));
		}

		TestcaseInheritanceHelper.postProcessAllTestcases(allTestcases, instructionsPerTestcase, LOG);
	}

	private Map<MethodIdentifier, Integer> getCountInstructionsData(Mode mode, ITestClassDetector testClassDetector,
			String inputJarFile) throws Throwable {
		try {
			JarAnalyzeIterator iterator;

			if (m_configuration.getOperateFaultTolerant().getValue()) {
				iterator = new FaultTolerantJarAnalyzeIterator(inputJarFile, LOG);
			} else {
				iterator = new JarAnalyzeIterator(inputJarFile);
			}

			if (mode == Mode.TESTCASE) {
				ITestCollector testCollector = new TestCollector(testClassDetector);
				iterator.execute(testCollector);
				this.allTestcases.putAll(testCollector.getTestClassesWithTestcases());
			}

			InstructionCounterOperation operation = new InstructionCounterOperation(testClassDetector, mode);

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
		return instructionsPerMethod;
	}

	public Map<MethodIdentifier, Integer> getInstructionsPerTestcase() {
		return instructionsPerTestcase;
	}

	public enum Mode {
		METHOD, TESTCASE;
	}
}
