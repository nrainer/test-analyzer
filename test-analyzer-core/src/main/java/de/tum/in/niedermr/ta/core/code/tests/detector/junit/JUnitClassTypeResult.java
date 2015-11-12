package de.tum.in.niedermr.ta.core.code.tests.detector.junit;

import de.tum.in.niedermr.ta.core.code.tests.detector.ClassType;

public class JUnitClassTypeResult extends ClassType {
	public static final ClassType TEST_CLASS_JUNIT_3 = new JUnitClassTypeResult(3, false);
	public static final ClassType TEST_CLASS_JUNIT_4 = new JUnitClassTypeResult(4, false);
	public static final ClassType TEST_SUITE_JUNIT_3 = new JUnitClassTypeResult(3, true);
	public static final ClassType TEST_SUITE_JUNIT_4 = new JUnitClassTypeResult(4, true);

	private final int m_jUnitVersion;
	private final boolean m_isSuite;

	private JUnitClassTypeResult(int jUnitVersion, boolean isSuite) {
		super(true);
		this.m_jUnitVersion = jUnitVersion;
		this.m_isSuite = isSuite;
	}

	public int getJUnitVersion() {
		return m_jUnitVersion;
	}

	public boolean isSuite() {
		return m_isSuite;
	}
}