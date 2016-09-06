package de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.tests;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.util.BytecodeUtility;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;

public class AssertionInformation {
	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(AssertionInformation.class);

	@SuppressWarnings("deprecation")
	private static final Class<?>[] CORE_ASSERTION_CLASSES = new Class<?>[] { org.junit.Assert.class,
			junit.framework.Assert.class };
	private final AssertionResult m_noAssertionResult = new AssertionResult(false, null);

	private final Set<Class<?>> m_assertionClassesInUse;
	private final Map<MethodIdentifier, int[]> m_assertions;

	/** Constructor. */
	public AssertionInformation(Class<?>... furtherAssertionClasses) {
		this.m_assertionClassesInUse = new HashSet<>();
		this.m_assertions = new HashMap<>();

		m_assertionClassesInUse.addAll(Arrays.asList(CORE_ASSERTION_CLASSES));
		m_assertionClassesInUse.addAll(Arrays.asList(furtherAssertionClasses));

		initAllAssertionClasses();
	}

	private void initAllAssertionClasses() {
		for (Class<?> assertionClass : m_assertionClassesInUse) {
			initAssertionClass(assertionClass);
		}
	}

	@SuppressWarnings("unchecked")
	private void initAssertionClass(Class<?> assertionClass) {
		try {
			final String className = assertionClass.getName();
			ClassNode cn = BytecodeUtility.getAcceptedClassNode(className);

			for (MethodNode methodNode : (List<MethodNode>) cn.methods) {
				if (BytecodeUtility.isPublicMethod(methodNode)) {
					MethodIdentifier methodIdentifier = MethodIdentifier.create(className, methodNode);
					int[] popOpcodes = calculatePopOpcodes(methodNode.desc);

					m_assertions.put(methodIdentifier, popOpcodes);
				}
			}
		} catch (IOException ex) {
			LOGGER.error("IOException", ex);
		}
	}

	private int[] calculatePopOpcodes(String methodDesc) {
		final Type[] argumentTypes = Type.getArgumentTypes(methodDesc);

		int[] popOpcodes = new int[argumentTypes.length];

		for (int i = 0; i < argumentTypes.length; i++) {
			final int typeSort = argumentTypes[i].getSort();

			if (typeSort == Type.LONG || typeSort == Type.DOUBLE) {
				popOpcodes[i] = Opcodes.POP2;
			} else {
				popOpcodes[i] = Opcodes.POP;
			}
		}

		return popOpcodes;
	}

	public AssertionResult isAssertionMethod(MethodIdentifier methodIdentifier) throws ClassNotFoundException {
		if (isKnownNativeAssertion(methodIdentifier)) {
			return new AssertionResult(true, methodIdentifier);
		}

		Class<?> cls = Class.forName(methodIdentifier.getOnlyClassName());

		for (Class<?> assertionClass : m_assertionClassesInUse) {
			if (JavaUtility.inheritsClass(cls, assertionClass)) {
				// create a new identifier in which the name of the class is replaced with the name of the assertion
				// super class
				// example: org.example.UtilTests.assertEquals(int,int) -> org.junit.Assert.assertEquals(int,int)
				MethodIdentifier newIdentifier = MethodIdentifier
						.parse(methodIdentifier.get().replace(cls.getName(), assertionClass.getName()));

				if (isKnownNativeAssertion(newIdentifier)) {
					return new AssertionResult(true, newIdentifier);
				} else {
					return m_noAssertionResult;
				}
			}
		}

		return m_noAssertionResult;
	}

	private boolean isKnownNativeAssertion(MethodIdentifier identifier) {
		return m_assertions.containsKey(identifier);
	}

	public class AssertionResult {
		private final boolean m_isAssertion;
		private final int[] m_popInstructionOpcodes;

		public AssertionResult(boolean isAssertion, MethodIdentifier originalMethodIdentifier) {
			this.m_isAssertion = isAssertion;
			this.m_popInstructionOpcodes = retrievePopInstructionOpcodes(originalMethodIdentifier);
		}

		private int[] retrievePopInstructionOpcodes(MethodIdentifier methodIdentifier) {
			if (methodIdentifier == null) {
				return null;
			} else {
				return m_assertions.get(methodIdentifier);
			}
		}

		public boolean isAssertion() {
			return m_isAssertion;
		}

		public int[] getPopInstructionOpcodes() {
			return m_popInstructionOpcodes;
		}
	}
}
