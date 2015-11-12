package de.tum.in.niedermr.ta.core.code.tests.detector;

import java.util.Collection;
import java.util.LinkedList;
import java.util.regex.Pattern;

import org.objectweb.asm.tree.ClassNode;

import de.tum.in.niedermr.ta.core.code.util.BytecodeUtility;
import de.tum.in.niedermr.ta.core.code.util.Identification;
import de.tum.in.niedermr.ta.core.common.util.StringUtility;

public abstract class AbstractTestClassDetector implements ITestClassDetector {
	protected final static boolean IGNORE_IGNORED_TEST_CASES = true;

	private final Collection<Pattern> m_ignoredClassNamePatterns;
	private final boolean m_ignoreAbstractClasses;

	public AbstractTestClassDetector(boolean acceptAbstractTestClasses, String... ignoredTestClassRegexes) {
		this.m_ignoreAbstractClasses = !(acceptAbstractTestClasses);
		this.m_ignoredClassNamePatterns = new LinkedList<>();

		for (String item : ignoredTestClassRegexes) {
			if (!StringUtility.isNullOrEmpty(item)) {
				m_ignoredClassNamePatterns.add(Pattern.compile(item));
			}
		}
	}

	@Override
	public final ClassType analyzeIsTestClass(ClassNode cn) {
		if (isClassToBeIgnored(cn.name)) {
			return ClassType.IGNORED_CLASS;
		} else {
			if (m_ignoreAbstractClasses && BytecodeUtility.isAbstractClass(cn)) {
				return ClassType.IGNORED_ABSTRACT_CLASS;
			} else {
				return isTestClassInternal(cn);
			}
		}
	}

	protected abstract ClassType isTestClassInternal(ClassNode cn);

	private boolean isClassToBeIgnored(String classPathOrName) {
		String className = Identification.asClassName(classPathOrName);

		for (Pattern p : m_ignoredClassNamePatterns) {
			if (p.matcher(className).find()) {
				return true;
			}
		}

		return false;
	}
}
