package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.logic.instrumentation;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import de.tum.in.niedermr.ta.core.code.operation.ICodeModificationOperation;
import de.tum.in.niedermr.ta.core.code.tests.detector.ITestClassDetector;

public class AnalysisInstrumentationOperation implements ICodeModificationOperation {
	private final ITestClassDetector testClassDetector;

	public AnalysisInstrumentationOperation(ITestClassDetector testClassDetector) {
		this.testClassDetector = testClassDetector;
	}

	@Override
	public void modify(ClassReader cr, ClassWriter cw) throws Exception {
		if (!isTestClass(cr)) {
			ClassVisitor cv = new AnalysisInstrumentationClassVisitor(cw, cr.getClassName());
			cr.accept(cv, ClassReader.EXPAND_FRAMES);
		} else {
			ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
				// NOP
			};
			cr.accept(cv, 0);
		}
	}

	private boolean isTestClass(ClassReader cr) {
		ClassNode cn = new ClassNode();
		cr.accept(cn, 0);

		return testClassDetector.analyzeIsTestClass(cn).isTestClass();
	}
}
