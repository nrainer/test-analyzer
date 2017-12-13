package de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.pit;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.ExtensionEnvironmentConstants;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.AbstractConverterWorkflow;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.pit.steps.PitConverterStep;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKey;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKeyNamespace;
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
	 * <code>extension.converter.pit.unrollMultipleTestcases.enabled</code>: Create multiple statements from entries
	 * with multiple testcases.
	 */
	public static final DynamicConfigurationKey CONFIGURATION_KEY_UNROLL_TESTCASES_ENABLED = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.EXTENSION, "converter.pit.unrollMultipleTestcases.enabled", true);

	/**
	 * <code>extension.converter.pit.unrollMultipleTestcases.separator</code>: Separator for multiple testcases in the
	 * PIT file.
	 */
	public static final DynamicConfigurationKey CONFIGURATION_KEY_UNROLL_TESTCASES_SEPARATOR = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.EXTENSION, "converter.pit.unrollMultipleTestcases.separator", "|");

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

		if (context.getConfiguration().getDynamicValues().getBooleanValue(CONFIGURATION_KEY_UNROLL_TESTCASES_ENABLED)) {
			String testcaseSeparator = context.getConfiguration().getDynamicValues()
					.getStringValue(CONFIGURATION_KEY_UNROLL_TESTCASES_SEPARATOR);
			converterStep.enableTestcaseUnrolling(testcaseSeparator);
		}
	}
}
