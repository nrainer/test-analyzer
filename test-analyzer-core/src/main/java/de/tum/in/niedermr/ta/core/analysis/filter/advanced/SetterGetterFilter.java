package de.tum.in.niedermr.ta.core.analysis.filter.advanced;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.analysis.filter.FilterResult;
import de.tum.in.niedermr.ta.core.analysis.filter.IMethodFilter;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.util.BytecodeUtility;
import de.tum.in.niedermr.ta.core.code.util.OpcodesUtility;

/** Filters simple setter and getter methods out. */
public class SetterGetterFilter implements IMethodFilter {

	/** The class names in the set indicate that information about the methods in these classes is already available. */
	private final Set<String> m_initializedClasses;

	/** Contains setter and getter methods of initialized classes. */
	private final Set<MethodIdentifier> m_setterGetterMethods;

	/** Constructor. */
	public SetterGetterFilter() {
		this.m_initializedClasses = new HashSet<>();
		this.m_setterGetterMethods = new HashSet<>();
	}

	/** {@inheritDoc} */
	@Override
	public FilterResult apply(MethodIdentifier identifier, MethodNode method) throws IOException {
		ensureDataAvailability(identifier.getOnlyClassName());

		if (m_setterGetterMethods.contains(identifier)) {
			return FilterResult.reject(SetterGetterFilter.class, "Setter or getter");
		} else {
			return FilterResult.accepted();
		}
	}

	@SuppressWarnings("unchecked")
	private void ensureDataAvailability(String className) throws IOException {
		if (m_initializedClasses.contains(className)) {
			return;
		}

		ClassNode cn = BytecodeUtility.getAcceptedClassNode(className);

		for (MethodNode method : (List<MethodNode>) cn.methods) {
			MethodIdentifier methodIdentifier = MethodIdentifier.create(className, method);
			boolean isSetterOrGetter = determineIsSetterOrGetter(method);

			if (isSetterOrGetter) {
				m_setterGetterMethods.add(methodIdentifier);
			}
		}

		m_initializedClasses.add(className);
	}

	protected boolean determineIsSetterOrGetter(MethodNode method) {
		return isSimpleSetter(method.instructions) || isSimpleGetter(method.instructions)
				|| isSimpleConstantGetter(method.instructions);
	}

	/**
	 * <code>
	 * 1 (IRRELEVANT INTERNAL INSTRUCTION) 
	 * 2 (IRRELEVANT INTERNAL INSTRUCTION) 
	 * 3 ALOAD
	 * 4 xLOAD
	 * 5 PUTFIELD 
	 * 6 (IRRELEVANT INTERNAL INSTRUCTION) 
	 * 7 (IRRELEVANT INTERNAL INSTRUCTION) 
	 * 8 RETURN 
	 * 9 (IRRELEVANT INTERNAL INSTRUCTION)
	 * </code>
	 */
	private boolean isSimpleSetter(InsnList instructionList) {
		if (instructionList.size() != 9) {
			return false;
		}

		@SuppressWarnings("unchecked")
		Iterator<AbstractInsnNode> it = instructionList.iterator();
		AbstractInsnNode node;

		// drop instruction 1 (irrelevant)
		it.next();

		// drop instruction 2 (irrelevant)
		it.next();

		// instruction 3
		node = it.next();

		if (node.getOpcode() != Opcodes.ALOAD) {
			return false;
		}

		// instruction 4
		node = it.next();

		if (!OpcodesUtility.isXLOAD(node.getOpcode())) {
			return false;
		}

		// instruction 5
		node = it.next();

		if (node.getOpcode() != Opcodes.PUTFIELD) {
			return false;
		}

		// drop instruction 6 (irrelevant)
		it.next();

		// drop instruction 7 (irrelevant)
		it.next();

		// instruction 8
		node = it.next();

		return node.getOpcode() == Opcodes.RETURN;
	}

	/**
	 * <code>
	 * 1 (IRRELEVANT INTERNAL INSTRUCTION)
	 * 2 (IRRELEVANT INTERNAL INSTRUCTION)
	 * 3 ALOAD
	 * 4 GETFIELD
	 * 5 xRETURN
	 * 6 (IRRELEVANT INTERNAL INSTRUCTION)
	 * </code>
	 */
	private boolean isSimpleGetter(InsnList instructionList) {
		if (instructionList.size() != 6) {
			return false;
		}

		@SuppressWarnings("unchecked")
		Iterator<AbstractInsnNode> it = instructionList.iterator();
		AbstractInsnNode node;

		// drop instruction 1 (irrelevant)
		it.next();

		// drop instruction 2 (irrelevant)
		it.next();

		// instruction 3
		node = it.next();

		if (node.getOpcode() != Opcodes.ALOAD) {
			return false;
		}

		// instruction 4
		node = it.next();

		if (node.getOpcode() != Opcodes.GETFIELD) {
			return false;
		}

		// instruction 5
		node = it.next();

		if (!OpcodesUtility.isXRETURN(node.getOpcode())) {
			return false;
		}

		return true;
	}

	/**
	 * <code>
	 * 1 (IRRELEVANT INTERNAL INSTRUCTION) 
	 * 2 (IRRELEVANT INTERNAL INSTRUCTION) 
	 * 3 xCONST_x | xPUSH | LDC | LDC_W | LDC2_W 
	 * 4 xRETURN 5 (IRRELEVANT INTERNAL INSTRUCTION)
	 * </code>
	 */
	private boolean isSimpleConstantGetter(InsnList instructionList) {
		if (instructionList.size() != 5) {
			return false;
		} else {
			@SuppressWarnings("unchecked")
			Iterator<AbstractInsnNode> it = instructionList.iterator();
			AbstractInsnNode node;

			// drop instruction 1 (irrelevant)
			it.next();

			// drop instruction 2 (irrelevant)
			it.next();

			// instruction 3
			node = it.next();

			if (node.getOpcode() > 20) {
				return false;
			}

			// instruction 4
			node = it.next();

			if (!OpcodesUtility.isXRETURN(node.getOpcode())) {
				return false;
			}

			return true;
		}
	}
}
