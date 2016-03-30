package de.tum.in.niedermr.ta.runner.configuration.property;

import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;
import de.tum.in.niedermr.ta.core.common.util.FileUtility;
import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.AbstractStringProperty;

public class WorkingFolderProperty extends AbstractStringProperty {

	public static final String WORKING_FOLDER_DEFAULT = "./WorkingArea/default/";

	@Override
	public String getName() {
		return "workingFolder";
	}

	@Override
	protected String getDefault() {
		return WORKING_FOLDER_DEFAULT;
	}

	@Override
	public String getDescription() {
		return "Folder to store the result and the temporary data";
	}

	@Override
	protected void validateFurther() throws ConfigurationException {
		if (getValue().length() > 1) {
			setValue(FileUtility.ensurePathEndsWithPathSeparator(getValue(), FileSystemConstants.PATH_SEPARATOR));
		}
	}
}