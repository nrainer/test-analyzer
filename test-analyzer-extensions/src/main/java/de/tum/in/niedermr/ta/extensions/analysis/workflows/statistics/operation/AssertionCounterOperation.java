package de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.operation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;
import de.tum.in.niedermr.ta.core.code.operation.AbstractTestAwareCodeAnalyzeOperation;
import de.tum.in.niedermr.ta.core.code.tests.detector.ClassType;
import de.tum.in.niedermr.ta.core.code.tests.detector.ITestClassDetector;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.bytecode.AssertionCounterMethodVisitor;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.tests.AssertionInformation;

public class AssertionCounterOperation extends AbstractTestAwareCodeAnalyzeOperation {
	private final Map<TestcaseIdentifier, Integer> m_assertionsPerTestcase;
	private final AssertionCounterMethodVisitor m_methodVisitor;

	public AssertionCounterOperation(ITestClassDetector testClassDetector, AssertionInformation assertionInformation) {
		super(testClassDetector);
		this.m_assertionsPerTestcase = new HashMap<>();
		this.m_methodVisitor = new AssertionCounterMethodVisitor(assertionInformation);
	}

	@Override
	protected void analyzeSourceClass(ClassNode cn, String originalClassPath) {
		// NOP
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void analyzeTestClass(ClassNode cn, String originalClassPath, ClassType testClassType) {
		for (MethodNode methodNode : (List<MethodNode>) cn.methods) {
			if (getTestClassDetector().analyzeIsTestcase(methodNode, testClassType)) {
				analyzeTestCase(cn, methodNode);
			}
		}
	}

	private void analyzeTestCase(ClassNode cn, MethodNode methodNode) {
		m_methodVisitor.reset();

		methodNode.accept(m_methodVisitor);

		TestcaseIdentifier identifier = TestcaseIdentifier.create(cn.name, methodNode.name);
		m_assertionsPerTestcase.put(identifier, m_methodVisitor.getCountAssertions());
	}

	public Map<TestcaseIdentifier, Integer> getAssertionsPerTestcase() {
		return m_assertionsPerTestcase;
	}
}
