package de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.InMemoryResultReceiver;
import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.steps.CoverageParserStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.AbstractWorkflow;
import de.tum.in.niedermr.ta.runner.analysis.workflow.common.PrepareWorkingFolderStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.common.SimplePersistResultStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKey;
import de.tum.in.niedermr.ta.runner.configuration.extension.DynamicConfigurationKeyNamespace;
import de.tum.in.niedermr.ta.runner.execution.ExecutionContext;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;

/** Parser for coverage information. Currently, only coverage in form of XML from JaCoCo is supported. */
public class CoverageParserWorkflow extends AbstractWorkflow {

	/** Default name of the coverage file. */
	private static final String DEFAULT_COVERAGE_FILE_NAME = "coverage.xml";

	/** <code>extension.code.coverage.file</code> */
	public static final DynamicConfigurationKey COVERAGE_FILE = DynamicConfigurationKey
			.create(DynamicConfigurationKeyNamespace.EXTENSION, "code.coverage.file", DEFAULT_COVERAGE_FILE_NAME);

	/** Result file name. */
	private static final String RESULT_FILE_NAME = EnvironmentConstants.PATH_WORKING_AREA_RESULT
			+ "coverage-information" + FileSystemConstants.FILE_EXTENSION_SQL_TXT;

	/** {@inheritDoc} */
	@Override
	protected void startInternal(ExecutionContext context, Configuration configuration) throws ExecutionException {
		PrepareWorkingFolderStep prepareStep = createAndInitializeExecutionStep(PrepareWorkingFolderStep.class);
		prepareStep.start();

		InMemoryResultReceiver coverageResultReceiver = new InMemoryResultReceiver();

		CoverageParserStep parseCoverageStep = createAndInitializeExecutionStep(CoverageParserStep.class);
		String coverageFileName = configuration.getDynamicValues().getStringValue(COVERAGE_FILE);
		parseCoverageStep.setCoverageFileName(coverageFileName);
		parseCoverageStep.setCoverageResultReceiver(coverageResultReceiver);
		parseCoverageStep.start();

		SimplePersistResultStep persistStep = createAndInitializeExecutionStep(SimplePersistResultStep.class);
		persistStep.setResult(coverageResultReceiver.getResult());
		persistStep.setResultFileName(RESULT_FILE_NAME);
		persistStep.start();
	}
}
