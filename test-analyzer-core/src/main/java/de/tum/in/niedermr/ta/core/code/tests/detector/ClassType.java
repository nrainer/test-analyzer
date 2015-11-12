package de.tum.in.niedermr.ta.core.code.tests.detector;

public class ClassType {
	public static final ClassType NO_TEST_CLASS = new ClassType(false);
	public static final ClassType IGNORED_CLASS = new ClassType(false);
	public static final ClassType IGNORED_ABSTRACT_CLASS = new ClassType(false);
	public static final ClassType TEST_CLASS = new ClassType(true);

	private final boolean m_isTestClass;

	protected ClassType(boolean isTestClass) {
		this.m_isTestClass = isTestClass;
	}

	public boolean isTestClass() {
		return m_isTestClass;
	}
}