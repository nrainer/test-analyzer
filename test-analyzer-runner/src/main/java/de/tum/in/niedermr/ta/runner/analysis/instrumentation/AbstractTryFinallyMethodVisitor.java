package de.tum.in.niedermr.ta.runner.analysis.instrumentation;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import de.tum.in.niedermr.ta.core.code.util.OpcodesUtility;

/**
 * <b>Must be used with <code>ClassWriter.COMPUTE_FRAMES</code>.</b>
 * 
 * <br/>
 * <b>Methods which only contain paths which throw an exception (only exit path is an <code>ATHROW</code>) will not
 * visit the exit code</b> for the following reason: This visitor not add a try-catch-block to on occurrences of
 * <code>ATHROW</code> because they could cause the multiple execution of the code on the method exit (if an exception
 * is catched and rethrown) and will cause problems when rethrowing exceptions from catch blocks in synchronized blocks.
 */
public abstract class AbstractTryFinallyMethodVisitor extends MethodVisitor implements Opcodes {
	private Label m_currentBeginLabel;
	private boolean m_inOriginalCode;

	public AbstractTryFinallyMethodVisitor(int version, MethodVisitor mv) {
		super(version, mv);
		m_inOriginalCode = true;
	}

	@Override
	public void visitCode() {
		try {
			m_inOriginalCode = false;

			execVisitBeforeFirstTryCatchBlock();
			beginTryBlock();
		} finally {
			m_inOriginalCode = true;
		}
	}

	@Override
	public void visitInsn(int opcode) {
		/*
		 * Do not include ATHROW (see class comment)!
		 */
		if (m_inOriginalCode && OpcodesUtility.isXRETURN(opcode)) {
			try {
				m_inOriginalCode = false;
				completeTryFinallyBlock();

				// visit the return or throw instruction
				visitInsn(opcode);

				// begin the next try-block (it will not be added until it has been completed)
				beginTryBlock();
			} finally {
				m_inOriginalCode = true;
			}
		} else {
			super.visitInsn(opcode);
		}
	}

	protected void beginTryBlock() {
		m_currentBeginLabel = new Label();
		visitLabel(m_currentBeginLabel);
		execVisitTryBlockBegin();
	}

	protected void completeTryFinallyBlock() {
		Label endLabel = new Label();
		Label handlerLabel = endLabel;

		visitTryCatchBlock(m_currentBeginLabel, endLabel, handlerLabel, null);
		Label l2 = new Label();
		visitJumpInsn(GOTO, l2);
		visitLabel(handlerLabel);
		visitVarInsn(ASTORE, 1);

		execVisitFinallyBlock();

		visitVarInsn(ALOAD, 1);
		visitInsn(ATHROW);
		visitLabel(l2);

		execVisitFinallyBlock();
	}

	protected void execVisitTryBlockBegin() {
		// NOP
	}

	protected void execVisitBeforeFirstTryCatchBlock() {
		// NOP
	}

	protected void execVisitFinallyBlock() {
		// NOP
	}
}
