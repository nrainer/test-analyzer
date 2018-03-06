package de.tum.in.niedermr.ta.core.code.tests;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;

public class TestInformation {
	private final MethodIdentifier m_methodUnderTest;
	private final Set<TestcaseIdentifier> m_testcases;

	public TestInformation(MethodIdentifier methodUnderTest) {
		this(methodUnderTest, new HashSet<TestcaseIdentifier>());
	}

	public TestInformation(MethodIdentifier methodUnderTest, Set<TestcaseIdentifier> testcases) {
		this.m_methodUnderTest = methodUnderTest;
		this.m_testcases = testcases;
	}

	public MethodIdentifier getMethodUnderTest() {
		return m_methodUnderTest;
	}

	public Set<TestcaseIdentifier> getTestcases() {
		return Collections.unmodifiableSet(m_testcases);
	}

	public int countTestcases() {
		return getTestcases().size();
	}

	public void addTestcase(TestcaseIdentifier testcase) {
		this.m_testcases.add(testcase);
	}

	public void addTestcase(Class<?> testClass, String testcaseMethod) {
		addTestcase(TestcaseIdentifier.create(testClass, testcaseMethod));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof TestInformation) {
			TestInformation t2 = (TestInformation) obj;

			return Objects.equals(this.m_methodUnderTest, t2.m_methodUnderTest)
					&& Objects.equals(this.m_testcases, t2.m_testcases);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(m_methodUnderTest, m_testcases);
	}
}
