package de.tum.in.niedermr.ta.runner.configuration.property.templates;

import java.io.File;

import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;
import de.tum.in.niedermr.ta.core.common.util.StringUtility;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.execution.environment.Environment;

public abstract class AbstractClasspathProperty extends AbstractMultiStringProperty implements FileSystemConstants {
	@Override
	public String getSeparator() {
		return CP_SEP;
	}

	@Override
	protected String modifyValueToBeSet(String value) {
		String result = value;
		result = ensureEndsWithSeparatorIfNotEmpty(result);
		result = result.replace(CP_SEP + CP_SEP, CP_SEP);

		return result;
	}

	protected boolean elementsMustExistWhenValidating() {
		return true;
	}

	@Override
	protected final void validateFurther() throws ConfigurationException {
		if (elementsMustExistWhenValidating()) {
			checkElementsExist();
		}
	}

	protected void checkElementsExist() throws ConfigurationException {
		for (String element : getElements()) {
			File file = new File(Environment.getPathWithoutWildcard(element));

			if (!file.exists()) {
				throw new ConfigurationException(this, "The file '" + element
						+ "' does not exist. (Assumed absolute path: '" + file.getAbsolutePath() + "')");
			}
		}
	}

	private String ensureEndsWithSeparatorIfNotEmpty(String value) {
		if (StringUtility.isNullOrEmpty(value) || value.endsWith(getSeparator())) {
			return value;
		} else {
			return value + getSeparator();
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <b>Replaces the windows classpath separator (';')</b> with the separator of the current OS. However, it <b>does
	 * NOT replace the linux classpath separator (':')</b> with the separator of the current OS because this would lead
	 * to problems under windows in absolute paths such as "E:/bin/"!
	 */
	@Override
	protected String parseValue(String value) {
		String cp = value;
		cp = cp.replace(CLASSPATH_SEPARATOR_WINDOWS, CP_SEP);

		return cp;
	}
}
