package de.tum.in.niedermr.ta.extensions.analysis.workflows.methodsignature.steps;

import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;
import de.tum.in.niedermr.ta.core.artifacts.exceptions.IteratorException;
import de.tum.in.niedermr.ta.core.artifacts.jars.JarAnalyzeIterator;
import de.tum.in.niedermr.ta.core.artifacts.jars.JarIteratorFactory;
import de.tum.in.niedermr.ta.core.code.constants.JavaConstants;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.tests.collector.ITestCollector;
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
	private Optional<Predicate<String>> m_classNameFilter = Optional.empty();

	/** Result output format. */
	private OutputFormat m_outputFormat = OutputFormat.getDefault();

	/** Exclude wrapper types and String. */
	private boolean m_excludeWrapperTypesAndString;

	/** Minimum occurrences of a type to be included in the result list. */
	private int m_minTypeOccurrences;

	/** Set the result receiver. */
	public void setResultReceiver(IResultReceiver resultReceiver) {
		m_resultReceiver = resultReceiver;
	}

	/** {@link #m_classNameFilter} */
	public void setClassNameFilter(Optional<Predicate<String>> classNameFilter) {
		m_classNameFilter = classNameFilter;
	}

	/** {@link #m_outputFormat} */
	public void setOutputFormat(OutputFormat outputFormat) {
		m_outputFormat = outputFormat;
	}

	/** {@link #m_excludeWrapperTypesAndString} */
	public void setExcludeWrapperAndString(boolean excludeWrapperTypesAndString) {
		m_excludeWrapperTypesAndString = excludeWrapperTypesAndString;
	}

	/** {@link #m_minTypeOccurrences} */
	public void setMinTypeOccurrenceCount(int minOccurrences) {
		m_minTypeOccurrences = minOccurrences;
	}

	/** {@inheritDoc} */
	@Override
	protected void runInternal(Configuration configuration, ProcessExecution processExecution)
			throws ExecutionException, ReflectiveOperationException {
		Map<String, Integer> returnTypeClassNameOccurrences = retrieveAndAggregateReturnTypeClassNameOccurrences(
				configuration);
		filterOccurrencesAndCreateOutput(returnTypeClassNameOccurrences);
	}

	/** Filter the occurrences and create the output. */
	protected void filterOccurrencesAndCreateOutput(Map<String, Integer> returnTypeClassNameOccurrences) {
		if (m_excludeWrapperTypesAndString) {
			returnTypeClassNameOccurrences.remove(String.class.getName());
			returnTypeClassNameOccurrences.entrySet()
					.removeIf(entry -> JavaConstants.WRAPPER_TYPE_CLASS_NAMES.contains(entry.getKey()));
		}

		int countTypes = returnTypeClassNameOccurrences.size();
		int countTypeUsages = returnTypeClassNameOccurrences.values().stream().mapToInt(Number::intValue).sum();

		if (m_classNameFilter.isPresent()) {
			returnTypeClassNameOccurrences.entrySet().removeIf(entry -> m_classNameFilter.get().test(entry.getKey()));
		}

		int countUnsupportedTypes = returnTypeClassNameOccurrences.size();
		int countUnsupportedTypeUsages = returnTypeClassNameOccurrences.values().stream().mapToInt(Number::intValue)
				.sum();

		appendStatistics(countTypes, countTypeUsages, countUnsupportedTypes, countUnsupportedTypeUsages);

		if (m_minTypeOccurrences > 1) {
			returnTypeClassNameOccurrences.entrySet().removeIf(entry -> entry.getValue() < m_minTypeOccurrences);
		}

		for (String returnTypeCls : getTypeListOrderedByName(returnTypeClassNameOccurrences)) {
			m_resultReceiver.append(format(returnTypeCls, returnTypeClassNameOccurrences));
		}

		m_resultReceiver.markResultAsComplete();
	}

	protected static List<String> getTypeListOrderedByName(Map<String, Integer> returnTypeClassNameOccurrences) {
		List<String> returnTypeClassNameList = new ArrayList<>();
		returnTypeClassNameList.addAll(returnTypeClassNameOccurrences.keySet());
		Collections.sort(returnTypeClassNameList);
		return returnTypeClassNameList;
	}

	protected void appendStatistics(int countTypes, int countTypeUsages, int countUnsupportedTypes,
			int countUnsupportedTypeUsages) {
		double ratioSupportedTypes = 1 - (countUnsupportedTypes / (double) countTypes);
		double ratioSupportedTypeUsages = 1 - (countUnsupportedTypeUsages / (double) countTypeUsages);

		m_resultReceiver.append("// NON-PRIMITIVE TYPE SUMMARY");

		if (m_excludeWrapperTypesAndString) {
			m_resultReceiver.append("// (NOTE: Wrapper types and String are not considered.)");
		}

		m_resultReceiver.append("// Total types: " + countTypes);
		m_resultReceiver.append("// Total unsupported types: " + countUnsupportedTypes);
		m_resultReceiver.append("// Ratio supported types: " + ratioSupportedTypes);
		m_resultReceiver.append("// Total type usages: " + countTypeUsages);
		m_resultReceiver.append("// Total unsupported type usages: " + countUnsupportedTypeUsages);
		m_resultReceiver.append("// Ratio supported type usages: " + ratioSupportedTypeUsages);
		m_resultReceiver.append(getSupportedTypeUsageTargetInfo(0.9, countTypeUsages, countUnsupportedTypeUsages,
				ratioSupportedTypeUsages));
		m_resultReceiver.append(getSupportedTypeUsageTargetInfo(0.8, countTypeUsages, countUnsupportedTypeUsages,
				ratioSupportedTypeUsages));
		m_resultReceiver.append("");
	}

	protected String getSupportedTypeUsageTargetInfo(double targetRatio, int countTypeUsages,
			int countUnsupportedTypeUsages, double ratioSupportedTypeUsages) {
		if (ratioSupportedTypeUsages >= targetRatio) {
			return "// Supported type usage of " + (targetRatio * 100) + "% reached.";
		}
		int countMissingTypeUsages = (int) Math.ceil(countTypeUsages * targetRatio)
				- (countTypeUsages - countUnsupportedTypeUsages);

		return "// Reduce unsupported type usages by at least " + countMissingTypeUsages + " to reach "
				+ (targetRatio * 100) + "% support ratio.";
	}

	protected Map<String, Integer> retrieveAndAggregateReturnTypeClassNameOccurrences(Configuration configuration)
			throws ReflectiveOperationException {
		ITestCollector testCollector = TestRunnerUtil.getAppropriateTestCollector(configuration, true);

		Map<String, Integer> aggregatedReturnTypeClassNameOccurrences = new HashMap<>();

		for (String sourceJarFileName : configuration.getCodePathToMutate().getElements()) {
			Map<String, Long> nonPrimitiveReturnTypes = getNonPrimitiveReturnTypes(configuration, testCollector,
					sourceJarFileName);

			for (Entry<String, Long> returnTypeWithOccurrence : nonPrimitiveReturnTypes.entrySet()) {
				int oldCountValue = aggregatedReturnTypeClassNameOccurrences
						.getOrDefault(returnTypeWithOccurrence.getKey(), 0);
				aggregatedReturnTypeClassNameOccurrences.put(returnTypeWithOccurrence.getKey(),
						oldCountValue + returnTypeWithOccurrence.getValue().intValue());
			}
		}

		return aggregatedReturnTypeClassNameOccurrences;
	}

	/** Format an output entry. */
	private List<String> format(String returnTypeCls, Map<String, Integer> returnTypeClassNameOccurrences) {
		if (m_outputFormat == OutputFormat.CODE) {
			return Arrays.asList(String.format("case \"%s\":", returnTypeCls),
					String.format(" return new %s();", returnTypeCls));
		}

		if (m_outputFormat == OutputFormat.LIST) {
			return Arrays.asList(returnTypeCls);
		}

		if (m_outputFormat == OutputFormat.LIST_WITH_COUNT) {
			return Arrays
					.asList(String.format("%s (%s)", returnTypeCls, returnTypeClassNameOccurrences.get(returnTypeCls)));
		}

		if (m_outputFormat == OutputFormat.LIST_WITH_ORIGIN_INFO) {
			return Arrays.asList(String.format("%s (%s)", returnTypeCls, tryGetClassOrigin(returnTypeCls)));
		}

		throw new IllegalArgumentException("Unsupported output format: " + m_outputFormat);
	}

	/** Get the origin from where a class was loaded. */
	private String tryGetClassOrigin(String className) {
		try {
			Class<?> cls = Class.forName(className);
			CodeSource codeSource = cls.getProtectionDomain().getCodeSource();

			if (codeSource != null) {
				return codeSource.getLocation().toString();
			}

			return "#unknown#";
		} catch (SecurityException e) {
			return "#not allowed#";
		} catch (Throwable t) {
			return "#exception occurred#";
		}
	}

	/**
	 * Get data about the method access modifier.
	 * 
	 * @return map with the class names and the number of occurrences
	 */
	protected Map<String, Long> getNonPrimitiveReturnTypes(Configuration configuration, ITestCollector testCollector,
			String sourceJarFileName) throws ExecutionException {
		JarAnalyzeIterator iterator = JarIteratorFactory.createJarAnalyzeIterator(sourceJarFileName,
				configuration.getOperateFaultTolerant().getValue());
		ReturnTypeRetrieverOperation operation = new ReturnTypeRetrieverOperation(testCollector.getTestClassDetector());

		try {
			iterator.execute(operation);
			Map<MethodIdentifier, String> returnTypeByMethodId = operation.getMethodReturnTypes();
			return returnTypeByMethodId.values().stream()
					.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
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
