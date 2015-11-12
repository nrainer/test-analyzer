package de.tum.in.niedermr.ta.runner.analysis.mutation;

import java.util.LinkedList;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.analysis.filter.MethodFilterCollection;
import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.IReturnValueGenerator;
import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.ReturnValueGeneratorUtil;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.operation.ICodeModificationOperation;
import de.tum.in.niedermr.ta.core.code.util.OpcodesUtility;

public class MutateMethodsOperation implements ICodeModificationOperation {
	private final IReturnValueGenerator m_returnValueGenerator;
	private final MethodFilterCollection m_methodFilters;
	private final List<MethodIdentifier> m_mutatedMethods;

	public MutateMethodsOperation(IReturnValueGenerator returnValueGen, MethodFilterCollection methodFilters) {
		this.m_returnValueGenerator = returnValueGen;
		this.m_methodFilters = methodFilters;

		this.m_mutatedMethods = new LinkedList<>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void modify(ClassReader cr, ClassWriter cw) throws Exception {
		ClassNode cn = new ClassNode();
		cr.accept(cn, 0);

		final String className = cr.getClassName();

		for (MethodNode method : (List<MethodNode>) cn.methods) {
			final MethodIdentifier methodIdentifier = MethodIdentifier.create(className, method);

			if (isToMutate(method, methodIdentifier)) {
				mutate(method, methodIdentifier);
			}
		}

		cn.accept(cw);
	}

	private boolean isToMutate(MethodNode method, MethodIdentifier identifier) {
		return m_methodFilters.acceptMethod(identifier, method).isAccepted() && !isSynthetic(method);
	}

	private void mutate(MethodNode method, MethodIdentifier methodIdentifier) {
		if (!ReturnValueGeneratorUtil.canHandleType(m_returnValueGenerator, methodIdentifier, method.desc)) {
			// Note that capability to handle the return type is - if used correctly - already checked by the method
			// filter.
			throw new IllegalStateException(
					"The selected return value generator does not support a value generation for the method "
							+ methodIdentifier.get() + ".");
		}

		final Type returnType = Type.getReturnType(method.desc);
		final int returnOpcode = OpcodesUtility.getReturnOpcode(returnType);

		method.instructions.clear();
		method.tryCatchBlocks.clear();
		method.localVariables.clear();

		m_returnValueGenerator.putReturnValueBytecodeInstructions(method, methodIdentifier, returnType);
		method.visitInsn(returnOpcode);

		m_mutatedMethods.add(methodIdentifier);
	}

	private boolean isSynthetic(MethodNode method) {
		return OpcodesUtility.hasFlag(method.access, Opcodes.ACC_SYNTHETIC);
	}

	public List<MethodIdentifier> getMutatedMethods() {
		return m_mutatedMethods;
	}
}