package de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.operation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.code.identifier.Identifier;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.core.code.operation.AbstractTestAwareCodeAnalyzeOperation;
import de.tum.in.niedermr.ta.core.code.tests.detector.ClassType;
import de.tum.in.niedermr.ta.core.code.tests.detector.ITestClassDetector;
import de.tum.in.niedermr.ta.core.code.util.BytecodeUtility;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps.InstructionCounterStep.Mode;

public class InstructionCounterOperation<T extends Identifier> extends AbstractTestAwareCodeAnalyzeOperation {
	private final Map<T, Integer> m_result = new HashMap<>();

	private final Mode m_mode;

	public InstructionCounterOperation(ITestClassDetector testClassDetector, Mode mode) {
		super(testClassDetector);
		this.m_mode = mode;
	}

	/** {@inheritDoc} */
	@Override
	protected void analyzeSourceClass(ClassNode cn, String originalClassPath) {
		if (m_mode == Mode.METHOD) {
			analyzeMethods(cn, ClassType.NO_TEST_CLASS);
		}
	}

	/** {@inheritDoc} */
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

		T identifier = createIdentifier(cn, methodNode);
		m_result.put(identifier, BytecodeUtility.countMethodInstructions(methodNode));
	}

	@SuppressWarnings("unchecked")
	private T createIdentifier(ClassNode cn, MethodNode methodNode) {

		switch (m_mode) {
		case METHOD:
			return (T) MethodIdentifier.create(cn.name, methodNode);
		case TESTCASE:
			return (T) TestcaseIdentifier.create(cn.name, methodNode.name);
		default:
			throw new IllegalArgumentException("Unexpected mode: " + m_mode);
		}
	}

	public Map<T, Integer> getResult() {
		return m_result;
	}

	/** {@inheritDoc} */
	@Override
	public void reset() {
		m_result.clear();
	}
}
