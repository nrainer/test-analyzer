package de.tum.in.niedermr.ta.runner.configuration.property;

import de.tum.in.niedermr.ta.core.analysis.filter.IMethodFilter;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractMultiClassnameProperty;

public class MethodFiltersProperty extends AbstractMultiClassnameProperty<IMethodFilter> {

	/** {@inheritDoc} */
	@Override
	public String getName() {
		return "methodFilters";
	}

	/** {@inheritDoc} */
	@Override
	public String getDescription() {
		return "Method filters";
	}

	/** {@inheritDoc} */
	@Override
	protected Class<? extends IMethodFilter> getRequiredType() {
		return IMethodFilter.class;
	}
}