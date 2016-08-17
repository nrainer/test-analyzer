package de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.bytecode;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.tests.AssertionInformation;

public class AssertionCounterMethodVisitor extends MethodVisitor {
	private final AssertionInformation m_assertionInformation;
	private int m_countAssertions;

	public AssertionCounterMethodVisitor(AssertionInformation assertionInformation) {
		super(Opcodes.ASM5);

		this.m_assertionInformation = assertionInformation;
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		try {
			analyzeMethodInvocation(owner, name, desc);
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}

	private void analyzeMethodInvocation(String owner, String name, String desc) throws ClassNotFoundException {
		if (m_assertionInformation.isAssertionMethod(MethodIdentifier.create(owner, name, desc)).isAssertion()) {
			m_countAssertions++;
		}
	}

	public int getCountAssertions() {
		return m_countAssertions;
	}

	public void reset() {
		this.m_countAssertions = 0;
	}
}
