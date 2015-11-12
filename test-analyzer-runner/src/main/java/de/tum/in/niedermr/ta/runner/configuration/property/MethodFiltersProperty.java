package de.tum.in.niedermr.ta.runner.configuration.property;

import de.tum.in.niedermr.ta.core.analysis.filter.IMethodFilter;
import de.tum.in.niedermr.ta.core.analysis.filter.advanced.SetterGetterFilter;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractMultiClassnameProperty;

public class MethodFiltersProperty extends AbstractMultiClassnameProperty<IMethodFilter> {

	@Override
	public String getName() {
		return "methodFilters";
	}

	@Override
	protected String getDefault() {
		return SetterGetterFilter.class.getName();
	}

	@Override
	public String getDescription() {
		return "Method filters";
	}

	@Override
	protected Class<? extends IMethodFilter> getRequiredType() {
		return IMethodFilter.class;
	}
}