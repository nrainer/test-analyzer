package de.tum.in.niedermr.ta.runner.analysis.instrumentation.test.bytecode;

import java.util.Set;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.ClassNode;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.common.constants.AsmConstants;

public class TestModeClassVisitor extends ClassVisitor {
	private final ClassNode m_cn;
	private final String m_className;
	private final Set<MethodIdentifier> m_testcases;

	/** Constructor. */
	public TestModeClassVisitor(ClassNode cn, ClassVisitor cv, Set<MethodIdentifier> testcases) {
		super(AsmConstants.ASM_VERSION, cv);

		m_cn = cn;
		m_className = cn.name;
		m_testcases = testcases;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

		if (m_testcases.contains(MethodIdentifier.create(m_className, name, desc))) {
			return new TestModeMethodVisitor(mv, m_cn, name, desc);
		} else {
			return mv;
		}
	}
}
