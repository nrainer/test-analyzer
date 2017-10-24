package de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.tum.in.niedermr.ta.core.artifacts.exceptions.IteratorException;
import de.tum.in.niedermr.ta.core.artifacts.iterator.IArtifactAnalysisIterator;
import de.tum.in.niedermr.ta.core.artifacts.jars.JarIteratorFactory;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.tests.collector.ITestCollector;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.operation.MethodModifierRetrievalOperation;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;
import de.tum.in.niedermr.ta.runner.tests.TestRunnerUtil;

/** Collect the access modifier of methods. */
public class MethodModifierRetrievalStep extends AbstractExecutionStep {

	/** The access modifier for each method. */
	private final Map<MethodIdentifier, String> m_modifierPerMethod = new HashMap<>();

	/** {@inheritDoc} */
	@Override
	protected String getSuffixForFullExecutionId() {
		return "MODRET";
	}

	/** {@inheritDoc} */
	@Override
	protected void runInternal(Configuration configuration, ProcessExecution processExecution)
			throws ExecutionException, ReflectiveOperationException, IOException {
		ITestCollector testCollector = TestRunnerUtil.getAppropriateTestCollector(configuration, true);

		for (String sourceJar : configuration.getCodePathToMutate().getElements()) {
			m_modifierPerMethod.putAll(getMethodModifierData(configuration, testCollector, sourceJar));
		}
	}

	/** Get data about the method access modifier. */
	protected Map<MethodIdentifier, String> getMethodModifierData(Configuration configuration,
			ITestCollector testCollector, String inputJarFile) throws ExecutionException {
		IArtifactAnalysisIterator iterator = JarIteratorFactory.createAnalyzeIterator(inputJarFile,
				configuration.getOperateFaultTolerant().getValue());

		MethodModifierRetrievalOperation operation = new MethodModifierRetrievalOperation(
				testCollector.getTestClassDetector());

		try {
			iterator.execute(operation);
			return operation.getResult();
		} catch (IteratorException e) {
			throw new ExecutionException(getExecutionId(), e);
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
