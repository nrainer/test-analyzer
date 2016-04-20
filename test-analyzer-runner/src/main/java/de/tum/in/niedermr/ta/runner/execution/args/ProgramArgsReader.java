package de.tum.in.niedermr.ta.runner.execution.args;

import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;
import de.tum.in.niedermr.ta.core.common.util.StringUtility;

/** Reader to retrieve the arguments array of a program invocation. */
public class ProgramArgsReader extends AbstractProgramArgsManager {

	/** Constructor. */
	public ProgramArgsReader(Class<?> programClass, String[] args) {
		super(programClass, args);
	}

	/**
	 * Get an argument by its key.
	 * 
	 * @throws IllegalArgumentException
	 *             if the value is null or empty
	 */
	public String getArgument(ProgramArgsKey key) {
		return getArgument(key, false);
	}

	/** Get an argument by its key or return {@code defaultValue} if the value is null or empty. */
	public String getArgument(ProgramArgsKey key, String defaultValue) {
		String value = getArgument(key, true);

		if (StringUtility.isNullOrEmpty(value)) {
			return defaultValue;
		}

		return value;
	}

	/**
	 * Get an argument by its key.
	 * 
	 * @throws IllegalArgumentException
	 *             if {@code allowEmpty} is false and the value is null or empty
	 */
	public String getArgument(ProgramArgsKey key, boolean allowEmpty) {
		checkProgramArgsKey(key, !allowEmpty);
		String value = getArgumentUnsafe(key.getIndex());

		if (!allowEmpty && StringUtility.isNullOrEmpty(value)) {
			throw new IllegalArgumentException(key + " is empty.");
		}

		return value;
	}

	/** Get an argument by its index. Returns null if out of argument bounds. Quotation marks are replaced! */
	public String getArgumentUnsafe(int index) {
		if (index >= m_args.length) {
			return null;
		}

		String value = m_args[index];

		if (value != null) {
			// needed for linux
			value = value.replace(CommonConstants.QUOTATION_MARK, "");
		}

		return value;
	}

	/** Get all arguments as readable string. */
	public String toArgsInfoString() {
		return toArgsInfoString(0);
	}

	/** Get the arguments from a given index as readable string. */
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
