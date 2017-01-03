package de.tum.in.niedermr.ta.runner.analysis.mutation;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.analysis.filter.MethodFilterList;
import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.IReturnValueGenerator;
import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.ReturnValueGeneratorUtil;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.operation.CodeOperationException;
import de.tum.in.niedermr.ta.core.code.operation.ICodeModificationOperation;
import de.tum.in.niedermr.ta.core.code.util.OpcodesUtility;

/** Code modification operation to mutate methods. */
public class MutateMethodsOperation implements ICodeModificationOperation {

	private final IReturnValueGenerator m_returnValueGenerator;
	private final MethodFilterList m_methodFilters;
	private final List<MethodIdentifier> m_mutatedMethods;

	/** Constructor. */
	public MutateMethodsOperation(IReturnValueGenerator returnValueGen, MethodFilterList methodFilters) {
		m_returnValueGenerator = returnValueGen;
		m_methodFilters = methodFilters;

		m_mutatedMethods = new ArrayList<>();
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public void modify(ClassReader cr, ClassWriter cw) throws CodeOperationException {
		ClassNode classNode = new ClassNode();
		cr.accept(classNode, 0);

		for (MethodNode method : (List<MethodNode>) classNode.methods) {
			MethodIdentifier methodIdentifier = MethodIdentifier.create(classNode, method);

			if (m_methodFilters.apply(methodIdentifier, method).isAccepted()) {
				mutate(method, methodIdentifier);
			}
		}

		classNode.accept(cw);
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

	/** {@link #m_mutatedMethods} */
	public List<MethodIdentifier> getMutatedMethods() {
		return m_mutatedMethods;
	}
}