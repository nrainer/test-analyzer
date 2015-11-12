package de.tum.in.niedermr.ta.core.code.util;

import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.code.constants.BytecodeConstants;

public class BytecodeUtility {

	public static ClassNode getAcceptedClassNode(Class<?> cls) throws IOException {
		return getAcceptedClassNode(cls.getName());
	}

	public static ClassNode getAcceptedClassNode(String className) throws IOException {
		ClassReader cr = new ClassReader(className);
		ClassNode cn = new ClassNode();

		cr.accept(cn, 0);

		return cn;
	}

	/**
	 * Returns true, if the method node represents a constructor or a static initializer.
	 */
	public static boolean isConstructor(MethodNode method) {
		return isConstructor(method.name);
	}

	/**
	 * Returns true, if the method node represents a constructor or a static initializer.
	 */
	public static boolean isConstructor(String name) {
		return name.equals(BytecodeConstants.NAME_CONSTRUCTOR) || name.equals(BytecodeConstants.NAME_STATIC_INITIALIZER);
	}

	public static boolean isAbstractClass(ClassNode cn) {
		return OpcodesUtility.hasFlag(cn.access, Opcodes.ACC_ABSTRACT);
	}

	public static boolean isAbstractMethod(MethodNode mn) {
		return OpcodesUtility.hasFlag(mn.access, Opcodes.ACC_ABSTRACT);
	}

	public static boolean isPublicMethod(MethodNode mn) {
		return OpcodesUtility.hasFlag(mn.access, Opcodes.ACC_PUBLIC);
	}

	public static int countMethodInstructions(MethodNode methodNode) {
		if (methodNode.instructions == null) {
			return 0;
		} else {
			int count = 0;

			for (AbstractInsnNode node : methodNode.instructions.toArray()) {
				if (node.getOpcode() != -1) {
					count++;
				}
			}

			return count;
		}
	}
}
