package de.tum.in.niedermr.ta.extensions.analysis.workflows.methodsignature.steps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import de.tum.in.niedermr.ta.core.analysis.jars.iteration.IteratorFactory;
import de.tum.in.niedermr.ta.core.analysis.jars.iteration.JarAnalyzeIterator;
import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;
import de.tum.in.niedermr.ta.core.code.iteration.IteratorException;
import de.tum.in.niedermr.ta.core.code.tests.collector.ITestCollector;
import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.methodsignature.operation.ReturnTypeRetrieverOperation;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;
import de.tum.in.niedermr.ta.runner.tests.TestRunnerUtil;

/** Step to collect return types. */
public class ReturnTypeCollectorStep extends AbstractExecutionStep {

	/** Result receiver. */
	private IResultReceiver m_resultReceiver;

	/** Filter for the class names. */
	private Optional<Predicate<String>> m_classNameFilter;

	/** Result output format. LIST or CODE. */
	private String m_outputFormat;

	/** Set the result receiver. */
	public void setResultReceiver(IResultReceiver resultReceiver) {
		m_resultReceiver = resultReceiver;
	}

	/** {@link #m_classNameFilter} */
	public void setClassNameFilter(Optional<Predicate<String>> classNameFilter) {
		m_classNameFilter = classNameFilter;
	}

	/** {@link #m_outputFormat} */
	public void setOutputFormat(String outputFormat) {
		m_outputFormat = outputFormat;
	}

	/** {@inheritDoc} */
	@Override
	protected void runInternal(Configuration configuration, ProcessExecution processExecution)
			throws ExecutionException, ReflectiveOperationException {
		ITestCollector testCollector = TestRunnerUtil.getAppropriateTestCollector(configuration, true);

		Set<String> returnTypeClassNameSet = new HashSet<>();

		for (String sourceJarFileName : configuration.getCodePathToMutate().getElements()) {
			returnTypeClassNameSet.addAll(getNonPrimitiveReturnTypes(configuration, testCollector, sourceJarFileName));
		}

		if (m_classNameFilter.isPresent()) {
			returnTypeClassNameSet.removeIf(m_classNameFilter.get());
		}

		List<String> returnTypeClassNameList = new ArrayList<>();
		returnTypeClassNameList.addAll(returnTypeClassNameSet);
		Collections.sort(returnTypeClassNameList);

		for (String returnTypeCls : returnTypeClassNameList) {
			m_resultReceiver.append(format(returnTypeCls));
		}

		m_resultReceiver.markResultAsComplete();
	}

	/** Format an output entry. */
	private String format(String returnTypeCls) {
		if (m_outputFormat.equals("CODE")) {
			return String.format("case \"%s\":%s return new %s();", returnTypeCls, CommonConstants.NEW_LINE,
					returnTypeCls);
		}

		return returnTypeCls;
	}

	/** Get data about the method access modifier. */
	protected Collection<String> getNonPrimitiveReturnTypes(Configuration configuration, ITestCollector testCollector,
			String sourceJarFileName) throws ExecutionException {
		JarAnalyzeIterator iterator = IteratorFactory.createJarAnalyzeIterator(sourceJarFileName,
				configuration.getOperateFaultTolerant().getValue());
		ReturnTypeRetrieverOperation operation = new ReturnTypeRetrieverOperation(testCollector.getTestClassDetector());

		try {
			iterator.execute(operation);
			return operation.getMethodReturnTypes().values();
		} catch (IteratorException e) {
			throw new ExecutionException(getExecutionId(), e);
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
