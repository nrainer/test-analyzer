package de.tum.in.niedermr.ta.runner.execution.args;

import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.core.common.util.StringUtility;

/** Reader to retrieve the arguments array of a program invocation. */
public class ProgramArgsReader extends AbstractProgramArgsManager {

	public ProgramArgsReader(Class<?> programClass, String[] args) {
		super(programClass, args);
	}

	public String getArgument(ProgramArgsKey key) {
		checkProgramArgsKey(key);
		return getArgumentUnsafe(key.getIndex());
	}

	public String getArgument(ProgramArgsKey key, String defaultValue) {
		String value = getArgument(key);

		if (StringUtility.isNullOrEmpty(value)) {
			return defaultValue;
		}

		return value;
	}

	public String getArgumentUnsafe(int index) {
		String value = m_args[index];

		// needed for linux
		value = value.replace(CommonConstants.QUOTATION_MARK, "");

		return value;
	}

	public String toArgsInfoString() {
		return toArgsInfoString(0);
	}

	public String toArgsInfoString(int fromIndex) {
		if (fromIndex >= m_args.length) {
			throw new IllegalArgumentException("fromIndex out of range");
		}

		StringBuilder builder = new StringBuilder();

		for (int i = fromIndex; i < m_args.length; i++) {
			builder.append("[");
			builder.append(i);
			builder.append("] = ");
			builder.append(m_args[i]);
			builder.append(CommonConstants.SEPARATOR_DEFAULT);
			builder.append(" ");
		}

		return builder.toString().trim();
	}
}
