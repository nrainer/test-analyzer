package de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.util.Identification;

/**
 * Note that wrapper classes of primitive types are not supported. Void is also
 * not supported.
 *
 */
abstract class AbstractSimpleReturnValueGenerator extends AbstractReturnValueGenerator {
	private final boolean m_supportStringType;

	public AbstractSimpleReturnValueGenerator(boolean supportStringType) {
		this.m_supportStringType = supportStringType;
	}

	public abstract void handleBooleanReturn(MethodVisitor mv);

	public abstract void handleIntegerReturn(MethodVisitor mv);

	public abstract void handleCharReturn(MethodVisitor mv);

	public abstract void handleLongReturn(MethodVisitor mv);

	public abstract void handleFloatReturn(MethodVisitor mv);

	public abstract void handleDoubleReturn(MethodVisitor mv);

	public abstract void handleStringReturn(MethodVisitor mv);

	/** {@inheritDoc} */
	@Override
	public final void putReturnValueBytecodeInstructions(MethodVisitor mv, MethodIdentifier methodIdentifier,
			Type type) {
		switch (type.getSort()) {
		case Type.BOOLEAN:
			handleBooleanReturn(mv);
			break;
		case Type.BYTE:
		case Type.SHORT:
		case Type.INT:
			handleIntegerReturn(mv);
			break;
		case Type.CHAR:
			handleCharReturn(mv);
			break;
		case Type.LONG:
			handleLongReturn(mv);
			break;
		case Type.FLOAT:
			handleFloatReturn(mv);
			break;
		case Type.DOUBLE:
			handleDoubleReturn(mv);
			break;
		case Type.OBJECT:
			handleObjectReturn(mv, type);
			break;
		default:
			throw new IllegalStateException("Unexpected");
		}
	}

	private void handleObjectReturn(MethodVisitor mv, Type type) {
		if (m_supportStringType && Identification.isString(type)) {
			handleStringReturn(mv);
		}
	}

	/** {@inheritDoc} */
	@Override
	public final boolean checkReturnValueSupported(MethodIdentifier methodIdentifier, Type returnType) {
		return m_supportStringType ? Identification.isPrimitiveOrString(returnType)
				: Identification.isPrimitive(returnType);
	}
}
