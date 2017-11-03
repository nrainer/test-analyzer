package de.tum.in.niedermr.ta.runner.analysis.result.presentation;

import de.tum.in.niedermr.ta.core.analysis.result.presentation.IResultPresentation;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;
import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.runner.configuration.property.ResultPresentationProperty;

/** Utility class to create result presentation instances. */
public class ResultPresentationUtil {

	/** Create the appropriate instance of {@link IResultPresentation} and set the execution id. */
	public static IResultPresentation createResultPresentation(String resultPresentation, IExecutionId executionId,
			ClassLoader classLoader) throws ReflectiveOperationException {
		IResultPresentation presentation = createResultPresentationWithoutExecutionId(resultPresentation, classLoader);
		presentation.setExecutionId(executionId);
		return presentation;
	}

	/** Create the appropriate instance of {@link IResultPresentation}. The execution id is not set. */
	public static IResultPresentation createResultPresentationWithoutExecutionId(String resultPresentation,
			ClassLoader classLoader) throws ReflectiveOperationException {
		if (resultPresentation.equals(ResultPresentationProperty.RESULT_PRESENTATION_TEXT)) {
			return new TextResultPresentation();
		} else if (resultPresentation.equals(ResultPresentationProperty.RESULT_PRESENTATION_DB)) {
			return new DatabaseResultPresentation();
		}

		return JavaUtility.createInstance(resultPresentation, classLoader);
	}
}
