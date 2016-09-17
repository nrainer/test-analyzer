package de.tum.in.niedermr.ta.core.code.tests.collector;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.code.tests.detector.ClassType;
import de.tum.in.niedermr.ta.core.code.tests.detector.ITestClassDetector;
import de.tum.in.niedermr.ta.core.code.util.BytecodeUtility;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;

public class TestCollector implements ITestCollector {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(TestCollector.class);

	protected final Map<Class<?>, Set<String>> m_result;
	protected final ITestClassDetector m_testClassDetector;

	public TestCollector(ITestClassDetector testClassDetector) {
		this.m_result = new HashMap<>();
		this.m_testClassDetector = testClassDetector;
	}

	public boolean collectTestcasesInNonAbstractSuperClasses() {
		return false;
	}

	public boolean collectTestcasesInAbstractSuperClasses() {
		return true;
	}

	@Override
	public void analyze(ClassReader cr, String originalClassPath) throws IOException, ClassNotFoundException {
		ClassNode cn = new ClassNode();
		cr.accept(cn, 0);

		ClassType testClassType = m_testClassDetector.analyzeIsTestClass(cn);

		if (testClassType.isTestClass()) {
			Set<String> testcases = collectTestcases(cn, testClassType);

			if (!testcases.isEmpty()) {
				Class<?> cls = Class.forName(JavaUtility.toClassName(originalClassPath));
				m_result.put(cls, testcases);
			}
		}
	}

	protected final Set<String> collectTestcases(ClassNode cn, ClassType testClassType) {
		Set<String> testcases = new HashSet<>();

		testcases.addAll(collectTestcasesInThisClass(cn, testClassType));

		if ((collectTestcasesInNonAbstractSuperClasses() || collectTestcasesInAbstractSuperClasses())
				&& JavaUtility.hasSuperClassOtherThanObject(cn)) {
			testcases.addAll(collectTestcasesInSuperClasses(cn.superName, testClassType));
		}

		return testcases;
	}

	@SuppressWarnings("unchecked")
	protected Set<String> collectTestcasesInThisClass(ClassNode cn, ClassType testClassType) {
		Set<String> testcases = new HashSet<>();

		for (MethodNode meth : (List<MethodNode>) cn.methods) {
			if (m_testClassDetector.analyzeIsTestcase(meth, testClassType)) {
				testcases.add(meth.name);
			}
		}

		return testcases;
	}

	protected Set<String> collectTestcasesInSuperClasses(String superClassName, ClassType testClassType) {
		Set<String> testcases = new HashSet<>();

		try {
			ClassReader crSuper = new ClassReader(superClassName);
			ClassNode cnSuper = new ClassNode();

			crSuper.accept(cnSuper, 0);

			boolean isAbstract = BytecodeUtility.isAbstractClass(cnSuper);

			if ((isAbstract && collectTestcasesInAbstractSuperClasses())
					|| (!isAbstract && collectTestcasesInNonAbstractSuperClasses())) {
				testcases.addAll(collectTestcasesInThisClass(cnSuper, testClassType));
			}

			if (JavaUtility.hasSuperClassOtherThanObject(cnSuper)) {
				testcases.addAll(collectTestcasesInSuperClasses(cnSuper.superName, testClassType));
			}
		} catch (IOException e) {
			LOGGER.error("Exception in collectTestcasesInSuperClasses", e);
		}

		return testcases;
	}

	@Override
	public Collection<Class<?>> getTestClasses() {
		return m_result.keySet();
	}

	@Override
	public Map<Class<?>, Set<String>> getTestClassesWithTestcases() {
		return m_result;
	}

	@Override
	public ITestClassDetector getTestClassDetector() {
		return m_testClassDetector;
	}
}
