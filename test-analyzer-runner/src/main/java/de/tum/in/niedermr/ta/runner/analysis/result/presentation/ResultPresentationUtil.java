package de.tum.in.niedermr.ta.runner.analysis.result.presentation;

import de.tum.in.niedermr.ta.core.analysis.result.presentation.IResultPresentation;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;
import de.tum.in.niedermr.ta.core.common.util.CommonUtility;
import de.tum.in.niedermr.ta.runner.configuration.property.ResultPresentationProperty;

public class ResultPresentationUtil {
	public static IResultPresentation getResultPresentation(String resultPresentation, String executionId)
			throws ReflectiveOperationException {
		IResultPresentation presentation = createResultPresentation(resultPresentation);

		if (executionId.length() >= CommonUtility.LENGTH_OF_RANDOM_ID) {
			presentation.setShortExecutionId(executionId.substring(0, CommonUtility.LENGTH_OF_RANDOM_ID));
		} else {
			presentation.setShortExecutionId(executionId);
		}

		return presentation;
	}

	/** Create the appropriate instance of {@link IResultPresentation}. */
	private static IResultPresentation createResultPresentation(String resultPresentation)
			throws ReflectiveOperationException {
		if (resultPresentation.equals(ResultPresentationProperty.RESULT_PRESENTATION_TEXT)) {
			return new TextResultPresentation();
		} else if (resultPresentation.equals(ResultPresentationProperty.RESULT_PRESENTATION_DB)) {
			return new DatabaseResultPresentation();
		}

		return JavaUtility.createInstance(resultPresentation);
	}
}
