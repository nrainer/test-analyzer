package de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage;

import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.steps.CoverageParserStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.AbstractWorkflow;
import de.tum.in.niedermr.ta.runner.analysis.workflow.common.PrepareWorkingFolderStep;
import de.tum.in.niedermr.ta.runner.analysis.workflow.common.SimplePersistResultStep;
import de.tum.in.niedermr.ta.runner.configuration.Configuration;
import de.tum.in.niedermr.ta.runner.configuration.extension.ConfigurationExtensionKey;
import de.tum.in.niedermr.ta.runner.execution.ExecutionContext;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;
import de.tum.in.niedermr.ta.runner.execution.exceptions.ExecutionException;

/** Parser for coverage information. Currently, only coverage in form of XML from JaCoCo is supported. */
public class CoverageParserWorkflow extends AbstractWorkflow {

	/** <code>extension.code.coverage.file</code> */
	public static final ConfigurationExtensionKey COVERAGE_FILE = ConfigurationExtensionKey
			.create("code.coverage.file");

	/** Default name of the coverage file. */
	private static final String DEFAULT_COVERAGE_FILE_NAME = "coverage.xml";

	/** Result file name. */
	private static final String RESULT_FILE_NAME = EnvironmentConstants.PATH_WORKING_AREA_RESULT
			+ "coverage-information" + FileSystemConstants.FILE_EXTENSION_SQL_TXT;

	/** {@inheritDoc} */
	@Override
	protected void startInternal(ExecutionContext context, Configuration configuration) throws ExecutionException {
		PrepareWorkingFolderStep prepareStep = createAndInitializeExecutionStep(PrepareWorkingFolderStep.class);
		prepareStep.run();

		CoverageParserStep parseCoverageStep = createAndInitializeExecutionStep(CoverageParserStep.class);
		String coverageFileName = configuration.getExtension().getStringValue(COVERAGE_FILE,
				DEFAULT_COVERAGE_FILE_NAME);
		parseCoverageStep.setCoverageFileName(coverageFileName);
		parseCoverageStep.run();

		SimplePersistResultStep persistStep = createAndInitializeExecutionStep(SimplePersistResultStep.class);
		persistStep.setResult(parseCoverageStep.getResult());
		persistStep.setResultFileName(RESULT_FILE_NAME);
		persistStep.run();
	}
}
