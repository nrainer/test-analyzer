package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.instrumentation;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;
import de.tum.in.niedermr.ta.core.code.visitor.AbstractTryFinallyMethodVisitor;

public class AnalysisInstrumentationMethodVisitor extends AbstractTryFinallyMethodVisitor {
	private final MethodIdentifier m_identifier;

	/**
	 * Path of the class that records the data gathered from the instrumentation.
	 */
	private final String m_instrumentationDataRetrieverClassPath;

	/** Constructor. */
	public AnalysisInstrumentationMethodVisitor(MethodVisitor mv, ClassNode cn, String className, String methodName,
			String desc, Class<?> instrumentationDataRetrieverClass) {
		super(Opcodes.ASM5, mv, cn, methodName, desc);

		m_instrumentationDataRetrieverClassPath = JavaUtility
				.toClassPathWithoutEnding(instrumentationDataRetrieverClass);
		m_identifier = MethodIdentifier.create(className, methodName, desc);
	}

	/** {@inheritDoc} */
	@Override
	protected void execVisitCodeBeforeFirstTryCatchBlock() {
		visitLdcInsn(m_identifier.get());
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, m_instrumentationDataRetrieverClassPath, "pushInvocation",
				"(Ljava/lang/String;)V", false);
	}

	/** {@inheritDoc} */
	@Override
	protected void execVisitCodeInFinallyBlock() {
		visitLdcInsn(m_identifier.get());
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, m_instrumentationDataRetrieverClassPath, "popInvocation",
				"(Ljava/lang/String;)V", false);
	}
}
