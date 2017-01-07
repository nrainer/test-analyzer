package de.tum.in.niedermr.ta.core.code.tests.detector;

/** Class type: application class, test class, or irrelevant class */
public class ClassType {

	/** Not a test class / application class */
	public static final ClassType NO_TEST_CLASS = new ClassType(false, true);

	/** Ignored test class (due to include / exclude patterns). */
	public static final ClassType IGNORED_TEST_CLASS = new ClassType(false, false);

	/**
	 * Abstract test class detected by a test class detector configured to
	 * ignore these.
	 */
	public static final ClassType IGNORED_ABSTRACT_TEST_CLASS = new ClassType(false, false);

	/** Test class. */
	public static final ClassType TEST_CLASS = new ClassType(true, false);

	/** Is considered as a test class. */
	private final boolean m_isTestClass;

	/** Is considered as a source class. */
	private final boolean m_isSourceClass;

	/** Constructor. */
	protected ClassType(boolean isTestClass, boolean isSourceClass) {
		m_isTestClass = isTestClass;
		m_isSourceClass = isSourceClass;
	}

	/** {@link #m_isTestClass} */
	public boolean isTestClass() {
		return m_isTestClass;
	}

	/** {@link #m_isSourceClass} */
	public boolean isSourceClass() {
		return m_isSourceClass;
	}
}