package de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.operation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.operation.AbstractTestAwareCodeAnalyzeOperation;
import de.tum.in.niedermr.ta.core.code.tests.detector.ClassType;
import de.tum.in.niedermr.ta.core.code.tests.detector.ITestClassDetector;
import de.tum.in.niedermr.ta.core.code.util.BytecodeUtility;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps.InstructionCounterStep.Mode;

public class InstructionCounterOperation extends AbstractTestAwareCodeAnalyzeOperation {
	private final Map<MethodIdentifier, Integer> m_result = new HashMap<>();

	private final Mode m_mode;

	public InstructionCounterOperation(ITestClassDetector testClassDetector, Mode mode) {
		super(testClassDetector);
		this.m_mode = mode;
	}

	@Override
	protected void analyzeSourceClass(ClassNode cn, String originalClassPath) {
		if (m_mode == Mode.METHOD) {
			analyzeMethods(cn, ClassType.NO_TEST_CLASS);
		}
	}

	@Override
	protected void analyzeTestClass(ClassNode cn, String originalClassPath, ClassType classType) {
		if (m_mode == Mode.TESTCASE) {
			analyzeMethods(cn, classType);
		}
	}

	@SuppressWarnings("unchecked")
	private void analyzeMethods(ClassNode cn, ClassType testClassType) {
		for (MethodNode methodNode : (List<MethodNode>) cn.methods) {
			analyzeMethod(cn, testClassType, methodNode);
		}
	}

	private void analyzeMethod(ClassNode cn, ClassType testClassType, MethodNode methodNode) {
		if (BytecodeUtility.isConstructor(methodNode) || BytecodeUtility.isAbstractMethod(methodNode)) {
			return;
		}

		if (m_mode == Mode.TESTCASE && (!getTestClassDetector().analyzeIsTestcase(methodNode, testClassType))) {
			return;
		}

		m_result.put(MethodIdentifier.create(cn.name, methodNode), BytecodeUtility.countMethodInstructions(methodNode));
	}

	public Map<MethodIdentifier, Integer> getResult() {
		return m_result;
	}
}
