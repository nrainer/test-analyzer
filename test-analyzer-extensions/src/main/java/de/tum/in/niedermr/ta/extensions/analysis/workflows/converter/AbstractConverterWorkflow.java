package de.tum.in.niedermr.ta.extensions.analysis.workflows.converter;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;
import de.tum.in.niedermr.ta.core.analysis.result.receiver.ResultReceiverFactory;
import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
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

		String[] inputFileNames = getInputFileNames(configuration);

		for (int i = 0; i < inputFileNames.length; i++) {

			boolean useMultipleOutputFiles = configuration.getDynamicValues()
					.getBooleanValue(getConfigurationKeyForMultipleOutputFileUsage());
			String inputFileName = inputFileNames[i];
			String resultFileName = getResultFileName(context, i);
			IResultReceiver resultReceiver = ResultReceiverFactory
					.createFileResultReceiverWithDefaultSettings(useMultipleOutputFiles, resultFileName);

			convert(context, inputFileName, resultReceiver);
		}
	}

	/**
	 * Get the name of the result file. <br/>
	 * Adds an index to the file name if the index is greater than 0. Do not add an index suffix to the name for index 0
	 * on purpose because in most cases only a single result file will be generated.
	 */
	protected String getResultFileName(ExecutionContext context, int index) {
		String defaultResultFileName = getFileInWorkingArea(context, getOutputFile());

		if (index > 0) {
			defaultResultFileName += "." + index;
		}

		return defaultResultFileName;
	}

	protected String[] getInputFileNames(Configuration configuration) {
		String inputFileParameterValue = configuration.getDynamicValues()
				.getStringValue(getConfigurationKeyForInputFile());
		return inputFileParameterValue.split(CommonConstants.SEPARATOR_DEFAULT);
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
		PARSER_STEP converterStep = createAndInitializeExecutionStep(getParserStep());
		configureStepBeforeConvert(context, converterStep, inputFileName, resultReceiver);
		converterStep.start();
	}

	/**
	 * @param context
	 *            which contains the configuration
	 */
	protected void configureStepBeforeConvert(ExecutionContext context, PARSER_STEP converterStep, String inputFileName,
			IResultReceiver resultReceiver) {
		converterStep.setInputFileName(inputFileName);
		converterStep.setResultReceiver(resultReceiver);
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
