package de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.bytecode;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.tests.assertions.AssertionInformation;

public class AssertionCounterMethodVisitor extends MethodVisitor {
	private final AssertionInformation assertionInformation;
	private int countAssertions;

	public AssertionCounterMethodVisitor(AssertionInformation assertionInformation) {
		super(Opcodes.ASM5);

		this.assertionInformation = assertionInformation;
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
		if (assertionInformation.isAssertionMethod(MethodIdentifier.create(owner, name, desc)).m_isAssertion) {
			countAssertions++;
		}
	}

	public int getCountAssertions() {
		return countAssertions;
	}

	public void reset() {
		this.countAssertions = 0;
	}
}
