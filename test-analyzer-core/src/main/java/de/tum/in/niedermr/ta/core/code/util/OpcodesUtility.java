package de.tum.in.niedermr.ta.core.code.util;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class OpcodesUtility {

	/** Constructor. */
	private OpcodesUtility() {
		// NOP
	}

	public static boolean isMethodExitOpcode(int opcode) {
		return isXRETURN(opcode) || opcode == Opcodes.ATHROW;
	}

	public static boolean isXRETURN(int opcode) {
		return opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN;
	}

	public static boolean isXLOAD(int opcode) {
		return opcode >= Opcodes.ILOAD && opcode <= Opcodes.ALOAD;
	}

	public static boolean hasFlag(int value, int flag) {
		return flag == (value & flag);
	}

	public static int getReturnOpcode(Type returnType) {
		switch (returnType.getSort()) {
		case Type.VOID:
			return Opcodes.RETURN;
		case Type.BOOLEAN:
		case Type.BYTE:
		case Type.SHORT:
		case Type.CHAR:
		case Type.INT:
			return Opcodes.IRETURN;
		case Type.LONG:
			return Opcodes.LRETURN;
		case Type.FLOAT:
			return Opcodes.FRETURN;
		case Type.DOUBLE:
			return Opcodes.DRETURN;
		case Type.OBJECT:
		case Type.ARRAY:
			return Opcodes.ARETURN;
		default:
			throw new IllegalArgumentException("Unexpected return type");
		}
	}
}
