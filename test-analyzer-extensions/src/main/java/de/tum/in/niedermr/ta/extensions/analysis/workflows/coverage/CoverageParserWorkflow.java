package de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;
import de.tum.in.niedermr.ta.core.analysis.result.receiver.ResultReceiverFactory;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.ExtensionEnvironmentConstants;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.steps.CoverageParserStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.AbstractWorkflow;
import de.tum.in.niedermr.ta.runner.analysis.workflow.common.PrepareWorkingFolderStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKey;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKeyNamespace;
import de.tum.in.niedermr.ta.runner.execution.ExecutionContext;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;

/**
 * Parser for coverage information. Currently, only coverage in form of XML from JaCoCo is supported.
 */
public class CoverageParserWorkflow extends AbstractWorkflow {

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
	protected void startInternal(ExecutionContext context, Configuration configuration) throws ExecutionException {
		PrepareWorkingFolderStep prepareStep = createAndInitializeExecutionStep(PrepareWorkingFolderStep.class);
		prepareStep.start();

		boolean useMultipleOutputFiles = configuration.getDynamicValues()
				.getBooleanValue(CONFIGURATION_KEY_USE_MULTIPLE_OUTPUT_FILES);
		String resultFileName = getFileInWorkingArea(context,
				ExtensionEnvironmentConstants.FILE_OUTPUT_COVERAGE_INFORMATION);
		IResultReceiver coverageResultReceiver = ResultReceiverFactory
				.createFileResultReceiverWithDefaultSettings(useMultipleOutputFiles, resultFileName);

		CoverageParserStep parseCoverageStep = createAndInitializeExecutionStep(CoverageParserStep.class);
		String coverageFileName = configuration.getDynamicValues().getStringValue(CONFIGURATION_KEY_COVERAGE_FILE);
		parseCoverageStep.setCoverageFileName(coverageFileName);
		parseCoverageStep.setCoverageResultReceiver(coverageResultReceiver);
		parseCoverageStep.start();
	}
}
