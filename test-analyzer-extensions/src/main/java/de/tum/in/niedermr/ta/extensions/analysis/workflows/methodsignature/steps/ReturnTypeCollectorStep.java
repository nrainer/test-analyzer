package de.tum.in.niedermr.ta.extensions.analysis.workflows.methodsignature.steps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.analysis.jars.iteration.IteratorFactory;
import de.tum.in.niedermr.ta.core.analysis.jars.iteration.JarAnalyzeIterator;
import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;
import de.tum.in.niedermr.ta.core.code.tests.collector.ITestCollector;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.methodsignature.operation.ReturnTypeRetrieverOperation;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.tests.TestRunnerUtil;

/** Step to collect return types. */
public class ReturnTypeCollectorStep extends AbstractExecutionStep {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(ReturnTypeCollectorStep.class);

	/** Result receiver. */
	private IResultReceiver m_resultReceiver;

	/** Set the result receiver. */
	public void setResultReceiver(IResultReceiver resultReceiver) {
		m_resultReceiver = resultReceiver;
	}

	/** {@inheritDoc} */
	@Override
	protected void runInternal(Configuration configuration, ProcessExecution processExecution) throws Throwable {
		ITestCollector testCollector = TestRunnerUtil.getAppropriateTestCollector(configuration, true);

		Set<String> returnTypeClassNameSet = new HashSet<>();

		for (String sourceJarFileName : configuration.getCodePathToMutate().getElements()) {
			returnTypeClassNameSet.addAll(getNonPrimitiveReturnTypes(configuration, testCollector, sourceJarFileName));
		}

		List<String> returnTypeClassNameList = new ArrayList<>();
		returnTypeClassNameList.addAll(returnTypeClassNameSet);
		Collections.sort(returnTypeClassNameList);

		m_resultReceiver.append(returnTypeClassNameList);
		m_resultReceiver.markResultAsComplete();
	}

	/** Get data about the method access modifier. */
	protected Collection<String> getNonPrimitiveReturnTypes(Configuration configuration, ITestCollector testCollector,
			String sourceJarFileName) throws Throwable {
		try {
			JarAnalyzeIterator iterator = IteratorFactory.createJarAnalyzeIterator(sourceJarFileName,
					configuration.getOperateFaultTolerant().getValue());

			ReturnTypeRetrieverOperation operation = new ReturnTypeRetrieverOperation(
					testCollector.getTestClassDetector());
			iterator.execute(operation);

			return operation.getMethodReturnTypes().values();
		} catch (Throwable t) {
			if (configuration.getOperateFaultTolerant().getValue()) {
				LOGGER.error("Skipping whole jar file " + sourceJarFileName
						+ " because of an error when operating in fault tolerant mode!", t);
				return new HashSet<>();
			}
			throw t;
		}
	}

	/** {@inheritDoc} */
	@Override
	protected String getDescription() {
		return "Collect declared return types";
	}

	/** {@inheritDoc} */
	@Override
	protected String getSuffixForFullExecutionId() {
		return "RETCOL";
	}
}
