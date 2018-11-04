package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.instrumentation;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import de.tum.in.niedermr.ta.core.code.util.JavaUtility;

public class AlreadyInstrumentedMethodVisitor extends MethodVisitor {

	private final String m_visitedClassName;
	private final String m_instrumentationDataRetrieverClassPath;

	/** Constructor. */
	public AlreadyInstrumentedMethodVisitor(MethodVisitor mv, String visitedClassName,
			Class<?> instrumentationDataRetrieverClass) {
		super(Opcodes.ASM5, mv);
		m_visitedClassName = visitedClassName;
		m_instrumentationDataRetrieverClassPath = JavaUtility
				.toClassPathWithoutEnding(instrumentationDataRetrieverClass);
	}

	/** {@inheritDoc} */
	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		super.visitMethodInsn(opcode, owner, name, desc, itf);

		if (opcode == Opcodes.INVOKESTATIC && m_instrumentationDataRetrieverClassPath.equals(owner)) {
			throw new IllegalStateException("Already instrumented: " + m_visitedClassName);
		}
	}
}
