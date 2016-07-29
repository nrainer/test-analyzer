package de.tum.in.niedermr.ta.runner.configuration.property;

import de.tum.in.niedermr.ta.core.analysis.result.presentation.IResultPresentation;
import de.tum.in.niedermr.ta.core.execution.id.IExecutionId;
import de.tum.in.niedermr.ta.runner.analysis.result.presentation.ResultPresentationUtil;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractClassnameProperty;

public class ResultPresentationProperty extends AbstractClassnameProperty<IResultPresentation> {
	public static final String RESULT_PRESENTATION_TEXT = "TEXT";
	public static final String RESULT_PRESENTATION_DB = "DB";

	@Override
	public String getName() {
		return "resultPresentation";
	}

	@Override
	protected String getDefault() {
		return RESULT_PRESENTATION_DB;
	}

	@Override
	public String getDescription() {
		return "Result presentation";
	}

	@Override
	protected String[] furtherAllowedConstants() {
		return new String[] { RESULT_PRESENTATION_DB, RESULT_PRESENTATION_TEXT };
	}

	@Override
	protected IResultPresentation createInstanceFromConstant(String value) throws ReflectiveOperationException {
		return ResultPresentationUtil.createResultPresentationWithoutExecutionId(value);
	}

	/** Create an instance and set the execution id. */
	public IResultPresentation createInstance(IExecutionId executionId) throws ReflectiveOperationException {
		IResultPresentation resultPresentation = super.createInstance();
		resultPresentation.setExecutionId(executionId);
		return resultPresentation;
	}

	@Override
	protected Class<? extends IResultPresentation> getRequiredType() {
		return IResultPresentation.class;
	}
}