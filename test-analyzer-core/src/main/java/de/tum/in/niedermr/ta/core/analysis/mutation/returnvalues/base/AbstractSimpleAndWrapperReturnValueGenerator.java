package de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.base;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import de.tum.in.niedermr.ta.core.code.constants.JavaConstants;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;

/** Abstract simple return value generator that supports wrapper classes. */
public abstract class AbstractSimpleAndWrapperReturnValueGenerator extends AbstractSimpleReturnValueGenerator {

	/** <code>java.lang</code> package name */
	private static final String JAVA_LANG_PACKAGE_NAME = "java.lang";

	/** Return value generator for simple values. */
	private final AbstractSimpleReturnValueGenerator m_returnValueGeneratorForSimpleValues;

	/** Constructor. */
	public AbstractSimpleAndWrapperReturnValueGenerator(
			AbstractSimpleReturnValueGenerator returnValueGeneratorForSimpleValues) {
		super(returnValueGeneratorForSimpleValues.isSupportStringType());
		m_returnValueGeneratorForSimpleValues = returnValueGeneratorForSimpleValues;
	}

	/** {@inheritDoc} */
	@Override
	protected void handleObjectReturn(MethodVisitor mv, Type type) {
		super.handleObjectReturn(mv, type);

		String className = type.getClassName();

		if (JavaConstants.WRAPPER_TYPE_CLASS_NAMES.contains(className)) {
			handleJavaLangObjectReturn(mv, className);
		}
	}

	private void handleJavaLangObjectReturn(MethodVisitor mv, String className) {
		if (Boolean.class.getName().equals(className)) {
			handleBooleanReturn(mv);
			putValueOfConversion(mv, Boolean.class, Type.BOOLEAN_TYPE);
		} else if (Byte.class.getName().equals(className)) {
			handleIntegerReturn(mv);
			putValueOfConversion(mv, Byte.class, Type.BYTE_TYPE);
		} else if (Short.class.getName().equals(className)) {
			handleIntegerReturn(mv);
			putValueOfConversion(mv, Short.class, Type.SHORT_TYPE);
		} else if (Integer.class.getName().equals(className)) {
			handleIntegerReturn(mv);
			putValueOfConversion(mv, Integer.class, Type.INT_TYPE);
		} else if (Character.class.getName().equals(className)) {
			handleCharReturn(mv);
			putValueOfConversion(mv, Character.class, Type.CHAR_TYPE);
		} else if (Long.class.getName().equals(className)) {
			handleLongReturn(mv);
			putValueOfConversion(mv, Long.class, Type.LONG_TYPE);
		} else if (Float.class.getName().equals(className)) {
			handleFloatReturn(mv);
			putValueOfConversion(mv, Float.class, Type.FLOAT_TYPE);
		} else if (Double.class.getName().equals(className)) {
			handleDoubleReturn(mv);
			putValueOfConversion(mv, Double.class, Type.DOUBLE_TYPE);
		} else {
			throw new IllegalStateException("Unexpected: " + className);
		}
	}

	/** Put the <code>valueOf</code> invocation on the wrapper class. */
	protected void putValueOfConversion(MethodVisitor mv, Class<?> wrapperClass, Type primitiveType) {
		String descriptor = String.format("(%s)L%s;", primitiveType.getDescriptor(),
				JavaUtility.toClassPathWithoutEnding(wrapperClass));
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, JavaUtility.toClassPathWithoutEnding(wrapperClass), "valueOf",
				descriptor, false);
	}

	/** {@inheritDoc} */
	@Override
	public void handleBooleanReturn(MethodVisitor mv) {
		m_returnValueGeneratorForSimpleValues.handleBooleanReturn(mv);
	}

	/** {@inheritDoc} */
	@Override
	public void handleIntegerReturn(MethodVisitor mv) {
		m_returnValueGeneratorForSimpleValues.handleIntegerReturn(mv);
	}

	/** {@inheritDoc} */
	@Override
	public void handleCharReturn(MethodVisitor mv) {
		m_returnValueGeneratorForSimpleValues.handleCharReturn(mv);
	}

	/** {@inheritDoc} */
	@Override
	public void handleLongReturn(MethodVisitor mv) {
		m_returnValueGeneratorForSimpleValues.handleLongReturn(mv);
	}

	/** {@inheritDoc} */
	@Override
	public void handleFloatReturn(MethodVisitor mv) {
		m_returnValueGeneratorForSimpleValues.handleFloatReturn(mv);
	}

	/** {@inheritDoc} */
	@Override
	public void handleDoubleReturn(MethodVisitor mv) {
		m_returnValueGeneratorForSimpleValues.handleDoubleReturn(mv);
	}

	/** {@inheritDoc} */
	@Override
	public void handleStringReturn(MethodVisitor mv) {
		m_returnValueGeneratorForSimpleValues.handleStringReturn(mv);
	}

	/** {@inheritDoc} */
	@Override
	public boolean checkReturnValueSupported(MethodIdentifier methodIdentifier, Type returnType) {
		if (super.checkReturnValueSupported(methodIdentifier, returnType)) {
			return true;
		}

		String className = returnType.getClassName();
		return className.startsWith(JAVA_LANG_PACKAGE_NAME)
				&& JavaConstants.WRAPPER_TYPE_CLASS_NAMES.contains(className);
	}
}
