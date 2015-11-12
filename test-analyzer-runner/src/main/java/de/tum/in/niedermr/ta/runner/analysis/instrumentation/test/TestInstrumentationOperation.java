package de.tum.in.niedermr.ta.runner.analysis.instrumentation.test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.operation.ICodeModificationOperation;
import de.tum.in.niedermr.ta.core.code.tests.detector.ClassType;
import de.tum.in.niedermr.ta.core.code.tests.detector.ITestClassDetector;
import de.tum.in.niedermr.ta.runner.analysis.instrumentation.test.bytecode.TestModeClassVisitor;

public class TestInstrumentationOperation implements ICodeModificationOperation {
	private ITestClassDetector m_testClassDetector;

	public TestInstrumentationOperation(ITestClassDetector detector) {
		this.m_testClassDetector = detector;
	}

	@Override
	public void modify(ClassReader cr, ClassWriter cw) throws Exception {
		ClassNode cn = new ClassNode();
		cr.accept(cn, 0);

		ClassType testClassType = m_testClassDetector.analyzeIsTestClass(cn);

		ClassVisitor cv;

		if (testClassType.isTestClass()) {
			Set<MethodIdentifier> testcases = getTestcases(cn, testClassType);

			cv = new TestModeClassVisitor(cn.name, cw, testcases);
			cr.accept(cv, 0);
		} else {
			cv = new ClassVisitor(Opcodes.ASM5, cw) {
				// NOP
			};
			cr.accept(cv, 0);
		}
	}

	@SuppressWarnings("unchecked")
	private Set<MethodIdentifier> getTestcases(ClassNode cn, ClassType testClassType) {
		Set<MethodIdentifier> result = new HashSet<>();

		for (MethodNode methodNode : (List<MethodNode>) cn.methods) {
			if (m_testClassDetector.analyzeIsTestcase(methodNode, testClassType)) {
				result.add(MethodIdentifier.create(cn.name, methodNode));
			}
		}

		return result;
	}
}
