package de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.analysis.jars.iteration.IteratorFactory;
import de.tum.in.niedermr.ta.core.analysis.jars.iteration.JarAnalyzeIterator;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.tests.collector.ITestCollector;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.operation.MethodModifierRetrievalOperation;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.tests.TestRunnerUtil;

/** Collect the access modifier of methods. */
public class MethodModifierRetrievalStep extends AbstractExecutionStep {

	private static final Logger LOG = LogManager.getLogger(MethodModifierRetrievalStep.class);

	/** The access modifier for each method. */
	private final Map<MethodIdentifier, String> m_modifierPerMethod = new HashMap<>();

	/** {@inheritDoc} */
	@Override
	protected void runInternal(Configuration configuration, ProcessExecution processExecution) throws Throwable {
		ITestCollector testCollector = TestRunnerUtil.getAppropriateTestCollector(configuration, true);

		for (String sourceJar : configuration.getCodePathToMutate().getElements()) {
			m_modifierPerMethod.putAll(getCountInstructionsData(configuration, testCollector, sourceJar));
		}
	}

	private Map<MethodIdentifier, String> getCountInstructionsData(Configuration configuration,
			ITestCollector testCollector, String inputJarFile) throws Throwable {
		try {
			JarAnalyzeIterator iterator = IteratorFactory.createJarAnalyzeIterator(inputJarFile,
					configuration.getOperateFaultTolerant().getValue());

			MethodModifierRetrievalOperation operation = new MethodModifierRetrievalOperation(
					testCollector.getTestClassDetector());

			iterator.execute(operation);

			return operation.getResult();
		} catch (Throwable t) {
			if (configuration.getOperateFaultTolerant().getValue()) {
				LOG.error("Skipping whole jar file " + inputJarFile
						+ " because of an error when operating in fault tolerant mode!", t);
				return new HashMap<>();
			}
			throw t;
		}
	}

	/** @see #m_modifierPerMethod */
	public Map<MethodIdentifier, String> getModifierPerMethod() {
		return m_modifierPerMethod;
	}

	/** {@inheritDoc} */
	@Override
	protected String getDescription() {
		return "Collect the method access modifiers";
	}
}
