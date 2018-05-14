package de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.pit;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.ExtensionEnvironmentConstants;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.AbstractConverterWorkflow;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.pit.steps.PitConverterStep;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKey;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKeyNamespace;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationValuesManager;
import de.tum.in.niedermr.ta.runner.execution.ExecutionContext;

/** Workflow to parse a PIT mutation testing result and convert it to SQL data. */
public class PitOutputConverterWorkflow extends AbstractConverterWorkflow<PitConverterStep> {

	/** Default name of the pit file. */
	private static final String DEFAULT_PIT_INPUT_FILE_NAME = "mutations.xml";

	/**
	 * <code>extension.converter.pit.inputFile</code>: path to the pit file to be parsed
	 */
	public static final DynamicConfigurationKey CONFIGURATION_KEY_PIT_INPUT_FILE = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.EXTENSION, "converter.pit.inputFile", DEFAULT_PIT_INPUT_FILE_NAME);

	/**
	 * <code>extension.converter.pit.useMultipleOutputFiles</code>: Split the output into multiple files.
	 */
	public static final DynamicConfigurationKey CONFIGURATION_KEY_USE_MULTIPLE_OUTPUT_FILES = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.EXTENSION, "converter.pit.useMultipleOutputFiles", false);

	/**
	 * <code>extension.converter.pit.mutationMatrix.enabled</code>: Create multiple statements from entries to build a
	 * complete matrix of killed and non-killed mutations.
	 */
	public static final DynamicConfigurationKey CONFIGURATION_KEY_PARSE_MUTATION_MATRIX = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.EXTENSION, "converter.pit.mutationMatrix.enabled", true);

	/**
	 * <code>extension.converter.pit.mutationMatrix.testcaseSeparator</code>: Separator for multiple testcases in the
	 * PIT file.
	 */
	public static final DynamicConfigurationKey CONFIGURATION_KEY_TESTCASES_SEPARATOR = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.EXTENSION, "converter.pit.mutationMatrix.testcaseSeparator", "|");

	/**
	 * <code>extension.converter.pit.mutationMatrix.skipNoCoverageMutations</code>: Ignore mutation results with status
	 * <code>NO_COVERAGE</code>.
	 */
	public static final DynamicConfigurationKey CONFIGURATION_KEY_SKIP_NO_COVERAGE_MUTATIONS = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.EXTENSION, "converter.pit.mutationMatrix.skipNoCoverageMutations",
					true);

	/** {@inheritDoc} */
	@Override
	protected DynamicConfigurationKey getConfigurationKeyForMultipleOutputFileUsage() {
		return CONFIGURATION_KEY_USE_MULTIPLE_OUTPUT_FILES;
	}

	/** {@inheritDoc} */
	@Override
	protected DynamicConfigurationKey getConfigurationKeyForInputFile() {
		return CONFIGURATION_KEY_PIT_INPUT_FILE;
	}

	/** {@inheritDoc} */
	@Override
	protected String getOutputFile() {
		return ExtensionEnvironmentConstants.FILE_OUTPUT_PIT_DATA;
	}

	/** {@inheritDoc} */
	@Override
	protected Class<PitConverterStep> getParserStep() {
		return PitConverterStep.class;
	}

	/** {@inheritDoc} */
	@Override
	protected void configureStepBeforeConvert(ExecutionContext context, PitConverterStep converterStep,
			String inputFileName, IResultReceiver resultReceiver) {
		super.configureStepBeforeConvert(context, converterStep, inputFileName, resultReceiver);

		DynamicConfigurationValuesManager dynamicConfigurationValuesManager = context.getConfiguration()
				.getDynamicValues();
		if (dynamicConfigurationValuesManager.getBooleanValue(CONFIGURATION_KEY_PARSE_MUTATION_MATRIX)) {
			String testcaseSeparator = dynamicConfigurationValuesManager
					.getStringValue(CONFIGURATION_KEY_TESTCASES_SEPARATOR);
			converterStep.enableParseMutationMatrix(testcaseSeparator);
		}

		converterStep.setSkipNoCoverageMutations(
				dynamicConfigurationValuesManager.getBooleanValue(CONFIGURATION_KEY_SKIP_NO_COVERAGE_MUTATIONS));
	}
}
