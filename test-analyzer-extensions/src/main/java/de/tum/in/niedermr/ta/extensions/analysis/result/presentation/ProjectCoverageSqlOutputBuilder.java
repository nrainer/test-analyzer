package de.tum.in.niedermr.ta.extensions.analysis.result.presentation;

import java.util.ArrayList;
import java.util.List;

import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.ECoverageLevel;

/** SQL output builder for the aggregated project coverage. */
public class ProjectCoverageSqlOutputBuilder {
	private final List<String> m_result;
	private final IExecutionId m_executionId;
	private boolean m_completed = false;

	public ProjectCoverageSqlOutputBuilder(IExecutionId executionId, ECoverageLevel coverageLevel) {
		m_executionId = executionId;
		m_result = new ArrayList<>();
		m_result.add("UPDATE Execution_Information SET " + getCoverageColumnName(coverageLevel) + " = ");
		m_result.add(
				"\t (SELECT CASE WHEN SUM(countCovered) = 0 THEN 0 ELSE SUM(countCovered) / (SUM(countCovered) + SUM(countNotCovered)) END FROM");
		m_result.add("\t\t (");
		m_result.add("\t\t SELECT 'none' AS sourceFolder, 0 AS countCovered, 0 AS countNotCovered");
	}

	public List<String> complete() {
		if (m_completed) {
			throw new IllegalStateException("Was already completed");
		}
		m_completed = true;

		m_result.add("\t\t ) X");
		m_result.add("\t )");
		m_result.add(String.format("WHERE execution = '%s';", m_executionId.getShortId()));
		return m_result;
	}

	public void addSourceFolder(String sourceFolderName, int countCovered, int countNotCovered) {
		String partialStatement = String.format("\t\t UNION SELECT '%s', '%s', '%s'", sourceFolderName, countCovered,
				countNotCovered);

		if (isTestFolder(sourceFolderName)) {
			partialStatement = "-- " + partialStatement;
		}

		m_result.add(partialStatement);
	}

	private boolean isTestFolder(String sourceFolderName) {
		return sourceFolderName.contains("test") || sourceFolderName.contains("tst");
	}

	private String getCoverageColumnName(ECoverageLevel coverageLevel) {
		switch (coverageLevel) {
		case METHOD:
			return "methodCoverage";
		case LINE:
			return "lineCoverage";
		case INSTRUCTION:
			return "instructionCoverage";
		case BRANCH:
			return "branchCoverage";
		default:
			throw new IllegalArgumentException("Unexpected coverage level: " + coverageLevel);
		}
	}
}
