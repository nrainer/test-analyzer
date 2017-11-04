package de.tum.in.niedermr.ta.core.code.visitor;

import java.util.List;
import java.util.ListIterator;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.code.util.OpcodesUtility;

/**
 * <b>Must be used with <code>ClassWriter.COMPUTE_FRAMES</code>.</b>
 * 
 * <br/>
 * <b>Methods which only contain paths which throw an exception (only exit path is an <code>ATHROW</code>) need a
 * special handling: Do not add a try-catch-block to <code>ATHROW</code> because they may cause the multiple execution
 * of the code on the method exit (if an exception is catched and rethrown) and will cause problems when rethrowing
 * exceptions from catch blocks in synchronized blocks.
 */
public abstract class AbstractTryFinallyMethodVisitor extends MethodVisitor implements Opcodes {
	private final ClassNode m_cn;
	private final String m_methodName;
	private final String m_desc;

	private Label m_currentBeginLabel;
	private boolean m_inOriginalCode;

	public AbstractTryFinallyMethodVisitor(int version, MethodVisitor mv, ClassNode cn, String methodName,
			String desc) {
		super(version, mv);
		m_cn = cn;
		m_methodName = methodName;
		m_desc = desc;
		m_inOriginalCode = true;
	}

	/** {@inheritDoc} */
	@Override
	public void visitCode() {
		try {
			m_inOriginalCode = false;

			execVisitCodeBeforeFirstTryCatchBlock();

			if (hasOnlyAThrowExits()) {
				execVisitCodeInFinallyBlock();
			} else {
				beginTryBlock();
			}

		} finally {
			m_inOriginalCode = true;
		}
	}

	/** {@inheritDoc} */
	@Override
	public void visitInsn(int opcode) {
		// Do not include ATHROWs.

		if (m_inOriginalCode && OpcodesUtility.isXRETURN(opcode)) {
			// complete a try block for each return statement and open a new one

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
	}

	protected void completeTryFinallyBlock() {
		Label endLabel = new Label();
		Label handlerLabel = endLabel;

		visitTryCatchBlock(m_currentBeginLabel, endLabel, handlerLabel, null);
		Label l2 = new Label();
		visitJumpInsn(GOTO, l2);
		visitLabel(handlerLabel);
		visitVarInsn(ASTORE, 1);

		execVisitCodeInFinallyBlock();

		visitVarInsn(ALOAD, 1);
		visitInsn(ATHROW);
		visitLabel(l2);

		execVisitCodeInFinallyBlock();
	}

	protected void execVisitCodeBeforeFirstTryCatchBlock() {
		// NOP
	}

	protected void execVisitCodeInFinallyBlock() {
		// NOP
	}

	@SuppressWarnings("unchecked")
	private boolean hasOnlyAThrowExits() {
		MethodNode correspondingMethodNode = null;

		for (MethodNode methodNode : (List<MethodNode>) m_cn.methods) {
			if (matches(methodNode)) {
				correspondingMethodNode = methodNode;
				break;
			}
		}

		if (correspondingMethodNode == null) {
			throw new IllegalStateException("Method does not exist");
		}

		ListIterator<AbstractInsnNode> instructionIterator = correspondingMethodNode.instructions.iterator();
		boolean hasReturnOpcode = false;
		boolean hasAThrowOpcode = false;

		while (instructionIterator.hasNext()) {
			AbstractInsnNode insnNode = instructionIterator.next();

			if (OpcodesUtility.isXRETURN(insnNode.getOpcode())) {
				hasReturnOpcode = true;
			} else if (insnNode.getOpcode() == Opcodes.ATHROW) {
				hasAThrowOpcode = true;
			}
		}

		return !hasReturnOpcode && hasAThrowOpcode;
	}

	private boolean matches(MethodNode methodNode) {
		return m_methodName.equals(methodNode.name) && m_desc.equals(methodNode.desc);
	}
}
