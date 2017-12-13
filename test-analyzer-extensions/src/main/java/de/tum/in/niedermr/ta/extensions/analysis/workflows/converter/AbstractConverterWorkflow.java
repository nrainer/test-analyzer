package de.tum.in.niedermr.ta.extensions.analysis.workflows.converter;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;
import de.tum.in.niedermr.ta.core.analysis.result.receiver.ResultReceiverFactory;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.ExtensionEnvironmentConstants;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.converter.parser.AbstractParserStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.AbstractWorkflow;
import de.tum.in.niedermr.ta.runner.analysis.workflow.common.PrepareWorkingFolderStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKey;
import de.tum.in.niedermr.ta.runner.execution.ExecutionContext;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;

/** Abstract converter workflow. */
public abstract class AbstractConverterWorkflow<PARSER_STEP extends AbstractParserStep> extends AbstractWorkflow {

	/** {@inheritDoc} */
	@Override
	protected void startInternal(ExecutionContext context, Configuration configuration) throws ExecutionException {
		prepareWorkingFolder();

		boolean useMultipleOutputFiles = configuration.getDynamicValues()
				.getBooleanValue(getConfigurationKeyForMultipleOutputFileUsage());
		String resultFileName = getFileInWorkingArea(context, getOutputFile());
		IResultReceiver resultReceiver = ResultReceiverFactory
				.createFileResultReceiverWithDefaultSettings(useMultipleOutputFiles, resultFileName);

		String inputFileName = configuration.getDynamicValues().getStringValue(getConfigurationKeyForInputFile());

		convert(context, inputFileName, resultReceiver);
	}

	/** Prepare the working folder. */
	protected void prepareWorkingFolder() {
		PrepareWorkingFolderStep prepareStep = createAndInitializeExecutionStep(PrepareWorkingFolderStep.class);
		prepareStep.start();
	}

	/**
	 * Convert the data from the input file and write it into the result receiver.
	 * 
	 * @param context
	 *            of the execution
	 */
	protected void convert(ExecutionContext context, String inputFileName, IResultReceiver resultReceiver) {
		PARSER_STEP parseCoverageStep = createAndInitializeExecutionStep(getParserStep());
		parseCoverageStep.setInputFileName(inputFileName);
		parseCoverageStep.setResultReceiver(resultReceiver);
		parseCoverageStep.start();
	}

	/** Get the configuration key for whether to use multiple output files. */
	protected abstract DynamicConfigurationKey getConfigurationKeyForMultipleOutputFileUsage();

	/** Get the configuration key for the input file. */
	protected abstract DynamicConfigurationKey getConfigurationKeyForInputFile();

	/** Get the output file constant from {@link ExtensionEnvironmentConstants}. */
	protected abstract String getOutputFile();

	/** Get the parser step. */
	protected abstract Class<PARSER_STEP> getParserStep();
}
