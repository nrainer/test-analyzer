package de.tum.in.niedermr.ta.runner.configuration.property.templates;

import java.lang.reflect.Array;

import de.tum.in.niedermr.ta.core.code.util.JavaUtility;
import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;

public abstract class AbstractMultiClassnameProperty<T> extends AbstractMultiStringProperty {
	public final void setValue(Class<?>... classes) {
		StringBuilder sB = new StringBuilder();

		for (Class<?> cls : classes) {
			sB.append(cls.getName());
			sB.append(getSeparator());
		}

		super.setValue(sB.toString());
	}

	public T[] createInstances() throws ReflectiveOperationException {
		String[] elements = getElements();

		@SuppressWarnings("unchecked")
		T[] result = (T[]) Array.newInstance(getRequiredType(), elements.length);

		for (int i = 0; i < elements.length; i++) {
			result[i] = JavaUtility.createInstance(elements[i]);
		}

		return result;
	}

	@Override
	public String getSeparator() {
		return CommonConstants.SEPARATOR_DEFAULT;
	}

	/**
	 * @return class object of a class or an interface
	 */
	protected abstract Class<? extends T> getRequiredType();

	@Override
	protected final void validateFurther() throws ConfigurationException {
		for (String className : getElements()) {
			AbstractClassnameProperty.validateClassName(className, getRequiredType(), this);
		}
	}
}
