package de.tum.in.niedermr.ta.core.code.identifier;

import de.tum.in.niedermr.ta.core.code.constants.BytecodeConstants;
import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;

public final class TestcaseIdentifier implements Identifier {
	public static final String SEPARATOR = CommonConstants.SEPARATOR_DEFAULT;

	private final String m_className;
	private final String m_testcaseName;

	private TestcaseIdentifier(String className, String testcaseName) {
		this.m_className = className;
		this.m_testcaseName = testcaseName;
	}

	public static TestcaseIdentifier create(Class<?> testClass, String testcaseName) {
		return new TestcaseIdentifier(testClass.getName(), testcaseName);
	}

	public static TestcaseIdentifier parse(String s) throws IllegalArgumentException {
		String[] values = s.split(SEPARATOR);

		if (values.length == 2) {
			return new TestcaseIdentifier(values[0], values[1]);
		} else {
			throw new IllegalArgumentException("Expected [className]" + SEPARATOR + "[testcaseName] but was: " + s);
		}
	}

	@Override
	public final String get() {
		return getTestClassName() + SEPARATOR + getTestcaseName();
	}

	public final String getTestClassName() {
		return m_className;
	}

	public final Class<?> resolveTestClass() throws ClassNotFoundException {
		return Class.forName(getTestClassName());
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
		return MethodIdentifier.create(getTestClassName(), getTestcaseName(), BytecodeConstants.DESCRIPTOR_NO_PARAM_AND_VOID);
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && obj instanceof TestcaseIdentifier && get().equals(((TestcaseIdentifier) obj).get());
	}

	@Override
	public int hashCode() {
		return get().hashCode();
	}

	@Override
	public String toString() {
		return get();
	}
}
