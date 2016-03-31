package de.tum.in.niedermr.ta.runner.execution.infocollection;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.tum.in.niedermr.ta.core.analysis.result.presentation.IResultPresentation;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.core.code.tests.TestInformation;
import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;

public class CollectedInformation {
	public static List<String> toPlainText(Collection<TestInformation> data) {
		List<String> output = new LinkedList<>();

		for (TestInformation info : data) {
			output.add(info.getMethodUnderTest().get());

			for (TestcaseIdentifier testcase : info.getTestcases()) {
				output.add(testcase.get());
			}

			output.add(CommonConstants.SEPARATOR_END_OF_BLOCK);
		}

		return output;
	}

	public static List<String> toResult(Collection<TestInformation> testInformationCollection, IResultPresentation resultPresentation) {
		List<String> output = new LinkedList<>();

		for (TestInformation info : testInformationCollection) {
			for (TestcaseIdentifier testcase : info.getTestcases()) {
				output.add(resultPresentation.formatMethodAndTestcaseMapping(info.getMethodUnderTest(), testcase));
			}
		}

		return output;
	}

	public static List<TestInformation> parseInformationCollectorData(List<String> data) {
		List<TestInformation> result = new LinkedList<>();

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
