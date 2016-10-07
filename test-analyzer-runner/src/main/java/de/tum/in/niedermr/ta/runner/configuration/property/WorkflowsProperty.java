package de.tum.in.niedermr.ta.runner.configuration.property;

import de.tum.in.niedermr.ta.runner.analysis.workflow.IWorkflow;
import de.tum.in.niedermr.ta.runner.analysis.workflow.TestWorkflow;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractMultiClassnameProperty;

public class WorkflowsProperty extends AbstractMultiClassnameProperty<IWorkflow> {

	@Override
	public String getName() {
		return "workflows";
	}

	@Override
	protected String getDefault() {
		return TestWorkflow.class.getName();
	}

	@Override
	public String getDescription() {
		return "Workflow to use";
	}

	@Override
	protected Class<? extends IWorkflow> getRequiredType() {
		return IWorkflow.class;
	}

	@Override
	protected boolean isEmptyAllowed() {
		return false;
	}
}