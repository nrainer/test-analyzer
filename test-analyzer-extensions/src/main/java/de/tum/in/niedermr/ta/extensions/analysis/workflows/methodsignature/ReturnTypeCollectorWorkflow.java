package de.tum.in.niedermr.ta.extensions.analysis.workflows.methodsignature;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.base.AbstractFactoryReturnValueGenerator;
import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;
import de.tum.in.niedermr.ta.core.analysis.result.receiver.ResultReceiverFactory;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;
import de.tum.in.niedermr.ta.extensions.analysis.mutation.returnvalues.CommonInstancesReturnValueGenerator;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.methodsignature.steps.ReturnTypeCollectorStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.AbstractWorkflow;
import de.tum.in.niedermr.ta.runner.analysis.workflow.common.PrepareWorkingFolderStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKey;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKeyNamespace;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractMultiClassnameProperty;
import de.tum.in.niedermr.ta.runner.execution.ExecutionContext;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;

/**
 * Workflow that collects the declared return types of methods under test. <b>Note that it does not collect / consider
 * primitive types, their wrapper types and String.
 */
public class ReturnTypeCollectorWorkflow extends AbstractWorkflow {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(ReturnTypeCollectorWorkflow.class);

	/**
	 * <code>extension.methodsignature.useMultipleOutputFiles</code>: Split the output into multiple files.
	 */
	public static final DynamicConfigurationKey CONFIGURATION_KEY_USE_MULTIPLE_OUTPUT_FILES = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.EXTENSION, "methodsignature.useMultipleOutputFiles", false);

	/**
	 * <code>extension.methodsignature.returnvalue.suppressSupportedTypes</code>: Whether to suppress types that are
	 * already supported by the specified factories.
	 */
	public static final DynamicConfigurationKey CONFIGURATION_KEY_SUPPRESS_SUPPORTED_TYPES = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.EXTENSION, "methodsignature.returnvalue.suppressSupportedTypes",
					true);

	/**
	 * <code>extension.methodsignature.returnvalue.existingFactories</code>: Qualified class names of existing factory
	 * return value generators of type {@link AbstractFactoryReturnValueGenerator}.
	 */
	public static final DynamicConfigurationKey CONFIGURATION_KEY_EXISTING_FACTORY_GENERATOR_NAMES = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.EXTENSION, "methodsignature.returnvalue.existingFactoryGenerators",
					CommonInstancesReturnValueGenerator.class.getName());

	/**
	 * <code>extension.methodsignature.returnvalue.excludeWrapperAndString</code>: Whether wrapper types and String
	 * should be excluded.
	 */
	public static final DynamicConfigurationKey CONFIGURATION_KEY_EXCLUDE_WRAPPER_AND_STRING = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.EXTENSION, "methodsignature.returnvalue.excludeWrapperAndString",
					true);

	/**
	 * <code>extension.methodsignature.returnvalue.minTypeOccurrenceCount</code>: Number of minimal occurrences for a
	 * type to be included in the result list.
	 */
	public static final DynamicConfigurationKey CONFIGURATION_KEY_MIN_TYPE_OCCURRENCE_COUNT = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.EXTENSION, "methodsignature.returnvalue.minTypeOccurrenceCount",
					1);

	/**
	 * <code>extension.methodsignature.returnvalue.outputFormat</code>: LIST, COUNT or CODE
	 */
	public static final DynamicConfigurationKey CONFIGURATION_KEY_OUTPUT_FORMAT = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.EXTENSION, "methodsignature.returnvalue.outputFormat", "LIST");

	/** Result file name. */
	private static final String RESULT_FILE_NAME = EnvironmentConstants.PATH_WORKING_AREA_RESULT + "return-type-list"
			+ FileSystemConstants.FILE_EXTENSION_TXT;

	/** {@inheritDoc} */
	@Override
	protected void startInternal(ExecutionContext context, Configuration configuration) throws ExecutionException {
		PrepareWorkingFolderStep prepareStep = createAndInitializeExecutionStep(PrepareWorkingFolderStep.class);
		prepareStep.start();

		IResultReceiver resultReceiver = createResultReceiver(context, configuration);

		ReturnTypeCollectorStep collectorStep = createAndInitializeExecutionStep(ReturnTypeCollectorStep.class);
		collectorStep.setResultReceiver(resultReceiver);

		collectorStep.setOutputFormat(configuration.getDynamicValues().getStringValue(CONFIGURATION_KEY_OUTPUT_FORMAT));
		collectorStep.setExcludeWrapperAndString(
				configuration.getDynamicValues().getBooleanValue(CONFIGURATION_KEY_EXCLUDE_WRAPPER_AND_STRING));
		collectorStep.setMinTypeOccurrenceCount(
				configuration.getDynamicValues().getIntegerValue(CONFIGURATION_KEY_MIN_TYPE_OCCURRENCE_COUNT));

		if (configuration.getDynamicValues().getBooleanValue(CONFIGURATION_KEY_SUPPRESS_SUPPORTED_TYPES)) {
			collectorStep.setClassNameFilter(createClassNameFilter(configuration));
		}

		collectorStep.start();
	}

	/** Create a class name filter based on the supported types of the existing factories. */
	private Optional<Predicate<String>> createClassNameFilter(Configuration configuration) {
		try {
			FactoryGeneratorProperty returnValueFactoryGeneratorProperty = configuration.getDynamicValues()
					.getValueAsProperty(CONFIGURATION_KEY_EXISTING_FACTORY_GENERATOR_NAMES,
							new FactoryGeneratorProperty());

			if (returnValueFactoryGeneratorProperty.countElements() == 0) {
				return Optional.empty();
			}

			List<AbstractFactoryReturnValueGenerator> returnValueFactories = Arrays
					.asList(returnValueFactoryGeneratorProperty.createInstances());

			Predicate<String> filter = className -> returnValueFactories.stream()
					.anyMatch(factoryGenerator -> factoryGenerator.getFactoryInstance().supports(MethodIdentifier.EMPTY,
							className));
			return Optional.of(filter);

		} catch (ConfigurationException | ReflectiveOperationException e) {
			LOGGER.error("Class name filter creation failed", e);
			return Optional.empty();
		}
	}

	/** Create the result receiver. */
	private IResultReceiver createResultReceiver(ExecutionContext context, Configuration configuration) {
		boolean useMultipleOutputFiles = configuration.getDynamicValues()
				.getBooleanValue(CONFIGURATION_KEY_USE_MULTIPLE_OUTPUT_FILES);
		String resultFileName = getFileInWorkingArea(context, RESULT_FILE_NAME);
		IResultReceiver resultReceiver = ResultReceiverFactory
				.createFileResultReceiverWithDefaultSettings(useMultipleOutputFiles, resultFileName);
		return resultReceiver;
	}

	/** Property for {@link #CONFIGURATION_KEY_EXISTING_FACTORY_GENERATOR_NAMES} */
	private static class FactoryGeneratorProperty
			extends AbstractMultiClassnameProperty<AbstractFactoryReturnValueGenerator> {

		/** {@inheritDoc} */
		@Override
		protected Class<AbstractFactoryReturnValueGenerator> getRequiredType() {
			return AbstractFactoryReturnValueGenerator.class;
		}

		/** {@inheritDoc} */
		@Override
		public String getName() {
			return CONFIGURATION_KEY_EXISTING_FACTORY_GENERATOR_NAMES.getName();
		}

		/** {@inheritDoc} */
		@Override
		public String getDescription() {
			return "";
		}
	}
}
