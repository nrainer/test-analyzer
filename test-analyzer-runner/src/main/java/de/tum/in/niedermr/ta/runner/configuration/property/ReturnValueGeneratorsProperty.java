package de.tum.in.niedermr.ta.runner.configuration.property;

import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.IReturnValueGenerator;
import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.SimpleReturnValueGeneratorWith0;
import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.SimpleReturnValueGeneratorWith1;
import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.VoidReturnValueGenerator;
import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractMultiClassnameProperty;

public class ReturnValueGeneratorsProperty extends AbstractMultiClassnameProperty<IReturnValueGenerator> {

	public static final String RETURN_VALUE_GENERATORS_VOID_PRIMITIVE_STRING = VoidReturnValueGenerator.class.getName()
			+ CommonConstants.SEPARATOR_DEFAULT + SimpleReturnValueGeneratorWith0.class.getName()
			+ CommonConstants.SEPARATOR_DEFAULT + SimpleReturnValueGeneratorWith1.class.getName();

	@Override
	public String getName() {
		return "returnValueGenerators";
	}

	@Override
	protected String getDefault() {
		return RETURN_VALUE_GENERATORS_VOID_PRIMITIVE_STRING;
	}

	@Override
	protected boolean isEmptyAllowed() {
		return false;
	}

	@Override
	public String getDescription() {
		return "Class name(s) of the return value generator(s)";
	}

	@Override
	protected Class<? extends IReturnValueGenerator> getRequiredType() {
		return IReturnValueGenerator.class;
	}
}