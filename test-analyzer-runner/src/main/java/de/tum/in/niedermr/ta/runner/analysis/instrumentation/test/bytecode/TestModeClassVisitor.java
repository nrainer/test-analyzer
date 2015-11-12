package de.tum.in.niedermr.ta.runner.analysis.instrumentation.test.bytecode;

import java.util.Set;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;

public class TestModeClassVisitor extends ClassVisitor {
	private final String m_className;
	private final Set<MethodIdentifier> m_testcases;

	public TestModeClassVisitor(String className, ClassVisitor cv, Set<MethodIdentifier> testcases) {
		super(Opcodes.ASM5, cv);

		this.m_className = className;
		this.m_testcases = testcases;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

		if (m_testcases.contains(MethodIdentifier.create(m_className, name, desc))) {
			return new TestModeMethodVisitor(mv);
		} else {
			return mv;
		}
	}
}
