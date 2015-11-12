package de.tum.in.niedermr.ta.runner.analysis.workflow.steps.impl.s5;

import java.io.File;

import org.conqat.lib.commons.filesystem.FileSystemUtils;

import de.tum.in.niedermr.ta.core.common.io.TextFileData;
import de.tum.in.niedermr.ta.runner.analysis.workflow.steps.AbstractExecutionStep;
import de.tum.in.niedermr.ta.runner.execution.ExecutionInformation;
import de.tum.in.niedermr.ta.runner.execution.environment.Environment;
import de.tum.in.niedermr.ta.runner.execution.environment.EnvironmentConstants;

public class FinalizeResultStep extends AbstractExecutionStep {
	private final boolean m_removeTempFilesAfterwards;

	public FinalizeResultStep(ExecutionInformation information) {
		super(information);

		this.m_removeTempFilesAfterwards = information.getConfiguration().getRemoveTempData().getValue();
	}

	@Override
	public void runInternal() throws Exception {
		if (m_configuration.getExecuteMutateAndTest().isTrue()) {
			final String destinationFilePath = getFileInWorkingArea(
					Environment.getGenericFilePathOfOutputResult(m_configuration));

			if (m_configuration.isMultiThreaded()) {
				TextFileData.mergeFiles(destinationFilePath, getFileInWorkingArea(FILE_TEMP_RESULT_X),
						m_configuration.getNumberOfThreads().getValue());
			} else {
				File dest = new File(destinationFilePath);

				if (dest.exists()) {
					dest.delete();
				}

				File resultFile = new File(getFileInWorkingArea(getWithIndex(FILE_TEMP_RESULT_X, 0)));

				resultFile.renameTo(dest);
			}
		}

		if (m_removeTempFilesAfterwards) {
			FileSystemUtils
					.deleteRecursively(new File(getFileInWorkingArea(EnvironmentConstants.PATH_WORKING_AREA_TEMP)));
		}
	}

	@Override
	protected String getDescription() {
		return "Finalizing the result" + (m_removeTempFilesAfterwards ? " and removing temp files" : "");
	}
}
