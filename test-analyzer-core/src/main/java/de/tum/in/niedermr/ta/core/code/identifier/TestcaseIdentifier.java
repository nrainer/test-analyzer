package de.tum.in.niedermr.ta.core.code.identifier;

import de.tum.in.niedermr.ta.core.code.constants.BytecodeConstants;
import de.tum.in.niedermr.ta.core.code.constants.JavaConstants;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;
import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;

/** Identifier for Java test case methods. */
public final class TestcaseIdentifier implements Identifier {
	public static final String SEPARATOR = CommonConstants.SEPARATOR_DEFAULT;

	private final String m_className;
	private final String m_testcaseName;

	/** Constructor. */
	private TestcaseIdentifier(String className, String testcaseName) {
		this.m_className = className;
		this.m_testcaseName = testcaseName;
	}

	/** Create an identifier for a test case. */
	public static TestcaseIdentifier create(Class<?> testClass, String testcaseName) {
		return create(testClass.getName(), testcaseName);
	}

	/** Create an identifier for a test case. */
	public static TestcaseIdentifier create(String testClassName, String testcaseName) {
		return new TestcaseIdentifier(testClassName, testcaseName);
	}

	/** Create an identifier for a test case. */
	public static TestcaseIdentifier createFromJavaName(String qualifiedMethodName) {
		if (qualifiedMethodName.contains(JavaConstants.ARGUMENTS_BEGIN)) {
			qualifiedMethodName = qualifiedMethodName.substring(0,
					qualifiedMethodName.indexOf(JavaConstants.ARGUMENTS_BEGIN));
		}

		if (!qualifiedMethodName.contains(JavaConstants.CLASS_METHOD_SEPARATOR)) {
			throw new IllegalArgumentException("Expected at least one occurence of "
					+ JavaConstants.CLASS_METHOD_SEPARATOR + " in: " + qualifiedMethodName);
		}

		String className = qualifiedMethodName.substring(0,
				qualifiedMethodName.lastIndexOf(JavaConstants.CLASS_METHOD_SEPARATOR));
		String methodName = qualifiedMethodName
				.substring(1 + qualifiedMethodName.lastIndexOf(JavaConstants.CLASS_METHOD_SEPARATOR));

		return create(className, methodName);
	}

	/** Parse an identifier (with {@value #SEPARATOR} as separator). */
	public static TestcaseIdentifier parse(String s) throws IllegalArgumentException {
		String[] values = s.split(SEPARATOR);

		if (values.length == 2) {
			return new TestcaseIdentifier(values[0], values[1]);
		} else {
			throw new IllegalArgumentException("Expected [className]" + SEPARATOR + "[testcaseName] but was: " + s);
		}
	}

	/** {@inheritDoc} */
	@Override
	public final String get() {
		return getTestClassName() + SEPARATOR + getTestcaseName();
	}

	public final String getTestClassName() {
		return m_className;
	}

	public final Class<?> resolveTestClass() throws ClassNotFoundException {
		return JavaUtility.loadClass(getTestClassName());
	}

	public final Class<?> resolveTestClassNoEx() {
		try {
			return resolveTestClass();
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	public final String getTestcaseName() {
		return m_testcaseName;
	}

	public MethodIdentifier toMethodIdentifier() {
		return MethodIdentifier.create(getTestClassName(), getTestcaseName(),
				BytecodeConstants.DESCRIPTOR_NO_PARAM_AND_VOID);
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		return obj != null && obj instanceof TestcaseIdentifier && get().equals(((TestcaseIdentifier) obj).get());
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return get().hashCode();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return get();
	}
}
