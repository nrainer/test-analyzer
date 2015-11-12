package de.tum.in.niedermr.ta.runner.configuration.property.templates;

import java.util.ArrayList;
import java.util.List;

import de.tum.in.niedermr.ta.core.common.util.StringUtility;

public abstract class AbstractMultiStringProperty extends AbstractStringProperty {
	public final String[] getElements() {
		List<String> elementList = new ArrayList<>();

		for (String element : getValue().split(getSeparator())) {
			String elementValue = element.trim();

			if (!StringUtility.isNullOrEmpty(elementValue)) {
				elementList.add(elementValue);
			}
		}

		return elementList.toArray(new String[0]);
	}

	public void setValue(String[] elements) {
		setValue(StringUtility.join(elements, getSeparator()));
	}

	public final int countElements() {
		return getElements().length;
	}

	@Override
	protected String getDefault() {
		return EMPTY_STRING;
	}

	public abstract String getSeparator();

	public final String getWithAlternativeSeparator(String alternativeSeparator) {
		return getValue().replace(getSeparator(), alternativeSeparator);
	}
}
