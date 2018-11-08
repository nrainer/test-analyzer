package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.instrumentation;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import de.tum.in.niedermr.ta.core.code.util.JavaUtility;
import de.tum.in.niedermr.ta.core.common.constants.AsmConstants;

public class AlreadyInstrumentedMethodVisitor extends MethodVisitor {

	private final String m_instrumentationDataRetrieverClassPath;
	private AlreadyInstrumentedHandler m_alreadyInstrumentedHandler;

	/** Constructor. */
	public AlreadyInstrumentedMethodVisitor(MethodVisitor mv, Class<?> instrumentationDataRetrieverClass,
			AlreadyInstrumentedHandler alreadyInstrumentedHandler) {
		super(AsmConstants.ASM_VERSION, mv);
		m_alreadyInstrumentedHandler = alreadyInstrumentedHandler;
		m_instrumentationDataRetrieverClassPath = JavaUtility
				.toClassPathWithoutEnding(instrumentationDataRetrieverClass);
	}

	/** {@inheritDoc} */
	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		super.visitMethodInsn(opcode, owner, name, desc, itf);

		if (opcode == Opcodes.INVOKESTATIC && m_instrumentationDataRetrieverClassPath.equals(owner)) {
			m_alreadyInstrumentedHandler.execOnAlreadyInstrumented();
		}
	}

	public interface AlreadyInstrumentedHandler {
		void execOnAlreadyInstrumented();
	}
}
