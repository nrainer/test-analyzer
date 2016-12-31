package de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.base;

import java.io.InvalidClassException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.IllegalFormatException;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.util.Identification;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;

/** Abstract return value generator that uses a factory. */
public abstract class AbstractFactoryReturnValueGenerator extends AbstractReturnValueGenerator {
	private static final String INSTANCE_FIELD_NAME = "INSTANCE";

	private final IReturnValueFactory m_factoryInstance;
	private final String m_factoryPathName;

	/**
	 * Constructor. <br/>
	 * Important: The factory (of type factoryClass) MUST have a static field named {@value #INSTANCE_FIELD_NAME} of the
	 * type factoryClass.
	 * 
	 * @throws IllegalFormatException
	 *             if the INSTANCE field is not specified as expected
	 */
	public AbstractFactoryReturnValueGenerator(Class<? extends AbstractReturnValueFactory> factoryClass)
			throws ReflectiveOperationException, InvalidClassException {
		m_factoryInstance = (IReturnValueFactory) factoryClass.getField(INSTANCE_FIELD_NAME).get(null);
		m_factoryPathName = JavaUtility.toClassPathWithoutEnding(factoryClass);

		checkFactoryValidity(factoryClass);
	}

	/** {@inheritDoc} */
	@Override
	public boolean checkReturnValueSupported(MethodIdentifier methodIdentifier, Type returnType) {
		return Identification.isArrayOrObject(returnType)
				&& m_factoryInstance.supports(methodIdentifier, returnType.getClassName());
	}

	/** {@inheritDoc} */
	@Override
	public void putReturnValueBytecodeInstructions(MethodVisitor mv, MethodIdentifier methodIdentifier,
			Type returnType) {
		mv.visitFieldInsn(Opcodes.GETSTATIC, m_factoryPathName, INSTANCE_FIELD_NAME,
				String.format("L%s;", m_factoryPathName));
		mv.visitLdcInsn(methodIdentifier.get());
		mv.visitLdcInsn(returnType.getClassName());
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, m_factoryPathName, "get",
				"(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;", false);
		mv.visitTypeInsn(Opcodes.CHECKCAST, returnType.getInternalName());
	}

	protected void checkFactoryValidity(Class<? extends AbstractReturnValueFactory> factoryClass)
			throws InvalidClassException {
		try {
			Field instanceField = factoryClass.getField(INSTANCE_FIELD_NAME);

			if (!(Modifier.isStatic(instanceField.getModifiers()) && Modifier.isPublic(instanceField.getModifiers()))) {
				throw new InvalidClassException("The field " + INSTANCE_FIELD_NAME + " is not public and static.");
			}

			if (!instanceField.getGenericType().equals(factoryClass)) {
				throw new InvalidClassException(
						"The field " + INSTANCE_FIELD_NAME + " is not of the type " + factoryClass.getName() + ".");
			}
		} catch (NoSuchFieldException ex) {
			throw new InvalidClassException(factoryClass.getName() + " does not have a public static field of name "
					+ INSTANCE_FIELD_NAME + " of the type of the factory.");
		}
	}
}
