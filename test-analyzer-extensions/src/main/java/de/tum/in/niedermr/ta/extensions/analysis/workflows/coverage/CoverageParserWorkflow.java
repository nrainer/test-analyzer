package de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage;

import de.tum.in.niedermr.ta.extensions.analysis.workflows.ExtensionEnvironmentConstants;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.AbstractConverterWorkflow;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.steps.CoverageParserStep;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKey;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKeyNamespace;

/**
 * Parser for coverage information. Currently, only coverage in form of XML from JaCoCo is supported.
 */
public class CoverageParserWorkflow extends AbstractConverterWorkflow<CoverageParserStep> {

	/** Default name of the coverage file. */
	private static final String DEFAULT_COVERAGE_FILE_NAME = "coverage.xml";

	/**
	 * <code>extension.code.coverage.useMultipleOutputFiles</code>: Split the output into multiple files.
	 */
	public static final DynamicConfigurationKey CONFIGURATION_KEY_USE_MULTIPLE_OUTPUT_FILES = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.EXTENSION, "code.coverage.useMultipleOutputFiles", false);

	/** <code>extension.code.coverage.file</code> */
	public static final DynamicConfigurationKey CONFIGURATION_KEY_COVERAGE_FILE = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.EXTENSION, "code.coverage.file", DEFAULT_COVERAGE_FILE_NAME);

	/** {@inheritDoc} */
	@Override
	protected DynamicConfigurationKey getConfigurationKeyForMultipleOutputFileUsage() {
		return CONFIGURATION_KEY_USE_MULTIPLE_OUTPUT_FILES;
	}

	/** {@inheritDoc} */
	@Override
	protected DynamicConfigurationKey getConfigurationKeyForInputFile() {
		return CONFIGURATION_KEY_COVERAGE_FILE;
	}

	/** {@inheritDoc} */
	@Override
	protected String getOutputFile() {
		return ExtensionEnvironmentConstants.FILE_OUTPUT_COVERAGE_INFORMATION;
	}

	/** {@inheritDoc} */
	@Override
	protected Class<CoverageParserStep> getParserStep() {
		return CoverageParserStep.class;
	}
}
