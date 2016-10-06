package de.tum.in.niedermr.ta.runner.execution.infocollection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.tum.in.niedermr.ta.core.analysis.result.presentation.IResultPresentation;
import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.core.code.tests.TestInformation;
import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;

/** Utility for collected information data. */
public class CollectedInformationUtility {

	/**
	 * Format the method-test relationship as parseable text.
	 * 
	 * @see #parseMethodTestcaseText(List)
	 */
	public static void convertToParseableMethodTestcaseText(Collection<TestInformation> data,
			IResultReceiver resultReceiver) {
		for (TestInformation info : data) {
			resultReceiver.append(info.getMethodUnderTest().get());

			for (TestcaseIdentifier testcase : info.getTestcases()) {
				resultReceiver.append(testcase.get());
			}

			resultReceiver.append(CommonConstants.SEPARATOR_END_OF_BLOCK);
		}

		resultReceiver.markResultAsComplete();
	}

	/** Format the method-test relationship as result output. */
	public static void convertToMethodTestcaseMappingResult(Collection<TestInformation> testInformationCollection,
			IResultPresentation resultPresentation, IResultReceiver resultReceiver) {

		for (TestInformation info : testInformationCollection) {
			for (TestcaseIdentifier testcase : info.getTestcases()) {
				resultReceiver
						.append(resultPresentation.formatMethodAndTestcaseMapping(info.getMethodUnderTest(), testcase));
			}
		}

		resultReceiver.markResultAsComplete();
	}

	/**
	 * Parse the method-testcase mapping.
	 * 
	 * @see #convertToParseableMethodTestcaseText(Collection, IResultReceiver)
	 */
	public static List<TestInformation> parseMethodTestcaseText(List<String> data) {
		List<TestInformation> result = new ArrayList<>();

		if (data.isEmpty()) {
			return result;
		}

		MethodIdentifier methodUnderTest = null;
		Set<TestcaseIdentifier> testcases = null;

		for (String line : data) {
			if (methodUnderTest == null) {
				methodUnderTest = MethodIdentifier.parse(line);
				testcases = new HashSet<>();
			} else {
				if (line.equals(CommonConstants.SEPARATOR_END_OF_BLOCK)) {
					TestInformation entry = new TestInformation(methodUnderTest, testcases);
					result.add(entry);

					methodUnderTest = null;
					testcases = null;
				} else {
					testcases.add(TestcaseIdentifier.parse(line));
				}
			}
		}

		return result;
	}
}
