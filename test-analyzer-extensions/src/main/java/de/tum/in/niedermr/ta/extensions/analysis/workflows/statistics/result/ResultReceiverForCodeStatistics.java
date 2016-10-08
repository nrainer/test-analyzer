package de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.result;

import java.util.Map;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.DelegationResultReceiver;
import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.extensions.analysis.result.presentation.IResultPresentationExtended;

/** Extended result receiver that delegates the data to a specified receiver. */
public class ResultReceiverForCodeStatistics extends DelegationResultReceiver {

	private final IResultPresentationExtended m_resultPresentationExtended;

	/** Constructor. */
	public ResultReceiverForCodeStatistics(IResultReceiver resultReceiver,
			IResultPresentationExtended resultPresentationExtended) {
		super(resultReceiver);
		m_resultPresentationExtended = resultPresentationExtended;
	}

	public void addResultInstructionsPerMethod(Map<MethodIdentifier, Integer> codeInformation) {
		codeInformation.entrySet().forEach(entry -> append(
				m_resultPresentationExtended.formatInstructionsPerMethod(entry.getKey(), entry.getValue())));
	}

	public void addResultModifierPerMethod(Map<MethodIdentifier, String> codeInformation) {
		codeInformation.entrySet().forEach(entry -> append(
				m_resultPresentationExtended.formatModifierPerMethod(entry.getKey(), entry.getValue())));
	}

	public void addResultInstructionsPerTestcase(Map<TestcaseIdentifier, Integer> codeInformation) {
		codeInformation.entrySet().forEach(entry -> append(
				m_resultPresentationExtended.formatInstructionsPerTestcase(entry.getKey(), entry.getValue())));
	}

	public void addResultAssertionsPerTestcase(Map<TestcaseIdentifier, Integer> codeInformation) {
		codeInformation.entrySet().forEach(entry -> append(
				m_resultPresentationExtended.formatAssertionsPerTestcase(entry.getKey(), entry.getValue())));
	}
}
