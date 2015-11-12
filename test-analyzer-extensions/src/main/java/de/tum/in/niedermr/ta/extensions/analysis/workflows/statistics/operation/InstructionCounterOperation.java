package de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.operation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.analysis.jars.operation.AbstractCodeAnalyzeOperation;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.tests.detector.ClassType;
import de.tum.in.niedermr.ta.core.code.tests.detector.ITestClassDetector;
import de.tum.in.niedermr.ta.core.code.util.BytecodeUtility;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps.InstructionCounterStep.Mode;

public class InstructionCounterOperation extends AbstractCodeAnalyzeOperation {
	private final Map<MethodIdentifier, Integer> result = new HashMap<>();

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
			if (!(BytecodeUtility.isConstructor(methodNode) || BytecodeUtility.isAbstractMethod(methodNode))) {
				if (m_mode == Mode.TESTCASE && (!m_testClassDetector.analyzeIsTestcase(methodNode, testClassType))) {
					// skip non-test methods in test classes
					continue;
				}

				result.put(MethodIdentifier.create(cn.name, methodNode), BytecodeUtility.countMethodInstructions(methodNode));
			}
		}
	}

	public Map<MethodIdentifier, Integer> getResult() {
		return result;
	}

	public void reset() {
		result.clear();
	}
}
