package de.tum.in.niedermr.ta.runner.analysis.instrumentation.test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.operation.AbstractTestAwareCodeModificationOperation;
import de.tum.in.niedermr.ta.core.code.tests.detector.ClassType;
import de.tum.in.niedermr.ta.core.code.tests.detector.ITestClassDetector;
import de.tum.in.niedermr.ta.runner.analysis.instrumentation.test.bytecode.TestModeClassVisitor;

public class TestInstrumentationOperation extends AbstractTestAwareCodeModificationOperation {

	/** Constructor. */
	public TestInstrumentationOperation(ITestClassDetector detector) {
		super(detector);
	}

	/** {@inheritDoc} */
	@Override
	protected void modifyTestClass(ClassReader cr, ClassWriter cw, ClassType classType) {
		ClassNode cn = new ClassNode();
		cr.accept(cn, 0);

		Set<MethodIdentifier> testcases = getTestcases(cn, classType);

		ClassVisitor cv = new TestModeClassVisitor(cn, cw, testcases);
		cr.accept(cv, 0);
	}

	@SuppressWarnings("unchecked")
	private Set<MethodIdentifier> getTestcases(ClassNode cn, ClassType testClassType) {
		Set<MethodIdentifier> result = new HashSet<>();

		for (MethodNode methodNode : (List<MethodNode>) cn.methods) {
			if (getTestClassDetector().analyzeIsTestcase(methodNode, testClassType)) {
				result.add(MethodIdentifier.create(cn.name, methodNode));
			}
		}

		return result;
	}
}
