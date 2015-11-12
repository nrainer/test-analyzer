package de.tum.in.niedermr.ta.runner.configuration.property;

import de.tum.in.niedermr.ta.runner.analysis.workflow.IWorkflow;
import de.tum.in.niedermr.ta.runner.analysis.workflow.TestWorkflow;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractMultiClassnameProperty;

public class TestWorkflowsProperty extends AbstractMultiClassnameProperty<IWorkflow> {

	@Override
	public String getName() {
		return "testWorkflows";
	}

	@Override
	protected String getDefault() {
		return TestWorkflow.class.getName();
	}

	@Override
	public String getDescription() {
		return "Test workflow to use";
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