package de.tum.in.niedermr.ta.core.analysis.jars.operation;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import de.tum.in.niedermr.ta.core.code.operation.ICodeAnalyzeOperation;
import de.tum.in.niedermr.ta.core.code.tests.detector.ClassType;
import de.tum.in.niedermr.ta.core.code.tests.detector.ITestClassDetector;

public abstract class AbstractCodeAnalyzeOperation implements ICodeAnalyzeOperation {
	protected final ITestClassDetector m_testClassDetector;

	public AbstractCodeAnalyzeOperation(ITestClassDetector testClassDetector) {
		this.m_testClassDetector = testClassDetector;
	}

	@Override
	public final void analyze(ClassReader cr, String originalClassPath) throws Exception {
		ClassNode cn = new ClassNode();
		cr.accept(cn, 0);

		ClassType classType;

		if (m_testClassDetector != null) {
			classType = m_testClassDetector.analyzeIsTestClass(cn);
		} else {
			classType = ClassType.NO_TEST_CLASS;
		}

		if (classType.isTestClass()) {
			analyzeTestClass(cn, originalClassPath, classType);
		} else {
			analyzeSourceClass(cn, originalClassPath);
		}
	}

	protected abstract void analyzeSourceClass(ClassNode cn, String originalClassPath);

	protected abstract void analyzeTestClass(ClassNode cn, String originalClassPath, ClassType testClassType);
}
